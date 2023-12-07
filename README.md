### Plateau Android SDK

**Transfer your Low Code development with Plateau Studio to the Android platform in a few steps with Plateau SDK implementation.**

**#`Start`**

****1.Dependencies**　

***Gradle:***

    implementation "com.softtech.quick.sdk:version"

    //Third party
    implementation 'com.facebook.soloader:soloader:0.10.4'
    implementation "com.airbnb.android:lottie:4.2.0"
    implementation "com.google.android.material:material:$materialVersion"
    implementation "com.google.dagger:dagger-android:$rootProject.ext.daggerVersion"
    implementation "com.google.code.gson:gson:$rootProject.ext.gsonVersion"
    implementation "com.squareup.retrofit2:converter-gson:$rootProject.ext.retrofitVersion"
    implementation "io.reactivex.rxjava3:rxandroid:$rootProject.ext.rxAndroidVersion"
    implementation "com.squareup.retrofit2:converter-scalars:$rootProject.ext.retrofitVersion"
    implementation "com.squareup.retrofit2:adapter-rxjava3:$rootProject.ext.retrofitVersion"
    implementation "com.jakewharton.timber:timber:$rootProject.ext.timberVersion"
    implementation 'org.conscrypt:conscrypt-android:2.2.1'
    implementation "com.squareup.okhttp3:okhttp:4.9.3"
    implementation "commons-beanutils:commons-beanutils:$rootProject.ext.apacheCommons.beanutilsVersion"
    implementation "org.apache.commons:commons-lang3:$rootProject.ext.apacheCommons.commonslangVersion"
    implementation "com.eclipsesource.j2v8:j2v8:6.2.1@aar"
    implementation 'com.facebook.yoga:yoga-layout:2.0.0'

    implementation "com.github.bumptech.glide:glide:$rootProject.ext.glideVersion"
    implementation 'com.jakewharton.rxbinding4:rxbinding-core:4.0.0'
    implementation "commons-codec:commons-codec:1.15"

    annotationProcessor "com.github.bumptech.glide:compiler:$rootProject.ext.glideVersion"

    def room_version = "2.4.0-alpha03"
    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"


 ***Maven:***

    <dependency>
      <groupId>com.softtech.quick.sdk</groupId>
      <artifactId>android</artifactId>
      <version>1.0.0</version>
    </dependency>

***Local:***

    dependencies {
    	implementation files('../quickcomponents.aar')
    	implementation files('../qcommon.aar')
    	implementation files('../renderingfw.aar')
    	implementation files('../networkfw.aar')
    	implementation files('../quickbridge.aar')
    	implementation files('../quickannotation.aar')
    	implementation files('../quickmobileandroid.aar')
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
                .setBaseUrl(SuperAppUrlMap.BASE_URL.getUrl());  
  
	quickService = builder.build(this).getQuickService();  
	quickService.initializeAsync(this);  
  }  
  
    @Override  
  public void onInitialized(QuickService quickService) {  
        quickService.injectActivity(this);  
	quickService.startMiniApp();  
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
  public void callFunction(String functionName, QV8Element params, QuickService.FunctionCallBackListener resultListener) {  
     QV8Object v8Object = new QV8Object();

//To add primitive type, use QV8Primitive(new QV8Primitive(true),new QV8Primitive(2),new QV8Primitive("string")).
//To add map type, use QV8Object (object = new QV8Object(),object.add("key",string|boolean,integer,float,QV8Primitive,QV8Array,QV8Object)).
//To add array type, use QV8Array (array = new QV8Array(),array.add(string|boolean,integer,float,QV8Primitive,QV8Array,QV8Object)).

     switch (functionName) {  
            case "GetIdentity":  
            v8Object.add("TCKN", "11111111118");  
            v8Object.add("Name", "Şükrü");  
            v8Object.add("Surname", "Karamann");  
            break; 
            
            case "GetMailAddress":  
            v8Object.add("Email", "email");  
            
            break;  
            case "GetPhoneNumber":  
                v8Object.add("PhoneNumber", "tel");  
            break;  
	
        resultListener.onFunctionResult(v8Object);  
      
    }


//Adding custom certificate

QuickCertificate.Builder certificateBuilder = QuickCertificate.Builder.newQuickCertificate();
       certificateBuilder.add("host","public key");

//Adding certificate to quick sdk

QuickSdk.Builder builder =
QuickSdk.Builder.newInstance().setQuickCertificate(certificateBuilder.build()) 
                








