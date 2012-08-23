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
import org.nexyu.nexyuAndroid.MainActivity;
import org.nexyu.nexyuAndroid.R;
import org.nexyu.nexyuAndroid.SMSManagement.SMSReceiver;
import org.nexyu.nexyuAndroid.client.ClientPipelineFactory;
import org.nexyu.nexyuAndroid.client.protocol.SMSReceivedNetworkMessage;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Messenger;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Service that maintain the connection between Nex yu Android & Nex yu
 * computer.
 * 
 * @author Paul Ecoffet
 * 
 */
public class ConnectService extends Service
{
	public static final int		DEF_PORT				= 34340;
	private static final String	TAG						= "ConnectService";
	private static final int	ONGOING_NOTIFICATION	= 34340;
	public static final int		MSG_CONNECT				= 1;
	public static final int		MSG_CONNECTED			= 2;
	public static final int		MSG_IMPOSSIBLE_CONNECT	= 3;
	protected ChannelFactory	factory;
	protected Channel			chan;
	private Notification		notification;
	private Messenger			messenger;
	private SMSReceiver			smsReceiver;

	/**
	 * Default constructor.
	 * 
	 * @author Paul Ecoffet
	 */
	public ConnectService()
	{
		chan = null;
		notification = null;
		factory = null;
		messenger = new Messenger(new ServiceHandler(this));
		smsReceiver = new SMSReceiver(this);
	}

	/**
	 *
	 */
	public void activateSMSReceiver()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReceiver, filter);
		Log.d(TAG, "SMSReceiver registered.");
	}

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

		if (port == 0) // TODO Remove this
			port = DEF_PORT;
		ChannelFuture fuConn = bootstrap.connect(new InetSocketAddress(ip, port));
		fuConn.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture fuConn) throws Exception
			{
				chan = fuConn.getChannel();
				activateSMSReceiver();
			}
		});

	}

	/**
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();
		notification = new Notification(R.drawable.ic_launcher, getText(R.string.app_name),
				System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(this, getText(R.string.notification_title),
				getText(R.string.notif_not_connected), pendingIntent);
		startForeground(ONGOING_NOTIFICATION, notification);
		Log.i(TAG, "service started");
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
	 * Disconnect the android app from the computer server if the connection
	 * exist.
	 * 
	 * @author Paul Ecoffet
	 */
	private void disconnect()
	{
		if (connected())
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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return START_STICKY;
	}

	/**
	 * @return
	 */
	private boolean connected()
	{
		return chan != null && chan.isConnected();
	}

	/**
	 * Return the binder from the messenger.
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0)
	{
		return messenger.getBinder();
	}

	/**
	 * @param messages
	 */
	public void sendMessages(ArrayList<SmsMessage> messages)
	{
		if (connected())
		{
			SMSReceivedNetworkMessage toSend = new SMSReceivedNetworkMessage(messages);
			chan.write(toSend);
		}
	}
}