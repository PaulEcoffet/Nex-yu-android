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

import org.nexyu.nexyuAndroid.client.ConnectionManager;
import org.nexyu.nexyuAndroid.client.protocol.SMSSent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseArray;

/**
 * @author Paul Ecoffet
 * 
 */
public class SMSSentChecker extends BroadcastReceiver
{
	SparseArray<Integer>	SMSList	= null;
	ConnectionManager		cm		= null;

	/**
	 *
	 */
	public SMSSentChecker(ConnectionManager _cm)
	{
		SMSList = new SparseArray<Integer>();
		cm = _cm;
	}

	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 *      android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		boolean error = true;
		String message = "ok";

		switch (getResultCode())
		{
		case Activity.RESULT_OK:
			int id = intent.getIntExtra("id", -1);
			SMSList.append(id, SMSList.get(id, 0) + 1);
			Log.i("SMSSentChecker",
					id + ": " + SMSList.get(id) + " out of " + intent.getIntExtra("size", 0));
			if (id != -1 && SMSList.get(id) >= intent.getIntExtra("size", 1))
			{
				SMSList.delete(id);
				cm.send(new SMSSent(id, SMSSent.SUCCESS));
			}
			error = false;
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			message = "Error.";
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			message = "Error: No service.";
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			message = "Error: Null PDU.";
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			message = "Error: Radio off.";
			break;
		default:
			break;
		}
		if (error)
		{
			Log.d("SMSSentChecker", message);
		}
	}

}
