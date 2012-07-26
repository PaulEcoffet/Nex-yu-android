/**
 * 
 */
package org.nexyu.nexyu.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

import static org.jboss.netty.buffer.ChannelBuffers.*;

/**
 * @author Paul Ecoffet
 * 
 */
public class MessageClientHandler extends SimpleChannelHandler
{

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	{
		Channel ch = e.getChannel();
		ChannelBuffer buf = dynamicBuffer();
		
		
		ch.close();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		Channel ch = e.getChannel();
		ChannelBuffer buf = dynamicBuffer();
		ByteArrayOutputStream message = new ByteArrayOutputStream();
		String data = "{\"type\":\"message\"}";
		try
		{
			message.write(data.getBytes().length);
			message.write(data.getBytes());
		}
		catch (IOException err)
		{
			err.printStackTrace();
		}
		buf.writeBytes(message.toByteArray());

		ChannelFuture future = ch.write(buf);
		future.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture arg0) throws Exception
			{
				System.out.println("JSON sent");
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		e.getCause().printStackTrace();
		Channel ch = e.getChannel();
		ch.close();
	}
}
