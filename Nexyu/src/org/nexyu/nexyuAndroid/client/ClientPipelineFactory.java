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

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.util.CharsetUtil;

import android.os.IBinder;
import android.os.Messenger;

/**
 * Pipeline factory that creates the pipeline used by netty to format the
 * messages to send and received over the network.
 * 
 * @author Paul Ecoffet
 * 
 */
public final class ClientPipelineFactory implements ChannelPipelineFactory
{
	private final Messenger	mService;

	/**
	 * Constructor of the class which requires a NexyuService binder so as to
	 * communicate with the Nexyu core service.
	 * 
	 * @param serviceBinder
	 *            NexyuService binder with whom the network thread will
	 *            communicate.
	 * 
	 * @author Paul Ecoffet
	 */
	public ClientPipelineFactory(IBinder serviceBinder)
	{
		mService = new Messenger(serviceBinder);
	}

	/**
	 * Create the Pipeline that encodes the messages to send over the network
	 * and decode the messages from it. All these messages are caught by
	 * messageClientHandler before.
	 * 
	 * @author Paul Ecoffet
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		SslHandler sslHandler = new SslHandler(engine, true);
		
		LengthFieldBasedFrameDecoder lengthDecod = new LengthFieldBasedFrameDecoder(
				Integer.MAX_VALUE, 0, Integer.SIZE / Byte.SIZE, 0, Integer.SIZE / Byte.SIZE);
		
		StringDecoder stringDecod = new StringDecoder(CharsetUtil.UTF_8);
		
		StringJSONtoNetMessageDecoder jsonDecoder = new StringJSONtoNetMessageDecoder();
		
		NetMessageToJSONEncoder jsonEncoder = new NetMessageToJSONEncoder();
		
		LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4);
		
		StringEncoder stringEncoder = new StringEncoder(CharsetUtil.UTF_8);
		
		MessageClientHandler messageClientHandler = new MessageClientHandler(mService);
		
		
		return Channels.pipeline(lengthDecod, stringDecod, jsonDecoder, lengthFieldPrepender,
				stringEncoder, jsonEncoder, messageClientHandler);
	}

}
