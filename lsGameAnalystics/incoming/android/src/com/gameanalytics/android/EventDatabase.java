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

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventDatabase {

	private SQLiteDatabase db;

	public EventDatabase(Context context) {
		this.db = new OpenHelper(context).getWritableDatabase();
	}

	// Other values
	public final static String DEFAULT_GAME_KEY = "default_game_key";

	// DATABASE SCHEMA
	// Common
	protected final static String TABLENAME = "events";
	protected final static String ROW_ID = "_id";
	protected final static String GAME_KEY = "game_key";
	protected final static String SECRET_KEY = "secret_key";
	protected final static String TYPE = "type";
	protected final static String USER_ID = "user_id";
	protected final static String SESSION_ID = "session_id";
	protected final static String BUILD = "build";
	protected final static String EVENT_ID = "event_id";
	protected final static String AREA = "area";
	protected final static String X = "x";
	protected final static String Y = "y";
	protected final static String Z = "z";
	private static int MAXIMUM_EVENT_STORAGE = 0; // Default is 0 (unlimited)

	// Design
	protected final static String VALUE = "value";

	// Business
	protected final static String CURRENCY = "currency";
	protected final static String AMOUNT = "amount";

	// User
	protected final static String GENDER = "gender";
	protected final static String BIRTH_YEAR = "birth_year";
	protected final static String FRIEND_COUNT = "friend_count";

	// Optional user events added in V1.10
	protected final static String PLATFORM = "platform";
	protected final static String DEVICE = "device";
	protected final static String OS_MAJOR = "os_major";
	protected final static String OS_MINOR = "os_minor";
	protected final static String SDK_VERSION = "sdk_version";
	protected final static String INSTALL_PUBLISHER = "install_publisher";
	protected final static String INSTALL_SITE = "install_site";
	protected final static String INSTALL_CAMPAIGN = "install_campaign";
	protected final static String INSTALL_ADGROUP = "install_adgroup";
	protected final static String INSTALL_AD = "install_ad";
	protected final static String INSTALL_KEYWORD = "install_keyword";
	protected final static String ANDROID_ID = "android_id";

	// Quality & Error
	protected final static String MESSAGE = "message";
	protected final static String SEVERITY = "severity";

	protected final static String CREATE_TABLE = "create table " + TABLENAME
			+ " (" + ROW_ID + " integer primary key autoincrement not null,"
			+ TYPE + " text," + USER_ID + " text," + SESSION_ID + " text,"
			+ BUILD + " text," + EVENT_ID + " text," + AREA + " text," + X
			+ " num," + Y + " num," + Z + " num," + VALUE + " num," + CURRENCY
			+ " text," + AMOUNT + " num," + GENDER + " text," + BIRTH_YEAR
			+ " num," + FRIEND_COUNT + " num," + MESSAGE + " text," + PLATFORM
			+ " text," + DEVICE + " text," + OS_MAJOR + " text," + OS_MINOR
			+ " text," + SDK_VERSION + " text," + INSTALL_PUBLISHER + " text,"
			+ INSTALL_SITE + " text," + INSTALL_CAMPAIGN + " text,"
			+ INSTALL_ADGROUP + " text," + INSTALL_AD + " text,"
			+ INSTALL_KEYWORD + " text," + GAME_KEY + " text," + SECRET_KEY
			+ " text," + ANDROID_ID + " text," + SEVERITY + " text" + ");";

	// Database operations (SYNCHRONIZED)
	// The following methods are synchronized so that extra events won't be
	// added to the database while the current lot are being pulled out and
	// sent.
	synchronized protected Object[] getEvents() {
		// Get all events
		Cursor cursor = db.query(TABLENAME, null, null, null, null, null,
				ROW_ID);

		// Create Hashmaps of event arrays to support multiple game ids
		HashMap<String, EventList<DesignEvent>> designEvents = new HashMap<String, EventList<DesignEvent>>();
		HashMap<String, EventList<UserEvent>> userEvents = new HashMap<String, EventList<UserEvent>>();
		HashMap<String, EventList<BusinessEvent>> businessEvents = new HashMap<String, EventList<BusinessEvent>>();
		HashMap<String, EventList<QualityEvent>> qualityEvents = new HashMap<String, EventList<QualityEvent>>();
		HashMap<String, EventList<ErrorEvent>> errorEvents = new HashMap<String, EventList<ErrorEvent>>();

		// Columns
		int rowId;
		String type;
		String userId;
		String sessionId;
		String build;
		String eventId;
		String area;
		Float x = null;
		Float y = null;
		Float z = null;
		Float value = null;
		String currency;
		int amount;
		Character gender = null;
		Integer birthYear = null;
		Integer friendCount = null;
		String message;
		String platform;
		String device;
		String osMajor;
		String osMinor;
		String sdkVersion;
		String installPublisher;
		String installSite;
		String installCampaign;
		String installAdgroup;
		String installAd;
		String installKeyword;
		String gameKey;
		String secretKey;
		String androidId;
		String severity;

		// Populate ArrayLists
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				rowId = cursor.getInt(0);
				type = cursor.getString(1);
				userId = cursor.getString(2);
				sessionId = cursor.getString(3);
				build = cursor.getString(4);
				area = cursor.getString(6);
				String xString = cursor.getString(7);
				if (xString != null) {
					x = Float.valueOf(xString);
				}
				String yString = cursor.getString(8);
				if (yString != null) {
					y = Float.valueOf(yString);
				}
				String zString = cursor.getString(9);
				if (zString != null) {
					z = Float.valueOf(zString);
				}

				// By saving gameId for every event we support the game id
				// changing between app versions
				gameKey = cursor.getString(28);
				secretKey = cursor.getString(29);

				// For backward compatibility, is gameKey null?
				if (gameKey == null) {
					gameKey = DEFAULT_GAME_KEY;
				}

				if (type.equals(GameAnalytics.DESIGN)) {
					// Create new arraylist if first event with this game id
					if (designEvents.get(gameKey) == null) {
						designEvents.put(gameKey, new EventList<DesignEvent>(
								secretKey));
					}
					eventId = cursor.getString(5);
					String valueString = cursor.getString(10);
					if (valueString != null) {
						value = Float.valueOf(valueString);
					}
					designEvents.get(gameKey).addEvent(
							new DesignEvent(userId, sessionId, build, eventId,
									area, x, y, z, value), rowId);
				} else if (type.equals(GameAnalytics.BUSINESS)) {
					// Create new arraylist if first event with this game id
					if (businessEvents.get(gameKey) == null) {
						businessEvents.put(gameKey,
								new EventList<BusinessEvent>(secretKey));
					}
					eventId = cursor.getString(5);
					currency = cursor.getString(11);
					amount = cursor.getInt(12);
					businessEvents.get(gameKey).addEvent(
							new BusinessEvent(userId, sessionId, build,
									eventId, area, x, y, z, currency, amount),
							rowId);
				} else if (type.equals(GameAnalytics.USER)) {
					// Create new arraylist if first event with this game id
					if (userEvents.get(gameKey) == null) {
						userEvents.put(gameKey, new EventList<UserEvent>(
								secretKey));
					}
					String genderString = cursor.getString(13);
					if (genderString != null) {
						gender = genderString.toCharArray()[0];
					}
					String birthYearString = cursor.getString(14);
					if (birthYearString != null) {
						birthYear = Integer.valueOf(birthYearString);
					}
					String friendCountString = cursor.getString(15);
					if (friendCountString != null) {
						friendCount = Integer.valueOf(friendCountString);
					}
					platform = cursor.getString(17);
					device = cursor.getString(18);
					osMajor = cursor.getString(19);
					osMinor = cursor.getString(20);
					sdkVersion = cursor.getString(21);
					installPublisher = cursor.getString(22);
					installSite = cursor.getString(23);
					installCampaign = cursor.getString(24);
					installAdgroup = cursor.getString(25);
					installAd = cursor.getString(26);
					installKeyword = cursor.getString(27);
					androidId = cursor.getString(30);
					userEvents.get(gameKey).addEvent(
							new UserEvent(userId, sessionId, build, area, x, y,
									z, gender, birthYear, friendCount,
									platform, device, osMajor, osMinor,
									sdkVersion, installPublisher, installSite,
									installCampaign, installAdgroup, installAd,
									installKeyword, androidId), rowId);
				} else if (type.equals(GameAnalytics.QUALITY)) {
					// Create new arraylist if first event with this game id
					if (qualityEvents.get(gameKey) == null) {
						qualityEvents.put(gameKey, new EventList<QualityEvent>(
								secretKey));
					}
					eventId = cursor.getString(5);
					message = cursor.getString(16);
					qualityEvents.get(gameKey).addEvent(
							new QualityEvent(userId, sessionId, build, eventId,
									area, x, y, z, message), rowId);
				} else if (type.equals(GameAnalytics.ERROR)) {
					// Create new arraylist if first event with this game id
					if (errorEvents.get(gameKey) == null) {
						errorEvents.put(gameKey, new EventList<ErrorEvent>(
								secretKey));
					}
					message = cursor.getString(16);
					severity = cursor.getString(31);
					errorEvents.get(gameKey).addEvent(
							new ErrorEvent(userId, sessionId, build, area, x,
									y, z, message, severity), rowId);
				}
				cursor.moveToNext();
			}
		}
		cursor.close();

		// Return Hashmaps
		return new Object[] { designEvents, businessEvents, userEvents,
				qualityEvents, errorEvents };
	}

	synchronized private void insert(ContentValues values) {
		if (MAXIMUM_EVENT_STORAGE == 0 || !isFull()) {
			db.insert(TABLENAME, null, values);
		}
	}

	protected void deleteSentEvents(ArrayList<Integer> eventsToDelete,
			String category) {
		GALog.i("Deleting " + eventsToDelete.size() + " " + category
				+ " events");
		String idList = "(";
		for (Integer i : eventsToDelete) {
			if (!idList.equals("(")) {
				idList += ",";
			}
			idList = idList + i;
		}
		idList += ")";

		db.delete(TABLENAME, "_id IN " + idList, null);
	}

	protected void addDesignEvent(String gameKey, String secretKey,
			String userId, String sessionId, String build, String eventId,
			String area, Float x, Float y, Float z, Float value) {
		final ContentValues values = new ContentValues();
		values.put(GAME_KEY, gameKey);
		values.put(SECRET_KEY, secretKey);
		values.put(TYPE, GameAnalytics.DESIGN);
		values.put(USER_ID, userId);
		values.put(SESSION_ID, sessionId);
		values.put(BUILD, build);
		values.put(EVENT_ID, eventId);
		values.put(AREA, area);
		// Position parameters are optional
		if (x != null) {
			values.put(X, x);
		}
		if (y != null) {
			values.put(Y, y);
		}
		if (z != null) {
			values.put(Z, z);
		}
		// Value parameter is optional
		if (value != null) {
			values.put(VALUE, value);
		}
		// Do insert on seperate thread so that if synchronization locks up the
		// method, main thread can return.
		new Thread() {
			public void run() {
				insert(values);
			}
		}.start();
	}

	protected void addBusinessEvent(String gameKey, String secretKey,
			String userId, String sessionId, String build, String eventId,
			String area, Float x, Float y, Float z, String currency, int amount) {
		final ContentValues values = new ContentValues();
		values.put(GAME_KEY, gameKey);
		values.put(SECRET_KEY, secretKey);
		values.put(TYPE, GameAnalytics.BUSINESS);
		values.put(USER_ID, userId);
		values.put(SESSION_ID, sessionId);
		values.put(BUILD, build);
		values.put(EVENT_ID, eventId);
		values.put(AREA, area);
		// Position parameters are optional
		if (x != null) {
			values.put(X, x);
		}
		if (y != null) {
			values.put(Y, y);
		}
		if (z != null) {
			values.put(Z, z);
		}
		values.put(CURRENCY, currency);
		values.put(AMOUNT, amount);
		// Do insert on seperate thread so that if synchronization locks up the
		// method, main thread can return.
		new Thread() {
			public void run() {
				insert(values);
			}
		}.start();
	}

	protected void addUserEvent(String gameKey, String secretKey,
			String userId, String sessionId, String build, String area,
			Float x, Float y, Float z, Character gender, Integer birthYear,
			Integer friendCount, String platform, String device,
			String osMajor, String osMinor, String sdkVersion,
			String installPublisher, String installSite,
			String installCampaign, String installAdgroup, String installAd,
			String installKeyword, String androidId) {
		final ContentValues values = new ContentValues();
		values.put(GAME_KEY, gameKey);
		values.put(SECRET_KEY, secretKey);
		values.put(TYPE, GameAnalytics.USER);
		values.put(USER_ID, userId);
		values.put(SESSION_ID, sessionId);
		values.put(BUILD, build);
		values.put(AREA, area);
		// Position parameters are optional
		if (x != null) {
			values.put(X, x);
		}
		if (y != null) {
			values.put(Y, y);
		}
		if (z != null) {
			values.put(Z, z);
		}
		// User info parameters are optional
		if (gender != null) {
			values.put(GENDER, String.valueOf(gender));
		}
		if (birthYear != null) {
			values.put(BIRTH_YEAR, birthYear);
		}
		if (friendCount != null) {
			values.put(FRIEND_COUNT, friendCount);
		}
		values.put(PLATFORM, platform);
		values.put(DEVICE, device);
		values.put(OS_MAJOR, osMajor);
		values.put(OS_MINOR, osMinor);
		values.put(SDK_VERSION, sdkVersion);
		values.put(INSTALL_PUBLISHER, installPublisher);
		values.put(INSTALL_SITE, installSite);
		values.put(INSTALL_CAMPAIGN, installCampaign);
		values.put(INSTALL_ADGROUP, installAdgroup);
		values.put(INSTALL_AD, installAd);
		values.put(INSTALL_KEYWORD, installKeyword);
		values.put(ANDROID_ID, androidId);
		// Do insert on seperate thread so that if synchronization locks up the
		// method, main thread can return.
		new Thread() {
			public void run() {
				insert(values);
			}
		}.start();
	}

	protected void addQualityEvent(String gameKey, String secretKey,
			String userId, String sessionId, String build, String eventId,
			String area, Float x, Float y, Float z, String message) {
		final ContentValues values = new ContentValues();
		values.put(GAME_KEY, gameKey);
		values.put(SECRET_KEY, secretKey);
		values.put(TYPE, GameAnalytics.QUALITY);
		values.put(USER_ID, userId);
		values.put(SESSION_ID, sessionId);
		values.put(BUILD, build);
		values.put(EVENT_ID, eventId);
		values.put(AREA, area);
		// Position parameters are optional
		if (x != null) {
			values.put(X, x);
		}
		if (y != null) {
			values.put(Y, y);
		}
		if (z != null) {
			values.put(Z, z);
		}
		values.put(MESSAGE, message);
		// Do insert on seperate thread so that if synchronization locks up the
		// method, main thread can return.
		new Thread() {
			public void run() {
				insert(values);
			}
		}.start();
	}

	protected void addErrorEvent(String gameKey, String secretKey,
			String userId, String sessionId, String build, String area,
			Float x, Float y, Float z, String message, String severity) {
		final ContentValues values = new ContentValues();
		values.put(GAME_KEY, gameKey);
		values.put(SECRET_KEY, secretKey);
		values.put(TYPE, GameAnalytics.ERROR);
		values.put(USER_ID, userId);
		values.put(SESSION_ID, sessionId);
		values.put(BUILD, build);
		values.put(AREA, area);
		// Position parameters are optional
		if (x != null) {
			values.put(X, x);
		}
		if (y != null) {
			values.put(Y, y);
		}
		if (z != null) {
			values.put(Z, z);
		}
		values.put(MESSAGE, message);
		values.put(SEVERITY, severity);
		// Do insert on seperate thread so that if synchronization locks up the
		// method, main thread can return.
		new Thread() {
			public void run() {
				insert(values);
			}
		}.start();
	}

	private boolean isFull() {
		Cursor cursor = db.query(TABLENAME, new String[] { ROW_ID }, null,
				null, null, null, null);
		if (cursor.getCount() >= MAXIMUM_EVENT_STORAGE) {
			return true;
		} else {
			return false;
		}
	}

	protected void setMaximumEventStorage(int maximumEventStorage) {
		MAXIMUM_EVENT_STORAGE = maximumEventStorage;
	}

	protected void clear() {
		db.delete(TABLENAME, null, null);
	}

	// OPENHELPER CLASS
	private class OpenHelper extends SQLiteOpenHelper {

		// Database details
		private final static String DB_NAME = "GameAnalytics";
		private final static int DB_VERSION = 3;

		public OpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Database is created for the first time
			// Create tables:
			GALog.i("Creating database to store events.");
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Version 1 - ORIGINAL
			// Version 2 - Added optional user fields
			// Version 3 - Added severity column
			if (newVersion > oldVersion) {
				String addColumn = "ALTER TABLE " + TABLENAME + " ADD COLUMN ";
				String text = " text";
				if (oldVersion <= 1) {
					db.execSQL(addColumn + PLATFORM + text);
					db.execSQL(addColumn + DEVICE + text);
					db.execSQL(addColumn + OS_MAJOR + text);
					db.execSQL(addColumn + OS_MINOR + text);
					db.execSQL(addColumn + SDK_VERSION + text);
					db.execSQL(addColumn + INSTALL_PUBLISHER + text);
					db.execSQL(addColumn + INSTALL_SITE + text);
					db.execSQL(addColumn + INSTALL_CAMPAIGN + text);
					db.execSQL(addColumn + INSTALL_ADGROUP + text);
					db.execSQL(addColumn + INSTALL_AD + text);
					db.execSQL(addColumn + INSTALL_KEYWORD + text);
					db.execSQL(addColumn + GAME_KEY + text);
					db.execSQL(addColumn + SECRET_KEY + text);
					db.execSQL(addColumn + ANDROID_ID + text);
				}
				if (oldVersion <= 2) {
					db.execSQL(addColumn + SEVERITY + text);
				}
			}
		}
	}
}