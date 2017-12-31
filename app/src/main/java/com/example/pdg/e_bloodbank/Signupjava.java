package com.example.pdg.e_bloodbank;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prakhar Dev Gupta on 13-06-2017.
 */

public class Signupjava extends AppCompatActivity {

    int date, month, yer; int sex_int=0 ; RelativeLayout relativeLayout;
    ImageView calender;  String sex;
    TextView dob; int i; BigInteger BIG_ph;
    Button validate,confirm;
    EditText fullname, aadhar_number, email_add, address_full, phone_number, username, pass1, pass2;
    String email,address, phone,uname, p1,p2;

    Spinner sp; final String url="http://192.168.43.206/shruti/signupScript.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup);

        calender = (ImageView) findViewById(R.id.calender);


        fullname = (EditText) findViewById(R.id.name);
        aadhar_number = (EditText) findViewById(R.id.aadhaarno);
        email_add = (EditText) findViewById(R.id.email);
        address_full = (EditText) findViewById(R.id.address);
        phone_number = (EditText) findViewById(R.id.phone);
        username = (EditText) findViewById(R.id.username_signup);
        pass1 = (EditText) findViewById(R.id.password_signup);
        pass2 = (EditText) findViewById(R.id.password_signup_confirm);



        dob = (TextView) findViewById(R.id.dob);
        sp = (Spinner) findViewById(R.id.spinner);
        relativeLayout = (RelativeLayout) findViewById(R.id.rela);
        //relativeLayout.setEnabled(false);

        for(i=0 ; i<relativeLayout.getChildCount();i++){
            View child =  relativeLayout.getChildAt(i);
            //Toast.makeText(getApplicationContext(),"Disabled",Toast.LENGTH_SHORT).show();
            child.setEnabled(false);
        }
        validate = (Button) findViewById(R.id.validate);
        confirm = (Button) findViewById(R.id.create);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Signupjava.this, android.R.layout.simple_expandable_list_item_1,
                getResources().getStringArray(R.array.spinner));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setPrompt("Select One");
        sp.setAdapter(adapter );
        sp.setSelected(false);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = parent.getItemAtPosition(position).toString();
                sex_int = position;
                //Toast.makeText(getApplicationContext(),sex,Toast.LENGTH_SHORT).show();
//                if(sex =="Female") sex_int = 1;
//                if(sex =="Male")sex_int =2;
//                if (sex =="Other") sex_int= 3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, aadhar_no;
                name = fullname.getText().toString();
                aadhar_no = aadhar_number.getText().toString();
//                Toast.makeText(getApplicationContext(),Integer.toString(name.length()) + "fuck "+ Integer.toString(aadhar_no.length())+
//                        " sfs "+ Integer.toString(sex_int), Toast.LENGTH_SHORT).show();

                if(name.length() > 0 && sex_int>0 && aadhar_no.length()==12 ){
                    for(i=0 ; i<relativeLayout.getChildCount();i++){
                        View child =  relativeLayout.getChildAt(i);
                        //Toast.makeText(getApplicationContext(),"Disabled",Toast.LENGTH_SHORT).show();
                        child.setEnabled(true);
                    }
                    Toast.makeText(getApplicationContext(),"Aadhar Validated! Please proceed",Toast.LENGTH_SHORT).show();


                }
                else {
                    Toast.makeText(getApplicationContext(),"Something is wrong in the credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = email_add.getText().toString();
                address = address_full.getText().toString();
                phone = phone_number.getText().toString();
                 //BIG_ph = new BigInteger(phone);
                uname = username.getText().toString();
                p1  = pass1.getText().toString();
                p2 = pass2.getText().toString();
//                Toast.makeText(getApplicationContext(),email+Integer.toString(email.length()),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),address+Integer.toString(address.length()),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),phone+" "+Integer.toString(phone.length()),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),uname+Integer.toString(uname.length()),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),p1 +Integer.toString(p1.length()),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),p2+Integer.toString(p2.length()),Toast.LENGTH_SHORT).show();
//                if(p1.equals(p2))
//                    Toast.makeText(getApplicationContext(),"TRUE",Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(),"P1 and P2 are not equal",Toast.LENGTH_SHORT).show();


                if(email.length() >0 && address.length() > 0 && phone.length() ==10 && uname.length()> 0 && p1.length()>0 && p1.equals(p2)){

                    StringRequest strq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("uname", uname);
                            params.put("pass", p1);
                            params.put("phone", phone);
                            params.put("email", email);
                            params.put("sex", Integer.toString(sex_int));
                            params.put("add", address);
                            params.put("name", fullname.getText().toString());
                            params.put("dob",dob.getText().toString());

                            return params;


                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(strq);



                }
                else {
                    Toast.makeText(getApplicationContext(),"Invalid credentials! Please Check again!",Toast.LENGTH_SHORT).show();
                }


            }
        });





//        dob.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(s.length() == 2 && count++ <2)
//                    s.append('-');
//
//            }
//        });
    }

    public void datepicker(View v) {
        final Calendar c = Calendar.getInstance();
        date = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        yer = c.get(Calendar.YEAR);


        DatePickerDialog dp=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                date=dayOfMonth;
//                month=monthOfYear+1;
//
//                yer=year;
//
//                StringBuilder sb = new StringBuilder();
//                if()
//
//
                dob.setText(set_date(year, monthOfYear+1,dayOfMonth));


            }

            private String set_date(int year, int month, int day) {
                String formattedDay = (String.valueOf(day));
                String formattedMonth = (String.valueOf(month));

                if (day < 10) {
                    formattedDay = "0" + day;
                }

                if (month < 10) {
                    formattedMonth = "0" + month;
                }
                String formatted_year = String.valueOf(year);
                return formatted_year+"-"+formattedMonth+"-"+formattedDay;

            }
        },month,date,yer);
        dp.show();

    }
}
