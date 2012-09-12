/**
 *
 */
package org.nexyu.nexyuAndroid.SMSManagement;

import java.lang.ref.WeakReference;

import org.nexyu.nexyuAndroid.service.NexyuService;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Message;
import android.telephony.SmsManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Paul Ecoffet
 *
 */
public class SMSSender
{
	/**
	 * @param msg
	 * @param refService
	 */
	public static void sendSMSthroughMobile(Message msg, WeakReference<NexyuService> refService)
	{
		JsonObject json;
		SmsManager smsManager = SmsManager.getDefault();
		NexyuService service = refService.get();

		json = ((JsonElement) msg.obj).getAsJsonObject();

		String recipient = json.get("recipient").getAsString();
		String body = json.get("body").getAsString();

		smsManager.sendMultipartTextMessage(recipient, null, smsManager.divideMessage(body), null,
				null);
		ContentValues values = new ContentValues();
		values.put("address", recipient);
		values.put("body", body);

		service.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
