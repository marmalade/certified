/*
java implementation of the s3eAndroidGooglePlayBilling extension.

Add android-specific functionality here.

These functions are called via JNI from native code.
*/

package com.ideaworks3d.marmalade.s3eAndroidGooglePlayBilling;

import java.util.Arrays;

import com.ideaworks3d.marmalade.LoaderAPI;

import com.ideaworks3d.marmalade.s3eAndroidGooglePlayBilling.util.*;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.os.Bundle;

import java.util.List;
import java.util.ArrayList;

class s3eAndroidGooglePlayBilling
{
	private static final String TAG = "s3eAndroidGooglePlayBilling";
    // The helper object
    public static IabHelper mHelper;
    private boolean s3eAndroidGooglePlayBillingAvailable = false;
    
    public int s3eAndroidGooglePlayBillingStart(String base64Key)
    {
	    Log.d(TAG, "s3eAndroidGooglePlayBillingStart called.");
    	if (base64Key == null)
    	{
            Log.d(TAG, "ERROR: No public key sent.");
            return 1; // S3E_RESULT_ERROR
    	}
        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(LoaderAPI.getActivity(),base64Key);
        mHelper.enableDebugLogging(true);
        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() 
        { // this stuff is asynchronous
            public void onIabSetupFinished(IabResult result) 
            {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) 
                {
                    // there was a problem.
                	Log.d(TAG, "Problem setting up in-app billing: " + result);
                    return;
                }
                s3eAndroidGooglePlayBillingAvailable = true;
                // IAB is fully set up
            }
        });
        return 0; // S3E_RESULT_SUCCESS - immediate response
    }
    
    public void s3eAndroidGooglePlayBillingStop()
    {
        // very important:
        Log.d(TAG, "s3eAndroidGooglePlayBillingStop called.");
        if (mHelper != null)
        {
        	mHelper.dispose();
        }
        else	
        {
            Log.d(TAG, "s3eAndroidGooglePlayBillingStop called without having been successfully started.");
        }
        mHelper = null;
        s3eAndroidGooglePlayBillingAvailable = false;
    }
    
    public int s3eAndroidGooglePlayBillingIsSupported()
    {
        return (s3eAndroidGooglePlayBillingAvailable)?1:0;
    }
    
    // Callback for when a purchase is finished - note this is static final and so the same listener is used across multiple instances of this class (which shouldn't happen as it's created once by Marmalade)
    public static final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() 
    {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) 
        {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (result.isFailure()) 
            {
            	safe_native_PURCHASE_CALLBACK(result,null);
                return;
            }
            
            Log.d(TAG, "Purchase successful.");

            safe_native_PURCHASE_CALLBACK(result,purchase);
        }
    };
   
    public void s3eAndroidGooglePlayBillingRequestPurchase(String productID, boolean inApp, String developerPayLoad)
    { 	
        Log.d(TAG, "s3eAndroidGooglePlayBillingRequestPurchase called for: "+productID);
        if (!s3eAndroidGooglePlayBillingAvailable){
    		IabResult err = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,"Android Market Billing is not available, did you call s3eAndroidGooglePlayBillingStart?");
    		safe_native_PURCHASE_CALLBACK(err,null);
    		return;
    	}
    	
    	Intent i = new Intent(LoaderAPI.getActivity(), PurchaseProxy.class);
    	i.putExtra("productID",productID);
    	i.putExtra("inApp",inApp);
    	i.putExtra("developerPayLoad",developerPayLoad);   	
    	
    	// launch our Activity inner class - it will close itself down when we have a response
    	LoaderAPI.getActivity().startActivity(i);
    }
    
    /**
     * The IabHelper adopts a rather obtuse approach to combining inventory with purchased products that doesn't
     * really fit our use pattern, so we've added our own version to the end of IabHelper.
     */
    
    public void s3eAndroidGooglePlayBillingRequestProductInformation(String[] inAppSkus, String[] subSkus)
    {
        Log.d(TAG, "s3eAndroidGooglePlayBillingRequestProductInformation called for:");
        if (inAppSkus != null)
            Log.d(TAG, "inApp: "+inAppSkus.toString());
        if (subSkus != null)
            Log.d(TAG, "subs: "+subSkus.toString());
    	if (!s3eAndroidGooglePlayBillingAvailable){
    		IabResult err = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,"Android Market Billing is not available, did you call s3eAndroidGooglePlayBillingStart?");
    		safe_native_LIST_PRODUCTS_CALLBACK(err,null);
    		return;
    	}
    	// kick off the async query
    	List<String> inAppList=null,subsList=null;
    	if (inAppSkus != null)
    		inAppList = Arrays.asList(inAppSkus);
    	if (subSkus != null)
    		subsList = Arrays.asList(subSkus);
    	
    	mHelper.querySkusAsync(inAppList, subsList, mGotSkusListener);
    }
    
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryProductsFinishedListener mGotSkusListener = new IabHelper.QueryProductsFinishedListener() {
        public void onQuerySkusFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query products finished.");
            if (result.isFailure()) {
        		safe_native_LIST_PRODUCTS_CALLBACK(result,null);    	
                return;
            }

            Log.d(TAG, "Query products was successful.");
            safe_native_LIST_PRODUCTS_CALLBACK(result,inventory);
        }
    };
 
    public void s3eAndroidGooglePlayBillingRestoreTransactions()
    {
        Log.d(TAG, "s3eAndroidGooglePlayBillingRestoreTransactions called.");
        if (!s3eAndroidGooglePlayBillingAvailable){
    		IabResult err = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,"Android Market Billing is not available, did you call s3eAndroidGooglePlayBillingStart?");
    		safe_native_RESTORE_CALLBACK(err,null);
    		return;
    	}
    	// kick off the async query
    	mHelper.queryInventoryAsync(false,null,mGotInventoryListener);
    }
    
    // Listener that's called when we finish querying the subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
        	if (!s3eAndroidGooglePlayBillingAvailable){
        		IabResult err = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,"Android Market Billing is not available, did you call s3eAndroidGooglePlayBillingStart?");
        		safe_native_RESTORE_CALLBACK(err,null);
        		return;
        	}

            Log.d(TAG, "Query inventory was successful.");
            
            safe_native_RESTORE_CALLBACK(result,inventory);
        }
    };

    public void s3eAndroidGooglePlayBillingConsumeItem(String purchaseToken)
    {
        Log.d(TAG, "s3eAndroidGooglePlayBillingConsumeItem called for: "+purchaseToken); 
    	if (!s3eAndroidGooglePlayBillingAvailable){
    		IabResult err = new IabResult(IabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE,"Android Market Billing is not available, did you call s3eAndroidGooglePlayBillingStart?");
    		safe_native_CONSUME_CALLBACK(err);
    		return;
    	}
    	// construct a Purchase the helper is happy with
    	Purchase purchase = new Purchase(purchaseToken);
    	// kick off the async query
    	mHelper.consumeAsync(purchase,mConsumeFinishedListener);
    }
    
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
            safe_native_CONSUME_CALLBACK(result);
        }
    };   
    
    /* Our native callbacks
     * Below is the C++ format for reference
     * void JNICALL s3e_ANDROIDGOOGLEPLAYBILLING_PURCHASE_CALLBACK( JNIEnv* env,  jobject obj, jint status, jstring errorMsg, jobject purchaseData);
	 * void JNICALL s3e_ANDROIDGOOGLEPLAYBILLING_LIST_PRODUCTS_CALLBACK( JNIEnv* env,  jobject obj, jint status, jstring errorMsg, jobjectArray products);
	 * void JNICALL s3e_ANDROIDGOOGLEPLAYBILLING_RESTORE_CALLBACK( JNIEnv* env,  jobject obj, jint status, jstring errorMsg, jobjectArray purchases);
	 * void JNICALL s3e_ANDROIDGOOGLEPLAYBILLING_CONSUME_CALLBACK( JNIEnv* env,  jobject obj, jint status, jstring errorMsg
     */
    
	public static native void native_PURCHASE_CALLBACK(int status,String errorMsg, S3eBillingPurchase purchase);
	public static native void native_LIST_PRODUCTS_CALLBACK(int status,String errorMsg, S3eBillingItemInfo[] skus);
	public static native void native_RESTORE_CALLBACK(int status,String errorMsg, S3eBillingPurchase[] purchases);
	public static native void native_CONSUME_CALLBACK(int status,String errorMsg);
	
	// simple public classes for the Native C++ callbacks
	
	public static class S3eBillingPurchase
	{
	 	public String m_OrderID;
	 	public String m_PackageID;
	 	public String m_ProductId;
	 	public long m_PurchaseTime;
	 	public int m_PurchaseState;
	 	public String m_PurchaseToken;
	 	public String m_DeveloperPayload;
	 	public String m_JSON;	 	
	 	public String m_Signature;
	 	public S3eBillingPurchase(Purchase p)
	 	{
		 	m_OrderID = p.getOrderId();
		 	m_PackageID = p.getPackageName();
		 	m_ProductId = p.getSku();
		 	m_PurchaseTime = p.getPurchaseTime();
		 	m_PurchaseState = p.getPurchaseState();
		 	m_PurchaseToken = p.getToken();
		 	m_DeveloperPayload = p.getDeveloperPayload();
		 	m_JSON = p.getOriginalJson();	 	
		 	m_Signature = p.getSignature();
	 	}
	};
	
	public static class S3eBillingItemInfo
	{
	 	public String m_ProductID;
	 	public String m_Type;
	 	public String m_Price;
	 	public String m_Title;
	 	public String m_Description;
	 	public S3eBillingItemInfo(SkuDetails s)
	 	{
	 		m_ProductID = s.getSku();
		 	m_Type = s.getType();
		 	m_Price = s.getPrice();
		 	m_Title = s.getTitle();
		 	m_Description = s.getDescription(); 		
	 	};
	};
	
	// Note, these are private static so they can be accessed from within the static nested Activity
    
	private static void safe_native_PURCHASE_CALLBACK(IabResult res, Purchase purchase)
	{
		// apparently there's no way to check if the JNI has bound this native function other than catch it 
		try 
		{
			S3eBillingPurchase p = null;
			if (purchase != null)
				p = new S3eBillingPurchase(purchase);
			native_PURCHASE_CALLBACK(res.getResponse(), res.getMessage(), p);
		}
        catch (UnsatisfiedLinkError e)
        {
           	Log.v(TAG, "No native handlers installed for safe_native_PURCHASE_CALLBACK, we received "+res.getResponse()+" "+res.getMessage());      	
        }
	}
	
	private static void safe_native_LIST_PRODUCTS_CALLBACK(IabResult res, Inventory inv)
	{
		// apparently there's no way to check if the JNI has bound this native function other than catch it 
		try 
		{
			if (inv != null)
			{
				List<SkuDetails> skus = inv.getAllSkus();		
				S3eBillingItemInfo[] s = new S3eBillingItemInfo[skus.size()];
				for (int i=0;i<skus.size();i++)
					s[i] = new S3eBillingItemInfo(skus.get(i));
				native_LIST_PRODUCTS_CALLBACK(res.getResponse(), res.getMessage(), s);
			}
			else
				native_LIST_PRODUCTS_CALLBACK(res.getResponse(), res.getMessage(), null);
		}
        catch (UnsatisfiedLinkError e)
        {
           	Log.v(TAG, "No native handlers installed for native_LIST_PRODUCTS_CALLBACK, we received "+res.getResponse()+" "+res.getMessage());      	
        }
	}
	
	private static void safe_native_RESTORE_CALLBACK(IabResult res, Inventory inv)
	{
		// apparently there's no way to check if the JNI has bound this native function other than catch it 
		try 
		{
			if (inv != null)
			{
				List<Purchase> purchases = inv.getAllPurchases();		
				S3eBillingPurchase[] p = new S3eBillingPurchase[purchases.size()];
				for (int i=0;i<purchases.size();i++)
					p[i] = new S3eBillingPurchase(purchases.get(i));
				native_RESTORE_CALLBACK(res.getResponse(), res.getMessage(), p);
			}
			else
				native_RESTORE_CALLBACK(res.getResponse(), res.getMessage(), null);
		}
        catch (UnsatisfiedLinkError e)
        {
           	Log.v(TAG, "No native handlers installed for native_LIST_PRODUCTS_CALLBACK, we received "+res.getResponse()+" "+res.getMessage());      	
        }
	}
	
	private static void safe_native_CONSUME_CALLBACK(IabResult res)
	{
		// apparently there's no way to check if the JNI has bound this native function other than catch it 
		try 
		{
			native_CONSUME_CALLBACK(res.getResponse(), res.getMessage());

		}
        catch (UnsatisfiedLinkError e)
        {
           	Log.v(TAG, "No native handlers installed for native_LIST_PRODUCTS_CALLBACK, we received "+res.getResponse()+" "+res.getMessage());      	
        }
	}
    
}
