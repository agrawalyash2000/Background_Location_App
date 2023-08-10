package com.example.bgapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.Manifest;

import android.app.NotificationChannel;

import android.app.NotificationManager;

import android.app.PendingIntent;

//import android.app.Service;

//import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Binder;

import android.os.Build;

//import android.os.IBinder;

import android.view.View;
import android.widget.Toast;



import androidx.core.app.NotificationCompat;

import androidx.core.content.ContextCompat;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationCallback;

import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationResult;

import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForegroundService extends Service {
    private final IBinder mBinder = new MyBinder();

    private static final String CHANNEL_ID = "2";



    @Override

    public IBinder onBind(Intent intent) {

        return mBinder;

    }



    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }



    @Override

    public void onCreate() {

        super.onCreate();

        buildNotification();

        requestLocationUpdates();

    }



    private void buildNotification() {

        String stop = "stop";

        PendingIntent broadcastIntent = PendingIntent.getBroadcast(

                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the persistent notification

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)

                .setContentTitle(getString(R.string.app_name))

                .setContentText("Location tracking is working")

                .setOngoing(true)

                .setContentIntent(broadcastIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name),

                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setShowBadge(false);

            channel.setDescription("Location tracking is working");

            channel.setSound(null, null);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel);

        }



        startForeground(1, builder.build());

    }

    private void requestLocationUpdates() {

        LocationRequest request = new LocationRequest();

        request.setInterval(1000);

        request.setFastestInterval(3000);

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this,

                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {

            // Request location updates and when an update is

            // received, store the location in Firebase

            client.requestLocationUpdates(request, new LocationCallback() {

                @Override

                public void onLocationResult(LocationResult locationResult) {



                    String location = "Latitude : " + locationResult.getLastLocation().getLatitude() +

                            "\nLongitude : " + locationResult.getLastLocation().getLongitude();


//                    postDataUsingVolley("Yash","Bekaar");
                    Toast.makeText(ForegroundService.this, location, Toast.LENGTH_SHORT).show();

                }

            }, null);

        } else {

            stopSelf();

        }

    }



//    private void postDataUsingVolley(String name, String job) {
//        // url to post our data
//        String url = "https://jsonplaceholder.typicode.com/posts";
//
//        // creating a new variable for our request queue
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//
//        // on below line we are calling a string
//        // request method to post the data to our API
//        // in this we are calling a post method.
//        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                // inside on response method we are
//                // hiding our progress bar
//                // and setting data to edit text as empty
//
//
//                // on below line we are displaying a success toast message.
//                Toast.makeText(ForegroundService.this, "Data added to API", Toast.LENGTH_SHORT).show();
//                try {
//                    // on below line we are parsing the response
//                    // to json object to extract data from it.
//                    JSONObject respObj = new JSONObject(response);
//
//                    // below are the strings which we
//                    // extract from our json object.
//                    String name = respObj.getString("title");
//                    String job = respObj.getString("body");
//
//                    // on below line we are setting this string s to our text view.
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // method to handle errors.
//                Toast.makeText(ForegroundService.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // below line we are creating a map for
//                // storing our values in key and value pair.
//                Map<String, String> params = new HashMap<String, String>();
//
//                // on below line we are passing our key
//                // and value pair to our parameters.
//                params.put("title", name);
//                params.put("body", job);
//                params.put("id", "1211");
//                params.put("userId", "a123");
//
//                // at last we are
//                // returning our params.
//                return params;
//            }
//        };
//        // below line is to make
//        // a json object request.
//        queue.add(request);
//    }

    public class MyBinder extends Binder {

        public ForegroundService getService() {

            return ForegroundService.this;

        }

    }

}