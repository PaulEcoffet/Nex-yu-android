/**
 *
 */
package org.nexyu.nexyuAndroid.service;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

/**
 * Message handler that call NexyuService's functions depending on the message
 * received.
 * 
 * @author Paul Ecoffet
 */
class NexyuServiceHandler extends Handler
{
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
	}
}
