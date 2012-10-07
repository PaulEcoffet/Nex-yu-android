/*
 * Copyright 2012 Nex yu Android authors
 * 
 * This file is part of Nex yu Android.
 * 
 * Nex yu Android is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Nex yu Android is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Nex yu Android. If not, see <http://www.gnu.org/licenses/>
 */
package org.nexyu.nexyuAndroid.ui;

import org.nexyu.nexyuAndroid.R;
import org.nexyu.nexyuAndroid.service.NexyuService;
import org.nexyu.nexyuAndroid.utils.IntentIntegrator;
import org.nexyu.nexyuAndroid.utils.IntentResult;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class MainActivity extends SherlockActivity implements View.OnClickListener
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
		Button connectButton = (Button) findViewById(R.id.ConnectBut);
		bindService(new Intent(this, NexyuService.class), mConnection, BIND_AUTO_CREATE);
		connectButton.setOnClickListener(this);
	}

	@Override
	public void onDestroy()
	{
		if (mBound)
			unbindService(mConnection);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, (com.actionbarsherlock.view.Menu) menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.ConnectBut)
		{
			if (mBound)
			{
				// TODO: Remove this not really pretty snippet.
				// Test if a device is an emulator by empirical testing.
				if (android.os.Build.PRODUCT.equals("sdk"))
				{
					// emulator
					sendConnectionMessage("10.0.2.2", 34340, "");
				}
				else
				{
					// not emulator
					IntentIntegrator integrator = new IntentIntegrator(this);
					integrator.initiateScan();
				}
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
		boolean validQr = false;
		if (result != null)
		{
			String contents = result.getContents();
			Uri path = Uri.parse(contents);
			if (path.getScheme().equals("nexyu"))
			{
				String host = path.getHost();
				int port = path.getPort();
				String verificationCode = path.getQueryParameter("verif");
				if ((host != null) && (port != -1) && (verificationCode != null))
				{
					sendConnectionMessage(host, port, verificationCode);
					validQr = true;
				}
			}
		}
		if (!validQr)
		{
			Toast.makeText(this, "Invalid QRCode", Toast.LENGTH_SHORT).show();
		}
	}

	private void sendConnectionMessage(String host, int port, String verificationCode)
	{
		Message connect = Message.obtain(null, NexyuService.What.MSG_CONNECT.ordinal());
		Bundle data = new Bundle();
		data.putString("ip", host);
		data.putInt("port", port);
		data.putString("verificationCode", verificationCode);
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
