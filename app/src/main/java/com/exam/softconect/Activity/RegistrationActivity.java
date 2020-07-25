package com.exam.softconect.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.R;
import com.exam.softconect.Services.LoginVolleyService;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText edt_name, edt_father_name, edt_email, edt_mobile_number, edt_password, edt_sponse_id,edt_txt_city,edt_txt_distict;
    TextInputLayout til_name, til_email, til_mobile_number, til_password, til_sponse_id,txt_city;
    String str_name, str_email, str_mobile, str_password, str_otp, otp,str_cityname;
    TextView txt_login;
    Button btn_register_now;
    String status, message, mobile_number, sponsor_id;

    TextInputLayout inputLayout, inputLayout_otp, inputLayout_password;
    public TextInputEditText inputEditText, inputEditText_otp, inputEditText_password;
    Button button, btn_otp_process, btn_otp_cancel, btn_password_cancel, btn_password_submit;
    AlertDialog alertDialog;

    LoginVolleyService mVolleyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Get Reference for EditText
        edt_name = findViewById(R.id.edt_name);
        edt_father_name = findViewById(R.id.edt_father_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile_number = findViewById(R.id.edt_mobile_number);
        edt_password = findViewById(R.id.edt_password);
        edt_sponse_id = findViewById(R.id.edt_sponse_id);
        til_name = findViewById(R.id.til_name);
//        til_father_name = findViewById(R.id.til_father_name);
        til_email = findViewById(R.id.til_email);
        til_mobile_number = findViewById(R.id.til_mobile_number);
        til_password = findViewById(R.id.til_password);
        til_sponse_id = findViewById(R.id.til_sponse_id);
        txt_login = findViewById(R.id.txt_login_now_registration);
        txt_city=findViewById(R.id.txt_city);
        edt_txt_city=findViewById(R.id.edt_txt_city);
//        txt_distict=findViewById(R.id.txt_distict);
//        edt_txt_distict=findViewById(R.id.edt_txt_distict);
        btn_register_now = findViewById(R.id.btn_register_now);

        //Set Listner
        btn_register_now.setOnClickListener(this);
        txt_login.setOnClickListener(this);

        mVolleyService = new LoginVolleyService(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_login_now_registration:

                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            case R.id.btn_register_now:

                str_name = edt_name.getText().toString().trim();
//                str_father_name = edt_father_name.getText().toString().trim();
                str_email = edt_email.getText().toString().trim();
                str_mobile = edt_mobile_number.getText().toString().trim();
                str_password = edt_password.getText().toString().trim();
                sponsor_id = edt_sponse_id.getText().toString().trim();
                str_cityname = edt_txt_city.getText().toString().trim();
//                str_districtname = edt_txt_distict.getText().toString().trim();

                if (str_name.equalsIgnoreCase("")) {
                    til_name.setError("Please Enter Your Name");
//                    til_father_name.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_mobile_number.setErrorEnabled(false);
                    til_password.setErrorEnabled(false);
                    edt_name.requestFocus();
                } /*else if (str_father_name.equalsIgnoreCase("")) {
                    til_father_name.setError("Please Enter Your Father Name");
                    edt_father_name.requestFocus();
                    til_name.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_mobile_number.setErrorEnabled(false);
                    til_password.setErrorEnabled(false);
                }*/ else if (str_email.equalsIgnoreCase("")) {
                    til_email.setError("Please Enter Your Email");
                    edt_email.requestFocus();
                    til_name.setErrorEnabled(false);
//                    til_father_name.setErrorEnabled(false);
                    til_mobile_number.setErrorEnabled(false);
                    til_password.setErrorEnabled(false);
                } else if (str_mobile.equalsIgnoreCase("")) {
                    til_mobile_number.setError("Please Enter Your Mobile Number");
                    edt_mobile_number.requestFocus();
                    til_name.setErrorEnabled(false);
//                    til_father_name.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_password.setErrorEnabled(false);
                } else if (str_password.equalsIgnoreCase("")) {
                    til_password.setError("Please Enter Your Password");
                    edt_password.requestFocus();
                    til_name.setErrorEnabled(false);
//                    til_father_name.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_mobile_number.setErrorEnabled(false);
                } else if (str_cityname.equalsIgnoreCase("")) {
                    txt_city.setError("Please Enter Your City");
                    edt_txt_city.requestFocus();
                    til_name.setErrorEnabled(false);
//                    til_father_name.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_mobile_number.setErrorEnabled(false);
                }/*else if (str_districtname.equalsIgnoreCase("")) {
                    txt_distict.setError("Please Enter Your Distict");
                    edt_txt_distict.requestFocus();
                    til_name.setErrorEnabled(false);
                    til_father_name.setErrorEnabled(false);
                    til_email.setErrorEnabled(false);
                    til_mobile_number.setErrorEnabled(false);
                }*/ else {
                    if (isNetworkAvailable()) {

                        //Set all eroor is Enabled
                        til_name.setErrorEnabled(false);
//                        til_father_name.setErrorEnabled(false);*/
                        til_email.setErrorEnabled(false);
                        til_mobile_number.setErrorEnabled(false);
                        til_password.setErrorEnabled(false);





                        //Start Progress dialog
                        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
                        progressDialog.setContentView(R.layout.custom_loader);
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        //Send data on Server
                        StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.Register_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();
                                        Log.e("@@registration",response.toString());
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

                                            status = jsonObject.getString("status");

                                            if (status.equalsIgnoreCase("1")) {
                                                mobile_number = jsonObject.getString("mobile");

                                                //Show OTP Dialog
                                                OTP_Dialog();


                                            } else {

                                                message = jsonObject.getString("message");
                                                CommonUtils.AwesomeErrorDialog(RegistrationActivity.this, message);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                        progressDialog.dismiss();
                                        Log.d("Error.Response", error.getMessage());
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("name", str_name);
//                                params.put("fname", str_father_name);
                                params.put("email", str_email);
                                params.put("mobile", str_mobile);
                                params.put("password", str_password);
                                params.put("sponser_id", sponsor_id);
                                params.put("city", str_cityname);
//                                params.put("district", str_districtname);

                                return params;
                            }
                        };

                        RequestQueue queue = Volley.newRequestQueue(this);
                        queue.add(request);

                    } else {
                        Snackbar.make(v, "You are now offline.", Snackbar.LENGTH_LONG).show();

                    }

                }

                break;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void OTP_Dialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.otp_dialog_layout, null);
        inputLayout_otp = confirmDialog.findViewById(R.id.textInputLayout_otp);
        inputEditText_otp = confirmDialog.findViewById(R.id.textInputEditText_otp);
        btn_otp_process = confirmDialog.findViewById(R.id.btn_otp_process);
        btn_otp_cancel = confirmDialog.findViewById(R.id.btn_otp_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alert.setCancelable(false);

        alertDialog = alert.create();
        alertDialog.show();

        btn_otp_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_otp = inputEditText_otp.getText().toString();
                if (str_otp.equals("")) {
                    inputLayout_otp.setError("Enter OTP");
                } else {
                    OTPVerify();
                }
            }
        });

        btn_otp_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

    }

    public void OTPVerify() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.otp_verify_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                String mobile = jsonObject.getString("mobile");
                                String pass = jsonObject.getString("password");

                                //Close OTP Dialog
                                alertDialog.dismiss();

                                //Send Data for Login
                                mVolleyService.postDataVolley(RegistrationActivity.this, ServerUtils.Base_url + ServerUtils.Login_url, mobile, pass, "");


                            } else {

                                message = jsonObject.getString("message");
                                CommonUtils.AwesomeErrorDialog(RegistrationActivity.this, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progressDialog.dismiss();
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile_number);
                params.put("otp_code", str_otp);


                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}