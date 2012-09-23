package org.nexyu.nexyuAndroid.service;

import org.nexyu.nexyuAndroid.R;
import org.nexyu.nexyuAndroid.ContactsManagement.ContactsGatherer;
import org.nexyu.nexyuAndroid.SMSManagement.SMSReceiver;
import org.nexyu.nexyuAndroid.SMSManagement.SMSSender;
import org.nexyu.nexyuAndroid.SMSManagement.SMSSentChecker;
import org.nexyu.nexyuAndroid.client.ConnectionManager;
import org.nexyu.nexyuAndroid.client.protocol.ContactsList;
import org.nexyu.nexyuAndroid.client.protocol.SMSToCell;
import org.nexyu.nexyuAndroid.client.protocol.SMSToComputer;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

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
	private static final String	TAG	= "ConnectService";

	public static enum What
	{
		MSG_CONNECT, MSG_CONNECTED, MSG_IMPOSSIBLE_CONNECT, MSG_SEND_SMS, MSG_SEND_CONTACT_LIST
	};

	static public What[]		whatList	= What.values();
	private Messenger			messenger;
	private SMSReceiver			smsReceiver;
	private ConnectionManager	connectionManager;
	private SMSSentChecker		smsSentChecker;

	/**
	 * Default constructor.
	 *
	 * @author Paul Ecoffet
	 */
	public NexyuService()
	{
		messenger = new Messenger(new NexyuServiceHandler(this));
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
		if (smsReceiver == null)
		{
			smsReceiver = new SMSReceiver(this);
			IntentFilter filter = new IntentFilter();
			filter.addAction("android.provider.Telephony.SMS_RECEIVED");
			registerReceiver(smsReceiver, filter);
			Log.d(TAG, "SMSReceiver registered.");
		}
	}

	/**
	 * Deactivate the SMSReceiver which communicate with this service.
	 *
	 * @author Paul Ecoffet
	 */
	public void deactivateSMSReceiver()
	{
		if (smsReceiver != null)
		{
			unregisterReceiver(smsReceiver);
			smsReceiver = null;
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
		deactivateSMSSentChecker();
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
	public void sendMessagesToComputer(SMSToComputer toSend)
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
	 * @param msg
	 */
	public void handleMessage(Message msg)
	{
		Bundle data;

		switch (NexyuService.whatList[msg.what])
		{
		case MSG_CONNECT:
			data = msg.getData();
			connectionManager.connect(data.getString("ip"), data.getInt("port"));
			break;
		case MSG_CONNECTED:
			Log.i(TAG, "Connected message received");
			Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
			break;
		case MSG_IMPOSSIBLE_CONNECT:
			Toast.makeText(this, R.string.impossible_to_connect, Toast.LENGTH_LONG).show();
			break;
		case MSG_SEND_SMS:
			SMSSender.sendSMSthroughCellNetwork((SMSToCell) msg.obj, this);
			break;
		case MSG_SEND_CONTACT_LIST:
			ContactsGatherer cg = new ContactsGatherer(this);
			connectionManager.send(new ContactsList(cg.gatherContacts()));
		default:
			break;
		}
	}

	/**
	 *
	 */
	public void activateSMSSentChecker()
	{
		if (smsSentChecker == null)
		{
			smsSentChecker = new SMSSentChecker(connectionManager);
			registerReceiver(smsSentChecker, new IntentFilter(SMSSender.ACTION_SMS_SENT));
		}
	}

	public void deactivateSMSSentChecker()
	{
		if (smsSentChecker != null)
		{
			unregisterReceiver(smsSentChecker);
			smsSentChecker = null;
		}
	}
}
