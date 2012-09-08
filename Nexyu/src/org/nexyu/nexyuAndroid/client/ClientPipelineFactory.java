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
 * Pipeline factory that creates the pipeline used by netty to format the
 * messages to send and received over the network.
 * 
 * @author Paul Ecoffet
 * 
 */
public final class ClientPipelineFactory implements ChannelPipelineFactory
{
	private Messenger				mService;
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
	 * 
	 * 
	 * @author Paul Ecoffet
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
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
