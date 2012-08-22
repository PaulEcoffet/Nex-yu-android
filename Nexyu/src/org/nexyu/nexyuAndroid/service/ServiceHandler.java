/**
 * 
 */
package org.nexyu.nexyuAndroid.service;

import java.lang.ref.WeakReference;

import org.nexyu.nexyuAndroid.R;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Message handler that call the service function depending on the message
 * received.
 * 
 * @author Paul Ecoffet
 */
class ServiceHandler extends Handler
{
	private static final String	TAG	= "IncomingHandler";
	private final WeakReference<ConnectService>	mService;

	/**
	 * Unique constructor, create a reference to the service that must be
	 * manipulated.
	 * 
	 * @author Paul Ecoffet
	 */
	public ServiceHandler(ConnectService service)
	{
		mService = new WeakReference<ConnectService>(service);
	}

	/**
	 * Callback called when a message is received. It manage which function
	 * of the service is called depending of the type of message received
	 * 
	 * @author Paul Ecoffet
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg)
	{
		ConnectService service = mService.get();
		switch (msg.what)
		{
		case ConnectService.MSG_CONNECT:
			boolean success = service.connect(msg.getData().getString("ip"), msg.getData()
					.getInt("port"));
			if (success)
			{
				Log.d(TAG, "Connection is a sucess");
				service.activateSMSReceiver();
			}
			else
			{
				Toast.makeText(service, R.string.impossible_to_connect, Toast.LENGTH_LONG)
						.show();
			}
			break;
		case ConnectService.MSG_CONNECTED:
			Log.i(TAG, "Connected message received");
			Toast.makeText(service, "Connected", Toast.LENGTH_SHORT).show();
		default:
			super.handleMessage(msg);
		}
	}
}