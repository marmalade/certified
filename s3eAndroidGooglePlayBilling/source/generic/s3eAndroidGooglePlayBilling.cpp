/*
Generic implementation of the s3eAndroidGooglePlayBilling extension.
This file should perform any platform-indepedentent functionality
(e.g. error checking) before calling platform-dependent implementations.
*/

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#include "s3eAndroidGooglePlayBilling_internal.h"
s3eResult s3eAndroidGooglePlayBillingInit()
{
    //Add any generic initialisation code here
    return s3eAndroidGooglePlayBillingInit_platform();
}

void s3eAndroidGooglePlayBillingTerminate()
{
    //Add any generic termination code here
    s3eAndroidGooglePlayBillingTerminate_platform();
}

void s3eAndroidGooglePlayBillingStart(const char* base64PublicKey)
{
	s3eAndroidGooglePlayBillingStart_platform(base64PublicKey);
}

void s3eAndroidGooglePlayBillingStop()
{
	s3eAndroidGooglePlayBillingStop_platform();
}

s3eResult s3eAndroidGooglePlayBillingIsSupported()
{
	return s3eAndroidGooglePlayBillingIsSupported_platform();
}

void s3eAndroidGooglePlayBillingRequestPurchase(const char* productID, bool inApp, const char* developerPayLoad)
{
	s3eAndroidGooglePlayBillingRequestPurchase_platform(productID, inApp, developerPayLoad);
}

void s3eAndroidGooglePlayBillingRequestProductInformation(const char** inAppProducts, int numInAppProducts, const char** subProducts, int numSubProducts)
{
	s3eAndroidGooglePlayBillingRequestProductInformation_platform(inAppProducts, numInAppProducts, subProducts, numSubProducts);
}

void s3eAndroidGooglePlayBillingRestoreTransactions()
{
	s3eAndroidGooglePlayBillingRestoreTransactions_platform();
}

void s3eAndroidGooglePlayBillingConsumeItem(const char* purchaseToken)
{
	s3eAndroidGooglePlayBillingConsumeItem_platform(purchaseToken);
}
