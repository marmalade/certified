/*
 * WARNING: this is an autogenerated file and will be overwritten by
 * the extension interface script.
 */
/*
 * This file contains the automatically generated loader-side
 * functions that form part of the extension.
 *
 * This file is awlays compiled into all loaders but compiles
 * to nothing if this extension is not enabled in the loader
 * at build time.
 */
#include "IwDebug.h"
#include "lsGameAnalytics_autodefs.h"
#include "s3eEdk.h"
#include "lsGameAnalytics.h"
//Declarations of Init and Term functions
extern s3eResult lsGameAnalyticsInit();
extern void lsGameAnalyticsTerminate();


// On platforms that use a seperate UI/OS thread we can autowrap functions
// here.   Note that we can't use the S3E_USE_OS_THREAD define since this
// code is oftern build standalone, outside the main loader build.
#if defined I3D_OS_IPHONE || defined I3D_OS_OSX || defined I3D_OS_LINUX || defined I3D_OS_WINDOWS

static void lsGameAnalyticsInitialise_wrap(const char * secretKey, const char * gameKey, const char * build)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsInitialise"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsInitialise, 3, secretKey, gameKey, build);
}

static bool lsGameAnalyticsIsInitialised_wrap()
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsIsInitialised"));
    return (bool)(intptr_t)s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsIsInitialised, 0);
}

static bool lsGameAnalyticsIsSessionStarted_wrap()
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsIsSessionStarted"));
    return (bool)(intptr_t)s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsIsSessionStarted, 0);
}

static void lsGameAnalyticsStartSession_wrap()
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsStartSession"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsStartSession, 0);
}

static void lsGameAnalyticsStopSession_wrap()
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsStopSession"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsStopSession, 0);
}

static void lsGameAnalyticsNewDesignEvent_wrap(const char * eventId, float value, const char * area, float x, float y, float z)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsNewDesignEvent"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsNewDesignEvent, 6, eventId, value, area, x, y, z);
}

static void lsGameAnalyticsNewQualityEvent_wrap(const char * eventId, const char * message, const char * area, float x, float y, float z)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsNewQualityEvent"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsNewQualityEvent, 6, eventId, message, area, x, y, z);
}

static void lsGameAnalyticsNewErrorEvent_wrap(const char * message, const char * severity, const char * area, float x, float y, float z)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsNewErrorEvent"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsNewErrorEvent, 6, message, severity, area, x, y, z);
}

static void lsGameAnalyticsNewUserEvent_wrap(const char * eventId, const char * gender, int birthYear, int friendCount, const char * area, float x, float y, float z)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsNewUserEvent"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsNewUserEvent, 8, eventId, gender, birthYear, friendCount, area, x, y, z);
}

static void lsGameAnalyticsSetUserInfo_wrap(const char * gender, int birthYear, int friendCount)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalyticsSetUserInfo"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalyticsSetUserInfo, 3, gender, birthYear, friendCount);
}

static void lsGameAnalysticsSetReferralInfo_wrap(const char * installPublisher, const char * installSite, const char * installCampaign, const char * installAdgroup, const char * installAd, const char * installKeyword)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetReferralInfo"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetReferralInfo, 6, installPublisher, installSite, installCampaign, installAdgroup, installAd, installKeyword);
}

static void lsGameAnalysticsNewBusinessEvent_wrap(const char * eventId, const char * currency, int amount, const char * area, float x, float y, float z)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsNewBusinessEvent"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsNewBusinessEvent, 7, eventId, currency, amount, area, x, y, z);
}

static void lsGameAnalysticsSetSendEventsInterval_wrap(int millis)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetSendEventsInterval"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetSendEventsInterval, 1, millis);
}

static void lsGameAnalysticsSetNetworkPollInterval_wrap(int millis)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetNetworkPollInterval"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetNetworkPollInterval, 1, millis);
}

static void lsGameAnalysticsSetSessionTimeOut_wrap(int millis)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetSessionTimeOut"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetSessionTimeOut, 1, millis);
}

static void lsGameAnalysticsLogFPS_wrap()
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsLogFPS"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsLogFPS, 0);
}

