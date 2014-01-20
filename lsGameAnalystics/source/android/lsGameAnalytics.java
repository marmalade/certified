package com.longshot.marmalade.lsgameanalytics;

import android.util.Log;

import com.ideaworks3d.marmalade.LoaderAPI;
import com.ideaworks3d.marmalade.LoaderActivity;

import com.gameanalytics.android.GameAnalytics;
import com.gameanalytics.android.Severity;

class lsGameAnalytics
{
	private final String TAG = getClass().getName();

    public void lsGameAnalyticsInitialise(String secretKey, String gameKey, String build)
    {
		if(build == null)
			GameAnalytics.initialise(LoaderActivity.m_Activity.getApplicationContext(), secretKey, gameKey);
		else
			GameAnalytics.initialise(LoaderActivity.m_Activity.getApplicationContext(), secretKey, gameKey, build);
    }
    public boolean lsGameAnalyticsIsInitialised()
    {
        return GameAnalytics.isInitialised();
    }
    public boolean lsGameAnalyticsIsSessionStarted()
    {
        return GameAnalytics.isSessionStarted();
    }
    public void lsGameAnalyticsStartSession()
    {
        GameAnalytics.startSession(LoaderActivity.m_Activity);
    }
    public void lsGameAnalyticsStopSession()
    {
        GameAnalytics.stopSession();
    }
    public void lsGameAnalyticsNewDesignEvent(String eventId, float value, String area, float x, float y, float z)
    {
		if(area == null)
			GameAnalytics.newDesignEvent(eventId, value);
		else
			GameAnalytics.newDesignEvent(eventId, value, area, x, y, z);
    }
    public void lsGameAnalyticsNewQualityEvent(String eventId, String message, String area, float x, float y, float z)
    {
        GameAnalytics.newQualityEvent(eventId, message, area, x , y, z);
    }
    public void lsGameAnalyticsNewErrorEvent(String message, String severity, String area, float x, float y, float z)
    {
		Severity arg = new Severity(severity);

        GameAnalytics.newErrorEvent(message, arg, area, x, y, z);
    }
    public void lsGameAnalyticsNewUserEvent(String eventId, String gender, int birthYear, int friendCount, String area, float x, float y, float z)
    {
		Character arg = null;
		if(gender != null)
			arg = new Character(gender.charAt(0));

        GameAnalytics.newUserEvent(eventId, arg,birthYear, friendCount, area, x, y, z);
    }
    public void lsGameAnalyticsSetUserInfo(String gender, int birthYear, int friendCount)
    {
		Character arg = null;
		if(gender != null)
			arg = new Character(gender.charAt(0));

        GameAnalytics.setUserInfo(arg, birthYear, friendCount);
    }
    public void lsGameAnalysticsSetReferralInfo(String installPublisher, String installSite, String installCampaign, String installAdgroup, String installAd, String installKeyword)
    {
        GameAnalytics.setReferralInfo(installPublisher, installSite, installCampaign,installAdgroup,installAd, installKeyword);
    }
    public void lsGameAnalysticsNewBusinessEvent(String eventId, String currency, int amount, String area, float x, float y, float z)
    {
        GameAnalytics.newBusinessEvent(eventId, currency, amount, area, x, y, z);
    }
    public void lsGameAnalysticsSetSendEventsInterval(int millis)
    {
        GameAnalytics.setSendEventsInterval(millis);
    }
    public void lsGameAnalysticsSetNetworkPollInterval(int millis)
    {
        GameAnalytics.setNetworkPollInterval(millis);
    }
    public void lsGameAnalysticsSetSessionTimeOut(int millis)
    {
        GameAnalytics.setSessionTimeOut(millis);
    }
    public void lsGameAnalysticsLogFPS()
    {
        GameAnalytics.logFPS();
    }
    public void lsGameAnalysticsStopLoggingFPS(String area, float x, float y, float z)
    {
        GameAnalytics.stopLoggingFPS(area,x,y,z);
    }
    public void lsGameAnalysticsSetCriticalFPSLimit(int criticalFPS)
    {
        GameAnalytics.setCriticalFPSLimit(criticalFPS);
    }
    public void lsGameAnalysticsSetMinimumFPSTimePeriod(int minimumTimePeriod)
    {
        GameAnalytics.setMinimumFPSTimePeriod(minimumTimePeriod);
    }
    public void lsGameAnalysticsLogUnhandledExceptions()
    {
        GameAnalytics.logUnhandledExceptions();
    }
    public void lsGameAnalysticsSetUserId(String userId)
    {
        GameAnalytics.setUserId(userId);
    }
    public void lsGameAnalysticsSetDebugLogLevel(int level)
    {
        GameAnalytics.setDebugLogLevel(level);
    }
    public void lsGameAnalysticsSetLocalCaching(boolean value)
    {
        GameAnalytics.setLocalCaching(value);
    }
    public void lsGameAnalysticsSetAutoBatch(boolean value)
    {
        GameAnalytics.setAutoBatch(value);
    }
    public void lsGameAnalysticsSetMaximumEventStorage(int max)
    {
        GameAnalytics.setMaximumEventStorage(max);
    }
    public void lsGameAnalysticsManualBatch()
    {
        GameAnalytics.manualBatch();
    }
    public String lsGameAnalysticsGetUserId()
    {
        return GameAnalytics.getUserId();
    }
    public void lsGameAnalysticsClearDatabase()
    {
        GameAnalytics.clearDatabase();
    }
}
