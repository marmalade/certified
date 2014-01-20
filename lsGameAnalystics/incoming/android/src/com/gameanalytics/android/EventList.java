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

public class EventList<E> extends ArrayList<E> {
	/**
	 * GENERATED ID JUST IN CASE SERIELISATION IS NEEDED IN FUTURE
	 */
	private static final long serialVersionUID = 6853023502311564615L;

	// Event ID list maintained alongside the main list of events
	private ArrayList<Integer> eventIdList = new ArrayList<Integer>();

	private String secretKey;

	public EventList(String secretKey) {
		super();
		this.secretKey = secretKey;
	}

	public boolean addEvent(E event, int id) {
		eventIdList.add(id);
		return super.add(event);
	}

	// GETTERS
	public String getSecretKey() {
		return secretKey;
	}

	public ArrayList<Integer> getEventIdList() {
		return eventIdList;
	}
}
