/*
 * Copyright 2012 Nex yu Android authors
 * 
 * This file is part of Nex yu Android.
 * 
 * Nex yu Android is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Nex yu Android is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Nex yu Android. If not, see <http://www.gnu.org/licenses/>
 */
package org.nexyu.nexyuAndroid.SMSManagement;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;

/**
 * Likely to be never implemented since it must use undocumented API methods
 * which should evolve in the next releases of Android.
 * 
 * @author Paul Ecoffet
 * 
 */
public class ConversationsGatherer
{
	protected Uri				mconversDatabase;
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
