/**
 * 
 */
package org.nexyu.nexyu.client;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.string.StringDecoder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Derived from the doc of Netty
 * 
 * @author Paul Ecoffet
 */

public class JSONDecoder extends StringDecoder
{
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			Object buf) throws Exception
	{
		String str = (String) buf;
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(str).getAsJsonObject();
		return json;
	}
}
