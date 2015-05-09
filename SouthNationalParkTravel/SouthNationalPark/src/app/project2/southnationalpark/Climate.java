package app.project2.southnationalpark;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import app.project2.southnationalpark.NationalPark.DownloadJSONFileAsync;

public class Climate extends Activity {
	public static final int DIALOG_DOWNLOAD_JSON_PROGRESS = 0;
	public static final int DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS = 1;
	 private ProgressDialog mProgressDialog;
	 
 	String strClimate = "";
 	String strParkname = "";
 	String strImage = "";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.climate);


		 if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }
		  showInfo();
		  

	}
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS:
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Downloading.....");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
            return mProgressDialog; 
        default:
            return null;
        }
    }

	private void showInfo() {
		// TODO Auto-generated method stub
		final TextView climate = (TextView)findViewById(R.id.txt_climate_data);
		final TextView parkname = (TextView)findViewById(R.id.txt_parkname);
		//Button Tab
		Button btn_home = (Button)findViewById(R.id.btn_home);
		Button btn_topography = (Button)findViewById(R.id.btn_topography);
		Button btn_climate = (Button)findViewById(R.id.btn_climate);
		Button btn_plant = (Button)findViewById(R.id.btn_plant);
		Button btn_place = (Button)findViewById(R.id.btn_place);
		ImageView img_climate_national = (ImageView)findViewById(R.id.img_climate_national);
    	
    	//String url = "http://10.0.3.2/SouthNationalParkTravel/getClimate.php";
    	//URL จาก Host
    	String url = "http://nationparktravel.ictte-project.com/southnationalparktravel/getClimate.php";
    	
    	Intent intent= getIntent();
    	final String id = intent.getStringExtra("pid"); 

		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("climateID", id));
        
        //รับค่า id เพื่อส่งต่อไปยัง หน้าอื่นๆ
        btn_home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Home = new Intent(Climate.this, MainMenu.class);
                Home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Home);
				finish();
            }
        });
        btn_topography.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent topography = new Intent(Climate.this, Topography.class);
                topography.putExtra("pid", id);
				startActivity(topography);

            }
        });
        btn_climate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent climate = new Intent(Climate.this, Climate.class);
                climate.putExtra("pid", id);
				startActivity(climate);

            }
        });
        btn_plant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent plant = new Intent(Climate.this, Plant.class);
                plant.putExtra("pid", id);
				startActivity(plant);

            }
        });
        btn_place.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent place = new Intent(Climate.this, PlaceNationalPark.class);
                place.putExtra("pid", id);
				startActivity(place);

            }
        });
        
        String resultServer  = getHttpPost(url,params);
	  	 

    	
    	JSONObject c;
		try {
			c = new JSONObject(resultServer);
			strClimate = c.getString("climate");
			strParkname  = c.getString("parkname");
			strImage = c.getString("image");
			
			if(!strClimate.equals(""))
			{
				climate.setText(strClimate);
				parkname.setText(strParkname);

				img_climate_national.setImageBitmap(loadBitmap("http://www.nationparktravel.ictte-project.com/images/"+strImage));
				img_climate_national.setOnClickListener(new OnClickListener() {
				    public void onClick(View v) {
				        // your code here
				    	new DownloadFullPhotoFileAsync().execute(strParkname,strImage);
				     }
				 });
				
			}
			else
			{
				climate.setText("-");
				parkname.setText("-");
				img_climate_national.setImageResource(R.drawable.ic_launcher);
			}
        	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
	// Show Dialog Popup 
    public void showDialogPopup(String strImageName,Bitmap ImageFullPhoto)
    {
    
        final AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        
        //ไฟล์ xml
    	  View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                  (ViewGroup) findViewById(R.id.layout_root));
          ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
          
         	 try
         	 {
         		image.setImageBitmap(ImageFullPhoto);
         	 } catch (Exception e) {
         		 // When Error
         		image.setImageResource(android.R.drawable.ic_menu_report_image);
         	 }
			
          imageDialog.setIcon(android.R.drawable.btn_star_big_on);	
  		  imageDialog.setTitle(strImageName);
          imageDialog.setView(layout);
          imageDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){

              public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
              }

          });

          imageDialog.create();
          imageDialog.show(); 
          
    }
 // Download Full Photo in Background
    public class DownloadFullPhotoFileAsync extends AsyncTask<String, Void, Void> {
    	
    	String strImageName = "";
    	String ImageFullPhoto = "";
    	
    	Bitmap ImageFullBitmap = null;

        protected void onPreExecute() {
        	super.onPreExecute();
        	showDialog(DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS);
        }

        @Override
        protected Void doInBackground(String... params) {
        	strImageName = params[0];
            ImageFullPhoto = params[1];

            ImageFullBitmap = (Bitmap)loadBitmap("http://www.nationparktravel.ictte-project.com/images/"+ImageFullPhoto);
    		return null;
        }

        protected void onPostExecute(Void unused) {
        	showDialogPopup(strImageName,ImageFullBitmap); // When Finish Show Popup
            dismissDialog(DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS);
            removeDialog(DIALOG_DOWNLOAD_FULL_PHOTO_PROGRESS);
        }
        

    }
	public String getHttpPost(String url,List<NameValuePair> params) {
		StringBuilder str = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { // Status OK
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} else {
				Log.e("Log", "Failed to download result..");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
		
	}
	/***** Get Image Resource from URL (Start) *****/
	private static final String TAG = "ERROR";
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	public static Bitmap loadBitmap(String url) {
	    Bitmap bitmap = null;
	    InputStream in = null;
	    BufferedOutputStream out = null;

	    try {
	    	
	        in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

	        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
	        out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
	        copy(in, out);
	        out.flush();

	        final byte[] data = dataStream.toByteArray();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        //options.inSampleSize = 1;

	        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
	    } catch (IOException e) {
	        Log.e(TAG, "Could not load Bitmap from: " + url);
	    } finally {
	        closeStream(in);
	        closeStream(out);
	    }

	    return bitmap;
	}

	 private static void closeStream(Closeable stream) {
	        if (stream != null) {
	            try {
	                stream.close();
	            } catch (IOException e) {
	                android.util.Log.e(TAG, "Could not close stream", e);
	            }
	        }
	    }
	 
	 private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
	 /***** Get Image Resource from URL (End) *****/
}
