/**
 *
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
			PendingIntent sentPI = PendingIntent.getBroadcast(service, (sms.getId() * 100 + i), sentIntent, 0);

			sentPIs.add(sentPI);
		}

		smsManager.sendMultipartTextMessage(sms.getRecipient(), null, bodyParts, sentPIs, null);

		ContentValues values = new ContentValues();
		values.put("address", sms.getRecipient());
		values.put("body", sms.getBody());

		service.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
