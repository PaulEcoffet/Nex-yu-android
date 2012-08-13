package org.nexyu.nexyu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener
{

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
			Intent serv = new Intent(this, ConnectService.class);
			startService(serv);
		}
		else if (v.getId() == R.id.ConnectBut)
		{
			Intent serv = new Intent(this, ConnectService.class);
			serv.putExtra("action", "connect");
			serv.putExtra("ip", "10.0.2.2");
			startService(serv);
		}
	}
}