static void lsGameAnalysticsStopLoggingFPS_wrap(const char * area, float x, float y, float z)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsStopLoggingFPS"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsStopLoggingFPS, 4, area, x, y, z);
}

static void lsGameAnalysticsSetCriticalFPSLimit_wrap(int criticalFPS)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetCriticalFPSLimit"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetCriticalFPSLimit, 1, criticalFPS);
}

static void lsGameAnalysticsSetMinimumFPSTimePeriod_wrap(int minimumTimePeriod)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetMinimumFPSTimePeriod"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetMinimumFPSTimePeriod, 1, minimumTimePeriod);
}

static void lsGameAnalysticsLogUnhandledExceptions_wrap()
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsLogUnhandledExceptions"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsLogUnhandledExceptions, 0);
}

static void lsGameAnalysticsSetUserId_wrap(const char * userId)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetUserId"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetUserId, 1, userId);
}

static void lsGameAnalysticsSetLocalCaching_wrap(bool value)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetLocalCaching"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetLocalCaching, 1, value);
}

static void lsGameAnalysticsSetAutoBatch_wrap(bool value)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetAutoBatch"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetAutoBatch, 1, value);
}

static void lsGameAnalysticsSetMaximumEventStorage_wrap(int max)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsSetMaximumEventStorage"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsSetMaximumEventStorage, 1, max);
}

static void lsGameAnalysticsManualBatch_wrap()
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsManualBatch"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsManualBatch, 0);
}

static void lsGameAnalysticsGetUserId_wrap(char * destination)
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsGetUserId"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsGetUserId, 1, destination);
}

static void lsGameAnalysticsClearDatabase_wrap()
{
    IwTrace(LSGAMEANALYTICS_VERBOSE, ("calling lsGameAnalytics func on main thread: lsGameAnalysticsClearDatabase"));
    s3eEdkThreadRunOnOS((s3eEdkThreadFunc)lsGameAnalysticsClearDatabase, 0);
}

#define lsGameAnalyticsInitialise lsGameAnalyticsInitialise_wrap
#define lsGameAnalyticsIsInitialised lsGameAnalyticsIsInitialised_wrap
#define lsGameAnalyticsIsSessionStarted lsGameAnalyticsIsSessionStarted_wrap
#define lsGameAnalyticsStartSession lsGameAnalyticsStartSession_wrap
#define lsGameAnalyticsStopSession lsGameAnalyticsStopSession_wrap
#define lsGameAnalyticsNewDesignEvent lsGameAnalyticsNewDesignEvent_wrap
#define lsGameAnalyticsNewQualityEvent lsGameAnalyticsNewQualityEvent_wrap
#define lsGameAnalyticsNewErrorEvent lsGameAnalyticsNewErrorEvent_wrap
#define lsGameAnalyticsNewUserEvent lsGameAnalyticsNewUserEvent_wrap
#define lsGameAnalyticsSetUserInfo lsGameAnalyticsSetUserInfo_wrap
#define lsGameAnalysticsSetReferralInfo lsGameAnalysticsSetReferralInfo_wrap
#define lsGameAnalysticsNewBusinessEvent lsGameAnalysticsNewBusinessEvent_wrap
#define lsGameAnalysticsSetSendEventsInterval lsGameAnalysticsSetSendEventsInterval_wrap
#define lsGameAnalysticsSetNetworkPollInterval lsGameAnalysticsSetNetworkPollInterval_wrap
#define lsGameAnalysticsSetSessionTimeOut lsGameAnalysticsSetSessionTimeOut_wrap
#define lsGameAnalysticsLogFPS lsGameAnalysticsLogFPS_wrap
#define lsGameAnalysticsStopLoggingFPS lsGameAnalysticsStopLoggingFPS_wrap
#define lsGameAnalysticsSetCriticalFPSLimit lsGameAnalysticsSetCriticalFPSLimit_wrap
#define lsGameAnalysticsSetMinimumFPSTimePeriod lsGameAnalysticsSetMinimumFPSTimePeriod_wrap
#define lsGameAnalysticsLogUnhandledExceptions lsGameAnalysticsLogUnhandledExceptions_wrap
#define lsGameAnalysticsSetUserId lsGameAnalysticsSetUserId_wrap
#define lsGameAnalysticsSetLocalCaching lsGameAnalysticsSetLocalCaching_wrap
#define lsGameAnalysticsSetAutoBatch lsGameAnalysticsSetAutoBatch_wrap
#define lsGameAnalysticsSetMaximumEventStorage lsGameAnalysticsSetMaximumEventStorage_wrap
#define lsGameAnalysticsManualBatch lsGameAnalysticsManualBatch_wrap
#define lsGameAnalysticsGetUserId lsGameAnalysticsGetUserId_wrap
#define lsGameAnalysticsClearDatabase lsGameAnalysticsClearDatabase_wrap

