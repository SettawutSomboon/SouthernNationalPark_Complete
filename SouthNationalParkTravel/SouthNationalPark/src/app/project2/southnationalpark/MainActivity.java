package app.project2.southnationalpark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements LocationListener {

	final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map;
	private String park_name, latitude, longitude, category_id, id,
			image, address;

	private GoogleMap mMap;
	double lat, lng;
	private Marker mMarker;
	private ViewGroup infoWindows;
	private TextView infoTitle;
	private TextView infoSnippet;
	private ImageView imageNationalpark;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private MapWrapperLayout mapWrapperLayout;

	LocationManager lm;
	LocationListener locationListener;
	Context mContext;
	LatLng startPosition = new LatLng(13.095397, 100.964078);
	LatLng endPosition = new LatLng(13.171791, 100.926245);

	ImageLoader imageLoader;
	DisplayImageOptions options;

	ArrayList<Bitmap> bmpList = new ArrayList<Bitmap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        mMap = mapFragment.getMap();
        
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20)); 
        
        infoWindows = (ViewGroup) getLayoutInflater().inflate(R.layout.info_window_main, null);
        
        infoTitle = (TextView) infoWindows.findViewById(R.id.title);
        infoSnippet = (TextView) infoWindows.findViewById(R.id.snippet);
        imageNationalpark = (ImageView) infoWindows.findViewById(R.id.img_nationalpark);
        infoButton = (Button) infoWindows.findViewById(R.id.bt_detail);
        
        infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
        		null,
        		null) 
        {
        	@Override
            protected void onClickConfirmed(View v, Marker marker) {
        		if(marker != null)
        			//Toast.makeText(getApplicationContext(), marker.getSnippet(), Toast.LENGTH_LONG).show();

        		for (int i = 0; i < MyArrList.size(); i++) {
    				if (MyArrList.get(i).get("park_name")
    						.equalsIgnoreCase(marker.getTitle())) {

       					Intent detailActivity = new Intent(getApplicationContext(),Topography.class);
    					detailActivity.putExtra("pid",  MyArrList.get(i).get("id"));
    					startActivity(detailActivity);

    					break;
    				}
    			}
 

            }
        };
        
        infoButton.setOnTouchListener(infoButtonListener);
        
        mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
            	infoTitle.setText(marker.getTitle());
    			infoSnippet.setText(marker.getSnippet());
    			infoButtonListener.setMarker(marker);
    			
    			for (int i = 0; i < MyArrList.size(); i++) {
    				if (MyArrList.get(i).get("park_name")
    						.equalsIgnoreCase(marker.getTitle())) {

    					imageNationalpark.setImageBitmap(bmpList.get(i));

    					break;
    				}
    			}
    			mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindows);
    			return infoWindows;
            }
        });

		// UNIVERSAL IMAGE LOADER SETUP
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(false)
				.imageScaleType(ImageScaleType.EXACTLY)
				.displayer(new FadeInBitmapDisplayer(300)).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new WeakMemoryCache()).build();

		ImageLoader.getInstance().init(config);
		// END - UNIVERSAL IMAGE LOADER SETUP
		options = new DisplayImageOptions.Builder().cacheInMemory(false)
				.resetViewBeforeLoading(false)
				.showImageForEmptyUri(0)
				.showImageOnFail(0)
				.showImageOnLoading(0)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

	}

	LocationListener listener = new LocationListener() {
		public void onLocationChanged(Location loc) {
			LatLng coordinate = new LatLng(loc.getLatitude(),
					loc.getLongitude());
			lat = loc.getLatitude();
			lng = loc.getLongitude();

			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setCompassEnabled(true);
			mMap.getUiSettings().setRotateGesturesEnabled(true);

			mMap.animateCamera(CameraUpdateFactory
					.newLatLngZoom(coordinate, 16));
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	public void onResume() {
		super.onResume();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		boolean isNetwork = lm
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (isNetwork) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
					10, listener);
			Location loc = lm
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				lat = loc.getLatitude();
				lng = loc.getLongitude();
			}
		}
		if (isGPS) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
					listener);
			Location loc = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (loc != null) {
				lat = loc.getLatitude();
				lng = loc.getLongitude();
			}
		}
	}

	public void onPause() {
		super.onPause();
		lm.removeUpdates(listener);
	}

	private void showMarker() {
		// TODO Auto-generated method stub
		new LoadContentFromServer().execute();
	}

	class LoadContentFromServer extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {

			 //String url="http://10.0.3.2/SouthNationalParkTravel/getMapMarker.php";
			String url = "http://nationparktravel.ictte-project.com/southnationalparktravel/getMapMarker.php";

			return getJSONUrl(url);
		}

		public String getJSONUrl(String url) {
			StringBuilder str = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			try {
				HttpResponse response = client.execute(httpGet);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					HttpEntity entity = response.getEntity();
					InputStream content = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(content));
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

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONArray data = new JSONArray(result);

				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);

					final int mCounter = i;
					
					map = new HashMap<String, String>();
					map.put("id", c.getString("id"));
					map.put("park_name", c.getString("park_name"));
					map.put("latitude", c.getString("latitude"));
					map.put("longtitude", c.getString("longtitude"));
					map.put("category_id", c.getString("category_id"));
					map.put("address", c.getString("address"));
					map.put("image", c.getString("image"));
					MyArrList.add(map);

					Log.d("imgLink","http://www.nationparktravel.ictte-project.com/images/"+c.getString("image"));

					bmpList.add(null);
					
					ImageLoader.getInstance().loadImage(
							"http://www.nationparktravel.ictte-project.com/images/"
									+ MyArrList.get(i).get("image")
							, options
							, new SimpleImageLoadingListener() {
								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									bmpList.set(mCounter, loadedImage);
								}
							});

					id = map.get("id");
					park_name = map.get("park_name");
					latitude = map.get("latitude");
					longitude = map.get("longtitude");
					category_id = map.get("category_id");
					image = map.get("image");
					address = map.get("address");
					lat = Double.parseDouble(latitude);
					lng = Double.parseDouble(longitude);

					try {
						if (category_id.equalsIgnoreCase("1")) {
							mMap.addMarker(new MarkerOptions()
									.position(new LatLng(lat, lng))
									.title(park_name)
									.snippet("ที่ตั้ง : " + address)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

						}
						if (category_id.equalsIgnoreCase("2")) {
							mMap.addMarker(new MarkerOptions()
									.position(new LatLng(lat, lng))
									.title(park_name)
									.snippet("ที่ตั้ง : " + address)
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

						}
						// mMap.setInfoWindowAdapter(new
						// CustomInfoWindowAdapter());
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nationalpark_menu:
			CameraPosition cameraK = new CameraPosition.Builder()
					.target(startPosition).zoom(10).bearing(0).tilt(0).build();
			mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraK));
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPosition,
					6));
			showMarker();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * private void addMarker() { mMap.addMarker(new MarkerOptions()
	 * .position(KhaoPhanomBenchaNationalPark)
	 * .title(getString(R.string.KhaoPhanomBenchaNationalPark_title))
	 * .snippet(getString(R.string.KhaoPhanomBenchaNationalPark_snippet)) ); }
	 */

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());
		Log.e("GPS", "location changed: lat=" + lat + ", lon=" + lon);
		// tv.setText("lat="+lat+", lon="+lon);
		LatLng coordinates = new LatLng(location.getLatitude(),
				location.getLongitude());
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12));

		// Update Value of Lat Long
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	/*
	 * class CustomInfoWindowAdapter implements InfoWindowAdapter { private
	 * final View infoWindow; CustomInfoWindowAdapter() { infoWindow =
	 * getLayoutInflater().inflate(R.layout.info_window_main, null); }
	 * 
	 * @Override public View getInfoWindow(Marker marker) {
	 * 
	 * String title = marker.getTitle(); String snippet = marker.getSnippet();
	 * Log.e("t",title); if(title!=null){ for (int i = 0; i < MyArrList.size();
	 * i++) { if (MyArrList.get(i).get("park_name").equalsIgnoreCase(title)) {
	 * TextView textViewTitle = (TextView) infoWindow.findViewById(R.id.title);
	 * textViewTitle.setText(title);
	 * 
	 * TextView textViewSnippet = (TextView)
	 * infoWindow.findViewById(R.id.snippet); textViewSnippet.setText(snippet);
	 * 
	 * Log.e("test","http://www.nationparktravel.ictte-project.com/images/"+
	 * MyArrList.get(i).get("image")); ImageView img_nationalpark = (ImageView)
	 * infoWindow.findViewById(R.id.img_nationalpark);
	 * Picasso.with(infoWindow.getContext
	 * ()).load("http://www.nationparktravel.ictte-project.com/images/"
	 * +MyArrList.get(i).get("image")).into(img_nationalpark); } } return
	 * infoWindow; } else{ return null; }
	 * 
	 * }
	 * 
	 * @Override public View getInfoContents(Marker marker) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * 
	 * }
	 */

	public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

	private void getMemory(){
    	final Runtime runtime = Runtime.getRuntime();
    	final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
    	final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
    	Log.d("check_memory","usedMemInMB="+usedMemInMB);
    	Log.d("check_memory","maxHeapSizeInMB="+maxHeapSizeInMB);
    }

}