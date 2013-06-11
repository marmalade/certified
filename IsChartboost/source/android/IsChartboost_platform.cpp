/*
 * android-specific implementation of the IsChartboost extension.
 * Add any platform-specific functionality here.
 */
/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */
#include "IsChartboost_internal.h"

#include "s3eEdk.h"
#include "s3eEdk_android.h"
#include <jni.h>
#include "IwDebug.h"


//----------------------------------------------------------------------------------------
// Define JNI Statics Here
//----------------------------------------------------------------------------------------

static jobject g_Obj;
static jmethodID g_IsChartboostSetAppID;
static jmethodID g_IsChartboostSetAppSignature;
static jmethodID g_IsChartboostStartSession;
static jmethodID g_IsChartboostRequestAd;
static jmethodID g_IsChartboostCacheInterstitial;
static jmethodID g_IsChartboostShowInterstitial;
static jmethodID g_IsChartboostCacheMoreApps;
static jmethodID g_IsChartboostShowMoreApps;

int g_Result = 0;
jstring app_id;
jstring signature_jstr;

//----------------------------------------------------------------------------------------
// IsChartboostRequestCallback
//----------------------------------------------------------------------------------------
void IsChartboostRequestCallback(JNIEnv* env, jobject obj, jint result)
{
    g_Result = (int)result;
    IwTrace(IsChartboost, ("IsChartboostRequestCallback %i", g_Result));
    s3eEdkCallbacksEnqueue(S3E_EXT_ISCHARTBOOST_HASH, ISCHARTBOOST_CALLBACK_REQUEST_RESPONSE, &g_Result, sizeof(int));
}

//----------------------------------------------------------------------------------------
// IsChartboostAdClosedCallback
//----------------------------------------------------------------------------------------
void IsChartboostAdClosedCallback(JNIEnv* env, jobject obj, jint result)
{
    g_Result = (int)result;
    IwTrace(IsChartboost, ("IsChartboostAdClosedCallback %i", g_Result));
    s3eEdkCallbacksEnqueue(S3E_EXT_ISCHARTBOOST_HASH, ISCHARTBOOST_CALLBACK_AD_CLOSED, &g_Result, sizeof(int));
}

//----------------------------------------------------------------------------------------
// IsChartboostAdDismissedRequestCallback
//----------------------------------------------------------------------------------------
void IsChartboostAdDismissedRequestCallback(JNIEnv* env, jobject obj, jint result)
{
    g_Result = (int)result;
    IwTrace(IsChartboost, ("IsChartboostAdDismissedRequestCallback %i", g_Result));
    s3eEdkCallbacksEnqueue(S3E_EXT_ISCHARTBOOST_HASH, ISCHARTBOOST_CALLBACK_AD_DISMISSED, &g_Result, sizeof(int));
}


//----------------------------------------------------------------------------------------
// IsChartboostAdClickedRequestCallback
//----------------------------------------------------------------------------------------
void IsChartboostAdClickedRequestCallback(JNIEnv* env, jobject obj, jint result)
{
    g_Result = (int)result;
    IwTrace(IsChartboost, ("IsChartboostAdClickedRequestCallback %i", g_Result));
    s3eEdkCallbacksEnqueue(S3E_EXT_ISCHARTBOOST_HASH, ISCHARTBOOST_CALLBACK_AD_CLICKED, &g_Result, sizeof(int));
}