#endif

void lsGameAnalyticsRegisterExt()
{
    /* fill in the function pointer struct for this extension */
    void* funcPtrs[28];
    funcPtrs[0] = (void*)lsGameAnalyticsInitialise;
    funcPtrs[1] = (void*)lsGameAnalyticsIsInitialised;
    funcPtrs[2] = (void*)lsGameAnalyticsIsSessionStarted;
    funcPtrs[3] = (void*)lsGameAnalyticsStartSession;
    funcPtrs[4] = (void*)lsGameAnalyticsStopSession;
    funcPtrs[5] = (void*)lsGameAnalyticsNewDesignEvent;
    funcPtrs[6] = (void*)lsGameAnalyticsNewQualityEvent;
    funcPtrs[7] = (void*)lsGameAnalyticsNewErrorEvent;
    funcPtrs[8] = (void*)lsGameAnalyticsNewUserEvent;
    funcPtrs[9] = (void*)lsGameAnalyticsSetUserInfo;
    funcPtrs[10] = (void*)lsGameAnalysticsSetReferralInfo;
    funcPtrs[11] = (void*)lsGameAnalysticsNewBusinessEvent;
    funcPtrs[12] = (void*)lsGameAnalysticsSetSendEventsInterval;
    funcPtrs[13] = (void*)lsGameAnalysticsSetNetworkPollInterval;
    funcPtrs[14] = (void*)lsGameAnalysticsSetSessionTimeOut;
    funcPtrs[15] = (void*)lsGameAnalysticsLogFPS;
    funcPtrs[16] = (void*)lsGameAnalysticsStopLoggingFPS;
    funcPtrs[17] = (void*)lsGameAnalysticsSetCriticalFPSLimit;
    funcPtrs[18] = (void*)lsGameAnalysticsSetMinimumFPSTimePeriod;
    funcPtrs[19] = (void*)lsGameAnalysticsLogUnhandledExceptions;
    funcPtrs[20] = (void*)lsGameAnalysticsSetUserId;
    funcPtrs[21] = (void*)lsGameAnalysticsSetDebugLogLevel;
    funcPtrs[22] = (void*)lsGameAnalysticsSetLocalCaching;
    funcPtrs[23] = (void*)lsGameAnalysticsSetAutoBatch;
    funcPtrs[24] = (void*)lsGameAnalysticsSetMaximumEventStorage;
    funcPtrs[25] = (void*)lsGameAnalysticsManualBatch;
    funcPtrs[26] = (void*)lsGameAnalysticsGetUserId;
    funcPtrs[27] = (void*)lsGameAnalysticsClearDatabase;

    /*
     * Flags that specify the extension's use of locking and stackswitching
     */
    int flags[28] = { 0 };

    /*
     * Register the extension
     */
    s3eEdkRegister("lsGameAnalytics", funcPtrs, sizeof(funcPtrs), flags, lsGameAnalyticsInit, lsGameAnalyticsTerminate, 0);
}

#if !defined S3E_BUILD_S3ELOADER

#if defined S3E_EDK_USE_STATIC_INIT_ARRAY
int lsGameAnalyticsStaticInit()
{
    void** p = g_StaticInitArray;
    void* end = p + g_StaticArrayLen;
    while (*p) p++;
    if (p < end)
        *p = (void*)&lsGameAnalyticsRegisterExt;
    return 0;
}

int g_lsGameAnalyticsVal = lsGameAnalyticsStaticInit();

#elif defined S3E_EDK_USE_DLLS
S3E_EXTERN_C S3E_DLL_EXPORT void RegisterExt()
{
    lsGameAnalyticsRegisterExt();
}
#endif

#endif
