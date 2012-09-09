package org.nexyu.nexyuAndroid.service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.nexyu.nexyuAndroid.SMSManagement.SMSReceiver;
import org.nexyu.nexyuAndroid.client.ClientPipelineFactory;
import org.nexyu.nexyuAndroid.client.protocol.SMSToSendNetworkMessage;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Messenger;
import android.telephony.SmsMessage;
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
	public static final int		DEF_PORT				= 34340;
	private static final String	TAG						= "ConnectService";
	public static final int		MSG_CONNECT				= 1;
	public static final int		MSG_CONNECTED			= 2;
	public static final int		MSG_IMPOSSIBLE_CONNECT	= 3;
	public static final int		MSG_SEND_SMS			= 4;
	protected ChannelFactory	factory;
	protected Channel			chan;
	private Messenger			messenger;
	private SMSReceiver			smsReceiver;

	/**
	 * Default constructor.
	 * 
	 * @author Paul Ecoffet
	 */
	public NexyuService()
	{
		chan = null;
		factory = null;
		messenger = new Messenger(new ServiceHandler(this));
		smsReceiver = new SMSReceiver(this);
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
		Log.d(TAG, "SMSReceiver registered.");
	}

	/**
	 * Deactivate the SMSReceiver which communicate with this service.
	 * 
	 * @author Paul Ecoffet
	 */
	public void deactivateSMSReceiver()
	{
		unregisterReceiver(smsReceiver);
	}

	/**
	 * Connect the service to the IP given on port PORT.
	 * 
	 * @param ip
	 *            The IP to connect to.
	 * @param port
	 *            The port to connect on.
	 * @author Paul Ecoffet
	 */
	void connect(String ip, int port)
	{
		factory = new OioClientSocketChannelFactory(Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ClientPipelineFactory(messenger.getBinder()));
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		ChannelFuture fuConn = bootstrap.connect(new InetSocketAddress(ip, port));
		fuConn.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture fuConn) throws Exception
			{
				if (fuConn.isSuccess())
				{
					chan = fuConn.getChannel();
					activateSMSReceiver();
				}
				else
				{
					Log.w(TAG, "Impossible to connect");
				}
			}
		});

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
		disconnect();
		Log.i(TAG, "Service destroyed");
		super.onDestroy();
	}

	/**
	 * Disconnect the android application from the computer server if the
	 * connection exist.
	 * 
	 * @author Paul Ecoffet
	 */
	private void disconnect()
	{
		if (isConnected())
		{
			ChannelFuture f = chan.close();
			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception
				{
					Log.d(TAG, "Disconnected");
					factory.releaseExternalResources();
				}
			});
		}
	}

	/**
	 * Test if the connection between the Nex yu Android application and the Nex
	 * yu Comp software is made.
	 * 
	 * @return whether the application is connected to Nex yu Comp or not.
	 */
	private boolean isConnected()
	{
		return (chan != null) && chan.isConnected();
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
	 * @param messages
	 *            The list of messages to send to the computer.
	 */
	public void sendMessagesToComputer(ArrayList<SmsMessage> messages)
	{
		if (isConnected())
		{
			SMSToSendNetworkMessage toSend = new SMSToSendNetworkMessage(messages);
			chan.write(toSend);
		}
	}
}
