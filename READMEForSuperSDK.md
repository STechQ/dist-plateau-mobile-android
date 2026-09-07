### Plateau Android Super SDK

**Transfer your Low Code development with Plateau Studio to the Android platform in a few steps with Plateau Super SDK implementation.**

⚠️ **Warning:** *If you are going to integrate Plateau Android SDK, please read README!*

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
    implementation 'com.softtech.quick.sdk:plateausdk:1.8.3.003'

*Compile Options*

    compileOptions {
        coreLibraryDesugaringEnabled true
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    dependencies {
        coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:2.1.4'
    }

**2. Software Requirements and Tools IDE: Android Studio  Lang: Java,Kotlin**

#### Required Implementation
```kotlin
class MainActivity :
    AppCompatActivity(),
    ActivityController,
    QuickClientCallbackListener,
    QuickClient.InitializerListener,
    QRuntimePermissionHandler

    private lateinit var lottieAnimationView: LottieAnimationView
    private var serviceBaseUrl = "https://*********..."
    private var jsonBaseUrl = "https://*********..."
    private var appId = "************..."
    private var quickService: QuickService? = null
    private var applicationName = ""
    private var networkLogger: NetworkLogger? = null
    private var intentParams: StartMiniAppParams? = null
    private var permissionListener: QRuntimePermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null && intent.hasExtra(KEY_PARAMS)) {
            intentParams = intent.getSerializableExtra(KEY_PARAMS) as? StartMiniAppParams
        }
        if (intentParams == null) {
            intentParams = StartMiniAppParams(appId, null, null)
        }

        SoLoader.init(application, false)
        lottieAnimationView = findViewById(R.id.lottieLoading)

        val config = QuickConfig()
        val context: QuickContext = object : QuickContext {
            override fun getAndroidActivity(): AppCompatActivity {
                return this@MainActivity
            }

            override fun getAndroidApplication(): Application {
                return application
            }
        }

        config.context = context
        config.serviceBaseUrl = serviceBaseUrl
        config.jsonBaseUrl = jsonBaseUrl
        config.appId = intentParams?.appId
        config.callbackListener = this
        config.intentParams = intentParams
        config.functionCallTimeoutSeconds = 60L

        val sslPinningConfig = DefaultSslPinningConfig.Builder()
            .withSslPemFile("<certificate>")
            .withDomain("<base-domain>")
            .withCertificateId("<certificate-id>")
            .build()
        val platformInfo = QPlatform(baseContext).platFormInfo

        networkLogger = NetworkLogger(MiniAppHttpRequestLogCollector(
            config.serviceBaseUrl, config.appId, platformInfo
        ))
        val builder = QuickSdk.Builder.newInstance()
        builder.setAppId(intentParams?.appId)
        builder.maxRequestRetryCount(0)
        builder.setLanguage("tr-TR")
        builder.setSettingsUrl(null) // If settingsUrl not provided set null.
        builder.useEncrypt(true)
        builder.setClientCustomFunctionTriggerListener(this)
        builder.timeOutRequestSeconds(60)
        builder.addSslPinningConfig(sslPinningConfig)
        builder.setBaseUrl(config.serviceBaseUrl)
        builder.setRuntimePermissionCaller(this)
        builder.setHttpInterceptorListener(networkLogger)
        builder.setPlatFormInfo(platformInfo)
        config.quickBuilder = builder

        QuickInitializer.initialize(config, this)
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
        if(supportFragmentManager.backStackEntryCount == 1) {
            stopMiniApp()
            return
        }

        QuickInitializer.handleBack()
    }

    // Required to stop the sdk
    override fun stopMiniApp() {
        release()
        finish()
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

    override fun getAndroidApplication(): Application {
        return application
    }

    override fun goNativePage(p0: String?, p1: MutableMap<String, Any>?, p2: Animation?) {
        // no opt
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    override fun setAppId(p0: String) {
        QuickInitializer.setMiniAppAppId(p0)
    }

    // Consent information to be received from the application using the SDK
    // Dummy data added for example purposes
    override fun callFunction(
        functionName: String,
        params: QV8Element,
        callback: QuickService.FunctionCallBackListener
    ): Boolean {
        val v8Object = QV8Object()
        var handled = false

        when (functionName) {
            "GetIdentity" -> {
                v8Object.add("TCKN","52138251734")
                v8Object.add("Name","Ali")
                v8Object.add("Surname","Kaya")
                handled = true
            }

            "GetMailAddress" -> {
                v8Object.add("Email", "testemail@gmail.com")
                handled = true
            }
            "GetPhoneNumber" -> {
                v8Object.add("PhoneNumber", "905555555555")
                handled = true
            }

            else -> Log.e("MyApp", "Default Case")
        }
        if (handled) {
            callback.onFunctionResult(v8Object)
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

    override fun onInitialized(client: QuickClient) {
        if(client.quickService != null) {
            quickService = client.quickService

            val paramsObject: QV8Object? = intentParams?.params?.let {
                ObjectUtil.convertToObject(it).asQV8Object
            }
            val page = intentParams?.pageName ?: client.pageLabel
            quickService?.render(page, paramsObject)

            applicationName = client.applicationName.orEmpty()
        }
    }

    private fun release() {
        quickService?.release()
        quickService = null
        QuickInitializer.release()
    }

    override fun onStop() {
        networkLogger?.logCollector?.sendLogsToApi()
        super.onStop()
    }

    companion object {
        private const val KEY_PARAMS = "startMiniAppParams"
        private const val REQUEST_PERMISSION_CODE = 1001
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
            ActivityCompat.requestPermissions(this, permission, REQUEST_PERMISSION_CODE)
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionListener?.onRuntimePermissionGranted(permissions)
            } else {
                permissionListener?.onRuntimePermissionDenied(permissions)
            }
        }
    }
```




