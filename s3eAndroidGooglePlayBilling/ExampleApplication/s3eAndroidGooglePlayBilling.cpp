/**
 * @page This Application demonstrates usage of the Android Google Play Billing Marmalade extension.
 * The application is intended to be functional and demonstrate how to use the extension code without the distraction of complex UI.
 */

#include "s3eAndroidGooglePlayBilling.h"
#include "IwDebug.h"
#include "s3e.h"
#include "IwNUI.h"
#include "IwRandom.h"

#include "ExampleUI.h"
//#include "UnitTests.h"

//UnitTests *gTests;

// Button click handlers for the UI

const char *inAppSkus[] =
{
	"fluidtest.100coins",
	"fluidtest.200coins",
	"fluidtest.300coins",

};

const char *subSkus[] = 
{
	"fluidtest.subscription1",
	"fluidtest.subscription2"
};

// Query Shop
bool OnButton1Click(void* data, CButton* button)
{
	ExampleUI *ui = (ExampleUI*)data;
	ui->Log("Query Shop Items");
	s3eAndroidGooglePlayBillingRequestProductInformation(inAppSkus,sizeof(inAppSkus)/sizeof(const char*),subSkus,sizeof(subSkus)/sizeof(const char*));
	return true;
}

// Restore Purchases
bool OnButton2Click(void* data, CButton* button)
{
	ExampleUI *ui = (ExampleUI*)data;
	ui->Log("Attempting to Restore Purchases");
	s3eAndroidGooglePlayBillingRestoreTransactions();

	return true;
}

// Purchase fluidtest.100coins
bool OnButton3Click(void* data, CButton* button)
{
	ExampleUI *ui = (ExampleUI*)data;
	ui->Log("Attempting to purchase fluidtest.100coins");
	string randomPayload = string_format("TestPayload%d",IwRandMinMax(1,10000)); // let's be clear this is a test - in your code either don't set it or use something sensible you can check later on a different device
	s3eAndroidGooglePlayBillingRequestPurchase(inAppSkus[0],true,randomPayload.c_str());
	//s3eAndroidGooglePlayBillingRequestPurchase(subSkus[1],false,randomPayload.c_str());

	return true;
}

string hundredCoinsID;

// Consume fluidtest.100coins
bool OnButton4Click(void* data, CButton* button)
{
	ExampleUI *ui = (ExampleUI*)data;
	ui->Log("Attempting to consume fluidtest.100coins");
	if (hundredCoinsID.length() == 0)
		ui->Log("Error: no item to consume, try Restore if you restarted the Example app");
	else
		s3eAndroidGooglePlayBillingConsumeItem(hundredCoinsID.c_str());

	return true;
}

int32 ListCallback(void *systemData,void *userData)
{
    if ((systemData) && (userData))
    {
		ExampleUI* ui = (ExampleUI*)userData; // this is a pointer passed through from when the callback was registered
		s3eAndroidGooglePlayBillingSkuResponse *skus = (s3eAndroidGooglePlayBillingSkuResponse*)systemData;
		string str;
		if (skus->m_ErrorMsg) {
			str = string_format("List Sku returned : %d, %s", (int)skus->m_Status, skus->m_ErrorMsg);
			ui->Log(str);
		}
		if (skus->m_Status == S3E_ANDROIDGOOGLEPLAYBILLING_RESULT_OK)
		{
			str = string_format("%d items returned",skus->m_NumProducts);
			ui->Log(str);
			for (int i=0;i<skus->m_NumProducts;i++)
			{
				ui->Log("{");
				s3eAndroidGooglePlayBillingItemInfo *item = &skus->m_Products[i];
				ui->Log(string_format("	m_ProductID		: %s",item->m_ProductID));
				ui->Log(string_format("	m_Type			: %s",item->m_Type));
				ui->Log(string_format("	m_Price			: %s",item->m_Price));
				ui->Log(string_format("	m_Title			: %s",item->m_Title));
				ui->Log(string_format("	m_Description	: %s",item->m_Description));
				ui->Log("}");
			}
		}
	}
	return true;
}

