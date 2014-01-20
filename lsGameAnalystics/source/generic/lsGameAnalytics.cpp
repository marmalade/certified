/*
Generic implementation of the lsGameAnalytics extension.
This file should perform any platform-indepedentent functionality
(e.g. error checking) before calling platform-dependent implementations.
*/

/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */


#include "lsGameAnalytics_internal.h"
s3eResult lsGameAnalyticsInit()
{
    //Add any generic initialisation code here
    return lsGameAnalyticsInit_platform();
}

void lsGameAnalyticsTerminate()
{
    //Add any generic termination code here
    lsGameAnalyticsTerminate_platform();
}

void lsGameAnalyticsInitialise(const char * secretKey, const char * gameKey, const char * build)
{
	lsGameAnalyticsInitialise_platform(secretKey, gameKey, build);
}

bool lsGameAnalyticsIsInitialised()
{
	return lsGameAnalyticsIsInitialised_platform();
}

bool lsGameAnalyticsIsSessionStarted()
{
	return lsGameAnalyticsIsSessionStarted_platform();
}

void lsGameAnalyticsStartSession()
{
	lsGameAnalyticsStartSession_platform();
}

void lsGameAnalyticsStopSession()
{
	lsGameAnalyticsStopSession_platform();
}

void lsGameAnalyticsNewDesignEvent(const char * eventId, float value, const char * area, float x, float y, float z)
{
	lsGameAnalyticsNewDesignEvent_platform(eventId, value, area, x, y, z);
}

void lsGameAnalyticsNewQualityEvent(const char * eventId, const char * message, const char * area, float x, float y, float z)
{
	lsGameAnalyticsNewQualityEvent_platform(eventId, message, area, x, y, z);
}

void lsGameAnalyticsNewErrorEvent(const char * message, const char * severity, const char * area, float x, float y, float z)
{
	lsGameAnalyticsNewErrorEvent_platform(message, severity, area, x, y, z);
}

void lsGameAnalyticsNewUserEvent(const char * eventId, const char * gender, int birthYear, int friendCount, const char * area, float x, float y, float z)
{
	lsGameAnalyticsNewUserEvent_platform(eventId, gender, birthYear, friendCount, area, x, y, z);
}

void lsGameAnalyticsSetUserInfo(const char * gender, int birthYear, int friendCount)
{
	lsGameAnalyticsSetUserInfo_platform(gender, birthYear, friendCount);
}

void lsGameAnalysticsSetReferralInfo(const char * installPublisher, const char * installSite, const char * installCampaign, const char * installAdgroup, const char * installAd, const char * installKeyword)
{
	lsGameAnalysticsSetReferralInfo_platform(installPublisher, installSite, installCampaign, installAdgroup, installAd, installKeyword);
}

void lsGameAnalysticsNewBusinessEvent(const char * eventId, const char * currency, int amount, const char * area, float x, float y, float z)
{
	lsGameAnalysticsNewBusinessEvent_platform(eventId, currency, amount, area, x, y, z);
}

void lsGameAnalysticsSetSendEventsInterval(int millis)
{
	lsGameAnalysticsSetSendEventsInterval_platform(millis);
}

void lsGameAnalysticsSetNetworkPollInterval(int millis)
{
	lsGameAnalysticsSetNetworkPollInterval_platform(millis);
}

void lsGameAnalysticsSetSessionTimeOut(int millis)
{
	lsGameAnalysticsSetSessionTimeOut_platform(millis);
}

void lsGameAnalysticsLogFPS()
{
	lsGameAnalysticsLogFPS_platform();
}

void lsGameAnalysticsStopLoggingFPS(const char * area, float x, float y, float z)
{
	lsGameAnalysticsStopLoggingFPS_platform(area, x, y, z);
}

void lsGameAnalysticsSetCriticalFPSLimit(int criticalFPS)
{
	lsGameAnalysticsSetCriticalFPSLimit_platform(criticalFPS);
}

void lsGameAnalysticsSetMinimumFPSTimePeriod(int minimumTimePeriod)
{
	lsGameAnalysticsSetMinimumFPSTimePeriod_platform(minimumTimePeriod);
}

void lsGameAnalysticsLogUnhandledExceptions()
{
	lsGameAnalysticsLogUnhandledExceptions_platform();
}

void lsGameAnalysticsSetUserId(const char * userId)
{
	lsGameAnalysticsSetUserId_platform(userId);
}

void lsGameAnalysticsSetDebugLogLevel(int level)
{
	lsGameAnalysticsSetDebugLogLevel_platform(level);
}

void lsGameAnalysticsSetLocalCaching(bool value)
{
	lsGameAnalysticsSetLocalCaching_platform(value);
}

void lsGameAnalysticsSetAutoBatch(bool value)
{
	lsGameAnalysticsSetAutoBatch_platform(value);
}

void lsGameAnalysticsSetMaximumEventStorage(int max)
{
	lsGameAnalysticsSetMaximumEventStorage_platform(max);
}

void lsGameAnalysticsManualBatch()
{
	lsGameAnalysticsManualBatch_platform();
}

void lsGameAnalysticsGetUserId(char * destination)
{
	lsGameAnalysticsGetUserId_platform(destination);
}

void lsGameAnalysticsClearDatabase()
{
	lsGameAnalysticsClearDatabase_platform();
}
