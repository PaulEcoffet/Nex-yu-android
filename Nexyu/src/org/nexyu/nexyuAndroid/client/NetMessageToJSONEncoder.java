/**
 * 
 */
package org.nexyu.nexyuAndroid.client;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.nexyu.nexyuAndroid.client.protocol.NetworkMessage;

import com.google.gson.Gson;

/**
 * @author Paul Ecoffet
 *
 */
public class NetMessageToJSONEncoder extends OneToOneEncoder
{
	/**
	 * @see org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel chan, Object data) throws Exception
	{
		if(!(data instanceof NetworkMessage))
			throw new Exception("The data to sent must be a NetworkMessage object.");
		Gson gson = new Gson();
		return gson.toJson(data);
	}
}