package com.example.greenplanet;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LocactionService extends Service {

	private LocationManager locationManager;
	private Boolean locationChanged;
	String url = "", url1 = "";
	SharedPreferences sp;
	private Handler handler = new Handler();
	public static Location curLocation;
	public static boolean isService = true;
	public static String lati = "", logi = "", place = "";
	ListView l1;
	String venue, lastUpdatedVenue = "";
	String vehiclenum;
	int NOTIFICATION_ID = 234;
	SQLiteDatabase db;
	String datemsg="",temdate="";

	NotificationManager notificationManager;

	LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			if (curLocation == null) {
				curLocation = location;
				locationChanged = true;
			} else if (curLocation.getLatitude() == location.getLatitude() && curLocation.getLongitude() == location.getLongitude()) {
				locationChanged = false;
				return;
			} else
				locationChanged = true;
			curLocation = location;

			if (locationChanged)
				locationManager.removeUpdates(locationListener);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			if (status == 0)// UnAvailable
			{
			} else if (status == 1)// Trying to Connect
			{
			} else if (status == 2) {// Available
			}
		}
	};


	@Override
	public void onCreate() {
		super.onCreate();
		notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		curLocation = getBestLocation();
		sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());



		db=openOrCreateDatabase("med", Context.MODE_PRIVATE, null);
		db.execSQL("Create table if not exists medice(id integer primary key autoincrement,mid text,med text,t1 text,t2 text,t3 text,t4 text,presc text)");


		if (curLocation == null) {
			System.out.println("starting problem.........3...");
			Toast.makeText(this, "GPS problem..........", Toast.LENGTH_SHORT).show();
		} else {
			// Log.d("ssssssssssss", String.valueOf("latitude2.........."+curLocation.getLatitude()));
		}
		isService = true;
	}

	final String TAG = "LocationService";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override

	public void onLowMemory() {
		super.onLowMemory();

	}


	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "Start services", Toast.LENGTH_SHORT).show();

		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (!provider.contains("gps")) { //if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}
		handler.post(GpsFinder);
	}

	@Override
	public void onDestroy() {
		String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (provider.contains("gps")) { //if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			sendBroadcast(poke);
		}

		handler.removeCallbacks(GpsFinder);
		handler = null;
		Toast.makeText(this, "Service Stopped..!!", Toast.LENGTH_SHORT).show();
		isService = false;
	}

	public Runnable GpsFinder = new Runnable() {
		public void run() {

			Location tempLoc = getBestLocation();

			if (tempLoc != null) {
				curLocation = tempLoc;

				lati = String.valueOf(curLocation.getLatitude());
				logi = String.valueOf(curLocation.getLongitude());

				getnotify();

				RequestQueue queue = Volley.newRequestQueue(LocactionService.this);
				String url = "http://" + sp.getString("ip", "") + ":5000/update_loc";
//                    Toast.makeText(Login.this, ""+url, Toast.LENGTH_SHORT).show();
				// Request a string response from the provided URL.
				StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Display the response string.
						Log.d("+++++++++++++++++", response);
						try {
							JSONObject json = new JSONObject(response);
//                                Toast.makeText(Login.this, ""+json, Toast.LENGTH_SHORT).show();
							String res = json.getString("result");
							if (res.equalsIgnoreCase("success")) {
								Toast.makeText(LocactionService.this, "success", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(LocactionService.this, "failed", Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(LocactionService.this, "" + e, Toast.LENGTH_SHORT).show();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

//						pDialog.hide();
						Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
					}
				}) {
					@Override
					protected Map<String, String> getParams() {
						{
							Map<String, String> params = new HashMap<String, String>();
							params.put("Vehicle_id", sp.getString("lid", ""));
							params.put("Latitude", LocactionService.lati);
							params.put("Longitude", LocactionService.logi);


							return params;
						}
					}
				};
				queue.add(stringRequest);


//				String loc = "";
//				String address = "";
//				Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
//				try {
//					List<Address> addresses = geoCoder.getFromLocation(curLocation.getLatitude(), curLocation.getLongitude(), 1);
//					if (addresses.size() > 0) {
//						for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
//							address += addresses.get(0).getAddressLine(index) + " ";
//						//Log.d("get loc...", address);
//						place = addresses.get(0).getLocality().toString();
//						Toast.makeText(getApplicationContext(), lati + logi + place, Toast.LENGTH_SHORT).show();
//					} else {
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				Toast.makeText(getApplicationContext(), lati + "---"+logi , Toast.LENGTH_SHORT).show();
			}
			handler.postDelayed(GpsFinder, 30000);// register again to start after 35 seconds...
		}
	};

	private  void getnotify() {




		RequestQueue queue = Volley.newRequestQueue(LocactionService.this);
		url = "http://" + sp.getString("ip", "") + ":5000/check ";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// Display the response string.
				Log.d("+++++++++++++++++", response);
				try {
					JSONObject json = new JSONObject(response);
					String rslt = json.getString("task");

					if (rslt.equalsIgnoreCase("invalid")) {
//                                     Toast.makeText(getApplicationContext(), rslt, Toast.LENGTH_LONG).show();
					} else {

						String[] temp=rslt.split("\\@");
						if(temp.length>0){


							for (int i = 0; i < temp.length; i++) {
								String[] res=temp[i].split("\\#");
								datemsg=res[8];
//								Toast.makeText(getApplicationContext(), "ress", Toast.LENGTH_LONG).show();
								db.execSQL("insert into medice(mid,med,t1,t2,t3,t4,presc) values ('"+res[0]+"','"+res[1]+"','"+res[3]+"','"+res[4]+"','"+res[5]+"','"+res[6]+"','"+res[7]+"')");

//							notificationCheck();
							}
							Editor ed=sp.edit();
							ed.putString("enddate", datemsg);
							ed.commit();
						}








					}




				}


				catch (JSONException e) {
					e.printStackTrace();
				}


			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {


				Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String,String> params = new HashMap<String, String>();

				params.put("l_id", sp.getString("lid", ""));


				return params;
			}
		};
//		queue.add(stringRequest);




//		RequestQueue queue = Volley.newRequestQueue(LocactionService.this);
//		url = "http://" + sp.getString("ip", "") + ":5000/checkparking";
//
//		// Request a string response from the provided URL.
//		StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				// Display the response string.
//				Log.d("+++++++++++++++++", response);
//				try {
//
//
//
//
//
//					JSONObject json = new JSONObject(response);
//					vehiclenum = json.getString("task");
//
//					if (vehiclenum.equalsIgnoreCase("error")) {
//						//Toast.makeText(LocactionService.this, "not slot................", Toast.LENGTH_SHORT).show();
//
//
//
//					} else {
//
//
//
//
//						Editor edp = sp.edit();
//						edp.putString("vehiclenum", vehiclenum);
//						edp.commit();
//
//						notificationCheck();
//					}
//				} catch (Exception e) {
//					Log.d("=========", e.toString());
//				}
//
//			}
//		}, new Response.ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//
//
//				Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
//			}
//		}) {
//			@Override
//			protected Map<String, String> getParams() {
//				Map<String,String> params = new HashMap<String, String>();
//
//				//params.put("slot", sp.getString("slot", ""));
//
//
//				return params;
//			}
//		};
//		queue.add(stringRequest);
//


	}

	private void notificationCheck() {
		// TODO Auto-generated method stub


		try {
			Calendar mcurrentTime = Calendar.getInstance();
			int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
			int minute = mcurrentTime.get(Calendar.MINUTE);
			String time=hour+":"+minute;
//			Toast.makeText(getApplicationContext(), "checkk"+time, Toast.LENGTH_SHORT).show();
			Cursor cr=db.rawQuery("select * from medice where t1 ='"+time+"' or t2 ='"+time+"' or t3 ='"+time+"' or t4 ='"+time+"'", null);
			String msg="time for your medicine";

			notification_popup(msg);

			if(cr.getCount()>0){
				cr.moveToFirst();
				Log.d("checkk",cr.getString(3));
				 msg="Have "+cr.getString(2)+" \n on   "+time+" "+cr.getString(6);
				db.delete("medice", "id=?", new String[]{cr.getString(0)});
				notification_popup(msg);
			}
			else
			{
				Log.d("checkk","no");
			}

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

//		try {
//
//
//			//		String msg="Have "+medname+" \n on   "+time+" ";
//
//			notification_popup();
//		}
//
//
//		catch (Exception e) {
//			// TODO: handle exception
//			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
//		}

	}


	public void notification_popup(String msg) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String CHANNEL_ID = "my_channel_01";
			CharSequence name = "my_channel";
			String Description = "This is my channel";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
			mChannel.setDescription(Description);
			mChannel.enableLights(true);
			mChannel.setLightColor(Color.RED);
			mChannel.enableVibration(true);
			mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//       mChannel.setVibrationPattern(new long[]{0, 800, 200, 1200, 300, 2000, 400, 4000});
			mChannel.setShowBadge(false);
			notificationManager.createNotificationChannel(mChannel);
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "my_channel_01")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("New Notification For Medicine")
				.setContentText(msg).setSmallIcon(R.drawable.ic_launcher_background);
    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
    stackBuilder.addParentStack(MainActivity.class);
    stackBuilder.addNextIntent(resultIntent);
    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}


	private Location getBestLocation() {
		Location gpslocation = null;
		Location networkLocation = null;
		if (locationManager == null) {
			locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		}
		try {
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
				//
					//	return TODO;
				}
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
				gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				//  System.out.println("starting problem.......7.11....");

			}
			if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000, 0, locationListener);
				networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		} catch (IllegalArgumentException e) {
			Log.e("error", e.toString());
		}
		if(gpslocation==null && networkLocation==null)
			return null;

		if(gpslocation!=null && networkLocation!=null){
			if(gpslocation.getTime() < networkLocation.getTime()){
				gpslocation = null;
				return networkLocation;
			}else{
				networkLocation = null;
				return gpslocation;
			}
		}
		if (gpslocation == null) {
			return networkLocation;
		}
		if (networkLocation == null) {
			return gpslocation;
		}
		return null;
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
