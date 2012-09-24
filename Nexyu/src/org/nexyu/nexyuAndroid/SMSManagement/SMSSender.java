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

import org.nexyu.nexyuAndroid.client.protocol.SMSToCell;
import org.nexyu.nexyuAndroid.service.NexyuService;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

/**
 * @author Paul Ecoffet
 *
 */
public class SMSSender
{
	public static final String	ACTION_SMS_SENT	= "org.nexyu.nexyuAndroid.SMS_SENT_ACTION";

	/**
	 * @param sms
	 * @param service
	 */
	public static void sendSMSthroughCellNetwork(SMSToCell sms, NexyuService service)
	{
		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> bodyParts = smsManager.divideMessage(sms.getBody());

		ArrayList<PendingIntent> sentPIs = new ArrayList<PendingIntent>();

		for (int i = 0; i < bodyParts.size(); i++)
		{
			Intent sentIntent = new Intent(ACTION_SMS_SENT);
			sentIntent.putExtra("id", sms.getId());
			sentIntent.putExtra("size", bodyParts.size());
			// (ms.getId() * 100 + i) forces the PendingIntent to create a real
			// new PI and not to reuse an existing one
			PendingIntent sentPI = PendingIntent.getBroadcast(service, (sms.getId() * 100 + i),
					sentIntent, 0);

			sentPIs.add(sentPI);
		}

		smsManager.sendMultipartTextMessage(sms.getRecipient(), null, bodyParts, sentPIs, null);

		ContentValues values = new ContentValues();
		values.put("address", sms.getRecipient());
		values.put("body", sms.getBody());

		service.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
