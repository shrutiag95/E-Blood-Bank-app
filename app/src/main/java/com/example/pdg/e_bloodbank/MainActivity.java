package com.example.pdg.e_bloodbank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



import static com.example.pdg.e_bloodbank.R.layout.forgotpass;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    final String url="http://192.168.43.206/shruti/loginScript.php";
    Button login;
    EditText username, password; Bundle b;
    String u,p; Animation st;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        imageView = (ImageView) findViewById(R.id.imageView);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
         st = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_animation);
        imageView.startAnimation(st);
        login = (Button) findViewById(R.id.btn_login);
        b = new Bundle();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = username.getText().toString();
                p = password.getText().toString();
                if (p.equals("") || u.equals(""))
                    Toast.makeText(getApplicationContext(), "Credentials Required!", Toast.LENGTH_SHORT).show();
                else{
                    StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            repo(response);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", u);
                            params.put("password", p);
                            return params;
                        }

                    };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(strq);
            }


            }
        });




    }

    public void repo(String response){
        if(response.equals("Login successful")){
          //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT ).show();
            Intent i  = new Intent(MainActivity.this,Welcome.class);
            Bundle b = new Bundle();
            b.putString("bundle_username",u);
            i.putExtras(b);

            startActivity(i);
        }
        else {
            Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_SHORT).show();
        }

    }
    public void Forgot(final View view){

        ///view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        //username.getText().toString();
        final String user;
        user=username.getText().toString();
        String url="http://192.168.43.36/php/otp.php?username="+user;

        JsonObjectRequest req=new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {
              //  pDialog.hide();
                try {
                    // Toast.makeText(getApplicationContext(), "Type your otp", Toast.LENGTH_SHORT).show();
                    JSONArray array = response.getJSONArray("otp_info");
                    JSONObject obj = array.getJSONObject(0);
                    String session_id = obj.getString("Details").toString();
                    Intent i=new Intent(MainActivity.this,Forgotpassword.class);
                    b.putString("session_id",session_id);
                    b.putString("username",user);
                    i.putExtras(b);
                    startActivity(i);


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pDialog.hide();
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                view.setBackgroundColor(Color.parseColor("#ffffcc"));
//            }
//        }, 100);


//        Intent i = new Intent(this,Forgotpassword.class);
//        startActivity(i);
    }

    public void signup(View v){
        Intent i  = new Intent(MainActivity.this, Signupjava.class);
        startActivity(i);
    }

//    public void login(View v){
//        u = username.getText().toString();
//        p = password.getText().toString();
//
//
//    }
}

