package org.nexyu.nexyu;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;
import org.nexyu.nexyu.client.JSONDecoder;
import org.nexyu.nexyu.client.MessageClientHandler;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ConnectService extends IntentService
{

	public class ConnectBinder extends Binder
	{
		ConnectService getService()
		{
			return ConnectService.this;
		}
	}

	/**
	 * 
	 */
	private final static String	TAG						= "service";
	private final static String	NAME					= "nexConnectService";
	private static final int	ONGOING_NOTIFICATION	= 0;
	protected ChannelFactory	factory					= null;
	protected Channel			chan					= null;
	private final IBinder		mBinder					= new ConnectBinder();
	private Notification		notification;

	public ConnectService()
	{
		super(NAME);
	}

	/**
	 * Connect the service to the IP given on port PORT.
	 * 
	 * @param ip
	 *            The IP to connect to.
	 * @param port
	 *            TODO
	 * @author Paul Ecoffet
	 */
	private void connect(String ip, int port)
	{
		factory = new OioClientSocketChannelFactory(Executors.newCachedThreadPool());
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception
			{
				LengthFieldBasedFrameDecoder lengthDecod = new LengthFieldBasedFrameDecoder(
						Integer.MAX_VALUE, 0, Integer.SIZE / Byte.SIZE, 0, Integer.SIZE / Byte.SIZE);
				StringDecoder stringDecod = new StringDecoder(CharsetUtil.UTF_8);
				JSONDecoder jsonDecoder = new JSONDecoder();
				MessageClientHandler messageClientHandler = new MessageClientHandler();
				LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4);
				StringEncoder stringEncoder = new StringEncoder(CharsetUtil.UTF_8);
				return Channels.pipeline(lengthDecod, stringDecod, jsonDecoder,
						messageClientHandler, lengthFieldPrepender, stringEncoder);
			}
		});

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
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}

	/**
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate()
	{
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
	}

	/**
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "Received start id " + startId + ": " + intent);
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
		{
			String ip = intent.getStringExtra("ip");
			connect(ip, 4242);
		}
		else
		{
			Toast.makeText(
					this,
					"The device is not connected to the Internet, impossible to connect to the computer",
					Toast.LENGTH_LONG).show();
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent)
	{

	}

}
