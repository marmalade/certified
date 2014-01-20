/*
 * android-specific implementation of the lsGameAnalytics extension.
 * Add any platform-specific functionality here.
 */
/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */
#include "lsGameAnalytics_internal.h"

#include "s3eEdk.h"
#include "s3eEdk_android.h"
#include <jni.h>
#include "IwDebug.h"

static jobject g_Obj;
static jmethodID g_lsGameAnalyticsInitialise;
static jmethodID g_lsGameAnalyticsIsInitialised;
static jmethodID g_lsGameAnalyticsIsSessionStarted;
static jmethodID g_lsGameAnalyticsStartSession;
static jmethodID g_lsGameAnalyticsStopSession;
static jmethodID g_lsGameAnalyticsNewDesignEvent;
static jmethodID g_lsGameAnalyticsNewQualityEvent;
static jmethodID g_lsGameAnalyticsNewErrorEvent;
static jmethodID g_lsGameAnalyticsNewUserEvent;
static jmethodID g_lsGameAnalyticsSetUserInfo;
static jmethodID g_lsGameAnalysticsSetReferralInfo;
static jmethodID g_lsGameAnalysticsNewBusinessEvent;
static jmethodID g_lsGameAnalysticsSetSendEventsInterval;
static jmethodID g_lsGameAnalysticsSetNetworkPollInterval;
static jmethodID g_lsGameAnalysticsSetSessionTimeOut;
static jmethodID g_lsGameAnalysticsLogFPS;
static jmethodID g_lsGameAnalysticsStopLoggingFPS;
static jmethodID g_lsGameAnalysticsSetCriticalFPSLimit;
static jmethodID g_lsGameAnalysticsSetMinimumFPSTimePeriod;
static jmethodID g_lsGameAnalysticsLogUnhandledExceptions;
static jmethodID g_lsGameAnalysticsSetUserId;
static jmethodID g_lsGameAnalysticsSetDebugLogLevel;
static jmethodID g_lsGameAnalysticsSetLocalCaching;
static jmethodID g_setAutoBatch;
static jmethodID g_lsGameAnalysticsSetMaximumEventStorage;
static jmethodID g_lsGameAnalysticsManualBatch;
static jmethodID g_lsGameAnalysticsGetUserId;
static jmethodID g_lsGameAnalysticsClearDatabase;

