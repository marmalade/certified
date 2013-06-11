/*
 * Internal header for the IsChartboost extension.
 *
 * This file should be used for any common function definitions etc that need to
 * be shared between the platform-dependent and platform-indepdendent parts of
 * this extension.
 */

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#ifndef ISCHARTBOOST_INTERNAL_H
#define ISCHARTBOOST_INTERNAL_H

#include "s3eTypes.h"
#include "IsChartboost.h"
#include "IsChartboost_autodefs.h"


/**
 * Initialise the extension.  This is called once then the extension is first
 * accessed by s3eregister.  If this function returns S3E_RESULT_ERROR the
 * extension will be reported as not-existing on the device.
 */
s3eResult IsChartboostInit();

/**
 * Platform-specific initialisation, implemented on each platform
 */
s3eResult IsChartboostInit_platform();

/**
 * Terminate the extension.  This is called once on shutdown, but only if the
 * extension was loader and Init() was successful.
 */
void IsChartboostTerminate();

/**
 * Platform-specific termination, implemented on each platform
 */
void IsChartboostTerminate_platform();
void IsChartboostSetAppID_platform(const char* id);

void IsChartboostSetAppSignature_platform(const char* signature);

void IsChartboostStartSession_platform();

void IsChartboostRequestAd_platform();

void IsChartboostCacheInterstitial_platform(const char* name);

void IsChartboostShowInterstitial_platform(const char* name);

void IsChartboostCacheMoreApps_platform();

void IsChartboostShowMoreApps_platform();


#endif /* !ISCHARTBOOST_INTERNAL_H */