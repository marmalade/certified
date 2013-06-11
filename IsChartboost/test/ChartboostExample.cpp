/*
 * This file provided to demonstrate how to use the IsChartboost Extension
 * which is a wrapper for www.chartboost.com sdk 
 *
 */

/**
 * @page IsChartboost ChartbooxtExample
 *
 * The follow is an example of how to use the IsChartboost Extension ,
 * requesting advertisements, registering for callbacks and processing responses
 * 
 * Requesting Callback for Ad Requests:
 *  - IsChartboostRequestAd
 *  - RequestCB
 *  - s3eSurfaceShow()
 *
 *
 * @include ChartboostExample.cpp
 */

#include "IsChartboost.h"

#include "s3e.h"
#include "s3eDebug.h"

#include <string>

std::string requestCB = "`xffffffrequestCB = ";
std::string advertclosed = "`xffffffadvertclosed = ";
std::string advertdismissed = "`xffffffadvertdismissed = "; 
std::string advertclicked = "`xffffffadvertclicked = ";

void RequestCB(void* System, void* User)
{
	char buff[256];
	int* value = (int*)System;
	sprintf(buff, "%s %i", "`xffffffrequestCB = Recieved Value ", *value);
	requestCB = buff;

	if(*value == 0)
		IsChartboostShowInterstitial("");
}


void AdvertisementClosed(void* System, void* User)
{
	advertclosed += "Callback recieved";
}

void AdvertisementDismissed(void* System, void* User)
{
	advertdismissed += "Callback recieved";
}

void AdvertisementClicked(void* System, void* User)
{
	advertclicked += "Callback recieved";
}

// Main entry point for the application
int main()
{
	
	// Set the Applicatiosn ID and Signature 
	// APP_ID and APP_SECRET is taken automatically from ICF Settings under 
	// [ISCHARTBOOST]
	// AndroidAppID = ""
	// AndroidAppSecret = ""
	// IOSAppID  = ""
	// IOSAppSecret = ""
	// 
	// Alternatively, You can call IsChartboostSetAppID() or IsChartboostSetAppSignature
    // Remember to setup your Chartboost Account and your package id in the deployment tool or mkb


	// Register for callbacks 
	IsChartboostRegister( ISCHARTBOOST_CALLBACK_REQUEST_RESPONSE, (s3eCallback)RequestCB, NULL);
	IsChartboostRegister( ISCHARTBOOST_CALLBACK_AD_CLOSED, (s3eCallback)AdvertisementClosed, NULL);
	IsChartboostRegister( ISCHARTBOOST_CALLBACK_AD_DISMISSED, (s3eCallback)AdvertisementDismissed, NULL);
	IsChartboostRegister( ISCHARTBOOST_CALLBACK_AD_CLICKED,(s3eCallback) AdvertisementClicked, NULL);

	//5. Start the Chartbooss Session
	IsChartboostStartSession();


	// Request an Interstitial
	IsChartboostRequestAd();
	//Alternatively IsChartboostShowInterstitial("");
    // Wait for a quit request from the host OS
    while (!s3eDeviceCheckQuitRequest())
    {
        // Fill background blue
        s3eSurfaceClear(0, 0, 255);

        // Print a line of debug text to the screen at top left (0,0)
        // Starting the text with the ` (backtick) char followed by 'x' and a hex value
        // determines the colour of the text.
        s3eDebugPrint(120, 150, requestCB.c_str(), 0);
		s3eDebugPrint(120, 180, advertclosed.c_str(), 0);
		s3eDebugPrint(120, 210, advertdismissed.c_str(), 0);
		s3eDebugPrint(120, 240, advertclicked.c_str(), 0);

		if(IsChartboostAvailable())
			s3eDebugPrintf(120, 270, true, "`xffffffChartboost Extension is Available");
		
        // Flip the surface buffer to screen
        s3eSurfaceShow();

        // Sleep for 0ms to allow the OS to process events etc.
        s3eDeviceYield(0);
    }

	// UnRegister Callbacks
	IsChartboostUnRegister( ISCHARTBOOST_CALLBACK_REQUEST_RESPONSE, (s3eCallback)RequestCB);
	IsChartboostUnRegister( ISCHARTBOOST_CALLBACK_AD_CLOSED, (s3eCallback)AdvertisementClosed);
	IsChartboostUnRegister( ISCHARTBOOST_CALLBACK_AD_DISMISSED, (s3eCallback)AdvertisementDismissed);
	IsChartboostUnRegister( ISCHARTBOOST_CALLBACK_AD_CLICKED,(s3eCallback) AdvertisementClicked);

    return 0;
}
