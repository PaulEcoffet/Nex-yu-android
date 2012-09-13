/**
 *
 */
package org.nexyu.nexyuAndroid.SMSManagement;

import org.nexyu.nexyuAndroid.service.NexyuService;

import android.content.ContentValues;
import android.net.Uri;
import android.telephony.SmsManager;

import com.google.gson.JsonObject;

/**
 * @author Paul Ecoffet
 *
 */
public class SMSSender
{
	/**
	 * @param msg
	 * @param service
	 */
	public static void sendSMSthroughCellNetwork(JsonObject msg, NexyuService service)
	{
		SmsManager smsManager = SmsManager.getDefault();

		String recipient = msg.get("recipient").getAsString();
		String body = msg.get("body").getAsString();

		smsManager.sendMultipartTextMessage(recipient, null, smsManager.divideMessage(body), null,
				null);
		ContentValues values = new ContentValues();
		values.put("address", recipient);
		values.put("body", body);

		service.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
