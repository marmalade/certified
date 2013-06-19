/*
 * Internal header for the s3eAndroidMarketGooglePlay extension.
 *
 * This file should be used for any common function definitions etc that need to
 * be shared between the platform-dependent and platform-indepdendent parts of
 * this extension.
 */

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#ifndef S3EANDROIDMARKETGOOGLEPLAY_INTERNAL_H
#define S3EANDROIDMARKETGOOGLEPLAY_INTERNAL_H

#include "s3eTypes.h"
#include "s3eAndroidMarketGooglePlay.h"
#include "s3eAndroidMarketGooglePlay_autodefs.h"


/**
 * Initialise the extension.  This is called once then the extension is first
 * accessed by s3eregister.  If this function returns S3E_RESULT_ERROR the
 * extension will be reported as not-existing on the device.
 */
s3eResult s3eAndroidMarketGooglePlayInit();

/**
 * Platform-specific initialisation, implemented on each platform
 */
s3eResult s3eAndroidMarketGooglePlayInit_platform();

/**
 * Terminate the extension.  This is called once on shutdown, but only if the
 * extension was loader and Init() was successful.
 */
void s3eAndroidMarketGooglePlayTerminate();

/**
 * Platform-specific termination, implemented on each platform
 */
void s3eAndroidMarketGooglePlayTerminate_platform();
s3eResult s3eAndroidMarketGooglePlayIsBillingSupported_platform();

s3eIOSAndroidMarketGooglePlayItemInfo* s3eAndroidMarketGooglePlayGetItemData_platform(const char** skuList, const int listSize);

s3eIOSAndroidMarketGooglePlayPurchaseInfo* s3eAndroidMarketGooglePlayPurchaseItem_platform(const char* productID, const char* develperPayLoad);

int s3eAndroidMarketGooglePlayQueryPurchasedItemAmount_platform();

s3eIOSAndroidMarketGooglePlayPurchaseInfo* s3eAndroidMarketGooglePlayQueryPurchasedItem_platform(const int itemNo);


#endif /* !S3EANDROIDMARKETGOOGLEPLAY_INTERNAL_H */