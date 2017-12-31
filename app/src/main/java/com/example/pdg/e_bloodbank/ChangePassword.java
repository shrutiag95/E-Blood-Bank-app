package com.example.pdg.e_bloodbank;

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
//public class ChangePassword extends AppCompatActivity {
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        setContentView(R.layout.reset_pass);
//    }
//
//    public void Confirm(View v){
//
//    }
//}

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Agarwal's on 6/25/2017.
 */
public class ChangePassword extends AppCompatActivity {
    Button b;
    EditText pass,con_pass;
    ProgressDialog pDialog;
    Bundle bundle;
    String username;
    String url="http://192.168.43.206/shruti/change_password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_pass);
        b=(Button)findViewById(R.id.change);
        pass=(EditText)findViewById(R.id.newpass);
        con_pass=(EditText)findViewById(R.id.conpass);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Updating your password...");
        bundle=getIntent().getExtras();
        username=bundle.getString("username");


    }

    public void change_password(View v){

        if(pass.getText().toString().equals(con_pass.getText().toString())){
            pDialog.show();

            StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.hide();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.hide();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params= new HashMap<String, String>();
                    params.put("username",username);
                    params.put("new_password",pass.getText().toString());
                    return params;
                }

            };
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(strq);

        }



        else
            Toast.makeText(getApplicationContext(),"confirm your new password again", Toast.LENGTH_SHORT).show();

    }

}
