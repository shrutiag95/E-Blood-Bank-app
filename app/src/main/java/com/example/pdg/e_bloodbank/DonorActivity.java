package com.example.pdg.e_bloodbank;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prakhar Dev Gupta on 28-06-2017.
 */
public class DonorActivity extends AppCompatActivity{
    ListView listView;
    List<String> banks;
    List<String> x;
    Bundle b;
    List<String> y; double bank_dis;
    Location location, location_bank;
    final String url = "http://192.168.43.206/shruti/bloodbanklist.php";
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.bankslist);
        b = getIntent().getExtras();




        location = new Location("");
        location.setLatitude(Double.parseDouble(String.valueOf(b.getDouble("x"))));
        location.setLongitude(Double.parseDouble(String.valueOf(b.getDouble("y"))));


        banks = new ArrayList<String>();
        x = new ArrayList<String>();
        y = new ArrayList<String>();
        listView  = (ListView) findViewById(R.id.banklistview);
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();

                        try {
                            JSONArray jsonArray = response.getJSONArray("banks");
                            for(int i =0 ; i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String b = jsonObject.getString("bank_name");
                                banks.add(b);
                                String x_coor = jsonObject.getString("latitude");
                                x.add(x_coor);
                                String y_coor = jsonObject.getString("longitude");
                                y.add(y_coor);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Volley Error: "+ error.toString(),Toast.LENGTH_SHORT).show();

                    }
                }


        );

        requestQueue.add(jsonObjectRequest);
        MyAdapter myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                String lat=x.get(position);
                String lon=y.get(position);
                i.setData(Uri.parse("geo:"+lat+","+lon));
                startActivity(i);
            }
        });



    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return banks.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view  = getLayoutInflater().inflate(R.layout.bank_item,null);
            TextView bank_name = (TextView) view.findViewById(R.id.bn);
            TextView dis = (TextView) view.findViewById(R.id.dis);

            bank_name.setText(banks.get(position));
//            String t;
//            t = x.get(position)+ "  " + y.get(position);
//            //dis.setText(t);
            location_bank = new Location("");
            location_bank.setLatitude(Double.parseDouble(x.get(position)));
            location_bank.setLongitude(Double.parseDouble(y.get(position)));
            bank_dis = location.distanceTo(location_bank);
            String s= new DecimalFormat("#.00").format(bank_dis/1000);

            dis.setText(s+" Km Away");



            return view;
        }
    }
}
