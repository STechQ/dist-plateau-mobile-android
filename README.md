# MiniApp SDK – Integration Guide (Host Application)

This document explains how to integrate **MiniApp SDK** into a host Android application.

🎯 **Goal:**  
A developer reading this document should be able to integrate the SDK and run MiniApp screens inside their own application with minimum effort.

---

## Table of Contents

- [1. Scope](#1-scope)
- [2. Installation](#1-installation)
- [3. Quick Start](#3-quick-start)
- [4. Mandatory Overrides](#4-mandatory-overrides)
  - [4.1 onCreate](#41-oncreatesavedinstancestate)
  - [4.2 onInitialized](#42-oninitializedclient-quickclient)
  - [4.3 Fragment Navigation Management](#43-fragment-navigation-management-activitycontroller)
  - [4.4 onBackPressed](#44-onbackpressed)
  - [4.5 stopMiniApp / release](#45-stopminiapp--release)
  - [4.6 onDestroy](#46-ondestroy)
  - [4.7 onNewIntent](#47-onnewintentintent-recommended)
- [5. Host Activity Template (Copy & Use)](#5-host-activity-template-copy--use)
- [6. Optional JS → Native Bridge](#6-optional-js--native-bridge)
  - [6.1 callFunction Example](#61-callfunction-example-getdeviceid)
  - [6.2 callTokenFunction Example](#62-calltokenfunction-example-bi_frost)
- [7. Optional Modules](#7-optional-modules)
- [8. Recommended Architecture (Provider/Bridge Pattern)](#8-recommended-architecture-providerbridge-pattern)
- [9. Troubleshooting](#9-troubleshooting)
- [10. Integration Checklist](#10-integration-checklist)

---

# 1. Scope

This guide covers integration of MiniApp SDK through a host Android `Activity`.

### Mandatory parts
These must be implemented for the SDK to work:

- MiniApp initialization (`MiniAppSdk.initialize`)
- Quick runtime callback (`onInitialized`)
- Fragment management (`onQuickFragmentCreated*`)
- Back handling (`onBackPressed`)
- Resource cleanup (`onDestroy`)

### Optional parts
These depend on the host application needs:

- JS → Native function bridge (`callFunction`)
- Token bridge (`callTokenFunction`)
- Seal / Document / Signature operations
- Analytics integration
- Customer information functions

---

# 2. Installation

## Prerequisites

Open your root `settings.gradle` and add the following inside `dependencyResolutionManagement`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("https://maven.pkg.github.com/STechQ/dist-plateau-mobile-android")
            credentials {
                username = "USERNAME"
                password = "PASSWORD"
            }
        }
    }
}
```

Add the following to your `app/build.gradle`:
```groovy
android {
    compileOptions {
        coreLibraryDesugaringEnabled true
    }
}

dependencies {
    coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:2.1.4'
}
```

## Gradle (Groovy DSL)

Add the dependency to your `app/build.gradle`:
```groovy
dependencies {
    implementation 'com.softtech.quick.sdk:plateausdk:1.8.0.012'
}
```

## Maven
```xml
<dependency>
  <groupId>com.softtech.quick.sdk</groupId>
  <artifactId>plateausdk</artifactId>
  <version>1.8.0.012</version>
</dependency>
```

---

# 3. Quick Start

1. Create an Activity in your application (ex: `MiniAppHostActivity`)
2. Start this Activity with either:
   - `MiniAppID` (appId)
   - or `StartMiniAppParams`
3. Inside `onCreate`, call:

```kotlin
MiniAppSdk.initialize(params, language, this)
```

✅ Quick runtime will initialize automatically.

4. Implement `onInitialized(client)` and call:

```kotlin
quickService.render(pageName, paramsObject)
```

5. Implement fragment management callbacks (`onQuickFragmentCreated*`)
6. Implement `onBackPressed()` to delegate back actions to Quick
7. Release resources in `onDestroy()` (`quickService.release()`)

---

# 4. Mandatory Overrides

---

## 4.1 onCreate(savedInstanceState)

### Purpose
- Resolve MiniApp parameters from Intent
- Resolve language
- Call `MiniAppSdk.initialize(...)`
- Prepare observers if needed

### Expected behavior
- `MiniAppID` or `StartMiniAppParams` must be resolved.
- Initialization must be called.
- Host application may decide UI behavior (toolbar, fullscreen etc.)

Example:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val params = intent.getSerializableExtra(KEY_PARAMS) as? StartMiniAppParams
        ?: intent.getStringExtra(KEY_MINI_APP_ID)
            ?.let { StartMiniAppParams(it, null, null) }

    val language = LanguageUtil.getLanguageIdentifier(this)

    params?.let {
        MiniAppSdk.initialize(it, language, this)
    }
}
```

---

## 4.2 onInitialized(client: QuickClient)

### Purpose
Quick runtime calls this callback when it is ready.

This is where the MiniApp should be rendered.

### Expected behavior
- Save `quickService`
- Convert parameters to `QV8Object` (if any)
- Render page

```kotlin
override fun onInitialized(client: QuickClient) {
    quickService = client.quickService ?: return

    val paramsObj: QV8Object? = intentParams?.params?.let {
        ObjectUtil.convertToObject(it).asQV8Object
    }

    val page = intentParams?.pageName ?: client.pageLabel
    quickService?.render(page, paramsObj)
}
```

---

## 4.3 Fragment Navigation Management (ActivityController)

### Purpose
Quick framework creates `QFragment` objects and expects the host app to display them using `FragmentManager`.

### Mandatory methods
- `onQuickFragmentCreated(...)`
- `onQuickFragmentCreatedWithAnimation(...)`
- `onQuickBackPressed()`

### Expected behavior
- Hide current fragment (if QFragment)
- Add the new fragment
- Apply animation if provided
- Fix fragment visibility after back

---

## 4.4 onBackPressed()

### Purpose
- MiniApp navigation is handled internally by Quick
- Host must delegate back events to Quick

### Recommended behavior
- If fragment backstack is 1 → close MiniApp
- Else delegate to Quick

```kotlin
@Suppress("MissingSuperCall")
override fun onBackPressed() {
    if (supportFragmentManager.backStackEntryCount <= 1) {
        stopMiniApp()
        return
    }

    QuickInitializer.handleBack()
}
```

---

## 4.5 stopMiniApp / release

### Purpose
MiniApp must release Quick runtime properly.

```kotlin
override fun stopMiniApp() {
    release()
    finish()
}

private fun release() {
    quickService?.release()
    quickService = null
}
```

---

## 4.6 onDestroy()

### Purpose
Prevent memory leaks and release Quick runtime.

```kotlin
override fun onDestroy() {
    quickService?.release()
    quickService = null
    super.onDestroy()
}
```

---

## 4.7 onNewIntent(intent) (Recommended)

### Purpose
If your MiniApp supports NFC or intent-based events, you should forward new intents to the active fragment.

Example behavior:
- Check if top fragment is an NFC dialog
- Call its processing method

```kotlin
override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)

    val lastFragment = supportFragmentManager.fragments.lastOrNull()
    if (lastFragment is STMiniAppNFCReaderDialog) {
        lastFragment.processNFC(intent)
    }
}
```

If you don’t use NFC or intent events, this method may not be required.

---

# 5. Host Activity Template (Copy & Use)

> `setContentView(...)` and `containerId` are NOT fixed.  
> Every company defines its own layout and fragment container.

Below is a generic template Activity.

```kotlin
class MiniAppHostActivity :
    AppCompatActivity(),
    ActivityController,
    QuickClientCallbackListener,
    QuickClient.InitializerListener,
    QRuntimePermissionHandler {

    private var quickService: QuickService? = null
    private var intentParams: StartMiniAppParams? = null

    // Host application defines these
    @LayoutRes
    protected open val layoutResId: Int = R.layout.your_activity_layout

    @IdRes
    protected open val containerId: Int = R.id.your_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)

        intentParams =
            intent.getSerializableExtra(KEY_PARAMS) as? StartMiniAppParams
                ?: intent.getStringExtra(KEY_MINI_APP_ID)
                    ?.let { StartMiniAppParams(it, null, null) }

        val language = LanguageUtil.getLanguageIdentifier(this)

        // Quick runtime initializes automatically inside initialize()
        intentParams?.let { MiniAppSdk.initialize(it, language, this) }
    }

    override fun onInitialized(client: QuickClient) {
        quickService = client.quickService ?: return

        val paramsObj: QV8Object? = intentParams?.params?.let {
            ObjectUtil.convertToObject(it).asQV8Object
        }

        val page = intentParams?.pageName ?: client.pageLabel
        quickService?.render(page, paramsObj)
    }

    override fun onQuickFragmentCreated(
        addToBackStack: Boolean,
        fragment: QFragment?,
        tag: String?
    ) {
        onQuickFragmentCreatedWithAnimation(addToBackStack, fragment, tag, null)
    }

    override fun onQuickFragmentCreatedWithAnimation(
        addToBackStack: Boolean,
        fragment: QFragment?,
        tag: String?,
        pageTransitionAnimation: Animation?
    ) {
        // Host app must implement:
        // 1) Hide current QFragment
        // 2) Add new fragment into containerId
        // 3) Apply animations if needed
    }

    override fun onQuickBackPressed() {
        super.onBackPressed()
        // Optional: fix animation direction / show hidden fragment
    }

    @Suppress("MissingSuperCall")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            quickService?.release()
            quickService = null
            finish()
            return
        }

        QuickInitializer.handleBack()
    }

    override fun requestRuntimePermissions(
        permission: Array<out String>,
        permissionListener: QRuntimePermissionListener
    ) {
        // Optional: connect your permission handler
    }

    override fun hasPermission(vararg p0: String): Boolean {
        // Optional: implement permission check
        return true
    }

    override fun onDestroy() {
        quickService?.release()
        quickService = null
        super.onDestroy()
    }

    companion object {
        private const val KEY_MINI_APP_ID = "MiniAppID"
        private const val KEY_PARAMS = "startMiniAppParams"
    }
}
```

---

## ContainerId Recommendation

Because fragment management is required, you should provide a container.

Recommended solution:
- Create a dedicated layout for MiniApp screen containing only a `FragmentContainerView`.

Example:

```xml
<androidx.fragment.app.FragmentContainerView
    android:id="@+id/miniapp_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

---

# 6. Optional JS → Native Bridge

`callFunction` and `callTokenFunction` are optional.

However, it is recommended to provide at least one example for integration clarity.

---

## 6.1 callFunction Example (GetDeviceId)

### Scenario
MiniApp requests a unique device identifier.

- FunctionName: `GetDeviceId`
- Params: none
- Return:

```json
{ "deviceId": "..." }
```

Example implementation:

```kotlin
override fun callFunction(
    functionName: String?,
    params: QV8Element?,
    callback: QuickService.FunctionCallBackListener
): Boolean {
    if (functionName == "GetDeviceId") {
        val result = QV8Object().apply {
            add("deviceId", DeviceUtils.getUniqueDeviceId())
        }
        callback.onFunctionResult(result)
        return true
    }
    return false
}
```

---

## 6.2 callTokenFunction Example (BI_FROST)

### Scenario
MiniApp requests token via special token channel.

- FunctionName: `BI_FROST`
- Return:

```json
{
  "actionToken": "...",
  "cv": "..."
}
```

Example implementation:

```kotlin
override fun callTokenFunction(
    functionName: String,
    params: QV8Element?,
    resultListener: QuickService.FunctionCallBackListener
): Boolean {
    if (functionName == "BI_FROST") {
        val result = QV8Object().apply {
            add("actionToken", "YOUR_ACTION_TOKEN")
            add("cv", "YOUR_CV")
        }
        resultListener.onFunctionResult(result)
        return true
    }
    return false
}
```

---

# 7. Optional Modules

These features are **optional** and should be integrated only if required by the host application.

- Seal / Signature / Documents:
  - `GetTransactionDetail`
  - `DownloadDocument`
  - `SignDocument`
  - `SignTransaction`

- Analytics tracking:
  - `dataroidTrack`

- Customer information:
  - `GetIdentity`
  - `GetMailAddress`
  - `GetPhoneNumber`
  - `GetCustomerNumber`

- Device & Network:
  - `GetClientIp`
  - `GetDeviceId`

- Runtime permissions:
  - Camera / Location (only if used by MiniApp screens)

---

# 8. Recommended Architecture (Provider/Bridge Pattern)

To prevent the host Activity from becoming too large, it is recommended to isolate optional features into provider interfaces.

Example provider groups:

- `MiniAppDeviceProvider` (deviceId, ip)
- `MiniAppUserProvider` (identity, phone, email)
- `MiniAppTokenProvider` (BI_FROST etc.)
- `MiniAppAnalyticsProvider` (track events)
- `MiniAppDocumentSigner` (seal/signature/document)

With this design:
- Each company integrates only what it needs.
- Unsupported features can return `"not supported"` errors.

---

# 9. Troubleshooting

### MiniApp screen is blank / not rendering
- Is `onInitialized(client)` called?
- Is `client.quickService` null?
- Is `render(page, params)` called correctly?
- Is `MiniAppID` or `StartMiniAppParams` resolved correctly?

### Back button does not work
- Is `QuickInitializer.handleBack()` called in `onBackPressed()`?
- Is fragment backstack logic correct (`<=1` close MiniApp)?
- Are fragments added to backstack properly?

### callFunction callback does not return (optional)
- Is `callback.onFunctionResult(...)` always called?
- Is QV8Object format correct?
- Is async callback reference stored correctly?

### Permission request does not work
- Are permissions declared in AndroidManifest?
- Is `hasPermission()` implemented correctly?
- Is the listener triggered after user decision?

---

# 10. Integration Checklist

☑ MiniAppID or StartMiniAppParams is passed via Intent  
☑ `MiniAppSdk.initialize(...)` is called inside `onCreate`  
☑ `onInitialized(...)` renders the first page using QuickService  
☑ Fragment callbacks are implemented (Quick fragment navigation works)  
☑ `onBackPressed()` delegates back action to Quick  
☑ `quickService.release()` is called in `onDestroy()`  
☑ Optional `callFunction/callTokenFunction` examples are implemented if needed  
☑ Optional Seal / Analytics / Token features are integrated only when required  

---

✅ **End of Document**
