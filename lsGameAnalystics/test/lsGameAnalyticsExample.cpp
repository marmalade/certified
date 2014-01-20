
#include "s3e.h"
#include "lsGameAnalytics.h"
#include <string>

// GameAnalytics recommend Stop and Start to be called when the application pauses/resumes

void _Pause(void*, void*)
{
	if(lsGameAnalyticsIsInitialised())
		lsGameAnalyticsStopSession();
}

void _Resume(void*, void*)
{
	if(lsGameAnalyticsIsInitialised())
		lsGameAnalyticsStartSession();
}

int main()
{
	

	// Please enter your own KEYS here
	lsGameAnalyticsInitialise("2c37de60559ecc95e76527b17353c837139271ca","9c831ab81c32bfb40ad02a5f296979af", "1.0.0");
	lsGameAnalyticsStartSession();

	s3eDeviceRegister(S3E_DEVICE_PAUSE, (s3eCallback)_Pause, 0);
	s3eDeviceRegister(S3E_DEVICE_UNPAUSE, (s3eCallback)_Resume, 0);


	lsGameAnalyticsSetUserInfo("m", 1990, 22);
	lsGameAnalyticsNewDesignEvent("StartDesignEvent",20.0f,"main", 1.1f, 1.1f, 1.1f);
	
	lsGameAnalysticsNewBusinessEvent("Start", "£", 100, "Main");
	char userId[0xff];
	char display[0xff];

	lsGameAnalysticsGetUserId(userId);

	lsGameAnalyticsNewQualityEvent("Second::Test","Start Quality", "Main", 3.3f, 1.2f, 50.3f);

    while (!s3eDeviceCheckQuitRequest())
    {
        // Fill background blue
        s3eSurfaceClear(0, 0, 255);


        s3eDebugPrintf(120, 150, true, "UserId: %s",userId);

        // Flip the surface buffer to screen
        s3eSurfaceShow();

        // Sleep for 0ms to allow the OS to process events etc.
        s3eDeviceYield(0);
    }

	lsGameAnalyticsStopSession();
    return 0;
}
