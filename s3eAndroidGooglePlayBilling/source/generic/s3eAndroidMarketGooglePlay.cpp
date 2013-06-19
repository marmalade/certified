/*
Generic implementation of the s3eAndroidMarketGooglePlay extension.
This file should perform any platform-indepedentent functionality
(e.g. error checking) before calling platform-dependent implementations.
*/

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#include "s3eAndroidMarketGooglePlay_internal.h"
s3eResult s3eAndroidMarketGooglePlayInit()
{
    //Add any generic initialisation code here
    return s3eAndroidMarketGooglePlayInit_platform();
}

void s3eAndroidMarketGooglePlayTerminate()
{
    //Add any generic termination code here
    s3eAndroidMarketGooglePlayTerminate_platform();
}

s3eResult s3eAndroidMarketGooglePlayIsBillingSupported()
{
	return s3eAndroidMarketGooglePlayIsBillingSupported_platform();
}

s3eIOSAndroidMarketGooglePlayItemInfo* s3eAndroidMarketGooglePlayGetItemData(const char** skuList, const int listSize)
{
	return s3eAndroidMarketGooglePlayGetItemData_platform(skuList, listSize);
}

s3eIOSAndroidMarketGooglePlayPurchaseInfo* s3eAndroidMarketGooglePlayPurchaseItem(const char* productID, const char* develperPayLoad)
{
	return s3eAndroidMarketGooglePlayPurchaseItem_platform(productID, develperPayLoad);
}

int s3eAndroidMarketGooglePlayQueryPurchasedItemAmount()
{
	return s3eAndroidMarketGooglePlayQueryPurchasedItemAmount_platform();
}

s3eIOSAndroidMarketGooglePlayPurchaseInfo* s3eAndroidMarketGooglePlayQueryPurchasedItem(const int itemNo)
{
	return s3eAndroidMarketGooglePlayQueryPurchasedItem_platform(itemNo);
}
