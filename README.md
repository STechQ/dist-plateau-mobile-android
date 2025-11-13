### Plateau Android SDK

**Transfer your Low Code development with Plateau Studio to the Android platform in a few steps with Plateau SDK implementation.**

 ⚠️ **Warning:** *If you are going to integrate Super SDK, please read READMEForSuperSDK!*

**#`Start`**

****1.Dependencies**　

***Gradle:***

*Data Binding*

    dataBinding {
        enabled = true
    }

    buildFeatures {
        dataBinding = true
    }

*Libraries*

    // Plateau Mobile SDK Files
    implementation fileTree(dir: 'libs', include: ['*.jar'])

    implementation (files("libs/quickcomponents-release.aar"))
    implementation (files("libs/qcommon-release.aar"))
    implementation (files("libs/renderingfw-release.aar"))
    implementation (files("libs/networkfw-release.aar"))
    implementation (files("libs/quickbridge-release.aar"))
    implementation (files("libs/quickmobileandroid-release.aar"))
    implementation (files("libs/shared-release.aar"))
    implementation (files("libs/quick-initializer-release.aar"))
    implementation (files("libs/web-bridge-release.aar"))

    // Optional
    // implementation (files("libs/QAuth-release.aar"))

    // Third Party Libraries
    implementation 'androidx.core:core-ktx:1.3.2'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'androidx.activity:activity:1.2.4'
    implementation "androidx.appcompat:appcompat:1.2.0"
    implementation 'com.google.android.material:material:1.4.0'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    implementation 'com.facebook.soloader:soloader:0.12.1'
    implementation "com.airbnb.android:lottie:4.2.0"
    implementation "com.google.dagger:dagger-android:2.27"
    implementation "com.google.code.gson:gson:2.8.6"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation "io.reactivex.rxjava3:rxandroid:3.0.0"
    implementation "com.squareup.retrofit2:converter-scalars:2.9.0"
    implementation "com.squareup.retrofit2:adapter-rxjava3:2.9.0"
    implementation 'org.conscrypt:conscrypt-android:2.5.3'
    implementation "com.squareup.okhttp3:okhttp:4.9.3"
    implementation "commons-beanutils:commons-beanutils:1.9.4"
    implementation "org.apache.commons:commons-lang3:3.11"
    implementation "com.github.ynab:j2v8:6.2.1-16kb.2"
    implementation "com.facebook.yoga:yoga:3.2.1"

    implementation "com.github.bumptech.glide:glide:4.14.2"
    implementation 'com.jakewharton.rxbinding4:rxbinding-core:4.0.0'
    implementation "commons-codec:commons-codec:1.15"
    implementation "com.squareup.okhttp3:logging-interceptor:4.9.3"
    annotationProcessor "com.github.bumptech.glide:compiler:4.14.2"
    annotationProcessor "com.google.dagger:dagger-android-processor:2.27"
    annotationProcessor "com.google.dagger:dagger-compiler:2.27"

    implementation "androidx.room:room-runtime:2.4.2"
    annotationProcessor "androidx.room:room-compiler:2.4.2"

    implementation 'com.google.firebase:firebase-analytics:17.4.1'
    implementation platform('com.google.firebase:firebase-bom:32.1.0')
    implementation 'com.google.firebase:firebase-crashlytics:18.3.7'

    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation 'androidx.security:security-crypto:1.1.0-alpha03'
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
    implementation "androidx.lifecycle:lifecycle-common-java8:2.3.1",
    implementation "androidx.fragment:fragment:1.3.0"
    implementation "androidx.coordinatorlayout:coordinatorlayout:1.2.0"
    implementation "androidx.cardview:cardview:1.0.0"
    implementation "androidx.core:core:1.3.2"
    implementation "androidx.camera:camera-camera2:1.4.2"
    implementation "androidx.camera:camera-lifecycle:1.4.2"
    implementation "androidx.camera:camera-view:1.4.2"
    implementation "androidx.camera:camera-mlkit-vision:1.4.2"
    implementation "org.jetbrains.kotlin:kotlin-stdlib:1.8.0"
    implementation "com.google.dagger:dagger:2.27"
    implementation 'com.github.bumptech.glide:okhttp3-integration:4.4.0'
    implementation 'com.google.android.libraries.places:places:3.2.0'
    implementation 'com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.0'
    implementation "com.google.android.gms:play-services-auth:20.6.0"
    implementation "commons-io:commons-io:2.6"
    implementation "com.github.permissions-dispatcher:permissionsdispatcher:4.8.0"
    implementation "com.github.permissions-dispatcher:permissionsdispatcher-processor:4.8.0"
    implementation 'com.github.amitshekhariitbhu:PRDownloader:1.0.2'
    implementation 'com.vanniktech:android-image-cropper:4.3.3'
    implementation "org.java-websocket:Java-WebSocket:1.5.1"
    implementation "com.squareup.moshi:moshi:1.9.2"
    implementation "com.squareup.moshi:moshi-kotlin-codegen:1.9.2"
    implementation 'com.github.marain87:AndroidPdfViewer:3.2.8'
    implementation 'javax.inject:javax.inject:1'
    implementation 'com.caverock:androidsvg-aar:1.4'

    // Huawei Libraries
    implementation "com.huawei.hms:maps:6.11.0.304"
    implementation "com.huawei.hms:location:6.16.0.302"
    implementation "com.huawei.hms:scan:2.12.0.301"
    implementation "com.huawei.agconnect:agconnect-core:1.9.1.304"
    implementation "com.huawei.agconnect:agconnect-appmessaging:1.9.1.304"

    // Optional
    //implementation 'net.openid:appauth:0.11.1'
    //implementation 'com.auth0.android:jwtdecode:2.0.0'

