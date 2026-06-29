package com.softtech.plateausuperapp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.facebook.soloader.SoLoader
import com.softtech.quick.common.bridge.QV8Element
import com.softtech.quick.common.bridge.QV8Object
import com.softtech.quick.common.kotlin.StartMiniAppParams
import com.softtech.quick.common.logger.MiniAppHttpRequestLogCollector
import com.softtech.quick.common.logger.NetworkLogger
import com.softtech.quick.common.permission.QRuntimePermissionHandler
import com.softtech.quick.common.permission.QRuntimePermissionListener
import com.softtech.quick.common.ui.QFragment
import com.softtech.quick.common.utils.ObjectUtil
import com.softtech.quick.initializer.QuickClient
import com.softtech.quick.initializer.QuickClientCallbackListener
import com.softtech.quick.initializer.QuickConfig
import com.softtech.quick.initializer.QuickContext
import com.softtech.quick.initializer.QuickInitializer
import com.softtech.quick.renderingfw.ui.ActivityController
import com.softtech.quick.sdk.ClientFunctionError
import com.softtech.plateausuperapp.constant.Consent
import com.softtech.quick.common.network.SslPinningConfig
import com.softtech.quick.networkfw.DefaultSslPinningConfig
import com.softtech.quick.sdk.QuickSdk
import com.softtech.quick.sdk.QuickService
import com.softtech.quick.shared.anim.Animation
import com.softtech.quick.shared.platform.QPlatform
import androidx.core.net.toUri

