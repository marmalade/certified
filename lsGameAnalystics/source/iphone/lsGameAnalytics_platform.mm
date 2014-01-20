/*
 * iphone-specific implementation of the lsGameAnalytics extension.
 * Add any platform-specific functionality here.
 */
/*
 * NOTE: This file was originally written by the extension builder, but will not
 * be overwritten (unless --force is specified) and is intended to be modified.
 */
#include "lsGameAnalytics_internal.h"
#include "GameAnalytics.h"
#include <string>
#include "s3eDebug.h"

// KT: GA doesn't offer some functionality which has been provided for Android,
// Added the following for consistency 
bool _isSessionStarted = false;
bool _isInialised = false;

s3eResult lsGameAnalyticsInit_platform()
{
    // Add any platform-specific initialisation code here
    return S3E_RESULT_SUCCESS;
}

void lsGameAnalyticsTerminate_platform()
{
    // Add any platform-specific termination code here
}

void lsGameAnalyticsInitialise_platform(const char * secretKey, const char * gameKey, const char * build)
{
    
    NSString* _secretKey = [NSString stringWithUTF8String:secretKey];
    NSString* _gameKey = [NSString stringWithUTF8String:gameKey];
    NSString* _build = [NSString stringWithUTF8String:build];
    
    [GameAnalytics setGameKey:_gameKey secretKey:_secretKey build:_build];
    
    [GameAnalytics updateSessionID];
    
    _isInialised = true;
}

bool lsGameAnalyticsIsInitialised_platform()
{
    return _isInialised;
}

bool lsGameAnalyticsIsSessionStarted_platform()
{
    return _isSessionStarted;
}

void lsGameAnalyticsStartSession_platform()
{
    _isSessionStarted = true;
}

void lsGameAnalyticsStopSession_platform()
{
    _isSessionStarted = false;
}

void lsGameAnalyticsNewDesignEvent_platform(const char * eventId, float value, const char * area, float x, float y, float z)
{
    NSString* _eventId = [NSString stringWithUTF8String:eventId];
    NSString* _area = [NSString stringWithUTF8String:area];
    
    if( strlen(area) <= 0)
    {
        [GameAnalytics logGameDesignDataEvent:_eventId value:[NSNumber numberWithFloat:value]];
        return;
    }
    
    [GameAnalytics logGameDesignDataEvent:_eventId 
                                                value:[NSNumber numberWithFloat:value]
                                                area:_area
                                                x:[NSNumber numberWithFloat:x]
                                                y:[NSNumber numberWithFloat:y]
                                                z:[NSNumber numberWithFloat:z]];
                                                
}

void lsGameAnalyticsNewQualityEvent_platform(const char * eventId, const char * message, const char * area, float x, float y, float z)
{
    NSString* _eventId = [NSString stringWithUTF8String:eventId];
    NSString* _message = [NSString stringWithUTF8String:message];
    NSString* _area = [NSString stringWithUTF8String:area];
    
    if( strlen(area) <= 0)
    {
        [GameAnalytics logQualityAssuranceDataEvent:_eventId message:_message];
        return;
    }
    
    [GameAnalytics logQualityAssuranceDataEvent:_eventId 
                                                message:_message
                                                area:_area
                                                x:[NSNumber numberWithFloat:x]
                                                y:[NSNumber numberWithFloat:y]
                                                z:[NSNumber numberWithFloat:z]];
}

void lsGameAnalyticsNewErrorEvent_platform(const char * message, const char * severity, const char * area, float x, float y, float z)
{

}

void lsGameAnalyticsNewUserEvent_platform(const char * eventId, const char * gender, int birthYear, int friendCount, const char * area, float x, float y, float z)
{
}

void lsGameAnalyticsSetUserInfo_platform(const char * gender, int birthYear, int friendCount)
{
    NSString* _gender = [NSString stringWithUTF8String:gender];

    [GameAnalytics logUserDataWithGender:_gender
                    birthYear:[NSNumber numberWithInt:birthYear]
                  friendCount:[NSNumber numberWithInt:friendCount]];
}

void lsGameAnalysticsSetReferralInfo_platform(const char * installPublisher, const char * installSite, const char * installCampaign, const char * installAdgroup, const char * installAd, const char * installKeyword)
{
}

void lsGameAnalysticsNewBusinessEvent_platform(const char * eventId, const char * currency, int amount, const char * area, float x, float y, float z)
{

    NSString* _eventId = [NSString stringWithUTF8String:eventId];
    NSString* _currency = [NSString stringWithUTF8String:currency];
    NSString* _area = [NSString stringWithUTF8String:area];

    [GameAnalytics logBusinessDataEvent:_eventId
              currencyString:_currency
                amountNumber:[NSNumber numberWithInt:amount]
                        area:_area
                           x:[NSNumber numberWithFloat:x]
                           y:[NSNumber numberWithFloat:y]
                           z:[NSNumber numberWithFloat:z]];
                  
}

void lsGameAnalysticsSetSendEventsInterval_platform(int millis)
{
}

void lsGameAnalysticsSetNetworkPollInterval_platform(int millis)
{
}

void lsGameAnalysticsSetSessionTimeOut_platform(int millis)
{
}

void lsGameAnalysticsLogFPS_platform()
{
}

void lsGameAnalysticsStopLoggingFPS_platform(const char * area, float x, float y, float z)
{
}

void lsGameAnalysticsSetCriticalFPSLimit_platform(int criticalFPS)
{
}

void lsGameAnalysticsSetMinimumFPSTimePeriod_platform(int minimumTimePeriod)
{
}

void lsGameAnalysticsLogUnhandledExceptions_platform()
{
}

void lsGameAnalysticsSetUserId_platform(const char * userId)
{
    NSString* _userId = [NSString stringWithUTF8String:userId];
    [GameAnalytics setCustomUserID:_userId];
}

void lsGameAnalysticsSetDebugLogLevel_platform(int level)
{
}

void lsGameAnalysticsSetLocalCaching_platform(bool value)
{
}

void lsGameAnalysticsSetAutoBatch_platform(bool value)
{
}

void lsGameAnalysticsSetMaximumEventStorage_platform(int max)
{
}

void lsGameAnalysticsManualBatch_platform()
{
    
}

void lsGameAnalysticsGetUserId_platform(char * destination)
{
    NSString * userID = [GameAnalytics getUserID];
    const char * cuserID = [userID cStringUsingEncoding:NSASCIIStringEncoding];
    
    snprintf(destination, sizeof(destination) / sizeof(char), "%s", cuserID);
}

void lsGameAnalysticsClearDatabase_platform()
{
}
