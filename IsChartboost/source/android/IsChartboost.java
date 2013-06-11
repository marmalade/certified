package com.isextension;

import com.ideaworks3d.marmalade.LoaderAPI;
import com.ideaworks3d.marmalade.LoaderActivity;

import android.util.Log;
import android.os.Bundle;
import com.chartboost.sdk.*;


public class IsChartboost extends LoaderActivity
{
    private final String TAG = "CHARTBOOST";
	private String APP_ID = null;
    private String APP_SIGNATURE = null;
    private Boolean setCallback = false;
    
    public static IsChartboost m_Activity = null;
    private Chartboost cb;
    
    // Native JNI functions used for EDK Callback
    public native void IsChartboostRequestCallback(int result);
    public native void IsChartboostAdClosedCallback(int result);
    public native void IsChartboostAdDismissedRequestCallback(int result);
    public native void IsChartboostAdClickedRequestCallback(int result);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        
        m_Activity = this;
        
		this.cb = Chartboost.sharedChartboost();
  
    }
    
    @Override
    protected void onPause()
    {
        Log.v(TAG, "onPause");
        super.onPause();
    }

     @Override
    protected void onResume()
    {
        Log.v(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		this.cb.onDestroy(this);
    }

	@Override
	protected void onStop() {
		super.onStop();

	 this.cb.onStop(this);

	}

    @Override
    public void onBackPressed() {

        // If an interstitial is on screen, close it. Otherwise continue as normal.
       if (this.cb.onBackPressed())
          return;
	   else
            super.onBackPressed();
    }
   
    public void IsChartboostCacheInterstitial(String name)
    {
        this.cb.cacheInterstitial(name);
	}

        public void IsChartboostSetAppID(String id)
    {
        if(id == null)
            return;
            		Log.i(TAG, "AppID Called with "+ id);

		APP_ID = id;

    }
    public void IsChartboostSetAppSignature(String signature)
    {
        if(signature == null)
            return;

        Log.i(TAG, "AppSig Called with "+ signature);

		APP_SIGNATURE = signature;
    }
    public void IsChartboostStartSession()
    {
		this.cb.onCreate(this, APP_ID, APP_SIGNATURE, chartBoostDelegate);
		this.cb.startSession();
		this.cb.onStart(this);
    }

    public void IsChartboostRequestAd()
    {
        setCallback = true;
        this.cb.showInterstitial();
    }
    public void IsChartboostShowInterstitial(String name)
    {
        this.cb.showInterstitial();
    }
    public void IsChartboostCacheMoreApps()
    {
        this.cb.cacheMoreApps();
    }
    public void IsChartboostShowMoreApps()
    {
        this.cb.showMoreApps();
    }
    private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() {

    // See https://help.chartboost.com/documentation/android/delegation
    // For more information on delegates and how they behave on android 
    // by chartboost.

        @Override 
        public boolean shouldRequestInterstitial(String paramString)
        {
            Log.i(TAG, "shouldRequestInterstitial");
            return true;
        }
        
        @Override 
        public boolean shouldDisplayInterstitial(String paramString)
        {
            Log.i(TAG, "shouldDisplayInterstitial CB Set to " +  setCallback + " : " + paramString);

            if(setCallback)
            {
                setCallback = false;
                Chartboost chartb = Chartboost.sharedChartboost();
                chartb.cacheInterstitial();
                IsChartboostRequestCallback(0); // Success
                return false;
            }

           return true;


        }
        @Override
        public void didCacheInterstitial(String paramString) {}

        @Override
        public void didFailToLoadInterstitial(String paramString)
        {
            IsChartboostRequestCallback(1); 
            Log.i(TAG, "INTERSTITIAL REQUEST FAILED");
            
        }

        @Override
        public void didDismissInterstitial(String paramString)
        {
            IsChartboostAdDismissedRequestCallback(1);
            Log.i(TAG, "INTERSTITIAL DISMISSED");
        }
        
        @Override
        public void didCloseInterstitial(String paramString)
        {
            IsChartboostAdClosedCallback(1);
            Log.i(TAG, "INSTERSTITIAL CLOSED");

        }

        @Override
        public void didClickInterstitial(String paramString)
        {
            IsChartboostAdClosedCallback(1);
            Log.i(TAG, "DID CLICK INTERSTITIAL");
        }
        
        @Override
        public void didShowInterstitial(String paramString){}
   
        @Override 
        public boolean shouldDisplayLoadingViewForMoreApps()
        {
            return true;
        }
        
        @Override
        public boolean shouldRequestMoreApps() {

            return true;
        }
        
        @Override
        public void didCacheMoreApps()
        {

        }

        @Override
        public boolean shouldDisplayMoreApps()
        {
            Log.i(TAG, "SHOULD DISPLAY MORE APPS?");
            return true;
        }
        
        @Override
        public void didFailToLoadMoreApps()
        {
            Log.i(TAG, "MORE APPS REQUEST FAILED");
   
        }

        @Override
        public void didDismissMoreApps()
        {
            Log.i(TAG, "MORE APPS DISMISSED");
        }
        
        @Override
        public void didCloseMoreApps()
        {
            Log.i(TAG, "MORE APPS CLOSED");
        }

        @Override
        public void didClickMoreApps()
        {
            Log.i(TAG, "MORE APPS CLICKED");
        }       

        @Override
        public void didShowMoreApps()
        {

        }

        @Override
        public boolean shouldRequestInterstitialsInFirstSession()
        {
            return true;
        }
    };

}

