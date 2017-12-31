package com.example.pdg.e_bloodbank;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//
///**
// * Created by Prakhar Dev Gupta on 12-06-2017.
// */
//
//public class Forgotpassword extends AppCompatActivity {
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        setContentView(R.layout.forgotpass);
//    }
//
//    public void ConfirmOTP(View view){
//        Intent i =  new Intent(this, ChangePassword.class);
//        startActivity(i);
//    }
//}


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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

/**
 * Created by Agarwal's on 6/11/2017.
 */
public class Forgotpassword extends AppCompatActivity {

    EditText otp;
    String url;
    Bundle bundle;
    String session_id,is_correct;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass);
        otp=(EditText)findViewById(R.id.otp);
        bundle=getIntent().getExtras();
        session_id=bundle.getString("session_id");
        username=bundle.getString("username");


    }

    public void ConfirmOTP(View v){
        url="http://192.168.43.206/shruti/otp_confirmation.php?otp="+otp.getText().toString()+"&session_id="+session_id;
        Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();

        JsonObjectRequest req=new JsonObjectRequest(Request.Method.POST,url,null,new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray array = response.getJSONArray("otp_info");
                    JSONObject obj = array.getJSONObject(0);
                    is_correct=obj.getString("Details").toString();
                    if(is_correct.equals("OTP Matched")){
                        Bundle b=new Bundle();
                        b.putString("username",username);
                        Intent i =new Intent(Forgotpassword.this,ChangePassword.class);
                        i.putExtras(b);
                        startActivity(i);

                    }
                    else
                        Toast.makeText(getApplicationContext(),is_correct, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
    }

}