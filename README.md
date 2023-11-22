### Plateau Android SDK

**Transfer your Low Code development with Plateau Studio to the Android platform in a few steps with Plateau SDK implementation.**

#`Start`

**1.Dependencies　

```gradle
dependencies {
	implementation files('../quickcomponents.aar')
	implementation files('../qcommon.aar')
	implementation files('../renderingfw.aar')
	implementation files('../networkfw.aar')
	implementation files('../quickbridge.aar')
	implementation files('../quickannotation.aar')
	implementation files('../quickmobileandroid.aar')
}
```

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
					   .maxRequestRetryCount(0) .setLanguage("tr-TR")
						.setSettingsUrl(null)
						.setClientCustomFunctionTriggerListener(this)
						.timeOutRequestSeconds(60)
						.setBaseUrl(SuperAppUrlMap.BASE_URL.getUrl());
				
           quickService = builder.build(this).getQuickService();
           quickService.initializeAsync(this); }
    @Override
    public void onInitialized(QuickService quickService) {     
	       quickService.injectActivity(this); quickService.startMiniApp();
    }
    @Override
    protected void onDestroy() {
	      super.onDestroy(); quickService.release();
    }
```






