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

import org.nexyu.nexyuAndroid.client.protocol.SMSToComputer;
import org.nexyu.nexyuAndroid.service.NexyuService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Receiver which catch SMS broadcast so as to send its content to Nex yu core
 * service. Activated only when asked by the Nex yu core service.
 * 
 * @author Paul Ecoffet
 */
public class SMSReceiver extends BroadcastReceiver
{
	private static final String	SMS_RECEIVED	= "android.provider.Telephony.SMS_RECEIVED";
	private static final String	TAG				= "SMSReceiver";
	private NexyuService		mService;

	/**
	 * Default constructor, it needs the NexyuService that has started it so as
	 * to communicate with it.
	 * 
	 * @param service
	 *            The NexyuService that started the broadcast receiver. The
	 *            SMSReceiver will communicate with it.
	 * @author Paul Ecoffet
	 */
	public SMSReceiver(NexyuService service)
	{
		super();
		mService = service;
	}

	/**
	 * Callback called when SMS are received. It forwards the SMS to the Nexyu
	 * core service so that it sends them to Nexyu Comp.
	 * 
	 * @author Paul Ecoffet
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 *      android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.i(TAG, "SMS received.");
		if (intent.getAction().equals(SMS_RECEIVED))
		{
			Bundle bundle = intent.getExtras();
			if (bundle != null)
			{
				Object[] pdus = (Object[]) bundle.get("pdus");
				ArrayList<SmsMessage> messages = new ArrayList<SmsMessage>();
				for (Object pdu : pdus)
				{
					messages.add(SmsMessage.createFromPdu((byte[]) pdu));
				}
				SMSToComputer toSend = new SMSToComputer(messages);
				mService.sendMessagesToComputer(toSend);
			}
		}
	}
}