s3eResult IsChartboostInit_platform()
{
	s3eDebugOutputString("IsChartboostInit_platform");
    // Get the environment from the pointer
    JNIEnv* env = s3eEdkJNIGetEnv();
    jobject obj = NULL;
    jmethodID cons = NULL;
    jclass cls;
    jfieldID  field;
	char g_AppID[255];
	char g_AppSignature[255];

	const JNINativeMethod nativeMethodDefs[] =
    {
		{ "IsChartboostRequestCallback",				"(I)V",			(void*)&IsChartboostRequestCallback },
		{ "IsChartboostAdClosedCallback",				"(I)V",			(void*)&IsChartboostAdClosedCallback },
		{ "IsChartboostAdDismissedRequestCallback",				"(I)V",			(void*)&IsChartboostAdDismissedRequestCallback },
		{ "IsChartboostAdClickedRequestCallback",				"(I)V",			(void*)&IsChartboostAdClickedRequestCallback },
	};

    
    // Get the extension class
    cls = s3eEdkAndroidFindClass("com/isextension/IsChartboost");
    if (!cls)
        goto fail;

    // Setup and cache the Activity Field
    field = env->GetStaticFieldID(cls, "m_Activity", "Lcom/isextension/IsChartboost;");
    if (!field)
        goto fail;

    obj = env->GetStaticObjectField(cls, field);
    if (!obj)
        goto fail;
        
    // Get all the extension methods
    g_IsChartboostSetAppID = env->GetMethodID(cls, "IsChartboostSetAppID", "(Ljava/lang/String;)V");
    if (!g_IsChartboostSetAppID)
        goto fail;

    g_IsChartboostSetAppSignature = env->GetMethodID(cls, "IsChartboostSetAppSignature", "(Ljava/lang/String;)V");
    if (!g_IsChartboostSetAppSignature)
        goto fail;

    g_IsChartboostStartSession = env->GetMethodID(cls, "IsChartboostStartSession", "()V");
    if (!g_IsChartboostStartSession)
        goto fail;

    g_IsChartboostRequestAd = env->GetMethodID(cls, "IsChartboostRequestAd", "()V");
    if (!g_IsChartboostRequestAd)
        goto fail;

    g_IsChartboostCacheInterstitial = env->GetMethodID(cls, "IsChartboostCacheInterstitial", "(Ljava/lang/String;)V");
    if (!g_IsChartboostCacheInterstitial)
        goto fail;

    g_IsChartboostShowInterstitial = env->GetMethodID(cls, "IsChartboostShowInterstitial", "(Ljava/lang/String;)V");
    if (!g_IsChartboostShowInterstitial)
        goto fail;

    g_IsChartboostCacheMoreApps = env->GetMethodID(cls, "IsChartboostCacheMoreApps", "()V");
    if (!g_IsChartboostCacheMoreApps)
        goto fail;

    g_IsChartboostShowMoreApps = env->GetMethodID(cls, "IsChartboostShowMoreApps", "()V");
    if (!g_IsChartboostShowMoreApps)
        goto fail;

    if(env->RegisterNatives(cls, nativeMethodDefs, sizeof(nativeMethodDefs)/sizeof(nativeMethodDefs[0])))
		goto fail;

    IwTrace(CHARTBOOST, ("CHARTBOOST init success"));
    g_Obj = env->NewGlobalRef(obj);

    env->DeleteLocalRef(obj);
    env->DeleteGlobalRef(cls);
    // Add any platform-specific initialisation code here
    return S3E_RESULT_SUCCESS;

fail:
    jthrowable exc = env->ExceptionOccurred();
    if (exc)
    {
        env->ExceptionDescribe();
        env->ExceptionClear();
        IwTrace(IsChartboost, ("One or more java methods could not be found"));
    }
    return S3E_RESULT_ERROR;

}

void IsChartboostTerminate_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();

}

void IsChartboostSetAppID_platform(const char* id)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring id_jstr = env->NewStringUTF(id);
    env->CallVoidMethod(g_Obj, g_IsChartboostSetAppID, id_jstr);
}

void IsChartboostSetAppSignature_platform(const char* signature)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring signature_jstr = env->NewStringUTF(signature);
    env->CallVoidMethod(g_Obj, g_IsChartboostSetAppSignature, signature_jstr);
}

void IsChartboostStartSession_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_IsChartboostStartSession);
}

void IsChartboostRequestAd_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_IsChartboostRequestAd);
}

void IsChartboostCacheInterstitial_platform(const char* name)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring name_jstr = env->NewStringUTF(name);
    env->CallVoidMethod(g_Obj, g_IsChartboostCacheInterstitial, name_jstr);
}

void IsChartboostShowInterstitial_platform(const char* name)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring name_jstr = env->NewStringUTF(name);
    env->CallVoidMethod(g_Obj, g_IsChartboostShowInterstitial, name_jstr);
}

void IsChartboostCacheMoreApps_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_IsChartboostCacheMoreApps);
}

void IsChartboostShowMoreApps_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_IsChartboostShowMoreApps);
}