s3eResult lsGameAnalyticsInit_platform()
{
    // Get the environment from the pointer
    JNIEnv* env = s3eEdkJNIGetEnv();
    jobject obj = NULL;
    jmethodID cons = NULL;

    // Get the extension class
    jclass cls = env->FindClass("com/longshot/marmalade/lsgameanalytics/lsGameAnalytics");
    if (!cls)
        goto fail;

    // Get its constructor
    cons = env->GetMethodID(cls, "<init>", "()V");
    if (!cons)
        goto fail;

    // Construct the java class
    obj = env->NewObject(cls, cons);
    if (!obj)
        goto fail;

    // Get all the extension methods
    g_lsGameAnalyticsInitialise = env->GetMethodID(cls, "lsGameAnalyticsInitialise", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    if (!g_lsGameAnalyticsInitialise)
        goto fail;

    g_lsGameAnalyticsIsInitialised = env->GetMethodID(cls, "lsGameAnalyticsIsInitialised", "()Z");
    if (!g_lsGameAnalyticsIsInitialised)
        goto fail;

    g_lsGameAnalyticsIsSessionStarted = env->GetMethodID(cls, "lsGameAnalyticsIsSessionStarted", "()Z");
    if (!g_lsGameAnalyticsIsSessionStarted)
        goto fail;

    g_lsGameAnalyticsStartSession = env->GetMethodID(cls, "lsGameAnalyticsStartSession", "()V");
    if (!g_lsGameAnalyticsStartSession)
        goto fail;

    g_lsGameAnalyticsStopSession = env->GetMethodID(cls, "lsGameAnalyticsStopSession", "()V");
    if (!g_lsGameAnalyticsStopSession)
        goto fail;

    g_lsGameAnalyticsNewDesignEvent = env->GetMethodID(cls, "lsGameAnalyticsNewDesignEvent", "(Ljava/lang/String;FLjava/lang/String;FFF)V");
    if (!g_lsGameAnalyticsNewDesignEvent)
        goto fail;

    g_lsGameAnalyticsNewQualityEvent = env->GetMethodID(cls, "lsGameAnalyticsNewQualityEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;FFF)V");
    if (!g_lsGameAnalyticsNewQualityEvent)
        goto fail;

    g_lsGameAnalyticsNewErrorEvent = env->GetMethodID(cls, "lsGameAnalyticsNewErrorEvent", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;FFF)V");
    if (!g_lsGameAnalyticsNewErrorEvent)
        goto fail;

    g_lsGameAnalyticsNewUserEvent = env->GetMethodID(cls, "lsGameAnalyticsNewUserEvent", "(Ljava/lang/String;Ljava/lang/String;IILjava/lang/String;FFF)V");
    if (!g_lsGameAnalyticsNewUserEvent)
        goto fail;

    g_lsGameAnalyticsSetUserInfo = env->GetMethodID(cls, "lsGameAnalyticsSetUserInfo", "(Ljava/lang/String;II)V");
    if (!g_lsGameAnalyticsSetUserInfo)
        goto fail;

    g_lsGameAnalysticsSetReferralInfo = env->GetMethodID(cls, "lsGameAnalysticsSetReferralInfo", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    if (!g_lsGameAnalysticsSetReferralInfo)
        goto fail;

    g_lsGameAnalysticsNewBusinessEvent = env->GetMethodID(cls, "lsGameAnalysticsNewBusinessEvent", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;FFF)V");
    if (!g_lsGameAnalysticsNewBusinessEvent)
        goto fail;

    g_lsGameAnalysticsSetSendEventsInterval = env->GetMethodID(cls, "lsGameAnalysticsSetSendEventsInterval", "(I)V");
    if (!g_lsGameAnalysticsSetSendEventsInterval)
        goto fail;

    g_lsGameAnalysticsSetNetworkPollInterval = env->GetMethodID(cls, "lsGameAnalysticsSetNetworkPollInterval", "(I)V");
    if (!g_lsGameAnalysticsSetNetworkPollInterval)
        goto fail;

    g_lsGameAnalysticsSetSessionTimeOut = env->GetMethodID(cls, "lsGameAnalysticsSetSessionTimeOut", "(I)V");
    if (!g_lsGameAnalysticsSetSessionTimeOut)
        goto fail;

    g_lsGameAnalysticsLogFPS = env->GetMethodID(cls, "lsGameAnalysticsLogFPS", "()V");
    if (!g_lsGameAnalysticsLogFPS)
        goto fail;

    g_lsGameAnalysticsStopLoggingFPS = env->GetMethodID(cls, "lsGameAnalysticsStopLoggingFPS", "(Ljava/lang/String;FFF)V");
    if (!g_lsGameAnalysticsStopLoggingFPS)
        goto fail;

    g_lsGameAnalysticsSetCriticalFPSLimit = env->GetMethodID(cls, "lsGameAnalysticsSetCriticalFPSLimit", "(I)V");
    if (!g_lsGameAnalysticsSetCriticalFPSLimit)
        goto fail;

    g_lsGameAnalysticsSetMinimumFPSTimePeriod = env->GetMethodID(cls, "lsGameAnalysticsSetMinimumFPSTimePeriod", "(I)V");
    if (!g_lsGameAnalysticsSetMinimumFPSTimePeriod)
        goto fail;

    g_lsGameAnalysticsLogUnhandledExceptions = env->GetMethodID(cls, "lsGameAnalysticsLogUnhandledExceptions", "()V");
    if (!g_lsGameAnalysticsLogUnhandledExceptions)
        goto fail;

    g_lsGameAnalysticsSetUserId = env->GetMethodID(cls, "lsGameAnalysticsSetUserId", "(Ljava/lang/String;)V");
    if (!g_lsGameAnalysticsSetUserId)
        goto fail;

    g_lsGameAnalysticsSetDebugLogLevel = env->GetMethodID(cls, "lsGameAnalysticsSetDebugLogLevel", "(I)V");
    if (!g_lsGameAnalysticsSetDebugLogLevel)
        goto fail;

    g_lsGameAnalysticsSetLocalCaching = env->GetMethodID(cls, "lsGameAnalysticsSetLocalCaching", "(Z)V");
    if (!g_lsGameAnalysticsSetLocalCaching)
        goto fail;

    g_setAutoBatch = env->GetMethodID(cls, "lsGameAnalysticsSetAutoBatch", "(Z)V");
    if (!g_setAutoBatch)
        goto fail;

    g_lsGameAnalysticsSetMaximumEventStorage = env->GetMethodID(cls, "lsGameAnalysticsSetMaximumEventStorage", "(I)V");
    if (!g_lsGameAnalysticsSetMaximumEventStorage)
        goto fail;

    g_lsGameAnalysticsManualBatch = env->GetMethodID(cls, "lsGameAnalysticsManualBatch", "()V");
    if (!g_lsGameAnalysticsManualBatch)
        goto fail;

    g_lsGameAnalysticsGetUserId = env->GetMethodID(cls, "lsGameAnalysticsGetUserId", "()Ljava/lang/String;");
    if (!g_lsGameAnalysticsGetUserId)
        goto fail;

    g_lsGameAnalysticsClearDatabase = env->GetMethodID(cls, "lsGameAnalysticsClearDatabase", "()V");
    if (!g_lsGameAnalysticsClearDatabase)
        goto fail;



    IwTrace(LSGAMEANALYTICS, ("LSGAMEANALYTICS init success"));
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
        IwTrace(lsGameAnalytics, ("One or more java methods could not be found"));
    }
    return S3E_RESULT_ERROR;

}

void lsGameAnalyticsTerminate_platform()
{
    // Add any platform-specific termination code here
}

void lsGameAnalyticsInitialise_platform(const char * secretKey, const char * gameKey, const char * build)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring secretKey_jstr = env->NewStringUTF(secretKey);
    jstring gameKey_jstr = env->NewStringUTF(gameKey);
    jstring build_jstr = env->NewStringUTF(build);
    env->CallVoidMethod(g_Obj, g_lsGameAnalyticsInitialise, secretKey_jstr, gameKey_jstr, build_jstr);
}

