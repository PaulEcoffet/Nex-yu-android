/**
 *
 */
package org.nexyu.nexyuAndroid.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.nexyu.nexyuAndroid.client.protocol.NetworkMessageable;
import org.nexyu.nexyuAndroid.service.NexyuService;

import android.util.Log;

/**
 * @author Paul Ecoffet
 * 
 */
public class ConnectionManager
{
	protected static final String			TAG	= "ConnectionManager";
	private OioClientSocketChannelFactory	factory;
	private Channel							chan;
	private NexyuService					service;

	public ConnectionManager(NexyuService mService)
	{
		service = mService;
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
	public void connect(String host, int port)
	{
		factory = new OioClientSocketChannelFactory(Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ClientPipelineFactory(service.getMessenger().getBinder()));
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);

		ChannelFuture fuConn = bootstrap.connect(new InetSocketAddress(host, port));
		fuConn.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture fuConn) throws Exception
			{
				if (fuConn.isSuccess())
				{
					chan = fuConn.getChannel();
					service.activateSMSReceiver();
					service.activateSMSSentChecker();
				}
				else
				{
					Log.w(TAG, "Impossible to connect");
				}
			}
		});
	}

	/**
	 * Disconnect the android application from the computer server if the
	 * connection exist.
	 * 
	 * @author Paul Ecoffet
	 */
	public void disconnect()
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
	public boolean isConnected()
	{
		return (chan != null) && chan.isConnected();
	}

	public void send(NetworkMessageable toSend)
	{
		if (isConnected())
			chan.write(toSend.toNetworkMessage());
	}
}