int32 RestoreCallback(void *systemData,void *userData)
{
    if ((systemData) && (userData))
    {
		ExampleUI* ui = (ExampleUI*)userData; // this is a pointer passed through from when the callback was registered
		s3eAndroidGooglePlayBillingRestoreResponse *rr = (s3eAndroidGooglePlayBillingRestoreResponse*)systemData;
		string str;
		if (rr->m_ErrorMsg) {
			str = string_format("Restore returned : %d, %s", (int)rr->m_Status, rr->m_ErrorMsg);
			ui->Log(str);
		}
		if (rr->m_Status == S3E_ANDROIDGOOGLEPLAYBILLING_RESULT_OK)
		{
			str = string_format("%d items returned",rr->m_NumPurchases);
			ui->Log(str);
			for (int i=0;i<rr->m_NumPurchases;i++)
			{
				ui->Log("{");
				s3eAndroidGooglePlayBillingPurchase *item = &rr->m_Purchases[i];
				ui->Log(string_format("	m_OrderID			: %s",item->m_OrderID));
				ui->Log(string_format("	m_PackageID			: %s",item->m_PackageID));
				ui->Log(string_format("	m_ProductId			: %s",item->m_ProductId));
				ui->Log(string_format("	m_PurchaseTime		: %d",item->m_PurchaseTime));
				ui->Log(string_format("	m_PurchaseState		: %d",item->m_PurchaseState));
				ui->Log(string_format("	m_PurchaseToken		: %s",item->m_PurchaseToken));				
				ui->Log(string_format("	m_DeveloperPayload	: %s",item->m_DeveloperPayload));				
				ui->Log(string_format("	m_JSON				: %s",item->m_JSON));
				ui->Log(string_format("	m_Signature			: %s",item->m_Signature));
				ui->Log("}");

				if (!strcmp(item->m_ProductId,inAppSkus[0]))
				{
					hundredCoinsID = item->m_PurchaseToken;
					ui->SetConsumableText(hundredCoinsID);
				}
			}
		}
	}
	return true;
}

int32 PurchaseCallback(void *systemData,void *userData)
{
    if ((systemData) && (userData))
    {
		ExampleUI* ui = (ExampleUI*)userData; // this is a pointer passed through from when the callback was registered
		s3eAndroidGooglePlayBillingPurchaseResponse *pr = (s3eAndroidGooglePlayBillingPurchaseResponse*)systemData;
		string str;
		if (pr->m_ErrorMsg) {
			str = string_format("Purchase returned : %d, %s", (int)pr->m_Status, pr->m_ErrorMsg);
			ui->Log(str);
		}
		if (pr->m_Status == S3E_ANDROIDGOOGLEPLAYBILLING_RESULT_OK)
		{
			s3eAndroidGooglePlayBillingPurchase *item = pr->m_PurchaseDetails;
			ui->Log(string_format("	m_OrderID			: %s",item->m_OrderID));
			ui->Log(string_format("	m_PackageID			: %s",item->m_PackageID));
			ui->Log(string_format("	m_ProductId			: %s",item->m_ProductId));
			ui->Log(string_format("	m_PurchaseTime		: %d",item->m_PurchaseTime));
			ui->Log(string_format("	m_PurchaseState		: %d",item->m_PurchaseState));
			ui->Log(string_format("	m_PurchaseToken		: %s",item->m_PurchaseToken));				
			ui->Log(string_format("	m_DeveloperPayload	: %s",item->m_DeveloperPayload));				
			ui->Log(string_format("	m_JSON				: %s",item->m_JSON));
			ui->Log(string_format("	m_Signature			: %s",item->m_Signature));

			if (!strcmp(item->m_ProductId,inAppSkus[0]))
			{
				hundredCoinsID = item->m_PurchaseToken;
				ui->SetConsumableText(hundredCoinsID);
			}
		}
	}
	return true;
}

