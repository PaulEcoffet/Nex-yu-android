/*
 * Copyright 2012 Nex yu Android authors
 * 
 * This file is part of Nex yu Android.
 * 
 * Nex yu Android is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Nex yu Android is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Nex yu Android. If not, see <http://www.gnu.org/licenses/>
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
	private final NexyuService				service;

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
	public void connect(String host, int port, String fingerprint)
	{
		factory = new OioClientSocketChannelFactory(Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ClientPipelineFactory(service.getMessenger().getBinder(),
				fingerprint));
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
