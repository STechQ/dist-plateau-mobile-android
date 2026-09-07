# Plateau Android SDK – Integration Guide

This document explains how to integrate the **Plateau Android SDK** (`com.softtech.quick.sdk:plateausdk`) into a host Android application.

🎯 **Goal:** A developer should be able to integrate the SDK and render Plateau screens with minimum effort.

> **Note:** For MiniApp (Super SDK) integration, see [READMEForSuperSDK.md](READMEForSuperSDK.md).

---

## Table of Contents

- [1. Requirements](#1-requirements)
- [2. Installation](#2-installation)
- [3. Quick Start](#3-quick-start)
- [4. Mandatory Implementation](#4-mandatory-implementation)
- [5. Optional JS → Native Bridge](#5-optional-js--native-bridge)
- [6. Checklist](#6-checklist)

---

## 1. Requirements

- Android Studio (latest stable)
- Language: Java / Kotlin
- Min SDK: 23, Target/Compile SDK: 34, JVM Target: 17

---

## 2. Installation

### settings.gradle.kts

Add the GitHub Maven repository:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("https://maven.pkg.github.com/STechQ/dist-plateau-mobile-android")
            // credentials { username = "USERNAME"; password = "PASSWORD" } // if required
        }
    }
}
```

### app/build.gradle.kts

```kotlin
android {
    dataBinding { enable = true }
    buildFeatures { dataBinding = true }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    kotlin { jvmToolchain(17) }
}

dependencies {
    implementation("com.softtech.quick.sdk:plateausdk:1.8.3.003")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
}
```

---

## 3. Quick Start

1. Create an Activity that implements `ActivityController`, `QuickService.AsyncInitialListener`, `QuickService.LoadingJsonServiceListener` and `QuickService.QuickCallBackListener`.
2. Provide a layout containing:
   - `FragmentContainerView` with id `q_content_fragment_layout`
   - `LottieAnimationView` with id `lottieLoading`
3. Build a `QuickSdk.Builder` with SSL pinning, base URL and app id, then `build(this).quickService`.
4. Call `quickService.initializeAsync(this)` in `onCreate`, then `startRender(null)` inside `onInitialized(...)`.

---

## 4. Mandatory Implementation

```kotlin
class MainActivity :
    AppCompatActivity(),
    ActivityController,
    QuickService.AsyncInitialListener,
    QuickService.LoadingJsonServiceListener,
    QuickService.QuickCallBackListener {

    private var quickService: QuickService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SoLoader.init(application, false)
        initQuickService()
    }

    private fun initQuickService() {
        val sslPinningConfig: SslPinningConfig = DefaultSslPinningConfig.Builder()
            .withSslPemFile("<certificate>")
            .withDomain("<base-domain>")
            .withCertificateId("<certificate-id>")
            .build()

        val builder = QuickSdk.Builder.newInstance()
            .setSettingsUrl("settings/settings_mobile.json") // null if not used
            .setAppId("<app-id>")
            .setLanguage("tr-TR")
            .maxRequestRetryCount(0)
            .timeOutRequestSeconds(60)
            .addSslPinningConfig(sslPinningConfig)
            .setClientCustomFunctionTriggerListener(this)
            .setPlatFormInfo(QPlatform(baseContext).platFormInfo)
            .setBaseUrl("<service-base-url>")

        quickService = builder.build(this).quickService
        quickService?.updateSslPinning(builder.sslPinningConfig)
        quickService?.initializeAsync(this)
    }

    override fun onInitialized(quickService: QuickService) {
        this.quickService = quickService
        quickService.startRender(null)
    }

    // --- ActivityController ---

    override fun onQuickFragmentCreated(addToBackStack: Boolean, fragment: QFragment, tag: String) {
        onQuickFragmentCreatedWithAnimation(addToBackStack, fragment, tag, null)
    }

    override fun onQuickFragmentCreatedWithAnimation(
        addToBackStack: Boolean,
        fragment: QFragment?,
        tag: String?,
        pageTransitionAnimation: Animation?
    ) {
        if (isFinishing) return

        val transaction = supportFragmentManager.beginTransaction()

        pageTransitionAnimation?.let { anim ->
            if (anim.isInAnimation) {
                transaction.setCustomAnimations(anim.enterAnim, 0, anim.exitAnim, 0)
            } else {
                transaction.setCustomAnimations(anim.exitAnim, 0, anim.enterAnim, 0)
            }
        }

        val last = supportFragmentManager.fragments.lastOrNull()
        if (last is QFragment) transaction.hide(last)

        transaction.addToBackStack(tag)
        fragment?.let { transaction.add(R.id.q_content_fragment_layout, it, tag) }

        if (fragment?.isStateSaved == false) transaction.commit()
        else transaction.commitAllowingStateLoss()
    }

    override fun onQuickBackPressed() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        quickService?.handleBack()
    }

    override fun startMiniApp(params: StartMiniAppParams) {}

    override fun stopMiniApp() {
        runOnUiThread { finish() }
    }

    override fun getAndroidApplication(): Application = application

    override fun goNativePage(screenId: String?, args: Map<String?, Any?>?, transitionAnimation: Animation?) {}

    override fun setAppId(appId: String) {}

    // --- Loading ---

    override fun setLoadingJson(loadingJson: String) {}
    override fun setLoadingUrl(loadingUrl: String) {}
    override fun setLoadingJsonLocal() {}

    override fun showLoading() {}
    override fun hideLoading() {}

    override fun onDestroy() {
        quickService?.release()
        super.onDestroy()
    }
}
```

> Certificate, domain, app id and base URL values come from your Plateau project configuration. Unused `ActivityController` / loading callbacks can be left as no-ops.

---

## 5. Optional JS → Native Bridge

Implement `callFunction` / `callTokenFunction` to expose native capabilities. Unrecognized names must return `false` (or call `onFunctionError`).

```kotlin
override fun callFunction(
    functionName: String?,
    params: QV8Element?,
    resultListener: QuickService.FunctionCallBackListener
): Boolean {
    val v8Object = QV8Object()
    var handled = false

    when (functionName) {
        "GetDeviceId" -> {
            v8Object.add("deviceId", "<unique-device-id>")
            handled = true
        }
        else -> Log.e("MyApp", "Default Case: $functionName")
    }

    if (handled) resultListener.onFunctionResult(v8Object)
    return handled
}
```

---

## 6. Checklist

☑ `plateausdk` dependency + data binding + desugaring added  
☑ Activity implements `ActivityController` + `QuickService` listeners  
☑ Layout contains `q_content_fragment_layout` and `lottieLoading`  
☑ `QuickSdk.Builder` built and `initializeAsync(...)` called  
☑ `startRender(...)` called in `onInitialized()`  
☑ `quickService.release()` in `onDestroy()`  
☑ Baseline permissions (`INTERNET`, `ACCESS_NETWORK_STATE`) declared  

---

✅ **End of Document**