int32 ConsumeCallback(void *systemData,void *userData)
{
    if ((systemData) && (userData))
    {
		ExampleUI* ui = (ExampleUI*)userData; // this is a pointer passed through from when the callback was registered
		s3eAndroidGooglePlayBillingConsumeResponse *cr = (s3eAndroidGooglePlayBillingConsumeResponse*)systemData;
		string str;
		if (cr->m_ErrorMsg) {
			str = string_format("Purchase returned : %d, %s", (int)cr->m_Status, cr->m_ErrorMsg);
			ui->Log(str);
		}
		if (cr->m_Status == S3E_ANDROIDGOOGLEPLAYBILLING_RESULT_OK)
			ui->SetConsumableText("None");
	}
	return true;
}

// note this is the public license key provided by Google, not the one you sign this app with, it's in the developer console under Services & APIs

const char *publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgG+e7jDg7XMVq5sCuMM9O0O2ILYwMfWtvsVBJguK28+uXSU0jgWCWdNu4PyUsqpAxkpnLwMnX6OhU4Q5pIG3oRmTpMTSqGsx4pDfOQ901sW5n+k26t7YVTA2z/D0nDi/h528t7rn0AmEYUJwGESY75Xq9OFGFg3MHAO1M5Qxj6LBTX3BTASnRuGZSp9f5/tzh53SJa6vUl5Mej3McJh5ezbigbCzKhvwo0ziFA186KbOYG6va0wzrNaHFCo6IFlyZgpkoyjcWowKcFs3Ge8KvH6M4npHmE7X5iED5LJ4YjdOAAdfrlelooiVsZ75bzS8pN+iS3G64y9/4qLc7cQepQIDAQAB";
//const char *publicKey = "Put your public key here / if you dont do this / payments will succeed but the extension will still report them as having failed / also this string needs to be Base64 clean";

int main()
{
	char animatingText[] = "... Some Animating Text ...";
	uint64 animatingTextTimer;

	// seed RNG
	int32 ms = (int32)s3eTimerGetMs();
	IwRandSeed(ms);

	// create our Marmalade UI interface
    ExampleUI *ui = new ExampleUI(); 
	ui->Log("main()");
	//ui->EnableAllButtons(false);

	// Attempt to start up the Store interface
	s3eAndroidGooglePlayBillingStart(publicKey);

	// register callbacks and pass in our UI pointer which the callback
	s3eAndroidGooglePlayBillingRegister(S3E_ANDROIDGOOGLEPLAYBILLING_LIST_PRODUCTS_CALLBACK,ListCallback,ui); 
	s3eAndroidGooglePlayBillingRegister(S3E_ANDROIDGOOGLEPLAYBILLING_RESTORE_CALLBACK,RestoreCallback,ui); 
	s3eAndroidGooglePlayBillingRegister(S3E_ANDROIDGOOGLEPLAYBILLING_PURCHASE_CALLBACK,PurchaseCallback,ui); 
	s3eAndroidGooglePlayBillingRegister(S3E_ANDROIDGOOGLEPLAYBILLING_CONSUME_CALLBACK,ConsumeCallback,ui);

	ui->SetStatusText((s3eAndroidGooglePlayBillingIsSupported())?"Initialised":"Unitialised");

	// create the Unit Test singleton
	//gTests = new UnitTests(ui); // DH: Not implemented for this extension yet

	animatingTextTimer = s3eTimerGetMs();

    // run the app
	while (1)
	{
		//gTests->Update(); // update the tests if they're running

		//s3eAndroidGooglePlayBillingIsSupported()

		// animate the text
		if (s3eTimerGetMs() > animatingTextTimer + 20)
		{
			int len = strlen(animatingText);
			char c = animatingText[0];
			memmove(animatingText,animatingText+1,len-1);
			animatingText[len-1] = c;
			ui->SetAnimatingText(animatingText);
			animatingTextTimer = s3eTimerGetMs();
		}

		//ui->SetStatusText((s3eAndroidGooglePlayBillingIsSupported())?"Initialised":"Unitialised"); // annoying log spam
		ui->Update(); // update the UI
		s3eDeviceYield();
	}

    return 0;
}
