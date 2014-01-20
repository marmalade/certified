/* 
   Game Analytics Android Wrapper
   Copyright (c) 2013 Tim Wicksteed <tim@twicecircled.com>
   http:/www.gameanalytics.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.gameanalytics.android;

import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;
import android.text.format.Time;

import com.loopj.twicecircled.android.http.AsyncHttpClient;

import com.longshot.marmalade.lsgameanalytics.lsHelper;

/**
 * Public singleton class used to interface with the GameAnalytics servers.
 */
public class GameAnalytics {

	// WEBSERVER
	protected static final String API_URL = "http://api.gameanalytics.com/1/";
	protected static final String AUTHORIZATION = "Authorization";
	protected static final String CONTENT_TYPE = "Content-Type";
	protected static final String CONTENT_TYPE_JSON = "application/json";

	// DEBUGGING
	/**
	 * Used as the parameter for setDebugLogLevel(int level). VERBOSE logging
	 * will post to the debug log when every event is created and batched off to
	 * the server.
	 */
	public static final int VERBOSE = 0;
	/**
	 * Used as the parameter for setDebugLogLevel(int level). RELEASE logging
	 * will only post to the debug logs in the event of a warning or error.
	 */
	public static final int RELEASE = 1;
	protected static int LOGGING = RELEASE;

	// CATEGORIES
	protected static final String DESIGN = "design";
	protected static final String USER = "user";
	protected static final String QUALITY = "quality";
	protected static final String BUSINESS = "business";
	protected static final String ERROR = "error";

	// APP/DEVELOPER SPECIFIC
	private static String GAME_KEY;
	private static String SECRET_KEY;
	private static String USER_ID;
	private static String UNHASHED_ANDROID_ID;
	private static String SESSION_ID;
	private static String BUILD;
	private static String AREA;
	private static int SEND_EVENT_INTERVAL = 20000; // Default is 20 secs
	private static int NETWORK_POLL_INTERVAL = 60000; // Default is 60 secs
	private static int SESSION_TIME_OUT = 20000; // Default is 20 secs
	private static int MINIMUM_FPS_PERIOD = 5000; // Default is 5 second
	private static int CRITICAL_FPS_LIMIT = 30; // Default is 30 frames

	// PRECONFIGURED EVENTS
	private static final String FPS_EVENT_NAME = "GA:AverageFPS";
	private static final String CRITICAL_FPS_EVENT_NAME = "GA:CriticalFPS";
	private static final String ANDROID = "Android";
	private static final String SDK_VERSION = "GA Android SDK 1.11";

	// ERROR EVENT SEVERITY TYPES
	/**
	 * Used as the severity parameter for newErrorEvent(). Severity = "critical"
	 */
	public static final Severity CRITICAL_SEVERITY = new Severity("critical");
	/**
	 * Used as the severity parameter for newErrorEvent(). Severity = "error"
	 */
	public static final Severity ERROR_SEVERITY = new Severity("error");
	/**
	 * Used as the severity parameter for newErrorEvent(). Severity = "warning"
	 */
	public static final Severity WARNING_SEVERITY = new Severity("warning");
	/**
	 * Used as the severity parameter for newErrorEvent(). Severity = "info"
	 */
	public static final Severity INFO_SEVERITY = new Severity("info");
	/**
	 * Used as the severity parameter for newErrorEvent(). Severity = "debug"
	 */
	public static final Severity DEBUG_SEVERITY = new Severity("debug");

	// OTHER
	private static AsyncHttpClient CLIENT;
	private static BatchThread CURRENT_THREAD;
	private static EventDatabase EVENT_DATABASE;
	protected static UncaughtExceptionHandler DEFAULT_EXCEPTION_HANDLER;
	private static ExceptionLogger EXCEPTION_LOGGER;
	private static Context CONTEXT;
	private static boolean INITIALISED = false;
	private static boolean SESSION_STARTED = false;
	private static boolean CACHE_LOCALLY = true;
	private static boolean AUTO_BATCH = true;
	private static long SESSION_END_TIME;
	private static long START_FPS_TIME;
	private static int FPS_FRAMES;
	private static ArrayList<PostResponseHandler> FINISHED_SENDING_EVENTS = new ArrayList<PostResponseHandler>();
	private static boolean CAN_START_NEW_THREAD = true;