**2. Software Requirements and Tools IDE: Android Studio  Lang: Java,Kotlin**

#### Required Implementation
```kotlin
class MainActivity :
    AppCompatActivity(),
    QuickService.AsyncInitialListener,
    ActivityController,
    QuickService.LoadingJsonServiceListener,
    QuickService.QuickCallBackListener,
    QuickService.QuickActivityController {

    private var baseUrl = "https://otogb7m8y8f23d18ym35w7.z6.web.core.windows.net/"
    private var settingsUrl = "settings/settings_mobile.json"
    private var quickService: QuickService? = null
    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lottieAnimationView = findViewById(R.id.lottieLoading)
        setLoadingJsonLocal()

        initQuickService()
    }

    private fun initQuickService() {
        // If you want to add a certificate, you should add it like this and use addSslPinningConfig
        /*val sslPinningConfig = DefaultSslPinningConfig.Builder()
            .withSslPemFile("sslPemFile")
            .withDomain("domain")
            .withCertificateId("certificateId")
            .build()*/

        SoLoader.init(androidApplication, false)

        val builder = QuickSdk.Builder.newInstance()
            .setSettingsUrl(settingsUrl)
            .setAppId(null)
            .setLanguage("tr-TR")
            .maxRequestRetryCount(0)
            .timeOutRequestSeconds(60)
            .setDeepLinkParameters(null)
            //.addSslPinningConfig(sslPinningConfig)
            .setClientCustomFunctionTriggerListener(this)
            .setPlatFormInfo(QPlatform(baseContext).platFormInfo)
            .setBaseUrl(baseUrl)

        quickService = builder.build(this).quickService
        quickService!!.initializeAsync(this)
    }

    // Required for page transition
    override fun onQuickFragmentCreated(addToBackStack: Boolean, fragment: QFragment, tag: String) {
        onQuickFragmentCreatedWithAnimation(addToBackStack, fragment, tag, null)
    }

    override fun onQuickFragmentCreatedWithAnimation(
        addToBackStack: Boolean,
        fragment: QFragment,
        tag: String?,
        pageTransitionAnimation: Animation?
    ) {
        if (!isFinishing()) {
            val fragmentTransaction: FragmentTransaction =
                getSupportFragmentManager().beginTransaction()

            if (pageTransitionAnimation != null) {
                if (pageTransitionAnimation.isInAnimation()) {
                    fragmentTransaction.setCustomAnimations(
                        pageTransitionAnimation.getEnterAnim(),
                        0,
                        pageTransitionAnimation.getExitAnim(),
                        0
                    )
                } else {
                    fragmentTransaction.setCustomAnimations(
                        pageTransitionAnimation.getExitAnim(),
                        0,
                        pageTransitionAnimation.getEnterAnim(),
                        0
                    )
                }
            }

            val fragmentsSize: Int = getSupportFragmentManager().getFragments().size()

            if (fragmentsSize > 0) {
                val currentFragment: Fragment =
                    getSupportFragmentManager().getFragments().get(fragmentsSize - 1)

                if (currentFragment is QFragment) {
                    fragmentTransaction.hide(currentFragment)
                }
            }

            fragmentTransaction.addToBackStack(tag)
            fragmentTransaction.add(R.id.q_content_fragment_layout, fragment, tag)

            if (!fragment.isStateSaved()) {
                fragmentTransaction.commit()
            } else {
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    override fun onQuickBackPressed() {
        super.onBackPressed()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            stopMiniApp()
            return
        }

        quickService.handleBack().subscribe()
    }

    override fun showLoading() {
        lottieAnimationView.post(Runnable {
            lottieAnimationView.setVisibility(View.VISIBLE)
            lottieAnimationView.playAnimation()
        })
    }

    override fun hideLoading() {
        lottieAnimationView.post(Runnable {
            lottieAnimationView.setVisibility(View.GONE)
            lottieAnimationView.pauseAnimation()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    // Consent information to be received from the application using the SDK
    // Dummy data added for example purposes
    override fun callFunction(
        functionName: String?, params: QV8Element?,
        resultListener: FunctionCallBackListener
    ): Boolean {
        val v8Object = QV8Object()
        val resultData = QV8Object()
        var handled = false

        when (functionName) {
            "GetToken" -> {
                v8Object.add("token", "123456789")
                handled = true
            }

            "GetUserInfo" -> {
                v8Object.add("UserNameSurname", "testNameSurname")
                v8Object.add("SicilNo", "123456")
                handled = true
            }

            else -> Log.e("MyApp", "Default Case")
        }
        if (handled) {
            resultData.add("isSuccess", true)
            resultData.add("retVal", v8Object)
            resultListener.onFunctionResult(resultData)
        }
        return handled
    }

    override fun getUserOrDeviceInfo(): HashMap<String, String> {
        val returnObject = java.util.HashMap<String, String>()
        val isLogin = true

        if (isLogin) {
            returnObject["user"] = "123" //userId
        } else {
            returnObject["device"] = "123" //deviceId
        }
        return returnObject
    }

    private fun release() {
        quickService?.release()
        quickService = null
    }

    override fun onInitialized(quickService: QuickService) {
        quickService.injectActivity(this)
        quickService.startRender(null)
    }

    override fun setLoadingJson(loadingJson: String) {
        val compositionTask = LottieCompositionFactory.fromJsonString(loadingJson, null)
        compositionTask.addListener { result: LottieComposition ->
            lottieAnimationView.setCacheComposition(true)
            lottieAnimationView.setComposition(result)
            lottieAnimationView.setMaxFrame(31)
            lottieAnimationView.setMinFrame(1)
        }
    }

    override fun setAppId(p0: String) {
        // no-opt
    }

    override fun stopMiniApp() {
        release()
        finish()
    }

    override fun setTheme(p0: QThemeAttributes) {
        // no opt
    }

    override fun getAndroidApplication(): Application {
        return application
    }

    override fun setPageAndStatusColor(p0: MutableMap<String, Any>?) {
        // no opt
    }

    override fun clickNavigation(p0: Any?) {
        // no opt
    }

    override fun goNativePage(p0: String?, p1: MutableMap<String, Any>?, p2: Animation?) {
        // no opt
    }

    override fun setLoadingJsonLocal() {
        // no-opt
    }

    override fun setLoadingUrl(loadingUrl: String?) {
        // no-opt
    }

    override fun setLoadingJson(loadingJson: String?) {
        // no-opt
    }
    
    override fun addNavigationComponent(component: QBaseComponent<*>?) {
        // no-opt
    }

    override fun closeNavigation() {
        // no-opt
    }

    override fun openNavigation() {
        // no-opt
    }
```    
**3. XML Code Example**
```xml

    <?xml version="1.0" encoding="utf-8"?>
    <androidx.drawerlayout.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/q_drawerLayout"
    android:layout_width="match_parent"
    android:fitsSystemWindows="true"
    android:layout_height="match_parent">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.fragment.app.FragmentContainerView
            android:id="@+id/q_content_fragment_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

        </androidx.fragment.app.FragmentContainerView>

        <com.airbnb.lottie.LottieAnimationView
            android:id="@+id/lottieLoading"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_centerInParent="true"
            android:layout_gravity="center"
            android:background="#60000000"
            android:fitsSystemWindows="true"
            android:scaleType="centerInside"
            app:lottie_autoPlay="true"
            app:lottie_loop="true"
            app:lottie_speed="1" />
    </RelativeLayout>
    
    </androidx.drawerlayout.widget.DrawerLayout>
```
**3. AndroidManifest Adding Certificates**
- To avoid SSL errors, you can add a certificate by defining networkSecurityConfig in the Android manifest file.

```xml
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:networkSecurityConfig="@xml/config"
    android:supportsRtl="true"
    android:theme="@style/Theme.QuickProject">
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">kube.uatisbank</domain>
        <trust-anchors>
            <certificates src="@raw/kubeuatisbank"/>
            <certificates src="system"/>
        </trust-anchors>
    </domain-config>
</network-security-config>
```

#### Optional Implementation

- If you want to manage user permissions (camera, location permission, etc.) in the shell yourself, 
the QRuntimePermissionHandler interface must be implemented in the shell.
Then the following methods should be overridden.

```kotlin

    override fun requestRuntimePermissions(
        permission: Array<out String>,
        permissionListener: QRuntimePermissionListener
    ) {
        if (hasPermission(*permission)) {
            permissionListener.onRuntimePermissionGranted(permission)
        } else {
            this.permissionListener = permissionListener
            // request runtime permission
        }
    }

    override fun cancelPermission() {
        // no-opt
    }

    override fun confirmPermission() {
        // no-opt
    }

    override fun hasPermission(vararg p0: String): Boolean {
        p0.forEach {
            if (ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
```



    
