/*
 * Internal header for the lsGameAnalytics extension.
 *
 * This file should be used for any common function definitions etc that need to
 * be shared between the platform-dependent and platform-indepdendent parts of
 * this extension.
 */

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#ifndef LSGAMEANALYTICS_INTERNAL_H
#define LSGAMEANALYTICS_INTERNAL_H

#include "s3eTypes.h"
#include "lsGameAnalytics.h"
#include "lsGameAnalytics_autodefs.h"


/**
 * Initialise the extension.  This is called once then the extension is first
 * accessed by s3eregister.  If this function returns S3E_RESULT_ERROR the
 * extension will be reported as not-existing on the device.
 */
s3eResult lsGameAnalyticsInit();

/**
 * Platform-specific initialisation, implemented on each platform
 */
s3eResult lsGameAnalyticsInit_platform();

/**
 * Terminate the extension.  This is called once on shutdown, but only if the
 * extension was loader and Init() was successful.
 */
void lsGameAnalyticsTerminate();

/**
 * Platform-specific termination, implemented on each platform
 */
void lsGameAnalyticsTerminate_platform();
void lsGameAnalyticsInitialise_platform(const char * secretKey, const char * gameKey, const char * build);

bool lsGameAnalyticsIsInitialised_platform();

bool lsGameAnalyticsIsSessionStarted_platform();

void lsGameAnalyticsStartSession_platform();

void lsGameAnalyticsStopSession_platform();

void lsGameAnalyticsNewDesignEvent_platform(const char * eventId, float value, const char * area, float x, float y, float z);

void lsGameAnalyticsNewQualityEvent_platform(const char * eventId, const char * message, const char * area, float x, float y, float z);

void lsGameAnalyticsNewErrorEvent_platform(const char * message, const char * severity, const char * area, float x, float y, float z);

void lsGameAnalyticsNewUserEvent_platform(const char * eventId, const char * gender, int birthYear, int friendCount, const char * area, float x, float y, float z);

void lsGameAnalyticsSetUserInfo_platform(const char * gender, int birthYear, int friendCount);

void lsGameAnalysticsSetReferralInfo_platform(const char * installPublisher, const char * installSite, const char * installCampaign, const char * installAdgroup, const char * installAd, const char * installKeyword);

void lsGameAnalysticsNewBusinessEvent_platform(const char * eventId, const char * currency, int amount, const char * area, float x, float y, float z);

void lsGameAnalysticsSetSendEventsInterval_platform(int millis);

void lsGameAnalysticsSetNetworkPollInterval_platform(int millis);

void lsGameAnalysticsSetSessionTimeOut_platform(int millis);

void lsGameAnalysticsLogFPS_platform();

void lsGameAnalysticsStopLoggingFPS_platform(const char * area, float x, float y, float z);

void lsGameAnalysticsSetCriticalFPSLimit_platform(int criticalFPS);

void lsGameAnalysticsSetMinimumFPSTimePeriod_platform(int minimumTimePeriod);

void lsGameAnalysticsLogUnhandledExceptions_platform();

void lsGameAnalysticsSetUserId_platform(const char * userId);

void lsGameAnalysticsSetDebugLogLevel_platform(int level);

void lsGameAnalysticsSetLocalCaching_platform(bool value);

void lsGameAnalysticsSetAutoBatch_platform(bool value);

void lsGameAnalysticsSetMaximumEventStorage_platform(int max);

void lsGameAnalysticsManualBatch_platform();

void lsGameAnalysticsGetUserId_platform(char * destination);

void lsGameAnalysticsClearDatabase_platform();


#endif /* !LSGAMEANALYTICS_INTERNAL_H */