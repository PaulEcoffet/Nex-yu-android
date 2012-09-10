package org.nexyu.nexyuAndroid;

import org.nexyu.nexyuAndroid.service.NexyuService;
import org.nexyu.nexyuAndroid.utils.IntentIntegrator;
import org.nexyu.nexyuAndroid.utils.IntentResult;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener
{
	private Messenger			mService	= null;
	private boolean				mBound		= false;
	private ServiceConnection	mConnection	= new ServiceConnection() {

												@Override
												public void onServiceConnected(ComponentName name,
														IBinder service)
												{
													mService = new Messenger(service);
													mBound = true;
												}

												@Override
												public void onServiceDisconnected(ComponentName name)
												{
													mService = null;
													mBound = false;
												}

											};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button startServButton = (Button) findViewById(R.id.startServBut);
		Button connectButton = (Button) findViewById(R.id.ConnectBut);
		startServButton.setOnClickListener(this);
		connectButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onDestroy()
	{
		if (mBound)
			unbindService(mConnection);
		super.onDestroy();
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		Log.d("NEX", "onclick triggered");
		if (v.getId() == R.id.startServBut)
		{
			Log.d("NEX", "start triggered");
			bindService(new Intent(this, NexyuService.class), mConnection, BIND_AUTO_CREATE);
		}
		else if (v.getId() == R.id.ConnectBut)
		{
			if (mBound)
			{
				IntentIntegrator integrator = new IntentIntegrator(this);
				integrator.initiateScan();
			}
			else
			{
				Toast.makeText(this, "Not bound to the service", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (result != null)
		{
			String contents = result.getContents();
			String urischeme = "nexyu";
			// ipregex found on
			// http://stackoverflow.com/questions/4011855/regexp-to-check-if-an-ip-is-valid
			String ipregex = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
			//portregex found on http://social.msdn.microsoft.com/Forums/sk/regexp/thread/bc7f5bc2-ac30-434b-9ae9-908d602ee6ea
			String portregex = "(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3})";
			String controlregex = "(\\d{5})";
			if (contents != null
					&& contents.matches(urischeme + "://" + ipregex + ":" + portregex + "\\?"
							+ controlregex))
			{
				String ip = contents.replaceAll(ipregex, "$1");
				int port = Integer.parseInt(contents.replaceAll(portregex, "$1"));
				int verificationCode = Integer.parseInt(contents.replaceAll(controlregex, "$1"));
				sendConnectionMessage(ip, port, verificationCode);
			}
		}
	}

	private void sendConnectionMessage(String ip, int port, int verificationCode)
	{
		Message connect = Message.obtain(null, NexyuService.MSG_CONNECT);
		Bundle data = new Bundle();
		data.putString("ip", ip);
		data.putInt("port", port);
		data.putInt("verificationCode", verificationCode);
		connect.setData(data);
		try
		{
			mService.send(connect);
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

}
