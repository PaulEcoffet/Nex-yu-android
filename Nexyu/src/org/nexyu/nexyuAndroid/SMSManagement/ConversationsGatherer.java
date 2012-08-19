/**
 * 
 */
package org.nexyu.nexyuAndroid.SMSManagement;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;

/**
 * @author Paul Ecoffet
 * 
 */
public class ConversationsGatherer
{
	protected Uri		mconversDatabase;
	protected FragmentActivity	mCtx;

	public ConversationsGatherer(FragmentActivity ctx)
	{
		mCtx = ctx;
		mconversDatabase = Uri.parse("content://sms/");
	}

	/**
	 * 
	 */
	public void gatherSMS()
	{
		// TODO Auto-generated method stub
		
	}
}
