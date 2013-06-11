/*
Generic implementation of the IsChartboost extension.
This file should perform any platform-indepedentent functionality
(e.g. error checking) before calling platform-dependent implementations.
*/

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#include "IsChartboost_internal.h"

#include "s3eDevice.h"
#include "s3eConfig.h"
#include "s3eDebug.h"

namespace 
{

	int s_DisableChartboost = 0;
	char* s_ApplicationID; 
	char* s_ApplicationSecret;

	bool IsDisabled()
    {
        static int val = -1;

        if(val = -1)
        {
            val = 1;
		    s3eConfigGetInt("ISCHARTBOOST", "DisableChartboost", &s_DisableChartboost);
        }
        
        return (s_DisableChartboost == 1);
    }
	
};

s3eResult IsChartboostInit()
{

	s_ApplicationID = new char[0xff];
	s_ApplicationSecret = new char[0xff];

	switch( s3eDeviceGetInt(S3E_DEVICE_OS) )
	{
	case S3E_OS_ID_IPHONE:
		{
			s3eConfigGetString("ISCHARTBOOST", "IOSAppID", s_ApplicationID );
			s3eConfigGetString("ISCHARTBOOST", "IOSAppSecret", s_ApplicationSecret );
		}
		break;
	case S3E_OS_ID_ANDROID:
		{
			s3eConfigGetString("ISCHARTBOOST", "AndroidAppID", s_ApplicationID  );
			s3eConfigGetString("ISCHARTBOOST",  "AndroidAppSecret" , s_ApplicationSecret);
		}
		break;
	default: 
		//s3eDebugAssertShow(S3E_MESSAGE_CONTINUE, "This platform is not supported. Disabling Extension");
		s_DisableChartboost = 1;
		break;
	}

	s3eResult rtn = IsChartboostInit_platform();
	if( S3E_RESULT_SUCCESS == rtn ) 
	{
		IsChartboostSetAppID(s_ApplicationID);
		IsChartboostSetAppSignature(s_ApplicationSecret);
	}
    //Add any generic initialisation code here
    return rtn;
}

void IsChartboostTerminate()
{
	delete s_ApplicationID;
	delete s_ApplicationSecret;
    //Add any generic termination code here
    IsChartboostTerminate_platform();
}

void IsChartboostSetAppID(const char* id)
{
	if(IsDisabled())
		return;

	IsChartboostSetAppID_platform(id);
}

void IsChartboostSetAppSignature(const char* signature)
{
	if(IsDisabled())
		return;

	IsChartboostSetAppSignature_platform(signature);
}

void IsChartboostStartSession()
{
	if(IsDisabled())
		return;

	IsChartboostStartSession_platform();
}

void IsChartboostRequestAd()
{
	if(IsDisabled())
		return;

	IsChartboostRequestAd_platform();
}

void IsChartboostCacheInterstitial(const char* name)
{
	if(IsDisabled())
		return;

	IsChartboostCacheInterstitial_platform(name);
}

void IsChartboostShowInterstitial(const char* name)
{
	if(IsDisabled())
		return;

	IsChartboostShowInterstitial_platform(name);
}

void IsChartboostCacheMoreApps()
{
	if(IsDisabled())
		return;

	IsChartboostCacheMoreApps_platform();
}

void IsChartboostShowMoreApps()
{
	if(IsDisabled())
		return;

	IsChartboostShowMoreApps_platform();
}
