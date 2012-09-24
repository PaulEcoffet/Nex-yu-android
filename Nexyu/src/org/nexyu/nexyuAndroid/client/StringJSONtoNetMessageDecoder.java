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
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.nexyu.nexyuAndroid.client.protocol.NetworkMessage;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * Decoder that convert a String into a JsonObject using gson
 * 
 * @see org.jboss.netty.handler.codec.oneone.OneToOneDecoder
 * @author Paul Ecoffet
 */

public class StringJSONtoNetMessageDecoder extends OneToOneDecoder
{

	/**
	 * Decode the Object buf (which will be cast into a String) into a
	 * {@link JsonObject}. The object buf must be with a
	 * {@link StringJSONtoNetMessageDecoder} or a similar decoder before it is
	 * launch.
	 * 
	 * @param ctx
	 *            The context of the ChannelHandler
	 * @param channel
	 *            The channel
	 * @param buf
	 *            The String received from the StringDecoder received before
	 * @return A JSON object generated through gson from the string received
	 * @throws Exception
	 *             When the object received by JSONDecoder.decode is not a
	 *             String
	 * 
	 * 
	 * @see org.jboss.netty.handler.codec.string.StringDecoder#decode(org.jboss.netty.channel.ChannelHandlerContext,
	 *      org.jboss.netty.channel.Channel, java.lang.Object)
	 * @see com.google.gson.JsonObject
	 * @author Paul Ecoffet
	 */
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, Object buf)
			throws Exception
	{
		if (!(buf instanceof String))
			throw new Exception("JSONDecoder.decode must receive a String");
		String str = (String) buf;
		Gson gson = new Gson();
		NetworkMessage message = null;
		try
		{
			message = gson.fromJson(str, NetworkMessage.class);
		}
		catch (JsonSyntaxException e)
		{
			e.printStackTrace();
		}
		return message;
	}
}
