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
package org.nexyu.nexyuAndroid.service;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Message handler that call NexyuService's functions depending on the message
 * received.
 * 
 * @author Paul Ecoffet
 */
class NexyuServiceHandler extends Handler
{
	private static final String	TAG	= "NexyuServiceHandler";
	private final WeakReference<NexyuService>	mService;

	/**
	 * Unique constructor, create a weak reference to the service that must be
	 * manipulated.
	 * 
	 * @author Paul Ecoffet
	 */
	public NexyuServiceHandler(NexyuService service)
	{
		mService = new WeakReference<NexyuService>(service);
	}

	/**
	 * Callback called when a message is received. It manages which function of
	 * the service is called depending of the type of message received.
	 * 
	 * @author Paul Ecoffet
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg)
	{
		NexyuService service = mService.get();
		if (service != null)
		{
			service.handleMessage(msg);
		}
		else
		{
			Log.i(TAG, "The service is dead");
		}
	}
}