class MainActivity :
    AppCompatActivity(),
    ActivityController,
    QuickService.LoadingJsonServiceListener,
    QuickClientCallbackListener,
    QuickClient.InitializerListener,
    QRuntimePermissionHandler {

    private var quickService: QuickService? = null
    private lateinit var lottieAnimationView: LottieAnimationView
    private var composition: LottieComposition? = null
    private lateinit var lottieParentLayout: FrameLayout
    private var networkLogger: NetworkLogger? = null
    private var intentParams: StartMiniAppParams? = null
    private var permissionListener: QRuntimePermissionListener? = null

    private val serviceBaseUrl = PlateauNativeConstants.serviceBaseUrl()
    private val jsonBaseUrl = PlateauNativeConstants.jsonBaseUrl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SoLoader.init(application, false)

        if (savedInstanceState == null && intent.hasExtra(KEY_START_MINI_APP_PARAMS)) {
            intentParams = intent.getSerializableExtra(KEY_START_MINI_APP_PARAMS) as? StartMiniAppParams
        }

        if (intentParams == null) {
            intentParams = StartMiniAppParams(APP_ID, null, null)
        }

        setContentView(R.layout.activity_main)

        lottieAnimationView = findViewById(R.id.lottieLoading)
        lottieParentLayout = findViewById(R.id.lottieParent)
        setLoadingJsonLocal()

        val config = QuickConfig()
        val context: QuickContext = object : QuickContext {
            override fun getAndroidActivity(): AppCompatActivity = this@MainActivity
            override fun getAndroidApplication(): Application = application
        }

        config.context = context
        config.jsonBaseUrl = jsonBaseUrl
        config.serviceBaseUrl = serviceBaseUrl
        config.callbackListener = this
        config.appId = intentParams?.appId
        config.loadingJsonServiceListener = this
        config.intentParams = intentParams
        config.functionCallTimeoutSeconds = 60L

        initQuickService(config)
        QuickInitializer.initialize(config, this)
    }

    private fun initQuickService(config: QuickConfig) {
        val sslPinningConfig: SslPinningConfig = DefaultSslPinningConfig.Builder()
            .withSslPemFile(PlateauNativeConstants.certificate())
            .withDomain(PlateauNativeConstants.baseDomain())
            .withCertificateId(PlateauNativeConstants.certificateId())
            .build()

        val platformInfo = QPlatform(baseContext).platFormInfo
        networkLogger = NetworkLogger(
            MiniAppHttpRequestLogCollector(config.serviceBaseUrl, config.appId, platformInfo)
        )

        val builder = QuickSdk.Builder.newInstance()
            .setAppId(config.appId)
            .maxRequestRetryCount(0)
            .setLanguage("tr-TR")
            .setSettingsUrl(null)
            .useEncrypt(true)
            .setClientCustomFunctionTriggerListener(this)
            .timeOutRequestSeconds(60)
            .addSslPinningConfig(sslPinningConfig)
            .setBaseUrl(config.serviceBaseUrl)
            .setRuntimePermissionCaller(this)
            .setPlatFormInfo(platformInfo)
            .setHttpInterceptorListener(networkLogger)

        config.quickBuilder = builder
    }

    override fun onQuickFragmentCreated(addToBackStack: Boolean, fragment: QFragment, tag: String) {
        onQuickFragmentCreatedWithAnimation(addToBackStack, fragment, tag, null)
    }

    override fun onQuickFragmentCreatedWithAnimation(
        addToBackStack: Boolean,
        fragment: QFragment?,
        tag: String?,
        pageTransitionAnimation: Animation?
    ) {
        if (!isFinishing) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()

            if (pageTransitionAnimation != null) {
                if (pageTransitionAnimation.isInAnimation) {
                    fragmentTransaction.setCustomAnimations(
                        pageTransitionAnimation.enterAnim, 0,
                        pageTransitionAnimation.exitAnim, 0
                    )
                } else {
                    fragmentTransaction.setCustomAnimations(
                        pageTransitionAnimation.exitAnim, 0,
                        pageTransitionAnimation.enterAnim, 0
                    )
                }
            }

            val fragmentsSize = supportFragmentManager.fragments.size
            if (fragmentsSize > 0) {
                val currentFragment: Fragment = supportFragmentManager.fragments[fragmentsSize - 1]
                if (currentFragment is QFragment) {
                    fragmentTransaction.hide(currentFragment)
                }
            }

            fragmentTransaction.addToBackStack(tag)
            if (fragment != null) {
                fragmentTransaction.add(R.id.q_content_fragment_layout, fragment, tag)
            }

            if (fragment?.isStateSaved == false) {
                fragmentTransaction.commit()
            } else {
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    override fun onQuickBackPressed() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        QuickInitializer.handleBack()
    }

    override fun startMiniApp(params: StartMiniAppParams?) {
        val intent = newIntent(this, params)
        startActivity(intent)
    }

    override fun stopMiniApp() {
        networkLogger?.logCollector?.sendLogsToApi()
        runOnUiThread { finish() }
    }

    override fun setAppId(appId: String) {
        QuickInitializer.setMiniAppAppId(appId)
    }

    override fun getAndroidApplication(): Application = application

    override fun goNativePage(screenId: String?, args: Map<String?, Any?>?, transitionAnimation: Animation?) {
    }

    override fun showLoading() {
        lottieAnimationView.post {
            lottieParentLayout.visibility = View.VISIBLE
            lottieAnimationView.playAnimation()
        }
    }

    override fun hideLoading() {
        lottieAnimationView.post {
            lottieParentLayout.visibility = View.GONE
            lottieAnimationView.pauseAnimation()
        }
    }

    override fun setLoadingJson(loadingJson: String?) {
        val compositionTask = LottieCompositionFactory.fromJsonString(loadingJson, null)
        compositionTask.addListener { result ->
            composition = result
            lottieAnimationView.setCacheComposition(true)
            lottieAnimationView.setComposition(composition!!)
            lottieAnimationView.setMaxFrame(31)
            lottieAnimationView.setMinFrame(1)
        }
    }

    override fun setLoadingUrl(loadingUrl: String?) {
        val compositionTask = LottieCompositionFactory.fromUrl(this, loadingUrl)
        compositionTask.addListener { result ->
            composition = result
            lottieAnimationView.setCacheComposition(true)
            lottieAnimationView.setComposition(composition!!)
            lottieAnimationView.setMaxFrame(31)
            lottieAnimationView.setMinFrame(1)
        }
    }

    override fun setLoadingJsonLocal() {
        val compositionTask = LottieCompositionFactory.fromAsset(baseContext, LOTTIE_FILE_NAME)
        compositionTask.addListener { result ->
            composition = result
            if (lottieAnimationView.composition == null) {
                lottieAnimationView.setCacheComposition(true)
                lottieAnimationView.setComposition(composition!!)
                lottieAnimationView.setMaxFrame(31)
                lottieAnimationView.setMinFrame(1)
            }
        }
    }

    override fun onInitialized(client: QuickClient) {
        if (client.quickService != null) {
            quickService = client.quickService

            var paramsObject: QV8Object? = null
            if (intentParams?.params != null) {
                paramsObject = ObjectUtil.convertToObject(intentParams!!.params).asQV8Object
            }

            if (intentParams?.pageName == null) {
                quickService?.render(client.pageLabel, paramsObject)
            } else {
                quickService?.render(intentParams!!.pageName, paramsObject)
            }
        }
    }

    override fun callFunction(
        functionName: String?,
        params: QV8Element?,
        resultListener: QuickService.FunctionCallBackListener
    ): Boolean {
        val v8Object = QV8Object()
        var handled = false

        when (functionName) {
            Consent.GET_IDENTITY -> {
                v8Object.add(Consent.TCKN, Consent.DEFAULT_TCKN)
                v8Object.add(Consent.NAME, Consent.DEFAULT_NAME)
                v8Object.add(Consent.SURNAME, Consent.DEFAULT_SURNAME)
                handled = true
            }
            Consent.GET_MAIL_ADDRESS -> {
                v8Object.add(Consent.EMAIL, Consent.DEFAULT_EMAIL)
                handled = true
            }
            Consent.GET_PHONE_NUMBER -> {
                v8Object.add(Consent.PHONE_NUMBER, Consent.DEFAULT_PHONE_NUMBER)
                handled = true
            }
            Consent.GET_CARDS -> {
                resultListener.onFunctionResult(v8Object)
                return false
            }
            Consent.GET_TOKEN -> {
                v8Object.add(Consent.ACCESS_TOKEN, Consent.DEFAULT_ACCESS_TOKEN)
                handled = true
            }
            Consent.GET_CUSTOMER_NUMBER -> {
                v8Object.add(Consent.CUSTOMER_NUMBER, Consent.DEFAULT_CUSTOMER_NUMBER)
                handled = true
            }
            Consent.GET_QR_DATA -> {
                v8Object.add(Consent.QR_DATA, Consent.DEFAULT_QR_DATA)
                handled = true
            }
            Consent.GET_COMMERCIAL_CUSTOMER_NUMBER -> {
                v8Object.add(Consent.COMMERCIAL_CUSTOMER_NUMBER, Consent.DEFAULT_COMMERCIAL_CUSTOMER_NUMBER)
                handled = true
            }
            Consent.GET_CLIENT_IP -> {
                getClientIp(v8Object, resultListener)
                return true
            }
            Consent.DATAROID_TRACK -> {
                resultListener.onFunctionResult(params)
                return false
            }
            Consent.OPEN_MAP -> {
                val latitude = params?.asQV8Object?.get("latitude")?.toString()
                val longitude = params?.asQV8Object?.get("longitude")?.toString()

                if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty()) {
                    openMap(latitude, longitude)
                }
            }
            else -> {
                resultListener.onFunctionError(ClientFunctionError("", FUNC_NOT_IMPLEMENTED))
                Log.e("MyApp", "Default Case: $functionName not implemented")
            }
        }

        if (handled) {
            resultListener.onFunctionResult(v8Object)
        }
        return handled
    }

    override fun callTokenFunction(
        functionName: String?,
        params: QV8Element?,
        resultListener: QuickService.FunctionCallBackListener?
    ): Boolean {
        val v8Object = QV8Object()
        var handled = false

        when (functionName) {
            "BI_FROST" -> {
                v8Object.add("BI-FROST", "17274844322")
                handled = true
            }
            "IAM_CS" -> {
                v8Object.add("IAM-CS", "00300304848584")
                handled = true
            }
            else -> {
                resultListener?.onFunctionError(ClientFunctionError("", FUNC_NOT_IMPLEMENTED))
                Log.e("MyApp", "Default Case: $functionName not implemented")
            }
        }

        if (handled) {
            resultListener?.onFunctionResult(v8Object)
        }
        return handled
    }

    override fun getUserOrDeviceInfo(): HashMap<String, String> {
        val returnObject = HashMap<String, String>()
        val isLogin = true
        if (isLogin) {
            returnObject["user"] = "123" // userId
        } else {
            returnObject["device"] = "123" // deviceId
        }
        return returnObject
    }

    override fun requestRuntimePermissions(
        permissions: Array<out String?>?,
        runtimePermissionListener: QRuntimePermissionListener?
    ) {
        if (permissions != null && hasPermission(*permissions)) {
            runtimePermissionListener?.onRuntimePermissionGranted(permissions)
        } else {
            permissionListener = runtimePermissionListener
            val nonNullPerms = permissions?.filterNotNull()?.toTypedArray() ?: return
            ActivityCompat.requestPermissions(this, nonNullPerms, REQUEST_PERMISSION_CODE)
        }
    }

    override fun hasPermission(vararg permissions: String?): Boolean {
        return permissions.filterNotNull().all {
            ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionListener?.onRuntimePermissionGranted(permissions)
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        networkLogger?.logCollector?.sendLogsToApi()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
    }

    private fun getClientIp(v8Object: QV8Object, callback: QuickService.FunctionCallBackListener) {
        var ipAddress = ""
        try {
            java.util.Scanner(
                java.net.URL("https://api.ipify.org").openStream(), "UTF-8"
            ).useDelimiter("\\A").use { scanner ->
                ipAddress = scanner.next()
            }
        } catch (e: Exception) {
            Log.e("MyApp", "Failed to get client IP: ${e.message}")
        }
        v8Object.add(Consent.CLIENT_IP, ipAddress)
        callback.onFunctionResult(v8Object)
    }

    private fun openMap(latitude: String, longitude: String) {
        val mapsUri = "geo:$latitude,$longitude?q=$latitude,$longitude".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, mapsUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            val browserUri = "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude".toUri()
            startActivity(Intent(Intent.ACTION_VIEW, browserUri))
        }
    }

    private fun release() {
        quickService?.release()
        quickService = null
    }

    companion object {
        private const val KEY_START_MINI_APP_PARAMS = "startMiniAppParams"
        private const val APP_ID = ""
        private const val LOTTIE_FILE_NAME = "loadinglottie.json"
        private const val FUNC_NOT_IMPLEMENTED = "Function not implemented"
        private const val REQUEST_PERMISSION_CODE = 1001

        fun newIntent(context: Context, params: StartMiniAppParams?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(KEY_START_MINI_APP_PARAMS, params)
            intent.putExtras(bundle)
            return intent
        }
    }
}
