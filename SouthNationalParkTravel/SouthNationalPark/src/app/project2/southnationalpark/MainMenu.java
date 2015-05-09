package app.project2.southnationalpark;

import javax.security.auth.Destroyable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import app.project2.southnationalpark.feed.FeedMain;


public class MainMenu extends Activity implements OnClickListener{
	private ShareActionProvider shareProvider;

	Uri imgURI;
	final int LOAD_IMAGE_COMPLETE = 100;
	Button btnNational,btnMap,btnNews,btnActivity,btnFacebook;
	protected void onCreate(Bundle savedInstanceState) {
		if (isInternetConnect()) {
			//Toast.makeText(MainMenu.this, " Connect Internet Success ", Toast.LENGTH_SHORT).show();
		} else {
			showAlertNoNet();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnNational = (Button)findViewById(R.id.ImageButton01);
		btnMap = (Button)findViewById(R.id.ImageButton02);
		btnNews = (Button)findViewById(R.id.ImageButton03);
		btnActivity = (Button)findViewById(R.id.ImageButton04);
		btnFacebook = (Button)findViewById(R.id.btn_fb);
		
		btnNational.setOnClickListener(this);
		btnMap.setOnClickListener(this);
		btnNews.setOnClickListener(this);
		btnFacebook.setOnClickListener(this);
		btnActivity.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ImageButton01:
			Log.d("Information","Information");
			Intent InformationIntent = new Intent(this, Category.class);
			startActivity(InformationIntent);
			break;
			
		case R.id.ImageButton02:
			Log.d("Map","Map");
			Intent MapIntent = new Intent(this, MainActivity.class);
			startActivity(MapIntent);
			break;
			
		case R.id.ImageButton03:
			Log.d("Feed","Feed");
			Intent FeedIntent = new Intent(this, app.project2.southnationalpark.feed.FeedMain.class);
			startActivity(FeedIntent);
			break;
			
		case R.id.btn_fb:
			Log.d("Facebook","Facebook");
			//Intent FacebookIntent = new Intent(this, FacebookActivity.class);
			//startActivity(FacebookIntent);
			Intent i = new Intent(Intent.ACTION_PICK,
					   Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, LOAD_IMAGE_COMPLETE);
			break;
			
		case R.id.ImageButton04:
			Log.d("Activity","Activity");
			Intent ActivityIntent = new Intent(this, AdviceNational.class);
			startActivity(ActivityIntent);
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((resultCode == RESULT_OK) && (requestCode == LOAD_IMAGE_COMPLETE)) {
			imgURI = data.getData();	
			
			if (shareProvider != null) {
				shareProvider.setShareIntent(ShareIntent());
			}
		}
		
	}

	private Intent ShareIntent() {
		Intent i = new Intent(Intent.ACTION_SEND);
		if (imgURI != null) {
			i.putExtra(Intent.EXTRA_STREAM, imgURI);
			i.setType("image/png");
		}
		return i;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.share, menu);
		MenuItem item = menu.findItem(R.id.menu_item_share);

		shareProvider = (ShareActionProvider) item.getActionProvider();
		shareProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		return true;
		
	}
	@Override
    public void onDestroy() {
        super.onDestroy();
    }
	public void onBackPressed() {
	      AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	        dialog.setTitle("Exit");
	        dialog.setIcon(R.drawable.logosmall);
	        dialog.setCancelable(true);
	        dialog.setMessage("Do you want to exit?");

	        dialog.setNegativeButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int id) {
							// TODO Auto-generated method stub
							finish();

						}
					});
	        dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.cancel();
	            }
	        });
	        
	        dialog.show();               
    }
	private boolean isInternetConnect() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isConnected()) {
			return true;
		} else {
			return false;
		}

	}
	
	private void showAlertNoNet() {
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
		alertDlg.setMessage("App นี้ต้องเชื่อม Internet")
				.setTitle("แจ้งการใช้งาน")
				.setIcon(R.drawable.logosmall)
				.setCancelable(false)
				.setPositiveButton("เข้าตั้งค่าเปิด internet",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int id) {
								// TODO Auto-generated method stub
								Intent gpsOptionsIntent = new Intent(
										android.provider.Settings.ACTION_SETTINGS);
								startActivityForResult(gpsOptionsIntent, 0);
							}
						});
		alertDlg.setNegativeButton("ปิดApp",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		AlertDialog alert = alertDlg.create();
		alert.show();
	}
}
