package app.project2.southnationalpark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class Splashscreen extends Activity {

	Handler handler;
	Runnable runnable;
	long delay_time;
	long time = 3000L;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splashscreen);

		handler = new Handler();

		runnable = new Runnable() {
			public void run() {
				
				Intent intent = new Intent(Splashscreen.this, MainMenu.class);
				startActivity(intent);
				finish();
			}
		};
	}

	public void onResume() {
		super.onResume();
		delay_time = time;
		handler.postDelayed(runnable, delay_time);
		time = System.currentTimeMillis();
	}

	public void onPause() {
		super.onPause();
		handler.removeCallbacks(runnable);
		time = delay_time - (System.currentTimeMillis() - time);
	}

	
}
