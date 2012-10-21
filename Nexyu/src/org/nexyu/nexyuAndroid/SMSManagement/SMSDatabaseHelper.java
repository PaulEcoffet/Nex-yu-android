/**
 * 
 */
package org.nexyu.nexyuAndroid.SMSManagement;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

/**
 * @author Paul Ecoffet
 *
 */
public class SMSDatabaseHelper
{
	/**
	 * @param intent
	 */
	public static void addSMSSentToDatabase(Context service, String address, String body)
	{
		ContentValues values = new ContentValues();
		values.put("address", address);
		values.put("body", body);
		values.put("read", 1);
		service.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
