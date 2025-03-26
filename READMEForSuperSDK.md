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
    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.activity:activity:1.8.0'
    implementation "androidx.appcompat:appcompat:1.2.0"
    implementation 'com.google.android.material:material:1.10.0'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    implementation 'com.facebook.soloader:soloader:0.10.4'
    implementation "com.airbnb.android:lottie:4.2.0"
    implementation "com.google.dagger:dagger-android:2.35.1"
    implementation "com.google.code.gson:gson:2.8.6"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation "io.reactivex.rxjava3:rxandroid:3.0.0"
    implementation "com.squareup.retrofit2:converter-scalars:2.9.0"
    implementation "com.squareup.retrofit2:adapter-rxjava3:2.9.0"
    implementation "com.jakewharton.timber:timber:4.7.1"
    implementation 'org.conscrypt:conscrypt-android:2.2.1'
    implementation "com.squareup.okhttp3:okhttp:4.9.3"
    implementation "commons-beanutils:commons-beanutils:1.9.4"
    implementation "org.apache.commons:commons-lang3:3.11"
    implementation "com.eclipsesource.j2v8:j2v8:6.2.1@aar"
    implementation 'com.facebook.yoga:yoga-layout:2.0.0'
    implementation "com.github.bumptech.glide:glide:4.14.2"
    implementation 'com.jakewharton.rxbinding4:rxbinding-core:4.0.0'
    implementation "commons-codec:commons-codec:1.15"
    implementation "com.squareup.okhttp3:logging-interceptor:4.9.3"
    annotationProcessor "com.github.bumptech.glide:compiler:4.14.2"
    annotationProcessor "com.google.dagger:dagger-android-processor:2.27"
    annotationProcessor "com.google.dagger:dagger-compiler:2.27"
    implementation "androidx.room:room-runtime:2.4.0-alpha03"
    annotationProcessor "androidx.room:room-compiler:2.4.0-alpha03"
    implementation 'com.google.firebase:firebase-analytics:17.4.1'
    implementation platform('com.google.firebase:firebase-bom:32.1.0')
    implementation 'com.google.firebase:firebase-crashlytics'
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation 'androidx.security:security-crypto:1.1.0-alpha03'
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

    // Optional
    //implementation 'net.openid:appauth:0.11.1'
    //implementation 'com.auth0.android:jwtdecode:2.0.0'

 ***Maven:***

    <dependency>
      <groupId>com.softtech.quick.sdk</groupId>
      <artifactId>android</artifactId>
      <version>1.0.0</version>
    </dependency>

**2. Software Requirements and Tools IDE: Android Studio  Lang: Java,Kotlin**

#### Required Implementation
```kotlin
class MainActivity :
    AppCompatActivity(),
    ScreenNavController,
    QuickClientCallbackListener,
    QuickClient.InitializerListener

    private lateinit var lottieAnimationView: LottieAnimationView
    private var baseUrl = "https://*********..."
    private var appId = "************..."
    private var settingsUrl = "settings/settings_mobile.json"
    private var quickService: QuickService? = null
    private var applicationName = ""
    private var networkLogger: NetworkLogger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SoLoader.init(androidApplication, false)
        lottieAnimationView = findViewById(R.id.lottieLoading)

        val config = QuickConfig()
        val context: QuickContext = object : QuickContext {
            override fun getAndroidActivity(): AppCompatActivity {
                return this@MainActivity
            }

            override fun getAndroidApplication(): Application {
                return androidApplication
            }
        }

        config.context = context
        config.baseUrl = baseUrl
        config.appId = appId
        config.callbackListener = this

        networkLogger = NetworkLogger(MiniAppHttpRequestLogCollector(config.appId))
        val builder = QuickSdk.Builder.newInstance()
        builder.appId = config.AppId
        builder.maxRequestRetryCount(0)
        builder.language = "tr-TR"
        builder.settingsUrl = settingsUrl // If settingsUrl not provied set null.
        builder.previewNetworkListener = null
        builder.clientCustomFunctionTriggerListener = this
        builder.timeOutRequestSeconds(60)
        builder.baseUrl = config.baseUrl
        builder.setHttpInterceptorListener(networkLogger)
        builder.setPlatformInfo(QPlatform(getBaseContext()).platformInfo)
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

            "GetEmailAddress" -> {
                v8Object.add("Email", "testemail@gmail.com")
                handled = true
            }
            "GetPhoneNumber" -> {
                v8Object.add("PhoneNumber", "905555555555")
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
            quickService?.injectActivity(this)
            quickService?.render(client.pageLabel, null)
            applicationName = client.applicationName.orEmpty()
        }
    }

    private fun release() {
        quickService?.release()
        quickService = null
    }

    override fun onStop() {
        if (networkLogger != null) {
            networkLogger!!.logCollector.sendLogsToApi()
        }
        super.onStop()
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




