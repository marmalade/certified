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

import com.google.gson.Gson;
import com.loopj.twicecircled.android.http.AsyncHttpResponseHandler;

public class PostResponseHandler extends AsyncHttpResponseHandler {

	// ERRORS
	private static final String BAD_REQUEST = "Not all required fields are present in the data.";
	private static final String BAD_REQUEST_DESC = "When you see this, most likely some required fields are missing from the JSON data you sent. Make sure you include all required fields for the category you are using. Please note that incomplete events are discarded.";
	private static final String NO_GAME = "Game not found";
	private static final String NO_GAME_DESC = "The game key supplied was not recognised. Make sure that you use the game key you were supplied when you signed up in GameAnalytics.initialise().";
	private static final String DATA_NOT_FOUND = "Data not found";
	private static final String DATA_NOT_FOUND_DESC = "No JSON data was sent with the request. Make sure that you are sending some data as either a single JSON object or as an array of JSON objects.";
	private static final String UNAUTHORIZED = "Unauthorized";
	private static final String UNAUTHORIZED_DESC = "The value of the authorization header is not valid. Make sure you use exactly the same secret key as was supplied to you when you created your Game Analytics account.";
	private static final String SIG_NOT_FOUND = "Signature not found in request";
	private static final String SIG_NOT_FOUND_DESC = "The \"Authorization\" header is missing. Make sure that you add a header with the \"Authorization\" key to your API call.";
	private static final String FORBIDDEN_DESC = "Make sure that the URL is valid and that it conforms to the specifications.";
	private static final String GAME_NOT_FOUND = "Game key not found";
	private static final String GAME_NOT_FOUND_DESC = "The game key in the URL does not match any existing games. Make sure that you are using the correct game key (the key which you received when creating your game on the GameAnalytics website).";
	private static final String METHOD_NOT_SUPPORTED = "Method not found";
	private static final String METHOD_NOT_SUPPORTED_DESC = "The URI used to post data to the server was incorrect. This could be because the game key supplied the GameAnalytics during initialise() was blank or null.";
	private static final String INTERNAL_SERVER_ERROR_DESC = "Internal server error. Please bring this error to Game Analytics attention. We are sorry for any inconvenience caused.";
	private static final String NOT_IMPLEMENTED_DESC = "The used HTTP method is not supported. Please only use the POST method for submitting data.";

	private ArrayList<Integer> eventsToDelete;
	private EventDatabase eventDatabase;
	private String category;

	public PostResponseHandler(ArrayList<Integer> eventsToDelete,
			EventDatabase eventDatabase, String category) {
		this.category = category;
		this.eventsToDelete = eventsToDelete;
		this.eventDatabase = eventDatabase;
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onFinish() {
		GameAnalytics.finishedSendingEvents(this);
	}

	@Override
	public void onSuccess(int statusCode, String content) {
		// Print response to log
		GALog.i(category + " events: Succesful response: " + content);
		eventDatabase.deleteSentEvents(eventsToDelete, category);
	}

	@Override
	public void onFailure(Throwable error, String content) {
		// Try convert error content into JSON
		GALog.i(category + " events: Failure response: " + content);
		Gson gson = new Gson();
		ErrorResponse errorResponse = null;
		try {
			errorResponse = gson.fromJson(content, ErrorResponse.class);
		} catch (Exception e) {
			GALog.e(category
					+ " events: Error converting failure response from json: "
					+ content, e);
		}

		if (errorResponse != null) {
			// Give advice based on error code
			String errorDescription = null;
			switch (errorResponse.code) {
			case 400:
				if (errorResponse.message.equals(BAD_REQUEST)) {
					errorDescription = BAD_REQUEST_DESC;
				} else if (errorResponse.message.equals(NO_GAME)) {
					errorDescription = NO_GAME_DESC;
				} else if (errorResponse.message.equals(DATA_NOT_FOUND)) {
					errorDescription = DATA_NOT_FOUND_DESC;
				}
				break;
			case 401:
				if (errorResponse.message.equals(UNAUTHORIZED)) {
					errorDescription = UNAUTHORIZED_DESC;
				} else if (errorResponse.message.equals(SIG_NOT_FOUND)) {
					errorDescription = SIG_NOT_FOUND_DESC;
				}
				break;
			case 403:
				errorDescription = FORBIDDEN_DESC;
				break;
			case 404:
				if (errorResponse.message.equals(GAME_NOT_FOUND)) {
					errorDescription = GAME_NOT_FOUND_DESC;
				} else if (errorResponse.message.equals(METHOD_NOT_SUPPORTED)) {
					errorDescription = METHOD_NOT_SUPPORTED_DESC;
				}
				break;
			case 500:
				errorDescription = INTERNAL_SERVER_ERROR_DESC;
				break;
			case 501:
				errorDescription = NOT_IMPLEMENTED_DESC;
				break;
			}
			if (errorDescription != null) {
				GALog.e("Error response code: " + errorResponse.code
						+ System.getProperty("line.separator")
						+ errorDescription);
			} else {
				GALog.i("Code: " + errorResponse.code);
				GALog.i("Message: " + errorResponse.message);
				GALog.e("Unrecognised response code: " + error.toString(),
						error);
			}
		} else {
			GALog.e("Error: " + error.toString(), error);
		}
	}

	protected String getCategory() {
		return category;
	}
	
	protected int getNumberOfEvents() {
		return eventsToDelete.size();
	}
};