bool lsGameAnalyticsIsInitialised_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    return (bool)env->CallBooleanMethod(g_Obj, g_lsGameAnalyticsIsInitialised);
}

bool lsGameAnalyticsIsSessionStarted_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    return (bool)env->CallBooleanMethod(g_Obj, g_lsGameAnalyticsIsSessionStarted);
}

void lsGameAnalyticsStartSession_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalyticsStartSession);
}

void lsGameAnalyticsStopSession_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalyticsStopSession);
}

void lsGameAnalyticsNewDesignEvent_platform(const char * eventId, float value, const char * area, float x, float y, float z)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring eventId_jstr = env->NewStringUTF(eventId);
    jstring area_jstr = env->NewStringUTF(area);
    env->CallVoidMethod(g_Obj, g_lsGameAnalyticsNewDesignEvent, eventId_jstr, value, area_jstr, x, y, z);
}

void lsGameAnalyticsNewQualityEvent_platform(const char * eventId, const char * message, const char * area, float x, float y, float z)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring eventId_jstr = env->NewStringUTF(eventId);
    jstring message_jstr = env->NewStringUTF(message);
    jstring area_jstr = env->NewStringUTF(area);
    env->CallVoidMethod(g_Obj, g_lsGameAnalyticsNewQualityEvent, eventId_jstr, message_jstr, area_jstr, x, y, z);
}

void lsGameAnalyticsNewErrorEvent_platform(const char * message, const char * severity, const char * area, float x, float y, float z)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring message_jstr = env->NewStringUTF(message);
    jstring severity_jstr = env->NewStringUTF(severity);
    jstring area_jstr = env->NewStringUTF(area);
    env->CallVoidMethod(g_Obj, g_lsGameAnalyticsNewErrorEvent, message_jstr, severity_jstr, area_jstr, x, y, z);
}