	/**
	 * Initialise the GameAnalytics wrapper. It is recommended that you call
	 * this method from the entry activity of your application's onCreate()
	 * method. Uses the value of 'android:versionName' from the
	 * AndroidManifest.xml as the build version of the application by default.
	 * 
	 * @param context
	 *            the calling activity
	 * @param secretKey
	 *            secret key supplied when you registered at GameAnalytics
	 * @param gameKey
	 *            game key supplied when you registered at GameAnalytics
	 */
	public static void initialise(Context context, String secretKey,
			String gameKey) {
		// Get build version from AndroidManifest.xml
		String build;
		try {
			build = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			GALog.w("Warning: android:versionName tag is not set correctly in Android Manifest.");
			build = "unknown";
		}
		// Pass on to full initialise method
		initialise(context, secretKey, gameKey, build);
	}

	/**
	 * Initialise the GameAnalytics wrapper. It is recommended that you call
	 * this method from the entry activity of your application's onCreate()
	 * method.
	 * 
	 * @param context
	 *            the calling activity
	 * @param secretKey
	 *            secret key supplied when you registered at GameAnalytics
	 * @param gameKey
	 *            game key supplied when you registered at GameAnalytics
	 * @param build
	 *            optional - leave out to use 'android:versionName' from
	 *            manifest file by default
	 */
	public static void initialise(Context context, String secretKey,
			String gameKey, String build) {
		// Get user id
		UNHASHED_ANDROID_ID = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);

		// KT: 22/12/2013 Android 2.2 and other devices may return null
		// fallback to helper which will attempt to generate one and store it in prefs
		if(UNHASHED_ANDROID_ID == null)
			UNHASHED_ANDROID_ID = lsHelper.getDeviceUuid().toString();
		USER_ID = md5(UNHASHED_ANDROID_ID);
		// Set game and secret keys and build
		SECRET_KEY = secretKey;
		GAME_KEY = gameKey + "/";
		BUILD = build;

		// Initialise other variables
		CLIENT = new AsyncHttpClient();
		EVENT_DATABASE = new EventDatabase(context);
		DEFAULT_EXCEPTION_HANDLER = Thread.currentThread()
				.getUncaughtExceptionHandler();
		EXCEPTION_LOGGER = new ExceptionLogger();

