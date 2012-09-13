/**
 *
 */
package org.nexyu.nexyuAndroid.SMSManagement;

import org.nexyu.nexyuAndroid.client.protocol.SMSToCell;
import org.nexyu.nexyuAndroid.service.NexyuService;

import android.content.ContentValues;
import android.net.Uri;
import android.telephony.SmsManager;

/**
 * @author Paul Ecoffet
 * 
 */
public class SMSSender
{
	/**
	 * @param sms
	 * @param service
	 */
	public static void sendSMSthroughCellNetwork(SMSToCell sms, NexyuService service)
	{
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendMultipartTextMessage(sms.getRecipient(), null,
				smsManager.divideMessage(sms.getBody()), null, null);
		ContentValues values = new ContentValues();
		values.put("address", sms.getRecipient());
		values.put("body", sms.getBody());

		service.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
