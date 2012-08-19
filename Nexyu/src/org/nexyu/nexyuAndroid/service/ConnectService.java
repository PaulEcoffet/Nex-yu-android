package org.nexyu.nexyuAndroid.service;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.nexyu.nexyuAndroid.MainActivity;
import org.nexyu.nexyuAndroid.R;
import org.nexyu.nexyuAndroid.client.ClientPipeline;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
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
	private final static String	TAG						= "ConnectService";
	private static final int	ONGOING_NOTIFICATION	= 34340;
	public static final int		MSG_CONNECT				= 1;
	protected ChannelFactory	factory;
	protected Channel			chan;
	private Notification		notification;
	private Messenger			messenger;

	/**
	 * Message handler that call the service function depending on the message
	 * received.
	 * 
	 * @author Paul Ecoffet
	 */
	static class IncomingHandler extends Handler
	{
		private final WeakReference<ConnectService>	mService;

		/**
		 * Unique constructor, create a reference to the service that must be
		 * manipulated.
		 * 
		 * @author Paul Ecoffet
		 */
		public IncomingHandler(ConnectService service)
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
			case MSG_CONNECT:
				service.connect(msg.getData().getString("ip"), msg.getData().getInt("port"));
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

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
		messenger = new Messenger(new IncomingHandler(this));
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
	private boolean connect(String ip, int port)
	{
		factory = new OioClientSocketChannelFactory(Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ClientPipeline());
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		if (port == 0) // TODO Remove this
			port = DEF_PORT;
		ChannelFuture fuConn = bootstrap.connect(new InetSocketAddress(ip, port));
		fuConn.awaitUninterruptibly();
		chan = fuConn.getChannel();
		return fuConn.isSuccess();
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
		if (chan != null && chan.isConnected())
		{
			chan.close().awaitUninterruptibly();
			factory.releaseExternalResources();
			Log.i(TAG, "Connection closed");
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return START_STICKY;
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
}
