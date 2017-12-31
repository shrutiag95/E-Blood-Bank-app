package com.example.pdg.e_bloodbank;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.expandableListViewStyle;
import static android.R.attr.x;
import static android.R.attr.y;
import static com.google.android.gms.location.LocationServices.*;

/**
 * Created by Prakhar Dev Gupta on 27-06-2017.
 */


public class Welcome extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = DonorActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;


    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Gps updates intervals in sec
  private static int UPDATE_INTERVAL =60 ;
   private static int FATEST_INTERVAL =60;
   private static int DISPLACEMENT = 0;
    Location location;
    TextView wel,credit;
    String last_donated_date,recieved,displayname,displaycredit,ss;


    final String url="http://192.168.43.36/php/test_user_single.php";
    final String date_url = "http://192.168.43.36/php/date.php";
    //RequestQueue requestQueue ;
    Bundle b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.welcomescreen);

      //  b=new Bundle();
        location=new Location("");
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();

        }

        wel = (TextView) findViewById(R.id.welcomemsg);
        credit = (TextView) findViewById(R.id.credits );
        b = getIntent().getExtras();
        ss = b.getString("bundle_username");

       // wel.setText("Welcome "+b.getString("bundle_username"));                     ////////////////handle this

        StringRequest str = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                recieved  = response;
                try {

                    JSONArray jsonArray = new JSONArray(recieved);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                        displayname = jsonObject.getString("name");
                        displaycredit = jsonObject.getString("credits");
                        last_donated_date = jsonObject.getString("last_donated");
                        wel.setText("Welcome "+displayname);
                        credit.setText("My Credits: "+displaycredit);
                    //Toast.makeText(getApplicationContext(),displayname + displaycredit,Toast.LENGTH_LONG).show();



                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

            }
        }


        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("upost", b.getString("bundle_username"));
                //params.put("password", p);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(str);










    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }



    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

                //Toast.makeText(getApplicationContext(), String.valueOf(mLastLocation), Toast.LENGTH_LONG).show();
                double x = mLastLocation.getLatitude();
                double y = mLastLocation.getLongitude();
                b.putDouble("x", x);
                b.putDouble("y",y);
            }

         else {

            //Snackbar
            //.setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }

    /**
     * Method to toggle periodic location updates
     * */

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

    }

    /**
     * Stopping location updates
     */

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }





    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

//        Toast.makeText(getApplicationContext(), "Location changed!",
//                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }






    public void donor(View view){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, date_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response,Toast.LENGTH_LONG).show();
                if(response.equals("Eligible")){
                    try{
                        Intent i  = new Intent(Welcome.this,DonorActivity.class);
                        i.putExtras(b);
                        startActivity(i);}
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
                else {

                    AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT> Build.VERSION_CODES.FROYO){
            builder = new AlertDialog.Builder(Welcome.this);
            builder.setTitle("Attention Donor").setMessage("You donated the blood on "+last_donated_date+" which is less than 3 months ago." +
                    "You should not donate for now").setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try{
                        Intent i  = new Intent(Welcome.this,DonorActivity.class);
                        i.putExtras(b);
                        startActivity(i);}
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.show();



        }
        else
            Toast.makeText(getApplicationContext(),"Error Cant show AlertDialog",Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", ss);
                //params.put("password", p);
                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);






//        AlertDialog.Builder builder;
//        if (Build.VERSION.SDK_INT> Build.VERSION_CODES.FROYO){
//            builder = new AlertDialog.Builder(Welcome.this);
//            builder.setTitle("Attention Donor").setMessage("You donated the blood on "+last_donated_date+" which is less than 3 months ago." +
//                    "You should not donate for now").setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.show();
//
//
//
//        }
//        else
//            Toast.makeText(getApplicationContext(),"Error Cant show AlertDialog",Toast.LENGTH_LONG).show();



    }
}
