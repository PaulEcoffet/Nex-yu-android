/**
 * 
 */
package org.nexyu.nexyu.client;

import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.nexyu.nexyu.client.enums.IntegerHeaderJSONDecoderState;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Derived from the doc of Netty
 * 
 * @author Paul Ecoffet
 */

public class IntegerHeaderJSONDecoder extends
		ReplayingDecoder<IntegerHeaderJSONDecoderState>
{
	private int	length;

	public IntegerHeaderJSONDecoder()
	{
		// Set the initial state.
		super(IntegerHeaderJSONDecoderState.READ_LENGTH);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buf, IntegerHeaderJSONDecoderState state)
			throws Exception
	{
		
		
		switch (state)
		{
		case READ_LENGTH:
			length = buf.readInt();
			checkpoint(IntegerHeaderJSONDecoderState.READ_CONTENT);
		case READ_CONTENT:
			ChannelBuffer frame = buf.readBytes(length);
			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(frame.toString(CharsetUtil.UTF_8)).getAsJsonObject();
			checkpoint(IntegerHeaderJSONDecoderState.READ_LENGTH);
			return json;
		default:
			throw new Error("Shouldn't reach here.");
		}
	}
}
