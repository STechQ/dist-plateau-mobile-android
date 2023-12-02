### Plateau Android SDK

**Transfer your Low Code development with Plateau Studio to the Android platform in a few steps with Plateau SDK implementation.**

**#`Start`**

****1.Dependencies**　

***Gradle:***

    implementation "com.softtech.quick.sdk:version"

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
  
  @Override  
  protected void onCreate(@Nullable Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
		QuickSdk.Builder builder = QuickSdk.Builder.newInstance()  
                .setAppId("00000000000-*******-......")  
                .maxRequestRetryCount(0).setLanguage("tr-TR")  
                .setSettingsUrl(null)  
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
            v8Object.add("Email", "sukru.karaman@softtech.com.tr");  
            
            break;  
            case "GetPhoneNumber":  
                v8Object.add("PhoneNumber", "905514141986");  
            break;  
	
        resultListener.onFunctionResult(v8Object);  
      
    }








