/**
 *
 */
package org.nexyu.nexyuAndroid.client;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

import android.os.IBinder;
import android.os.Messenger;

/**
 * @author Paul Ecoffet
 * 
 */
public final class ClientPipeline implements ChannelPipelineFactory
{
	private Messenger				mService;
	private MessageClientHandler	messageClientHandler;

	public ClientPipeline(IBinder service)
	{
		mService = new Messenger(service);
		messageClientHandler = new MessageClientHandler(mService);
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		LengthFieldBasedFrameDecoder lengthDecod = new LengthFieldBasedFrameDecoder(
				Integer.MAX_VALUE, 0, Integer.SIZE / Byte.SIZE, 0, Integer.SIZE / Byte.SIZE);
		StringDecoder stringDecod = new StringDecoder(CharsetUtil.UTF_8);
		JSONDecoder jsonDecoder = new JSONDecoder();
		LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4);
		StringEncoder stringEncoder = new StringEncoder(CharsetUtil.UTF_8);
		return Channels.pipeline(lengthDecod, stringDecod, jsonDecoder, messageClientHandler,
				lengthFieldPrepender, stringEncoder);
	}

	public MessageClientHandler getMessageClientHandler()
	{
		return messageClientHandler;
	}
}
