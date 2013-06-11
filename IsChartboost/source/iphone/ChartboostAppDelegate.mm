//
//  ChartboostAppDelegate.m
//  ChartboostExample
//
//  Created by alfredo on 7/26/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ChartboostAppDelegate.h"
#import "ChartboostViewController.h"

#include "s3eDebug.h"
#include "s3eDevice.h"

#include "IsChartboost_internal.h"
#include "s3eEdk.h"
#include "s3eEdk_iphone.h"

#import "Chartboost.h"

namespace IsChartboostUtil
{
	static const int STR_MAX = 255;
	static bool g_IsAdRequested = false;
	static char buffer[STR_MAX];
	//-------------------------------------------------
	// Request an Ad
	//-------------------------------------------------

	void IsChartboostReqestAd()
	{
		g_IsAdRequested = true;
	}
	//-------------------------------------------------
	// IsChartboostIsRequestingAd
	//-------------------------------------------------
	static bool IsChartboostIsRequestingAd()
	{
		
		return g_IsAdRequested;
	}
	//-------------------------------------------------
	// IsChartboostResponseCB
	//-------------------------------------------------
	// for now, just send back 0 or 1 for all callbacks
	// not NSStrings
	static void IsChartboostResponseCB(int response)
	{
		s3eDebugOutputString("[Is3eChartboost] ChartboostResponseCB Called");

		if(IsChartboostIsRequestingAd())
		{
			//const char *response = [location cStringUsingEncoding:NSASCIIStringEncoding];
			//snprintf(buffer, STR_MAX, "%s", response);
			
			g_IsAdRequested = false;
			// For now, just send back 0 or 1
			s3eEdkCallbacksEnqueue(S3E_EXT_ISCHARTBOOST_HASH, ISCHARTBOOST_CALLBACK_REQUEST_RESPONSE, &response, sizeof(int));
		}
	}
	
	//-------------------------------------------------
	// IsChartboostDismissCB
	//-------------------------------------------------
	static void  IsChartboostDismissCB(NSString* location)
	{
		int response = 0;
		s3eEdkCallbacksEnqueue(S3E_EXT_ISCHARTBOOST_HASH, ISCHARTBOOST_CALLBACK_AD_DISMISSED, &response, sizeof(int));
	}
	//-------------------------------------------------
	// IsChartboostClicked
	//-------------------------------------------------
	static void IsChartboostClicked(NSString * location)
	{ 
		int response = 0;
		s3eEdkCallbacksEnqueue(S3E_EXT_ISCHARTBOOST_HASH, ISCHARTBOOST_CALLBACK_AD_CLICKED, &response, sizeof(int));
	}
	//-------------------------------------------------
	// IsChartboostClosed
	//-------------------------------------------------
	static void IsChartboostClosed(NSString * location)
	{ 
		int response = 0;
		s3eEdkCallbacksEnqueue(S3E_EXT_ISCHARTBOOST_HASH, ISCHARTBOOST_CALLBACK_AD_CLOSED, &response, sizeof(int));
	}

};


// Add the ChartboostDelegate to your AppDelegate
@interface ChartboostAppDelegate () <ChartboostDelegate>
@end

@implementation ChartboostAppDelegate


@synthesize window = _window;
@synthesize viewController = _viewController;



- (void)applicationDidBecomeActive:(UIApplication *)application
{
		
	s3eDebugOutputString("[s3eChartboost] Chartboost Active");    
}

// This lets your app know when an interstitial has been successfully cached
// You can use this to know which locations are ready to display immediately
- (void)didCacheInterstitial:(NSString *)location {
    s3eDebugOutputString("didCacheInterstitial with Location");
	
    IsChartboostUtil::IsChartboostResponseCB(0); // Notify Extension of a cache
}

// This is called when an interstitial has failed to load for any reason
// Possible causes are network connection or there's no publishing campaign setup for your app
- (void)didFailToLoadInterstitial:(NSString *)location {
    s3eDebugOutputString("[s3eChartboost] didFailToLoadInterstitial");
    
    IsChartboostUtil::IsChartboostResponseCB(1);    
    
}

// Same as above. If you get this, make sure you have added campaigns to the More Apps page setup for your app
// You can find this inside the App > Edit page in the Chartboost dashboard

- (void)didFailToLoadMoreApps {
    s3eDebugOutputString("[s3eChartboost] didFailToLoadMoreApps");
}

// This is used to control when an interstitial should or should not be displayed
// The default is YES, and that will let an interstitial display as normal
// If it's not preferable to display an interstitial, return NO to prevent the interstitial from displaying
- (BOOL)shouldDisplayInterstitial:(NSString *)location {
    s3eDebugOutputString("[s3eChartboost] shouldDisplayInterstitial with location");
        
    return YES;
}

// This is called when an interstitial is dismissed for a click, or a close.
// If you expect to show another interstitial at this location, use this to cache another when they close it.
- (void)didDismissInterstitial:(NSString *)location 
{
    s3eDebugOutputString("[s3eChartboost] s3eChartboost dismissed interstitial at location");
    
    IsChartboostUtil::IsChartboostDismissCB(location);
}


// Same as above, but only called when dismissed for a close
- (void)didCloseInterstitial:(NSString *)location
{
	IsChartboostUtil::IsChartboostClosed(location);
}

// Same as above, but only called when dismissed for a click
- (void)didClickInterstitial:(NSString *)location
{
	IsChartboostUtil::IsChartboostClicked(location);
}


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}


@end