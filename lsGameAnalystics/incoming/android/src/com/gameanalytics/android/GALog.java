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

import android.util.Log;

public class GALog {
	// Logging class for GameAnalytics: allows user to set the logging level for
	// GameAnalytics

	private static final String TAG = "GameAnalytics";

	protected static void i(String message) {
		if (GameAnalytics.LOGGING == GameAnalytics.VERBOSE)
			Log.i(TAG, message);
	}

	protected static void w(String message) {
		Log.w(TAG, message);
	}

	protected static void e(String message) {
		Log.e(TAG, message);
	}

	protected static void e(String message, Throwable e) {
		Log.e(TAG, message, e);
	}

}
