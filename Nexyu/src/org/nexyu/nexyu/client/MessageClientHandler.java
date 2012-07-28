/**
 * 
 */
package org.nexyu.nexyu.client;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import android.util.Log;

import com.google.gson.JsonObject;

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
		JsonObject data = (JsonObject) e.getMessage();
		ChannelFuture f = ch.close();
		f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception
			{
				Log.d("NEX", "Disconnected");
			}
		});
		manageDataReceived(data);
	}

	/**
	 * @param data
	 */
	private void manageDataReceived(JsonObject data)
	{
		String type = data.get("type").getAsString();
		if(type.equals("pong"))
			Log.d("NEX", "Pong received");
		else
			Log.d("NEX", "Unknown type");
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		Log.d("NEX", "Connected");
		Channel ch = e.getChannel();
		String buf = "{\"type\":\"ping\"}";
		ChannelFuture future = ch.write(buf);
		future.addListener(new ChannelFutureListener() {

			@Override
			public void operationComplete(ChannelFuture arg0) throws Exception
			{
				Log.d("NEX", "Ping");
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
