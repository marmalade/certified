IsChartboost Extension for Marmalade SDK 6.3~

IsChartboost Extension Version v0.2

This Extension currently supports

//	IPHONE  
//  ANDROID 
        built using ndk android-ndk-r8b

//  ----------------------------------------------------
//  IsChartboost v0.2
//  ----------------------------------------------------
//	Chartboost Android Version: 3.1.3
//	Chartboost IOS Version: 3.2.1
        using ndk android-ndk-r8b

//  Targetting Marmalade SDK: 6.3.0

For additional information please see the official chartboost documentation
regarding intended behaviour, current functionality withing the chartboost sdk
and much more.
----------------------------------------------------


Getting Started
-----------------

1. MKB Adjustment - Subproject the IsChartboost Extension into your Marmalade Project
subprojects
{
	IsChartboost
}

2. MKB Adjustment - Set your Application Package to match your chartboost account
deployments
{
	["Default"]
	android-pkgname='com.company.chartboostexample'
}

3. Project Settings - Configure your IsChartboost settings

	IsChartboostSetAppID("APP_ID");
	IsChartboostSetAppSignature("APP_SIGNATURE");

    or using ICF Settings
    
    [ISCHARTBOOST]
    DisableChartboost = 0  
    AndroidAppID      = ""
    AndroidAppSecret  = ""  
    IOSAppID          = ""  
    IOSAppSecret      = ""   

4. Project Settings - Register for callbacks as needed

	IsChartboostRegister( ISCHARTBOOST_CALLBACK_REQUEST_RESPONSE, (s3eCallback)RequestCB, NULL);
	IsChartboostRegister( ISCHARTBOOST_CALLBACK_AD_CLOSED, (s3eCallback)AdvertisementClosed, NULL);
	IsChartboostRegister( ISCHARTBOOST_CALLBACK_AD_DISMISSED, (s3eCallback)AdvertisementDismissed, NULL);
	IsChartboostRegister( ISCHARTBOOST_CALLBACK_AD_CLICKED,(s3eCallback) AdvertisementClicked, NULL);

5. Project Settings - Start the IsChartboost Session 

	IsChartboostStartSession();

6. Project Settings - Begin making calls to IsChartboost

	IsChartboostRequestAd();

	or

	IsChartboostShowInterstitial("");

7. Project Settings - UnRegister Callbacks
	
	IsChartboostUnRegister( ISCHARTBOOST_CALLBACK_REQUEST_RESPONSE, (s3eCallback)RequestCB);
	IsChartboostUnRegister( ISCHARTBOOST_CALLBACK_AD_CLOSED, (s3eCallback)AdvertisementClosed);
	IsChartboostUnRegister( ISCHARTBOOST_CALLBACK_AD_DISMISSED, (s3eCallback)AdvertisementDismissed);
	IsChartboostUnRegister( ISCHARTBOOST_CALLBACK_AD_CLICKED,(s3eCallback) AdvertisementClicked);

Additional Information / Marmalade
----------------------------------

Android: Chartboost requires modification of the applications Manifest, this is done for you in the extension using

android-extra-application-manifest='ChartboostExtraAppManifest.xml'

Please see the Official Chartboost documentation as for why these permissions are required.

Available ICF Settings
----------------------------------

[TRACE]
CHARTBOOST              TRACE CHANNEL
CHARTBOOST_VERBOSE      TRACE CHANNEL VERBOSE

[ISCHARTBOOST]
DisableChartboost   <integer>   1 = enabled , 0 = disabled , defaults to 1 if nothing is set
AndroidAppID        <string>    Chartboost App ID for Android
AndroidAppSecret    <string>    Chartboost App Secret for Android
IOSAppID            <string>    Chartboost App ID for IOS
IOSAppSecret        <string>    Chartboost App Secret for IOS