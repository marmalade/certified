/*
 * iphone-specific implementation of the IsChartboost extension.
 * Add any platform-specific functionality here.
 */
/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */
#include "IsChartboost_internal.h"

#include "s3eDebug.h"
#include "s3eEdk.h"
#include "s3eEdk_iphone.h"
#include "s3eDevice.h"

#include "Chartboost.h"

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "ChartboostAppDelegate.h"


//--------------------------------------------------------------
// Variables || I Club Objective Seals
//--------------------------------------------------------------
static char g_appID[255] ;
static char g_signature[255];

static bool g_IsCallbackRequest;

s3eResult IsChartboostInit_platform()
{
	g_IsCallbackRequest = false;

    return S3E_RESULT_SUCCESS;
}

void IsChartboostTerminate_platform()
{
    // Add any platform-specific termination code here
}

void IsChartboostSetAppID_platform(const char* id)
{
	if(id == NULL)
		return;

	snprintf(g_appID, 255, "%s", id);
}

void IsChartboostSetAppSignature_platform(const char* signature)
{
	if(signature == NULL)
		return;

	snprintf(g_signature, 255, "%s", signature);
}

void IsChartboostStartSession_platform()
{

	Chartboost* pChartboost = [Chartboost sharedChartboost];
    pChartboost.appId = [NSString stringWithFormat:@"%s", g_appID];
    pChartboost.appSignature  = [NSString stringWithFormat:@"%s", g_signature];
    
    pChartboost.delegate = [ChartboostAppDelegate alloc];
 
    // Begin a user session. This should be done once per boot
    [pChartboost startSession];
    
    s3eDebugOutputString("LaunchingAppDelegate");	

}

void IsChartboostRequestAd_platform()
{
	IsChartboostUtil::IsChartboostReqestAd();
    [[Chartboost sharedChartboost] cacheInterstitial];	
}

void IsChartboostCacheInterstitial_platform(const char* name)
{
	[[Chartboost sharedChartboost] cacheInterstitial];	
}

void IsChartboostShowInterstitial_platform(const char* name)
{
	[[Chartboost sharedChartboost] showInterstitial];
}

void IsChartboostCacheMoreApps_platform()
{
	[[Chartboost sharedChartboost]  cacheMoreApps];
}

void IsChartboostShowMoreApps_platform()
{
	[[Chartboost sharedChartboost] showMoreApps];
}
