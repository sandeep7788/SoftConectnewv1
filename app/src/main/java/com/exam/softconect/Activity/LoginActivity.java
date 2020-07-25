package com.exam.softconect.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Telephony;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText edt_user_name, edt_password, edt_institute_id;
    TextInputLayout til_user_name, til_password, til_institute_id;

    String str_user_name, str_password, status, message, str_otp, otp, mobile_number, str_new_password, str_institute_id = "", institute_status = "";
    Button btn_login, btn_institute_login, btn_user_login;
    TextView txt_forgot_password;
    Button txt_create_new_account;
    Dialog dialog1;

    LoginVolleyService mVolleyService;

    TextInputLayout inputLayout, inputLayout_otp, inputLayout_password, textInputLayout_new_password;
    public TextInputEditText inputEditText, inputEditText_otp, inputEditText_password, textInputEditText_new_password;
    Button button, btn_otp_process, btn_otp_cancel, btn_password_cancel, btn_password_submit, btn_change_password_cancel, btn_change_password_process;
    AlertDialog alertDialog;
    boolean statuse = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Reference for Edit Text
        edt_user_name = findViewById(R.id.edt_user_name);
        edt_password = findViewById(R.id.edt_password);
        edt_institute_id = findViewById(R.id.edt_institute_id);
        til_user_name = findViewById(R.id.til_user_name);
        til_password = findViewById(R.id.til_password);
        til_institute_id = findViewById(R.id.til_institute_id);
        txt_forgot_password = findViewById(R.id.txt_forgot_password);
        txt_create_new_account = findViewById(R.id.txt_new_registration);
        btn_login = findViewById(R.id.btn_login);
        btn_institute_login = findViewById(R.id.btn_institute_login);
        btn_user_login = findViewById(R.id.btn_user_login);

        //Set Listner on Button
        btn_login.setOnClickListener(this);
        btn_institute_login.setOnClickListener(this);
        btn_user_login.setOnClickListener(this);
        txt_forgot_password.setOnClickListener(this);
        txt_create_new_account.setOnClickListener(this);

        mVolleyService = new LoginVolleyService(this);
        blinkTextView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_institute_login:

                institute_status = "true";

                til_institute_id.setVisibility(View.VISIBLE);
                btn_user_login.setVisibility(View.VISIBLE);
                btn_institute_login.setVisibility(View.GONE);

                break;

            case R.id.btn_user_login:

                institute_status = "";

                til_institute_id.setVisibility(View.GONE);
                btn_user_login.setVisibility(View.GONE);
                btn_institute_login.setVisibility(View.VISIBLE);

                break;

            case R.id.btn_login:

                //Get Data From EditText
                str_user_name = edt_user_name.getText().toString().trim();
                str_password = edt_password.getText().toString().trim();
                str_institute_id = edt_institute_id.getText().toString().trim();

                if (str_user_name.equalsIgnoreCase("")) {
                    til_user_name.setError("Please Enter Your User Name");
                    til_password.setErrorEnabled(false);
                    edt_user_name.requestFocus();
                } else if (str_password.equalsIgnoreCase("")) {
                    til_password.setError("Please Enter Your Password");
                    til_user_name.setErrorEnabled(false);
                    edt_password.requestFocus();
                } else {

                    if (institute_status.equalsIgnoreCase("true")) {

                        if (str_institute_id.equalsIgnoreCase("")) {
                            til_institute_id.setError("Please Enter Institute ID");
                            til_user_name.setErrorEnabled(false);
                            til_password.setErrorEnabled(false);
                            edt_institute_id.requestFocus();
                        } else {
                            if (isNetworkAvailable()) {
                                til_password.setErrorEnabled(false);
                                til_user_name.setErrorEnabled(false);
                                til_institute_id.setErrorEnabled(false);

                                //Send Data for Login
                                mVolleyService.postDataVolley(this, ServerUtils.Base_url + ServerUtils.Login_url, str_user_name, str_password, str_institute_id);

                            } else {
                                Snackbar.make(v, "You are now offline.", Snackbar.LENGTH_LONG).show();

                            }
                        }
                    } else {

                        if (isNetworkAvailable()) {
                            til_password.setErrorEnabled(false);
                            til_user_name.setErrorEnabled(false);

                            //Send Data for Login
                            mVolleyService.postDataVolley(this, ServerUtils.Base_url + ServerUtils.Login_url, str_user_name, str_password, str_institute_id);

                        } else {
                            Snackbar.make(v, "You are now offline.", Snackbar.LENGTH_LONG).show();

                        }
                    }


                }

                break;
            case R.id.txt_forgot_password:

                dialog1 = new Dialog(this);
                dialog1.setContentView(R.layout.dialog_forgot_password);
                dialog1.getWindow().setLayout(-1, -2);
                dialog1.setTitle("Forgot Password");
                final EditText edtMobile = (EditText) dialog1.findViewById(R.id.edtMobile);
                ((Button) dialog1.findViewById(R.id.btnSend)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String mobileNo = edtMobile.getText().toString();
                        if (mobileNo.equalsIgnoreCase("")) {
                            edtMobile.setError("Please enter your mobile number.");
                        } else {
                            if (isNetworkAvailable()) {

                                //call method
                                sendForPass(edtMobile.getText().toString());

                            } else {
                                Snackbar.make(v, "You are now offline.", Snackbar.LENGTH_LONG).show();
                            }

                        }
                    }
                });
                dialog1.show();
                break;
            case R.id.txt_new_registration:

                Intent intent = new Intent(this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void sendForPass(final String mobile_number) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.ForgotPassword_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {
                                dialog1.dismiss();

                                //Show OTP Dialog
                                OTP_Dialog(mobile_number);

                            } else {
                                message = jsonObject.getString("message");

                                CommonUtils.AwesomeErrorDialog(LoginActivity.this, message);
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
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);


    }

    private void OTP_Dialog(final String mobile_number) {
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

        //Use when get OTP using Income Message
        inputEditText_otp.setText(otp);

        btn_otp_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_otp = inputEditText_otp.getText().toString();
                if (str_otp.equals("")) {
                    inputLayout_otp.setError("Enter OTP");
                } else {

                    OTPVerify(mobile_number);
                    inputEditText_otp.setText("");
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

    public void OTPVerify(final String mobile_number) {

        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.forgot_otp_verify_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                String mobile = jsonObject.getString("mobile");

                                //Close OTP Dialog
                                alertDialog.dismiss();

                                //Call method
                                Change_pass_Dialog(mobile);

                            } else {

                                message = jsonObject.getString("message");
                                CommonUtils.AwesomeErrorDialog(LoginActivity.this, message);
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

    private void Change_pass_Dialog(final String mobile_number) {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.dialog_change_password, null);
        textInputLayout_new_password = confirmDialog.findViewById(R.id.textInputLayout_new_password);
        textInputEditText_new_password = confirmDialog.findViewById(R.id.textInputEditText_new_password);
        btn_change_password_process = confirmDialog.findViewById(R.id.btn_change_password_process);
        btn_change_password_cancel = confirmDialog.findViewById(R.id.btn_change_password_cancel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        alert.setCancelable(false);

        alertDialog = alert.create();
        alertDialog.show();

        btn_change_password_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_new_password = textInputEditText_new_password.getText().toString();
                if (str_new_password.equals("")) {
                    textInputLayout_new_password.setError("Enter New Password");
                } else {
                    textInputLayout_new_password.setErrorEnabled(false);
                    ChangeNewPassword(mobile_number, str_new_password);
                }
            }
        });

        btn_change_password_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
    }

    public void ChangeNewPassword(final String mobile_number, final String new_pass) {

        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.update_password_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {


                                //Close OTP Dialog
                                alertDialog.dismiss();

                                message = jsonObject.getString("message");
                                CommonUtils.AwesomeSuccessDialog(LoginActivity.this, message);

                            } else {

                                message = jsonObject.getString("message");
                                CommonUtils.AwesomeErrorDialog(LoginActivity.this, message);
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
                params.put("new_password", new_pass);


                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    //For Get Otp from Incoming Message
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    private BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                // You are trying to register on Softconet, Your OTP is : 4951
                if (messageBody != null) {
                    otp = messageBody.split(":")[1];
                    inputEditText_otp.setText(otp);
                }
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(smsBroadcastReceiver, filter);

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(smsBroadcastReceiver);

    }
    private void blinkTextView() {

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 500;
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Button txt = findViewById(R.id.txt_new_registration);
                       /* if(txt.getVisibility() == View.VISIBLE) {
                            txt.setVisibility(View.INVISIBLE);
                        } else {
                            txt.setVisibility(View.VISIBLE);
                        }*/
//                      txt.setBackgroundTintList();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                        ) {
                            if(true == statuse)
                            {
                                txt.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorPrimaryDark));
                                statuse =false;
                            }
                            else
                            {
                                //txt.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),Color.parseColor("#ffffff")));
                                txt.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9A082C")));
                                statuse =true;
                            }

                        }
                        blinkTextView();
                    }
                });
            }
        }).start();
    }
    public void news_data()
    {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(Request.Method.POST,"https://www.softconect.com/api/getMessage",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response


                            String status;
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                status = jsonObject.getString("status");

                                if (status.equalsIgnoreCase("1")) {

                                    byte[] data = Base64.decode(jsonObject.getString("news"), Base64.DEFAULT);
                                    String question = new String(data, StandardCharsets.UTF_8);
                                    question = question.replace("\\", "\\\\");

                                    TextView txt = findViewById(R.id.t_news);
                                    txt.setText(question.trim());
                                    txt.setSelected(true);


                                    //Save data for next time login


                                    //



                                } else if (status.equalsIgnoreCase("0")) {




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

                            Log.e("@@error",error.getMessage()+" ");

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                /*    params.put("user_id", CommonUtils.userID);
                    params.put("old_pass", strOldPass);
                    params.put("new_pass", strNewPass);*/

                    return params;
                }
            };

            queue.add(request);

        } catch (Exception e) {

        }
    }
}