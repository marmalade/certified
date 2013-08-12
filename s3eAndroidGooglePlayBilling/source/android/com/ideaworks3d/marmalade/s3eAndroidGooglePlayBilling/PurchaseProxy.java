/*
java implementation of the s3eAndroidGooglePlayBilling extension.
*/

package com.ideaworks3d.marmalade.s3eAndroidGooglePlayBilling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ideaworks3d.marmalade.LoaderAPI;

import com.ideaworks3d.marmalade.s3eAndroidGooglePlayBilling.util.*;

public class PurchaseProxy extends Activity
{
    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 3147;
    private static final String TAG = "PurchaseProxy";
    private static boolean proxyIsActive = false;

	// Because this is activity is pushed atop the current, if it has been paused
	// assume the application has been paused 
	protected void onPause()
	{
	    Log.d(TAG, "Proxy OnPause");
		super.onPause();

		if (this.isFinishing())
		{
			Log.d(TAG, "Finishing Proxy..... return..");
			return;
		}

		proxyIsActive = false;
		finish();

	}

	protected void onResume()
	{
	    Log.d(TAG, "Proxy onResume");
		super.onResume();
	}
	 /**
     * 
     * Helper Activity used during the purchase flow, the only reason this is required is to get hold of the
     * onActivityResult response from the purchase intent and is easier and more convenient than forcing the 
     * implementor to subclass LoaderActivity.
     * Note, this activity will be the foreground Activity in front of the app till the purchase flow completes.
     *
     */
    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        if (proxyIsActive)
	        { // Some system reconfiguration event (rotate etc) caused our Activity to be recreated - don't start the helper again
		        Log.d(TAG, "PurchaseProxy Activity has been restarted with Helper in flight - notifying IabHelper");
		        s3eAndroidGooglePlayBilling.mHelper.SetPurchaseListener(this.mProxyPurchaseFinishedListener);
	        }
	        else
	        {
		        Log.d(TAG, "PurchaseProxy Activity has been started");
		        proxyIsActive = true;       
		        Intent i = getIntent();
		        String productID = i.getStringExtra("productID");
		        String developerPayLoad = i.getStringExtra("developerPayLoad");
		        boolean inApp = i.getBooleanExtra("inApp",true);  
		        
		        if (inApp)
		        	s3eAndroidGooglePlayBilling.mHelper.launchPurchaseFlow(this, productID, RC_REQUEST, mProxyPurchaseFinishedListener, developerPayLoad);
		        else
		        	s3eAndroidGooglePlayBilling.mHelper.launchSubscriptionPurchaseFlow(this, productID, RC_REQUEST, mProxyPurchaseFinishedListener, developerPayLoad);
	        }
	    }		
	    
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

	        // Pass on the activity result to the helper for handling
	        if (!s3eAndroidGooglePlayBilling.mHelper.handleActivityResult(requestCode, resultCode, data)) {
	            // not handled, so handle it ourselves (here's where you'd
	            // perform any handling of activity results not related to in-app
	            // billing...
	            super.onActivityResult(requestCode, resultCode, data);
	        }
	        else {
	            Log.d(TAG, "onActivityResult forwarded by PurchaseProxy. Closing down.");
	            //finish(); // listener will close down Activity
	        }
	    }
	    
	    // Pass through callback for when a purchase is finished - this is our chance to shut the proxy down    
	    public IabHelper.OnIabPurchaseFinishedListener mProxyPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() 
	    {
	        public void onIabPurchaseFinished(IabResult result, Purchase purchase) 
	        {
				Log.d(TAG, "onIabPurchaseFinished");

	        	s3eAndroidGooglePlayBilling.mPurchaseFinishedListener.onIabPurchaseFinished(result,purchase);
		        Log.d(TAG, "PurchaseProxy Activity - Closing Activity");
	            proxyIsActive = false;
				finish(); // Activity is done
	        }
	    };
};