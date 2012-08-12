package org.nexyu.nexyu;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.nexyu.nexyu.client.ClientPipeline;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ConnectService extends IntentService
{
	/**
	 * 
	 */
	private final static String	TAG						= "ConnectService";
	private final static String	NAME					= "nexConnectService";
	private static final int	ONGOING_NOTIFICATION	= 0;
	protected ChannelFactory	factory;
	protected Channel			chan;
	private Notification		notification;

	public ConnectService()
	{
		super(NAME);
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
	}

	/**
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
		if (chan.isConnected())
		{
			chan.close().awaitUninterruptibly();
			factory.releaseExternalResources();
			Log.i(TAG, "Connection closed");
		}
		Log.i(TAG, "Service destroyed");
		super.onDestroy();
	}

	/**
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent)
	{
		if (intent.getStringExtra("action") != null)
			handleCommand(intent.getExtras());
		else
			Log.e(TAG, "no intended action");
	}

	/**
	 * @param stringExtra
	 */
	private void handleCommand(Bundle bundle)
	{
		String action = bundle.getString("action");
		if (action.equals("kill"))
			stopSelf();
		else if (action.equals("connect"))
		{
			ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null && ni.isConnected())
			{
				String ip = bundle.getString("ip");
				connect(ip, 4242);
			}
			else
			{
				Toast.makeText(
						this,
						R.string.the_device_is_not_connected_to_the_internet_impossible_to_connect_to_the_computer,
						Toast.LENGTH_LONG).show();
			}
		}
		else
			Log.e(TAG, "undefined action");
	}

}
