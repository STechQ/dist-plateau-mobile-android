### Plateau Android SDK

**Transfer your Low Code development with Plateau Studio to the Android platform in a few steps with Plateau SDK implementation.**

**#`Start`**

****1.Dependencies**　

***Gradle:***

    implementation "com.softtech.quick.sdk:version"

    //Third party

    implementation deps.appcompat
    implementation deps.material
    implementation deps.kotlinStdLib
    implementation deps.cardview
    implementation deps.constraintLayout
    implementation deps.glide
    implementation deps.okhttp3Integration
    implementation deps.places
    implementation deps.webSocket
    implementation deps.moshi
    implementation deps.moshiKotlin
    implementation deps.bartekscPdf
    implementation deps.prdownloader
    implementation deps.MPAndroidChart
    implementation deps.imageCropper
    implementation deps.playMlKitBarcodeScanning
    implementation deps.yogaLayout
    implementation deps.cameraX
    implementation deps.cameraXLifecycle
    implementation deps.cameraXView
    implementation deps.rxBinding
    implementation deps.cameraXMlKitVision
    implementation deps.commonsLang
    implementation deps.dagger
    implementation deps.lifecycle_common_java8
    implementation deps.livedata_ktx
    implementation deps.retrofit
    implementation deps.retrofitGsonConverter
    implementation deps.loggingInterceptor
    implementation deps.okHttp
    implementation deps.conscrypt
    implementation deps.rxAndroid
    implementation deps.adapterRxJava3
    implementation deps.retrofitScalarsConverter
    implementation deps.firebaseCrashlytics
    implementation deps.gson
    implementation deps.timber
    implementation deps.soLoader
    implementation deps.beanutils
    implementation deps.commonsIO
    implementation deps.permissionsDispatcher
    implementation deps.recyclerview
    implementation deps.activity
    implementation deps.lottie
    implementation deps.encryptedSharedPreferences
    implementation deps.javaxInject
    implementation deps.fragment
    implementation deps.codec
    implementation deps.room
    implementation deps.svg
    implementation "com.eclipsesource.j2v8:j2v8:6.2.1@aar"
    implementation group: 'org.yaml', name: 'snakeyaml', version: '1.23'
    implementation group: 'commons-io', name: 'commons-io', version: '2.6'
    
    annotationProcessor deps.permissionsDispatcherProcessor
    annotationProcessor deps.daggerCompiler
    annotationProcessor deps.daggerProcessors
    annotationProcessor deps.glideCompiler
    annotationProcessor deps.roomCompiler
    
    kapt deps.daggerCompiler
    kapt deps.daggerProcessors
    kapt deps.roomCompiler
    
    coreLibraryDesugaring deps.desugar

 ***Maven:***

    <dependency>
      <groupId>com.softtech.quick.sdk</groupId>
      <artifactId>android</artifactId>
      <version>1.0.0</version>
    </dependency>

***Local:***

    dependencies {
    	implementation fileTree(dir: 'libs', include: ['*.jar'])
     
     	implementation(name:'quickcomponents-release', ext:'aar')
    	implementation(name:'qcommon-release', ext:'aar')
    	implementation(name:'renderingfw-release', ext:'aar')
    	implementation(name:'networkfw-release', ext:'aar')
    	implementation(name:'quickbridge-release', ext:'aar')
    	implementation(name:'quickmobileandroid-release', ext:'aar')
    }

**2. Software Requirements and Tools
**IDE: Android Studio  Lang: Java,Kotlin**

####Implementation
```java
public class SampleActivity extends AppCompatActivity implements QuickService.AsyncInitialListener,  
  ScreenNavController,  
  QuickService.QuickCallBackListener, {  
    private QuickService quickService;  

 /*
  private static final String settingsUrl =
            "settings/settings_mobile.json";
  private static final String baseUrl = "https://8r5xoyxj7y5c0ayoull0hi.z6.web.core.windows.net/";
*/

  @Override  
  protected void onCreate(@Nullable Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
		QuickSdk.Builder builder = QuickSdk.Builder.newInstance()  
                .setAppId("00000000000-*******-......")  // if appId not provided, set null.
                .maxRequestRetryCount(0).setLanguage("tr-TR")  
                .setSettingsUrl(settingsUrl)  // set settings url, if not provied set null.
                .setClientCustomFunctionTriggerListener(this)  
                .timeOutRequestSeconds(60)
		.setDeepLinkParameters(null)  
                .setBaseUrl(baseUrl);  
  
	quickService = builder.build(this).getQuickService();  
	quickService.initializeAsync(this);  
  }  
  
    @Override  
  public void onInitialized(QuickService quickService) {  
        quickService.injectActivity(this);  
	quickService.startRender(null);  
  }  
  
    @Override  
  protected void onDestroy() {  
        super.onDestroy();  
 	quickService.release();  
  }

 @Override
    public void onQuickFragmentCreated(boolean addToBackStack, QFragment fragment, String tag) {
        if (!isFinishing()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(tag);

	    // Change q_content_fragment_layout with your fragment id.
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(
                    R.id.q_content_fragment_layout, fragment
                    , tag).commit();
        }
    }


    @Override  
  public boolean callFunction(String functionName, QV8Element params, QuickService.FunctionCallBackListener resultListener) {  
     QV8Object v8Object = new QV8Object();
     QV8Object resultData = new QV8Object();

//To add primitive type, use QV8Primitive(new QV8Primitive(true),new QV8Primitive(2),new QV8Primitive("string")).
//To add map type, use QV8Object (object = new QV8Object(),object.add("key",string|boolean,integer,float,QV8Primitive,QV8Array,QV8Object)).
//To add array type, use QV8Array (array = new QV8Array(),array.add(string|boolean,integer,float,QV8Primitive,QV8Array,QV8Object)).

     switch (functionName) {  
          	  case "GetIdentity":  
          	  v8Object.add("TCKN", "11111111118");  
          	  v8Object.add("Name", "Şükrü");  
          	  v8Object.add("Surname", "Karamann");
	  	  handled = true;  
         	   break; 
            
            case "GetMailAddress":  
           	 v8Object.add("Email", "email");
	   	 handled = true;  
           	 break;
	  
            case "GetPhoneNumber":  
	    	v8Object.add("PhoneNumber", "tel");
	   	 handled = true;
           	 break;

	    case "GetToken":
                v8Object.add("token", "your_token");
                resultData.add("isSuccess", true);
                resultData.add("retVal", v8Object);
                handled = true;
                break;

            case "GetUserInfo":
                v8Object.add("UserNameSurname", "your_usernamesurname");
                v8Object.add("SicilNo", "your_sicilNo");
                resultData.add("isSuccess", true);
                resultData.add("retVal", v8Object);
                handled = true;
                break;

	   default:
                Log.e("DefaultCase", "Default Case");
	
        if (handled) {
            resultListener.onFunctionResult(resultData);
        }
	return handled;  
      
    }

    @Override
    public void onBackPressed() {
        quickService.handleBack().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean handled) throws Throwable {
                if(!handled){
                    SampleActivity.super.onBackPressed();
                }
            }
        });
    }



//Adding custom certificate

QuickCertificate.Builder certificateBuilder = QuickCertificate.Builder.newQuickCertificate();
       certificateBuilder.add("host","public key");

//Adding certificate to quick sdk

QuickSdk.Builder builder =
QuickSdk.Builder.newInstance().setQuickCertificate(certificateBuilder.build()) 
                








