/**
 * 
 */
package org.nexyu.nexyu.SMSManagement;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * @author Paul Ecoffet
 * 
 */
public class ConversationsGatherer
{
	protected Uri		conversDatabase;
	protected Context	ctx;

	public ArrayList<SmsMessage> gatherSMS()
	{
		CursorLoader curload = new CursorLoader(ctx, conversDatabase, null, null, null, null);
		Cursor curSms = curload.loadInBackground();
		curload.
		String[] names = curSms.getColumnNames();
		for(String name : names)
		{
			Log.i("Data", name);
		}
		return null;
	}

	public ConversationsGatherer(Context _ctx)
	{
		ctx = _ctx;
		conversDatabase = Uri.parse("content://sms/all");
	}

}
