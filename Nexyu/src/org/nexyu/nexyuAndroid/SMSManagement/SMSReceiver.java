/**
 * 
 */
package org.nexyu.nexyuAndroid.SMSManagement;

import java.util.ArrayList;

import org.nexyu.nexyuAndroid.service.ConnectService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * @author Paul Ecoffet
 * 
 */
public class SMSReceiver extends BroadcastReceiver
{
	private static final String	SMS_RECEIVED	= "android.provider.Telephony.SMS_RECEIVED";
	private ConnectService	mService;
	
	public SMSReceiver(ConnectService service)
	{
		super();
		mService = service;
	}

	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 *      android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(intent.getAction().equals(SMS_RECEIVED))
		{
			Bundle bundle = intent.getExtras();
			if (bundle != null)
			{
				Object[] pdus = (Object[]) bundle.get("pdus");
				ArrayList<SmsMessage> messages = new ArrayList<SmsMessage>();
				for(Object pdu :pdus)
				{
					messages.add(SmsMessage.createFromPdu((byte[]) pdu));
				}
				mService.sendMessages(messages);
			}
		}
	}
}
