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

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.nexyu.nexyuAndroid.client.protocol.NetworkMessage;

import com.google.gson.Gson;

/**
 * Encoder that encodes a NetworkMessage into a JsonObject thanks to gson then
 * forward it to the next encoder.
 * 
 * @author Paul Ecoffet
 * 
 */
public class NetMessageToJSONEncoder extends OneToOneEncoder
{
	/**
	 * Encodes a NetworkMessage into a JsonObject thanks to gson, then forward
	 * it to the next encoder.
	 * 
	 * @param ctx
	 *            The context of the channelHandler
	 * @param chan
	 *            The channel through whom the message is forwarded.
	 * @param data
	 *            The NetworkMessage which will be converted into a JsonObject.
	 * @return The NetworkMessage converted in a JsonObject
	 * @throws Exception
	 *             if the Object data is not a NetworkMessage.
	 * @see org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext,
	 *      org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel chan, Object data) throws Exception
	{
		if (!(data instanceof NetworkMessage))
			throw new Exception("The data to sent must be a NetworkMessage object.");
		Gson gson = new Gson();
		return gson.toJson(data);
	}
}