		// Set boolean initialised, newEvent() can only be called after
		// initialise() and startSession()
		INITIALISED = true;
	}

	/**
	 * Checks whether Game Analytics has been initialised.
	 * 
	 * @return true if initialised, otherwise false
	 */
	public static boolean isInitialised() {
		return INITIALISED;
	}

	/**
	 * Checks whether a Game Analytics session has been started.
	 * 
	 * @return true if session started, otherwise false
	 */
	public static boolean isSessionStarted() {
		return SESSION_STARTED;
	}

	/**
	 * Call this method in every activity's onResume() method to ensure correct
	 * session logging.
	 * 
	 * @param context
	 *            the calling activity
	 */
	public static void startSession(Context context) {
		// Update current context
		CONTEXT = context;
		AREA = context.getClass().getSimpleName();

		// Current time:
		long nowTime = System.currentTimeMillis();

		SESSION_STARTED = true;

		// Need to get a new sessionId?
		if (SESSION_ID == null
				|| (SESSION_END_TIME != 0 && nowTime > SESSION_END_TIME)) {
			// Set up unique session id
			SESSION_ID = getSessionId();
			GALog.i("Starting new session");

			// Send off model and OS version
			sendOffUserStats();
		}
	}

	/**
	 * Call this method in every activity's onPause() method to ensure correct
	 * session logging.
	 * 
	 */
	public static void stopSession() {
		// sessionTimeOut is some time after now
		SESSION_END_TIME = System.currentTimeMillis() + SESSION_TIME_OUT;
		SESSION_STARTED = false;
		CONTEXT = null;
	}

	/**
	 * Add a new design event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval().
	 * 
	 * @param eventId
	 *            use colons to denote subtypes, e.g. 'PickedUpAmmo:Shotgun'
	 * @param value
	 *            numeric value associated with event e.g. number of shells
	 * @param area
	 *            area/level associated with the event
	 * @param x
	 *            position on x-axis
	 * @param y
	 *            position on y-axis
	 * @param z
	 *            position on z-axis
	 */
	public static void newDesignEvent(String eventId, Float value, String area,
			Float x, Float y, Float z) {
		if (ready()) {
			GALog.i("New design event: " + eventId + ", value: " + value
					+ ", area: " + area + ", pos: (" + x + ", " + y + ", " + z
					+ ")");
			// Ensure we have a BatchThread ready to receive events
			startThreadIfReq();

			// Add design event to batch stack
			EVENT_DATABASE.addDesignEvent(GAME_KEY, SECRET_KEY, USER_ID,
					SESSION_ID, BUILD, eventId, area, x, y, z, value);
		}
	}

	/**
	 * Add a new design event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval(). The
	 * current activity will be used as the 'area' value for the event.
	 * 
	 * @param eventId
	 *            use colons to denote subtypes, e.g. 'PickedUpAmmo:Shotgun'
	 */
	public static void newDesignEvent(String eventId) {
		newDesignEvent(eventId, null);
	}

	/**
	 * Add a new design event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval(). The
	 * current activity will be used as the 'area' value for the event.
	 * 
	 * @param eventId
	 *            use colons to denote subtypes, e.g. 'PickedUpAmmo:Shotgun'
	 * @param value
	 *            numeric value associated with event e.g. number of shells
	 */
	public static void newDesignEvent(String eventId, Float value) {
		newDesignEvent(eventId, value, AREA, null, null, null);
	}

	/**
	 * Add a new quality event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval().
	 * 
	 * @param eventId
	 *            use colons to denote subtypes, e.g.
	 *            'Exception:NullPointerException'
	 * @param message
	 *            message associated with event e.g. the stack trace
	 * @param area
	 *            area/level associated with the event
	 * @param x
	 *            position on x-axis
	 * @param y
	 *            position on y-axis
	 * @param z
	 *            position on z-axis
	 * 
	 * @deprecated use {@link newErrorEvent()} instead.
	 */
	public static void newQualityEvent(String eventId, String message,
			String area, Float x, Float y, Float z) {
		if (ready()) {
			GALog.i("New quality event: " + eventId + ", message: " + message
					+ ", area: " + area + ", pos: (" + x + ", " + y + ", " + z
					+ ")");
			// Ensure we have a BatchThread ready to receive events
			startThreadIfReq();

			// Add quality event to batch stack
			EVENT_DATABASE.addQualityEvent(GAME_KEY, SECRET_KEY, USER_ID,
					SESSION_ID, BUILD, eventId, area, x, y, z, message);
		}
	}

	/**
	 * Add a new quality event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval(). The
	 * current activity will be used as the 'area' value for the event.
	 * 
	 * @param eventId
	 *            use colons to denote subtypes, e.g.
	 *            'Exception:NullPointerException'
	 * @param message
	 *            message associated with event e.g. the stack trace
	 * @deprecated use {@link newErrorEvent()} instead.
	 */
	public static void newQualityEvent(String eventId) {
		newQualityEvent(eventId, null);
	}

	/**
	 * Add a new quality event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval(). The
	 * current activity will be used as the 'area' value for the event.
	 * 
	 * @param eventId
	 *            use colons to denote subtypes, e.g.
	 *            'Exception:NullPointerException'
	 * @param message
	 *            message associated with event e.g. the stack trace
	 * @deprecated use {@link newErrorEvent()} instead.
	 */
	public static void newQualityEvent(String eventId, String message) {
		newQualityEvent(eventId, message, AREA, null, null, null);
	}

	/**
	 * Add a new error event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval().
	 * 
	 * @param message
	 *            message associated with the error e.g. the stack trace
	 * @param severity
	 *            severity of error, use GameAnalytics.CRITICAL,
	 *            GameAnalytics.ERROR, GameAnalytics.WARNING, GameAnalytics.INFO
	 *            or GameAnalytics.DEBUG.
	 * @param area
	 *            area/level associated with the event
	 * @param x
	 *            position on x-axis
	 * @param y
	 *            position on y-axis
	 * @param z
	 *            position on z-axis
	 */
	public static void newErrorEvent(String message, Severity severity,
			String area, Float x, Float y, Float z) {
		if (ready()) {
			if (doErrorSeverityCheck(severity)) {
				GALog.i("New error event: message: " + message + ", severity: "
						+ severity + ", area: " + area + ", pos: (" + x + ", "
						+ y + ", " + z + ")");
				// Ensure we have a BatchThread ready to receive events
				startThreadIfReq();

				// Add quality event to batch stack
				EVENT_DATABASE.addErrorEvent(GAME_KEY, SECRET_KEY, USER_ID,
						SESSION_ID, BUILD, area, x, y, z, message,
						severity.toString());
			}
		}
	}

	/**
	 * Add a new error event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval(). The
	 * current activity will be used as the 'area' value for the event.
	 * 
	 * @param message
	 *            message associated with the error e.g. the stack trace
	 * @param severity
	 *            severity of error, use GameAnalytics.CRITICAL,
	 *            GameAnalytics.ERROR, GameAnalytics.WARNING, GameAnalytics.INFO
	 *            or GameAnalytics.DEBUG.
	 */
	public static void newErrorEvent(String message, Severity severity) {
		newErrorEvent(message, severity, AREA, null, null, null);
	}

	// This is the privately accessible method to send all user event data.
	// Developers should use the following publically available ones:
	// 1. setUserInfo(char gender, int birthYear, int friendCount)
	// 2. setReferralInfo(String installPublisher, String installSite, String
	// installCampaign, String installAd, String installKeyword)
	private static void newUserEvent(Character gender, Integer birthYear,
			Integer friendCount, String area, Float x, Float y, Float z,
			String platform, String device, String osMajor, String osMinor,
			String sdkVersion, String installPublisher, String installSite,
			String installCampaign, String installAdgroup, String installAd,
			String installKeyword, String androidId) {
		if (ready()) {
			GALog.i("New user event: gender: " + gender + ", birthYear: "
					+ birthYear + ", friendCount: " + friendCount + ", area: "
					+ area + ", pos: (" + x + ", " + y + ", " + z + ", "
					+ platform + ", " + device + ", " + osMajor + ", "
					+ osMinor + ", " + sdkVersion + ", " + installPublisher
					+ ", " + installSite + ", " + installCampaign + ", "
					+ installAdgroup + ", " + installAd + ", " + installKeyword
					+ ", " + androidId + ")");
			// Ensure we have a BatchThread ready to receive events
			startThreadIfReq();

			// Add user event to batch stack
			EVENT_DATABASE.addUserEvent(GAME_KEY, SECRET_KEY, USER_ID,
					SESSION_ID, BUILD, area, x, y, z, gender, birthYear,
					friendCount, platform, device, osMajor, osMinor,
					sdkVersion, installPublisher, installSite, installCampaign,
					installAdgroup, installAd, installKeyword, androidId);
		}
	}

	private static boolean doErrorSeverityCheck(Severity severity) {
		if (CRITICAL_SEVERITY == severity || ERROR_SEVERITY == severity
				|| WARNING_SEVERITY == severity || INFO_SEVERITY == severity
				|| DEBUG_SEVERITY == severity) {
			return true;
		} else {
			GALog.w("Warning: unsupported severity level passed into newErrorEvent(), use GameAnalyics.CRITICAL, GameAnalyics.ERROR etc.");
			return false;
		}
	}

	/**
	 * Add a new user event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval(). The
	 * current activity will be used as the 'area' value for the event.
	 * 
	 * @param eventId
	 *            use colons to denote subtypes
	 * @param gender
	 *            user gender, use 'm' for male, 'f' for female
	 * @param birthYear
	 *            four digit birth year
	 * @param friendCount
	 *            number of friends
	 * 
	 * @deprecated use {@link setUserInfo()} instead.
	 */
	public static void newUserEvent(String eventId, Character gender,
			Integer birthYear, Integer friendCount) {
		newUserEvent(eventId, gender, birthYear, friendCount, AREA, null, null,
				null);
	}

	/**
	 * Add a new user event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval(). The
	 * current activity will be used as the 'area' value for the event.
	 * 
	 * @param eventId
	 *            use colons to denote subtypes
	 * @param gender
	 *            user gender, use 'm' for male, 'f' for female
	 * @param birthYear
	 *            four digit birth year
	 * @param friendCount
	 *            number of friends
	 * @param area
	 *            area/level associated with the event
	 * @param x
	 *            position on x-axis
	 * @param y
	 *            position on y-axis
	 * @param z
	 *            position on z-axis
	 * 
	 * @deprecated use {@link setUserInfo()} instead.
	 */
	public static void newUserEvent(String eventId, Character gender,
			Integer birthYear, Integer friendCount, String area, Float x,
			Float y, Float z) {
		newUserEvent(gender, birthYear, friendCount, area, x, y, z, null, null,
				null, null, null, null, null, null, null, null, null,
				UNHASHED_ANDROID_ID);
	}

	/**
	 * Send user info to the Game Analytics server. All parameters are optional,
	 * pass in 'null' if you do not have the data.
	 * 
	 * @param gender
	 *            user gender, use 'm' for male, 'f' for female
	 * @param birthYear
	 *            four digit birth year
	 * @param friendCount
	 *            number of friends
	 */
	public static void setUserInfo(Character gender, Integer birthYear,
			Integer friendCount) {
		newUserEvent(gender, birthYear, friendCount, AREA, null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null, UNHASHED_ANDROID_ID);
	}

	/**
	 * Manually send referral info to the Game Analytics server. All parameters
	 * are optional, use 'null' if you do not have the data.
	 * 
	 * For automatic referrals, extend ReferralReceiver class set up as
	 * broadcast receiver in Android Manifest.
	 * 
	 * @param installPublisher
	 *            e.g. FB, Chartboost, Google Adwords, Organic
	 * @param installSite
	 *            e.g. FB.com, FBApp, AppId
	 * @param installCampaign
	 *            e.g. Launch, EasterBoost, ChrismasSpecial
	 * @param installAd
	 *            e.g. Add#239823, KnutsShinyAd
	 * @param installKeyword
	 *            e.g. rts mobile game
	 */
	public static void setReferralInfo(String installPublisher,
			String installSite, String installCampaign, String installAdgroup,
			String installAd, String installKeyword) {
		// User event for GA
		newUserEvent(null, null, null, AREA, null, null, null, null, null,
				null, null, null, installPublisher, installSite,
				installCampaign, installAdgroup, installAd, installKeyword,
				UNHASHED_ANDROID_ID);
	}

	/**
	 * Add a new business event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval().
	 * 
	 * @param eventId
	 *            use colons to denote subtypes, e.g. 'PurchaseWeapon:Shotgun'
	 * @param currency
	 *            3 digit code for currency e.g. 'USD'
	 * @param amount
	 *            value of transaction
	 * @param area
	 *            area/level associated with the event
	 * @param x
	 *            position on x-axis
	 * @param y
	 *            position on y-axis
	 * @param z
	 *            position on z-axis
	 */
	public static void newBusinessEvent(String eventId, String currency,
			int amount, String area, Float x, Float y, Float z) {
		if (ready()) {
			GALog.i("New business event: " + eventId + ", currency: "
					+ currency + ", amount: " + amount + ", area: " + area
					+ ", pos: (" + x + ", " + y + ", " + z + ")");
			// Ensure we have a BatchThread ready to receive events
			startThreadIfReq();

			// Add business event to batch stack
			EVENT_DATABASE
					.addBusinessEvent(GAME_KEY, SECRET_KEY, USER_ID,
							SESSION_ID, BUILD, eventId, area, x, y, z,
							currency, amount);

		}
	}

	/**
	 * Add a new business event to the event stack. This will be sent off in a
	 * batched array after the time interval set using setTimeInterval(). The
	 * current activity will be used as the 'area' value for the event.
	 * 
	 * @param eventId
	 *            use colons to denote subtypes, e.g. 'PurchaseWeapon:Shotgun'
	 * @param currency
	 *            3 digit code for currency e.g. 'USD'
	 * @param amount
	 *            value of transaction
	 */
	public static void newBusinessEvent(String eventId, String currency,
			int amount) {
		newBusinessEvent(eventId, currency, amount, AREA, null, null, null);
	}

	/**
	 * Set the amount of time, in milliseconds, between each batch of events
	 * being sent. The default is 20 seconds.
	 * 
	 * @param millis
	 *            interval in milliseconds
	 */
	public static void setSendEventsInterval(int millis) {
		SEND_EVENT_INTERVAL = millis;
	}

	/**
	 * If a network is not available GameAnalytics will poll the connection and
	 * send the events once it is restored. Set the amount of time, in
	 * milliseconds, between polls. The default is 60 seconds.
	 * 
	 * @param millis
	 *            interval in milliseconds
	 */
	public static void setNetworkPollInterval(int millis) {
		NETWORK_POLL_INTERVAL = millis;
	}

	/**
	 * Set the amount of time, in milliseconds, for a session to timeout so that
	 * a new one is started when the application is restarted. being sent. The
	 * default is 20 seconds.
	 * 
	 * @param millis
	 *            interval in milliseconds
	 */
	public static void setSessionTimeOut(int millis) {
		SESSION_TIME_OUT = millis;
	}

	/**
	 * Place somewhere inside your draw loop to log FPS. If you are using openGL
	 * then that will be inside your Renderer class' onDrawFrame() method. You
	 * must then call stopLoggingFPS() at some point to collate the data and
	 * send it to GameAnalytics. You can either do this intermittently e.g.
	 * every 1000 frames, or over an entire gameplay session e.g. in the
	 * activity's onPause() method. Either way, the average FPS will be logged.
	 */
	public static void logFPS() {
		// Have we already started logging FPS?
		if (START_FPS_TIME == 0) {
			// No, start logging
			GALog.i("Start logging FPS.");
			START_FPS_TIME = System.currentTimeMillis();
		} else {
			// Increment number of frames
			FPS_FRAMES++;
		}
	}

	/**
	 * Call this method when you want to collate you FPS and send it to the
	 * server. You can either do this intermittently e.g. every 1000 frames, or
	 * over an entire gameplay session e.g. in the activity's onPause() method.
	 * Either way, the average FPS will be logged. The parameters are optional.
	 * If left out, the name of the current activity will be logged as the area
	 * parameter.
	 * 
	 * @param area
	 *            (optional - use to log the FPS in a specific level/area)
	 * @param x
	 *            (optional)
	 * @param y
	 *            (optional)
	 * @param z
	 *            (optional)
	 */
	public static void stopLoggingFPS(String area, Float x, Float y, Float z) {
		if (ready()) {
			GALog.i("Stop logging FPS.");
			// Ensure we are logging FPS?
			if (START_FPS_TIME != 0) {
				// Get elapsed time
				long elapsed = System.currentTimeMillis() - START_FPS_TIME;

				// Has enough time elapsed?
				if (elapsed > MINIMUM_FPS_PERIOD) {
					// Work out average FPS and send
					Float fps = (float) (FPS_FRAMES * 1000 / elapsed);
					newDesignEvent(FPS_EVENT_NAME, fps, area, x, y, z);
					if (fps < CRITICAL_FPS_LIMIT) {
						// FPS is below critical limit
						newDesignEvent(CRITICAL_FPS_EVENT_NAME, fps, area, x,
								y, z);
					}
					START_FPS_TIME = 0;
				} else {
					GALog.w("Warning: Insufficient time elapsed between starting and stopping FPS logging.");
					START_FPS_TIME = 0;
				}
			} else {
				GALog.w("Warning: stopLoggingFPS() was called before startLoggingFPS().");
			}
		}
	}

	/**
	 * Call this method when you want to collate you FPS and send it to the
	 * server. You can either do this intermittently e.g. every 1000 frames, or
	 * over an entire gameplay session e.g. in the activity's onPause() method.
	 * Either way, the average FPS will be logged. Area, x, y and z parameters
	 * can be added. If omitted, the current activity will be logged as the area
	 * parameter.
	 */
	public static void stopLoggingFPS() {
		stopLoggingFPS(AREA, null, null, null);
	}

	/**
	 * Set the critical FPS limit. If the average FPS over a period is under
	 * this value then a "FPSCritical" design event will be logged. The default
	 * is 20 frames per second.
	 * 
	 * @param criticalFPS
	 *            in frames per second
	 */
	public static void setCriticalFPSLimit(int criticalFPS) {
		CRITICAL_FPS_LIMIT = criticalFPS;
	}

	/**
	 * Set the minimum time period for an average FPS to be logged. This stops
	 * spurious results coming from very short time periods. Default is 5
	 * seconds.
	 * 
	 * @param minimumTimePeriod
	 *            in milliseconds
	 */
	public static void setMinimumFPSTimePeriod(int minimumTimePeriod) {
		MINIMUM_FPS_PERIOD = minimumTimePeriod;
	}

	/**
	 * Call this method at the same time as initialise() to automatically log
	 * any unhandled exceptions occuring on your main/GUI thread. You need to
	 * call this method from ever thread that you wish to log unhandled
	 * exceptions on e.g. update loop, draw loop etc.
	 */
	public static void logUnhandledExceptions() {
		Thread.currentThread().setUncaughtExceptionHandler(EXCEPTION_LOGGER);
	}

	/**
	 * Set a custom userId string to be attached to all subsequent events. By
	 * default, the user ID is generated from the unique Android device ID.
	 * 
	 * @param userId
	 *            Custom unique user ID
	 */
	public static void setUserId(String userId) {
		USER_ID = userId;
	}

	/**
	 * Set debug log level. Use GameAnalytics.VERBOSE while you are developing
	 * to see when every event is created and batched to server. Set to
	 * GameAnalytics.RELEASE (default) when you release your application so only
	 * warning and event logs are made.
	 * 
	 * @param level
	 *            Set to either GameAnalytics.VERBOSE or GameAnalytics.RELEASE
	 */
	public static void setDebugLogLevel(int level) {
		if (level == VERBOSE || level == RELEASE) {
			LOGGING = level;
		} else {
			GALog.w("Warning: You should pass in GameAnalytics.VERBOSE or GameAnalytics.RELEASE into GameAnalytics.setLoggingLevel()");
		}
	}

	/**
	 * Enable/disable local caching. By default (true) events are cached locally
	 * so that even if an internet connection is not available, they will be
	 * sent to the GA server when it is restored. If disabled (false) events
	 * will be discarded if a connection is unavailable.
	 * 
	 * @param value
	 *            true = enabled; false = disabled
	 */
	public static void setLocalCaching(boolean value) {
		CACHE_LOCALLY = value;
	}

	/**
	 * Enable/disable automatic batching. By default (true) events are sent off
	 * to the GA server after a time interval set using setSendEventsInterval().
	 * If disabled (false) then you will need to use manualBatch() to send the
	 * events to the server.
	 * 
	 * @param value
	 *            true = enabled; false = disabled
	 */
	public static void setAutoBatch(boolean value) {
		AUTO_BATCH = false;
	}

	/**
	 * Set maximum number of events that are stored locally. Additional events
	 * will be discarded. Set to 0 for unlimited (default).
	 * 
	 * @param max
	 *            maximum number of events that can be stored
	 */
	public static void setMaximumEventStorage(int max) {
		EVENT_DATABASE.setMaximumEventStorage(max);
	}

	/**
	 * Create a special BatchThread just to send events. This event will not
	 * wait for the sendEventInterval nor will it poll the internet connection.
	 * If there is no connection it will simply return.
	 */
	public static void manualBatch() {
		if (ready()) {
			if (CAN_START_NEW_THREAD) {
				GALog.i("Starting manual batch.");
				BatchThread sendEventThread = new BatchThread(CONTEXT, CLIENT,
						EVENT_DATABASE, GAME_KEY, SECRET_KEY,
						SEND_EVENT_INTERVAL, NETWORK_POLL_INTERVAL,
						CACHE_LOCALLY);
				CAN_START_NEW_THREAD = false;
				sendEventThread.manualBatch();
			} else {
				GALog.w("Warning: GameAnalytics batch thread already started, wait for it to finish before starting another.");
			}
		}
	}

	private static boolean ready() {
		if (INITIALISED) {
			if (SESSION_STARTED) {
				return true;
			} else {
				GALog.w("Warning: GameAnalytics session has not started. 1. Have you called GameAnalytics.startSession(Context context) in onResume()? OR 2. Are you trying to send events prior to onResume() being called, for example in onCreate()? You need to call startSession() before sending your first event.");
			}
		} else {
			GALog.w("Warning: GameAnalytics has not been initialised. Call GameAnalytics.initialise(Context context, String secretKey, String gameKey) first");
		}
		return false;
	}

	// Generates session id from md5 hash of current time
	private static String getSessionId() {
		Time time = new Time();
		time.setToNow();
		return md5(USER_ID + time.toString());
	}

	// Generates MD5 hash string from String, returns null on error
	protected static String md5(String s) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes("ISO-8859-1"), 0, s.length());
			byte[] byteArray = digest.digest();
			/*
			 * Convert byte array to hex string: .add(...) adds one to the
			 * beginning of the BigInteger representation of our byte array.
			 * Then .substring(1) removes it. This stops leading zeros being
			 * dropped when converting from BigInteger to hex string.
			 */
			String hash = (new BigInteger(1, byteArray).add(BigInteger.ONE
					.shiftLeft(8 * byteArray.length))).toString(16)
					.substring(1);
			return hash;
		} catch (NoSuchAlgorithmException e) {
			GALog.e("NoSuchAlgorithmException when making authorization hash.",
					e);
			return null;
		} catch (UnsupportedEncodingException e) {
			GALog.e("UnsupportedEncodingException when making authorization hash.",
					e);
			return null;
		}
	}

	private static void startThreadIfReq() {
		// Only start new thread IF (current thread is null OR current thread
		// has finished) AND auto-batch is switched on.
		if ((CURRENT_THREAD == null || CAN_START_NEW_THREAD) && AUTO_BATCH) {
			CURRENT_THREAD = new BatchThread(CONTEXT, CLIENT, EVENT_DATABASE,
					GAME_KEY, SECRET_KEY, SEND_EVENT_INTERVAL,
					NETWORK_POLL_INTERVAL, CACHE_LOCALLY);
			CURRENT_THREAD.start();
			CAN_START_NEW_THREAD = false;
		}
	}

	private static void sendOffUserStats() {
		// Automatically log version numbers, model and unhashed android id.
		newUserEvent(null, null, null, AREA, null, null, null, ANDROID,
				android.os.Build.MODEL, ANDROID + " "
						+ android.os.Build.VERSION.RELEASE.substring(0, 3),
				ANDROID + " " + android.os.Build.VERSION.RELEASE, SDK_VERSION,
				null, null, null, null, null, null, UNHASHED_ANDROID_ID);
	}

	protected static void sendingEvents(PostResponseHandler handler) {
		GALog.i("Sending " + handler.getNumberOfEvents() + " "
				+ handler.getCategory() + " events.");
		FINISHED_SENDING_EVENTS.add(handler);
	}

	protected static void finishedSendingEvents(PostResponseHandler handler) {
		GALog.i("Finished sending " + handler.getNumberOfEvents() + " "
				+ handler.getCategory() + " events.");
		FINISHED_SENDING_EVENTS.remove(handler);
		checkIfNoEvents();
	}

	protected static void canStartNewThread() {
		CAN_START_NEW_THREAD = true;
		FINISHED_SENDING_EVENTS.clear();
	}

	protected static void checkIfNoEvents() {
		if (FINISHED_SENDING_EVENTS.isEmpty()) {
			CAN_START_NEW_THREAD = true;
			GALog.i("OK, ready to start new thread.");
		}
	}

	/**
	 * The userId that the SDK uses to track each individual user on the server.
	 * 
	 * @return the user id or null if the SDK is not initialised.
	 */
	public static String getUserId() {
		if (INITIALISED) {
			return USER_ID;
		} else {
			GALog.w("Warning: GameAnalytics has not been initialised. Call GameAnalytics.initialise(Context context, String secretKey, String gameKey) first");
			return null;
		}
	}

	/**
	 * Manually clears the database, will result in loss of analytics data if
	 * used in production.
	 */
	public static void clearDatabase() {
		if (INITIALISED) {
			EVENT_DATABASE.clear();
		} else {
			GALog.w("Warning: GameAnalytics has not been initialised. Call GameAnalytics.initialise(Context context, String secretKey, String gameKey) first");
		}
	}
}