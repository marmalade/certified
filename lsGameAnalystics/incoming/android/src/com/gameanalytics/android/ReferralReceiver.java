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

import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class ReferralReceiver extends BroadcastReceiver {

	// Google campaign terms
	private static final String SOURCE = "utm_source";
	private static final String MEDIUM = "utm_medium";
	private static final String CAMPAIGN = "utm_campaign";
	private static final String CONTENT = "utm_content";
	private static final String TERM = "utm_term";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Set logging mode
		GameAnalytics.setDebugLogLevel(getDebugMode());

		// Get referrer details
		String referrerDetails = intent.getStringExtra("referrer");
		GALog.i("Referrer information = " + referrerDetails);

		String term;
		String value;
		String installPublisher = null; // utm_source
		String installSite = null; // utm_medium
		String installCampaign = null; // utm_campaign
		String installAd = null; // utm_content
		String installKeyword = null; // utm_term
		try {
			// Split into each campaign term e.g. "utm_source=..."
			StringTokenizer tokens = new StringTokenizer(
					intent.getStringExtra("referrer"), "&");
			StringTokenizer subTokens;
			while (tokens.hasMoreTokens()) {
				// Split into campaign term and value e.g:
				// token 1 = utm_source
				// token 2 = Google
				subTokens = new StringTokenizer(tokens.nextToken(), "=");
				term = subTokens.nextToken();
				value = subTokens.nextToken();
				if (term.equals(SOURCE)) {
					installPublisher = value;
				} else if (term.equals(MEDIUM)) {
					installSite = value;
				} else if (term.equals(CAMPAIGN)) {
					installCampaign = value;
				} else if (term.equals(CONTENT)) {
					installAd = value;
				} else if (term.equals(TERM)) {
					installKeyword = value;
				}
			}
		} catch (Exception e) {
			GALog.e("Error processing referral terms", e);
			return;
		}

		// Is game analytics initialised?
		if (!GameAnalytics.isInitialised()) {
			GameAnalytics.initialise(context, getSecretKey(), getGameKey());
		}
		if (GameAnalytics.isSessionStarted()) {
			// Send referral info
			GameAnalytics.setReferralInfo(installPublisher, installSite,
					installCampaign, null, installAd, installKeyword);
		} else {
			GameAnalytics.startSession(context);
			// Send referral info
			GameAnalytics.setReferralInfo(installPublisher, installSite,
					installCampaign, null, installAd, installKeyword);
			GameAnalytics.stopSession();
		}
	}

	public abstract String getSecretKey();

	public abstract String getGameKey();

	public abstract int getDebugMode();

}
