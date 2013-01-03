/*
 * Copyright 2012 Nex yu Android authors
 * 
 * This file is part of Nex yu Android.
 * 
 * Nex yu Android is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Nex yu Android is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Nex yu Android. If not, see <http://www.gnu.org/licenses/>
 */
package org.nexyu.nexyuAndroid.SMSManagement;

import java.util.ArrayList;

import org.nexyu.nexyuAndroid.client.protocol.Conversation;
import org.nexyu.nexyuAndroid.service.NexyuService;

import android.database.Cursor;
import android.net.Uri;

/**
 * Likely to be never implemented since it must use undocumented API methods
 * which should evolve in the next releases of Android.
 * 
 * @author Paul Ecoffet
 * 
 */
public class ConversationsGatherer
{
	protected Uri conversDatabase;
	protected NexyuService service;

	public ConversationsGatherer(NexyuService nexyuService)
	{
		service = nexyuService;
		conversDatabase = Uri.parse("content://sms/conversations");
	}

	public ArrayList<Conversation> gatherConversations()
	{
		ArrayList<Conversation> conversations = new ArrayList<Conversation>();
		String address = null, snippet = null;
		int thread_id = 0, date = 0, type = 0;
		Cursor cursor = service.getContentResolver().query(conversDatabase,
				null, null, null, "date DESC");

		while (cursor.moveToNext())
		{
			thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));
			snippet = cursor.getString(cursor.getColumnIndex("snippet"));
			Uri convUri = Uri.parse("content://sms/conversations/" + thread_id);
			Cursor cur = service.getContentResolver().query(convUri,
					new String[] { "address", "date", "type" }, null, null,
					"date DESC");
			if (cur.moveToNext())
			{
				address = cur.getString(cur.getColumnIndex("address"));
				date = cur.getInt(cur.getColumnIndex("date"));
				type = cur.getInt(cur.getColumnIndex("type"));
			}
			conversations.add(new Conversation(thread_id, address, snippet,
					date, type));
		}

		return conversations;
	}
}
