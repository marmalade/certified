# Android Google Play Billing

Summary
=======

Android Google Play Billion Extension for Marmalade
Marmalade Certified

Current Version: 1.0.2
Target Marmalade SDK: 6.2.2

This is a Marmalade extension that implements an interface to Google Android Billing v3.

The extension is built for Google Billing v3 which is supported on devices from Android 2.2 (Froyo API 7) onwards. The devices needs to have an up to date Google Play Store installed. Using Marmalade 6.2.2.

I have included a buildable example, however in order to run it you will need to provide your own keys and Google Developer details (see below).

V 1.0.2 – Fixed an issue with the Proxy Activity being recreated under the IabHelper
V 1.0.1 – initial release. 

David Hunt

Marmalade
=========

Marmalade acknowledges that this extension is considered certified but does not guarantee its maintenance. All certified Marmalade extensions are open sourced and we encourage users to make modifications to suite their needs.

Documentation
=============

/Readme.doc of this documentation has been provided for users to download An example has been provided under /ExampleApplication which will require modification as stated in /Readme.doc or under the Instructions 


Instructions:
-------------

- Set up IAP for your product in the Google Developer Console
- Copy this folder into your project path and add the relative path to the extension mkf file to your main mkb. For instance 
    subprojects
    {
        ../ s3eAndroidGooglePlayBilling/ s3eAndroidGooglePlayBilling.mkf
    }
- Include s3eAndroidGooglePlayBilling.h
- On Google Developer Console, select your app, go to Services and APIs, locate the Base64 encoded RSA public key and copy it into a string in your App. (Note this isn’t the key you use to sign your App, it’s your Google public license key).
- Initialise the extension with this key by calling s3eAndroidGooglePlayBillingStart.
- See the header file for additional documentation.

Example Application
-------------------

- I have included an Example app. In order to get it to work you will need to:
- Replace the publicKey string with your Base64 public license key from the Google Developer console -> Services & APIs.
- Edit the inAppSkus and subSkus string arrays to match your IAP product names.
- Provision your own keystore for signing the app.


What does this extension support?
---------------------------------

- Purchase requests – see s3eAndroidGooglePlayBillingRequestPurchase
- Restoring previous transactions – see s3eAndroidGooglePlayBillingRestoreTransactions
- Retrieving details of items in your store – see s3eAndroidGooglePlayBillingRequestProductInformation
- Consumption of items previously purchased – see s3eAndroidGooglePlayBillingConsumeItem
- All calls are asynchronous, meaning in order to receive the response you need to register a callback. See s32AndroidGooglePlayBillingRegister.
- The v3 interface moved to managed “in App” and “subscription” items only. When a user purchases a  managed item they will be unable to purchase it again until you issue a consume call. For in game Consumable goods such as coins that you want the user to be able to purchase repeatedly, you must issue a Consume call after each successful purchase. Subscription items can not be consumed and made available again on the store.
- It is in your interests to restore purchases at each restart and check for unconsumed consumable goods.

Security

- Google signs all purchase and restore responses using your private license key, the extension verifies the signature using the public license key before sending the response.  (Note this is not the keystore key you sign the apk with).
- For additional security you can also set a payload string for each purchase that is made, this will be sent back to you with the purchase and when restoring. You can then verify this with either an external server or by other private means.
- Don’t use debug keys to sign your App. Specifically set up the Application Keystore during deploy for all builds. Deploy Tool -> Android specific -> Application Keystore. 


Hints (Important)
-----------------

- Make sure your bundle ID is set correctly to match your product in the Google Developer console eg. com.example.mygame. This is set in the deploy tool -> Android specific -> Advanced -> Android Package Name.
- You need to upload at least on apk before you can start adding IAP to the Google Developer Console.
- The version of the App you test has to match the version of the app you uploaded. If it doesn’t then you will get “this version of the application is not enabled for in-app billing” errors.
- You may have to wait a couple of hours after the upload for the apk to process. (though in practise it is a lot faster)
- Don’t add un-managed IAP items
- Before you’ve published the App you will need to add a test account in order to test it.
- Don’t use your developer account (the one that owns the Developer account) as a test account (Google state this won’t work).
- Devices need to have an up to date Google Play Store installed or you will get S3E_ANDROIDGOOGLEPLAYBILLING_RESULT_BILLING_UNAVAILABLE.