void lsGameAnalyticsNewUserEvent_platform(const char * eventId, const char * gender, int birthYear, int friendCount, const char * area, float x, float y, float z)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring eventId_jstr = env->NewStringUTF(eventId);
    jstring gender_jstr = env->NewStringUTF(gender);
    jstring area_jstr = env->NewStringUTF(area);
    env->CallVoidMethod(g_Obj, g_lsGameAnalyticsNewUserEvent, eventId_jstr, gender_jstr, birthYear, friendCount, area_jstr, x, y, z);
}

void lsGameAnalyticsSetUserInfo_platform(const char * gender, int birthYear, int friendCount)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring gender_jstr = env->NewStringUTF(gender);
    env->CallVoidMethod(g_Obj, g_lsGameAnalyticsSetUserInfo, gender_jstr, birthYear, friendCount);
}

void lsGameAnalysticsSetReferralInfo_platform(const char * installPublisher, const char * installSite, const char * installCampaign, const char * installAdgroup, const char * installAd, const char * installKeyword)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring installPublisher_jstr = env->NewStringUTF(installPublisher);
    jstring installSite_jstr = env->NewStringUTF(installSite);
    jstring installCampaign_jstr = env->NewStringUTF(installCampaign);
    jstring installAdgroup_jstr = env->NewStringUTF(installAdgroup);
    jstring installAd_jstr = env->NewStringUTF(installAd);
    jstring installKeyword_jstr = env->NewStringUTF(installKeyword);
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetReferralInfo, installPublisher_jstr, installSite_jstr, installCampaign_jstr, installAdgroup_jstr, installAd_jstr, installKeyword_jstr);
}

void lsGameAnalysticsNewBusinessEvent_platform(const char * eventId, const char * currency, int amount, const char * area, float x, float y, float z)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring eventId_jstr = env->NewStringUTF(eventId);
    jstring currency_jstr = env->NewStringUTF(currency);
    jstring area_jstr = env->NewStringUTF(area);
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsNewBusinessEvent, eventId_jstr, currency_jstr, amount, area_jstr, x, y, z);
}

void lsGameAnalysticsSetSendEventsInterval_platform(int millis)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetSendEventsInterval, millis);
}

void lsGameAnalysticsSetNetworkPollInterval_platform(int millis)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetNetworkPollInterval, millis);
}

void lsGameAnalysticsSetSessionTimeOut_platform(int millis)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetSessionTimeOut, millis);
}

void lsGameAnalysticsLogFPS_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsLogFPS);
}

void lsGameAnalysticsStopLoggingFPS_platform(const char * area, float x, float y, float z)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring area_jstr = env->NewStringUTF(area);
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsStopLoggingFPS, area_jstr, x, y, z);
}

void lsGameAnalysticsSetCriticalFPSLimit_platform(int criticalFPS)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetCriticalFPSLimit, criticalFPS);
}

void lsGameAnalysticsSetMinimumFPSTimePeriod_platform(int minimumTimePeriod)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetMinimumFPSTimePeriod, minimumTimePeriod);
}

void lsGameAnalysticsLogUnhandledExceptions_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsLogUnhandledExceptions);
}

void lsGameAnalysticsSetUserId_platform(const char * userId)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring userId_jstr = env->NewStringUTF(userId);
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetUserId, userId_jstr);
}

void lsGameAnalysticsSetDebugLogLevel_platform(int level)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetDebugLogLevel, level);
}

void lsGameAnalysticsSetLocalCaching_platform(bool value)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetLocalCaching, value);
}

void lsGameAnalysticsSetAutoBatch_platform(bool value)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_setAutoBatch, value);
}

void lsGameAnalysticsSetMaximumEventStorage_platform(int max)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsSetMaximumEventStorage, max);
}

void lsGameAnalysticsManualBatch_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsManualBatch);
}

void lsGameAnalysticsGetUserId_platform(char * destination)
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    jstring rtn = (jstring)env->CallObjectMethod(g_Obj, g_lsGameAnalysticsGetUserId);

	const char *cString = env->GetStringUTFChars(rtn, 0);

	snprintf(destination, sizeof(destination) / sizeof(char), "%s", cString);

    env->ReleaseStringUTFChars(rtn, cString);
}

void lsGameAnalysticsClearDatabase_platform()
{
    JNIEnv* env = s3eEdkJNIGetEnv();
    env->CallVoidMethod(g_Obj, g_lsGameAnalysticsClearDatabase);
}
