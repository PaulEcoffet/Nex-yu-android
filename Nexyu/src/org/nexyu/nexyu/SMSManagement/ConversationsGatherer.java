/**
 * 
 */
package org.nexyu.nexyu.SMSManagement;

import java.util.ArrayList;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * @author Paul Ecoffet
 * 
 */
public class ConversationsGatherer implements LoaderCallbacks<Cursor>
{
	protected Uri		mconversDatabase;
	protected FragmentActivity	mCtx;

	public ConversationsGatherer(FragmentActivity ctx)
	{
		mCtx = ctx;
		mconversDatabase = Uri.parse("content://sms/");
	}

	/**
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
	 *      android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		CursorLoader curLoad = new CursorLoader(mCtx);
		String[] columns = {"_id" ,"thread_id", "person", "date", "read", "status", "body" };
		curLoad.setUri(mconversDatabase);
		curLoad.setProjection(columns);
		curLoad.setSortOrder("date DESC");
		return curLoad;
	}

	/**
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader,
	 *      java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> curLoad, Cursor curSms)
	{
		if(curSms == null)
		{
			return;
		}
		Log.i("DATA", String.valueOf(curSms.getCount()));
		while(curSms.moveToNext())
		{
			Log.i("DATA", curSms.getString(curSms.getColumnIndex("body")));
		}
	}

	/**
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> curLoad)
	{
	}

}
