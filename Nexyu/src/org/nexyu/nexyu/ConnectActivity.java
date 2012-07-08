package org.nexyu.nexyu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ConnectActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_connect, menu);
        return true;
    }

    
}
