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
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.project2.southnationalpark.MarineNationalPark.DownloadJSONFileAsync;
import app.project2.southnationalpark.MarineNationalPark.ImageAdapter;

public class LandNationalPark extends Activity {
	public static final int DIALOG_DOWNLOAD_JSON_PROGRESS = 5000;
    private ProgressDialog mProgressDialog;
    
    ArrayList<HashMap<String, String>> MyArrList;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_nationalpark);
		
		
		  // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
     // Download JSON File	
     		new DownloadJSONFileAsync().execute();
        
        // listView1
        //final ListView lstView_nationalpark = (ListView)findViewById(R.id.lv_nationalpark); 
        
        /** JSON from URL
         * [
         * {"ImageID":"1","ImageDesc":"My Sea View 1","ImagePath":"http://www.thaicreate.com/android/pic_a.png"},
         * {"ImageID":"2","ImageDesc":"My Sea View 2","ImagePath":"http://www.thaicreate.com/android/pic_b.png"},
         * {"ImageID":"3","ImageDesc":"My Sea View 3","ImagePath":"http://www.thaicreate.com/android/pic_c.png"},
         * {"ImageID":"4","ImageDesc":"My Sea View 4","ImagePath":"http://www.thaicreate.com/android/pic_d.png"}
         * ]
         */
        
        
        
      /* String url = "http://10.0.3.2/SouthNationalParkTravel/getProvince.php";
        
        try {
        	JSONArray data = new JSONArray(getJSONUrl(url));
			
	    	final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;
			
			for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
    			map = new HashMap<String, String>();
    			map.put("province_id",  c.getString("id"));
    			map.put("province_name", c.getString("province_name"));
    			map.put("province_image", c.getString("province_image"));
    			MyArrList.add(map);
			}
			
			lstView_nationalpark.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				@Override
		         public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
		                 long id) {
					String pid = "0";
					pid = MyArrList.get(position).get("province_id");
					Toast.makeText(NationalPark.this, " ID " +pid, Toast.LENGTH_SHORT).show();
					Intent in = new Intent(NationalPark.this, ProvinceNationalPark.class);
						in.putExtra("pid", pid);
						startActivity(in);
						
					}
		        
		 
		    });
			
			lstView_nationalpark.setAdapter(new ImageAdapter(this,MyArrList));
  
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
       
      
    }
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DIALOG_DOWNLOAD_JSON_PROGRESS:
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
	 
	// Show All Content
	    public void ShowAllContent()
	    {
	        // listView1
	    	final ListView lstView_nationalpark = (ListView)findViewById(R.id.lv_nationalpark);
	    	lstView_nationalpark.setAdapter(new ImageAdapter(LandNationalPark.this,MyArrList));
	    	lstView_nationalpark.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				@Override
		         public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
		                 long id) {
					String pid = "0";
					pid = MyArrList.get(position).get("id");
					//Toast.makeText(LandNationalPark.this, " ID " +pid, Toast.LENGTH_SHORT).show();
					Intent in = new Intent(LandNationalPark.this, Topography.class);
						in.putExtra("pid", pid);
						in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(in);
						finish();
						
					}
		        
		 
		    });
	    	mProgressDialog.dismiss();
	    }
    
    public class ImageAdapter extends BaseAdapter 
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list) 
        {
        	// TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }
 
        public int getCount() {
        	// TODO Auto-generated method stub
            return MyArr.size();
        }
 
        public Object getItem(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
 
        public long getItemId(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		 
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.landnationalpark, null); 
			}

			// ColImage
			ImageView imageView = (ImageView) convertView.findViewById(R.id.img_province_nationalpark);
			imageView.getLayoutParams().height = 400;
			imageView.getLayoutParams().width = 300;
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        	 try
        	 {
        		 //load image url from web site 
        		 //imageView.setImageBitmap(loadBitmap("http://www.nationparktravel.ictte-project.com/images/"+MyArr.get(position).get("image")));
        		 Picasso.with(context).load("http://www.nationparktravel.ictte-project.com/images/"+MyArr.get(position).get("image")).into(imageView);
        	 } catch (Exception e) {
        		 // When Error
        		 imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        	 }
				
			// ColPosition
			TextView txtParkname = (TextView) convertView.findViewById(R.id.txt_Name_nationalpark);
			txtParkname.setPadding(10, 0, 0, 0);
			txtParkname.setText("" + MyArr.get(position).get("park_name"));
			
			TextView txtProvince = (TextView) convertView.findViewById(R.id.txt_province_nationalpark);
			txtProvince.setPadding(10, 0, 0, 0);
			txtProvince.setText("จังหวัด : " + MyArr.get(position).get("province_name"));
					
			//Image Icon Next
			ImageView imageNext = (ImageView) convertView.findViewById(R.id.img_next);
			imageNext.setImageResource(R.drawable.icon_next);
		 
			return convertView;
				
		}

    } 
    // Download JSON in Background
    public class DownloadJSONFileAsync extends AsyncTask<String, Void, Void> {
    	
        @SuppressWarnings("deprecation")
		protected void onPreExecute() {
        	super.onPreExecute();
        	showDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub


        	//String url = "http://10.0.3.2/SouthNationalParkTravel/getLandNationalPark.php";
            //URL จาก Host
        	String url = "http://nationparktravel.ictte-project.com/southnationalparktravel/getLandNationalPark.php";
            try {
            	JSONArray data = new JSONArray(getJSONUrl(url));
    			
    	    	MyArrList = new ArrayList<HashMap<String, String>>();
    			HashMap<String, String> map;
    			
    			for(int i = 0; i < data.length(); i++){
                    JSONObject c = data.getJSONObject(i);
        			map = new HashMap<String, String>();
        			map.put("id",  c.getString("id"));
        			map.put("park_name", c.getString("park_name"));
        			map.put("province_name", c.getString("province_name"));
        			map.put("image", c.getString("image"));
        			MyArrList.add(map);
    			}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		return null;
        }

        @SuppressWarnings("deprecation")
		protected void onPostExecute(Void unused) {
        	ShowAllContent(); // When Finish Show Content
            dismissDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
            removeDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
        }
        

    }
    
    //*** Get JSON Code from URL ***//
	public String getJSONUrl(String url) {
		StringBuilder str = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { // Download OK
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} else {
				Log.e("Log", "Failed to download file..");
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
