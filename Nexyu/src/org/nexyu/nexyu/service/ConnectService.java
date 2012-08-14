package org.nexyu.nexyu.service;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.nexyu.nexyu.MainActivity;
import org.nexyu.nexyu.R;
import org.nexyu.nexyu.R.drawable;
import org.nexyu.nexyu.R.string;
import org.nexyu.nexyu.client.ClientPipeline;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ConnectService extends Service
{
	/**
	 * 
	 */
	private static final int	DEF_PORT				= 4242;
	/**
	 * 
	 */
	private final static String	TAG						= "ConnectService";
	private static final int	ONGOING_NOTIFICATION	= 34340;
	protected ChannelFactory	factory;
	protected Channel			chan;
	private Notification		notification;

	public ConnectService()
	{
		chan = null;
		notification = null;
		factory = null;
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
	private void connect(String ip, int port)
	{
		factory = new OioClientSocketChannelFactory(Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ClientPipeline());
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		ChannelFuture fuConn = bootstrap.connect(new InetSocketAddress(ip, port));
		fuConn.awaitUninterruptibly();
		if (!fuConn.isSuccess())
		{
			Log.e(TAG, getString(R.string.impossible_to_connect));
			fuConn.getCause().printStackTrace();
		}
		chan = fuConn.getChannel();
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
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
		if (chan != null && chan.isConnected())
		{
			chan.close().awaitUninterruptibly();
			factory.releaseExternalResources();
			Log.i(TAG, "Connection closed");
		}
		Log.i(TAG, "Service destroyed");
		super.onDestroy();
	}

	/**
	 * @param stringExtra
	 */
	private void handleCommand(Bundle bundle)
	{
		String action = bundle.getString("action");
		if (action.equals("kill"))
		{
			stopSelf();
		}
		else if (action.equals("connect"))
		{
			ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null && ni.isConnected())
			{
				String ip = bundle.getString("ip");
				connect(ip, DEF_PORT);
			}
			else
			{
				Toast.makeText(
						this,
						R.string.the_device_is_not_connected_to_the_internet_impossible_to_connect_to_the_computer,
						Toast.LENGTH_LONG).show();
			}
		}
		else if (action != null)
		{
			Log.e(TAG, "undefined action:" + action);
		}
	}

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}
}