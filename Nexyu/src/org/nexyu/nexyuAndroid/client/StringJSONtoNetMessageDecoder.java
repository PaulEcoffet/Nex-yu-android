/**
 *
 */
package org.nexyu.nexyuAndroid.client;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import org.nexyu.nexyuAndroid.client.protocol.NetworkMessage;

import android.util.Log;

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
	 * {@link JsonObject}. The object buf must be with a {@link StringJSONtoNetMessageDecoder} or a similar
	 * decoder before it is launch.
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
		Log.d("NEX", str);
		NetworkMessage message = null;
		Log.d("NEX", "interesting");
		try
		{
			message = gson.fromJson(str, NetworkMessage.class);
			Log.d("NEX", "LOL");
		}
		catch(JsonSyntaxException e)
		{
			Log.e("NEX", e.getMessage());
			e.printStackTrace();
		}
		Log.d("NEX", message.getType());
		return message;
	}
}
