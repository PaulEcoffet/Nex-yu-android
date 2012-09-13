/**
 *
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
