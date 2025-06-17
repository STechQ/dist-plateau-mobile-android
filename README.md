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
    implementation 'com.softtech.quick.sdk:plateausdk:7.3.0-alpha.32'

**2. Software Requirements and Tools IDE: Android Studio  Lang: Java,Kotlin**

#### Required Implementation
```kotlin
class MainActivity :
    AppCompatActivity(),
    QuickService.AsyncInitialListener,
    ScreenNavController,
    QuickService.LoadingJsonServiceListener,
    QuickService.QuickCallBackListener,
    QuickService.QuickActivityController{

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
        // If you want to add a certificate, you should add it like this and use setQuickCertificate
        //val certificateBuilder = QuickCertificate.Builder.newQuickCertificate()
        //certificateBuilder.add("host", "public_key")

        SoLoader.init(androidApplication, false)

        val builder = QuickSdk.Builder.newInstance()
            .setSettingsUrl(settingsUrl)
            .setAppId(null)
            .setLanguage("tr-TR")
            .maxRequestRetryCount(0)
            .timeOutRequestSeconds(60)
            .setDeepLinkParameters(null)
            //.setQuickCertificate(certificateBuilder.build())
            .setClientCustomFunctionTriggerListener(this)
            .setPlatFormInfo(QPlatform(baseContext).platFormInfo)
            .setBaseUrl(baseUrl)

        quickService = builder.build(this).quickService
        quickService!!.initializeAsync(this)
    }

    // Required for page transition
    override fun onQuickFragmentCreated(p0: Boolean, p1: QFragment, p2: String) {
        if (!isFinishing) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(p2)

            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(
                R.id.q_content_fragment_layout, p1,
                p2
            )

            if (!p1.isStateSaved()) {
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
        quickService!!.handleBack().subscribe { handled ->
            if (!handled) {
                super@MainActivity.onBackPressed()
            }
        }
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
                v8Object.add("token","123456789")
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
        // no-opt
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

    override fun goNativePage(p0: String?, p1: MutableMap<String, Any>?, p2: String?) {
        // no opt
    }

    override fun setLoadingJsonLocal() {
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



    
