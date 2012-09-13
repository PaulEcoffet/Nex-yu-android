package org.nexyu.nexyuAndroid.service;

import org.nexyu.nexyuAndroid.SMSManagement.SMSReceiver;
import org.nexyu.nexyuAndroid.client.ConnectionManager;
import org.nexyu.nexyuAndroid.client.protocol.SMSToSendNetworkMessage;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

/**
 * Core service of Nex yu Android. It receive message from the UI thread, the
 * SMS receiver thread & the client thread and trigger main action of the
 * software.
 * 
 * @author Paul Ecoffet
 * 
 */
public class NexyuService extends Service
{
	public static final int		DEF_PORT	= 34340;
	private static final String	TAG			= "ConnectService";

	public static enum What
	{
		MSG_CONNECT, MSG_CONNECTED, MSG_IMPOSSIBLE_CONNECT, MSG_SEND_SMS
	};

	static What[]				whatList				= What.values();
	private Messenger			messenger;
	private SMSReceiver			smsReceiver;
	private boolean				smsReceiverRegistered	= false;
	private ConnectionManager	connectionManager;

	/**
	 * Default constructor.
	 * 
	 * @author Paul Ecoffet
	 */
	public NexyuService()
	{
		messenger = new Messenger(new NexyuServiceHandler(this));
		smsReceiver = new SMSReceiver(this);
		connectionManager = new ConnectionManager(this);
	}

	/**
	 * Activate the SMSReceiver which will notify the service when a SMS is
	 * received.
	 * 
	 * @author Paul Ecoffet
	 */
	public void activateSMSReceiver()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReceiver, filter);
		smsReceiverRegistered = true;
		Log.d(TAG, "SMSReceiver registered.");
	}

	/**
	 * Deactivate the SMSReceiver which communicate with this service.
	 * 
	 * @author Paul Ecoffet
	 */
	public void deactivateSMSReceiver()
	{
		if (smsReceiverRegistered)
		{
			unregisterReceiver(smsReceiver);
			smsReceiverRegistered = false;
		}
	}

	/**
	 * Called when the service is destroy. It close the connection between the
	 * phone & the computer if any, then free resources (netty side)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
		deactivateSMSReceiver();
		connectionManager.disconnect();
		Log.i(TAG, "Service destroyed");
		super.onDestroy();
	}

	/**
	 * Return the binder from the messenger so that others threads could
	 * communicate with the service.
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0)
	{
		return messenger.getBinder();
	}

	/**
	 * Send the list of messages given to the computer through network.
	 * 
	 * @param toSend
	 *            The message to send to the computer.
	 */
	public void sendMessagesToComputer(SMSToSendNetworkMessage toSend)
	{
		connectionManager.send(toSend);
	}

	/**
	 * @return
	 */
	public Messenger getMessenger()
	{
		return messenger;
	}

	/**
	 * @param string
	 * @param int1
	 */
	public void connect(String host, int port)
	{
		connectionManager.connect(host, port);
	}
}
