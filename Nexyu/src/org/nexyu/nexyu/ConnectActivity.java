package org.nexyu.nexyu;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;
import org.nexyu.nexyu.client.JSONDecoder;
import org.nexyu.nexyu.client.MessageClientHandler;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class ConnectActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnected())
			connect();
		else
			Toast.makeText(ConnectActivity.this, "The device is not connected to the Internet",
					Toast.LENGTH_LONG).show();
	}

	/**
	 * 
	 */
	private void connect()
	{
		ChannelFactory factory = new OioClientSocketChannelFactory(Executors.newCachedThreadPool());
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception
			{
				LengthFieldBasedFrameDecoder lengthDecod = new LengthFieldBasedFrameDecoder(
						Integer.MAX_VALUE, 0, Integer.SIZE / Byte.SIZE, 0, Integer.SIZE / Byte.SIZE);
				StringDecoder stringDecod = new StringDecoder(CharsetUtil.UTF_8);
				JSONDecoder jsonDecoder = new JSONDecoder();
				MessageClientHandler messageClientHandler = new MessageClientHandler();
				LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(4);
				StringEncoder stringEncoder = new StringEncoder(CharsetUtil.UTF_8);
				return Channels.pipeline(lengthDecod, stringDecod, jsonDecoder,
						messageClientHandler, lengthFieldPrepender, stringEncoder);
			}
		});

		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		ChannelFuture fuConn = bootstrap.connect(new InetSocketAddress("10.0.2.2", 4242));
		fuConn.awaitUninterruptibly();
		if (!fuConn.isSuccess())
		{
			Log.d("NEX", "Not a success");
			fuConn.getCause().printStackTrace();
		}
		fuConn.getChannel().getCloseFuture().awaitUninterruptibly();
		factory.releaseExternalResources();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_connect, menu);
		return true;
	}

}
