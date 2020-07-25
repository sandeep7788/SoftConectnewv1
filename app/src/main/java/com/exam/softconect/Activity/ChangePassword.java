package com.exam.softconect.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    Button btnChangePass;
    String confirmPass, strNewPass, strOldPass;
    Context context;
    TextInputEditText edtConfirmPass, edtNewPass, edtOldPass;
    TextInputLayout newPassLayout, oldpassLayout, confirmpassLayout;
    ImageView img_back;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Get Reference for EditText
        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtRepeatNewPass);

        //Get Reference for ImageView
        img_back = findViewById(R.id.img_back);

        //Get Reference for Button
        btnChangePass = (Button) findViewById(R.id.btnChangePass);

        //Get Reference for TextInputLayout
        oldpassLayout = (TextInputLayout) findViewById(R.id.oldPassLayout);
        newPassLayout = (TextInputLayout) findViewById(R.id.newPassLayout);
        confirmpassLayout = (TextInputLayout) findViewById(R.id.confirmPassLayout);

        //Set Listener
        img_back.setOnClickListener(this);
        btnChangePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btnChangePass:

                strOldPass = edtOldPass.getText().toString().trim();
                strNewPass = edtNewPass.getText().toString().trim();
                confirmPass = edtConfirmPass.getText().toString().trim();
                if (strOldPass.equals("")) {
                    if (newPassLayout.isErrorEnabled()) {
                        newPassLayout.setErrorEnabled(false);
                    }
                    if (confirmpassLayout.isErrorEnabled()) {
                        confirmpassLayout.setErrorEnabled(false);
                    }
                    oldpassLayout.setError("Enter old password");
                    oldpassLayout.requestFocus();
                } else if (strNewPass.equals("")) {
                    if (oldpassLayout.isErrorEnabled()) {
                        oldpassLayout.setErrorEnabled(false);
                    }
                    if (confirmpassLayout.isErrorEnabled()) {
                        confirmpassLayout.setErrorEnabled(false);
                    }
                    newPassLayout.setError("Enter new password");
                    edtNewPass.requestFocus();
                } else if (strNewPass.equals(confirmPass)) {
                    if (oldpassLayout.isErrorEnabled()) {
                        oldpassLayout.setErrorEnabled(false);
                    }
                    if (confirmpassLayout.isErrorEnabled()) {
                        confirmpassLayout.setErrorEnabled(false);
                    }
                    if (newPassLayout.isErrorEnabled()) {
                        newPassLayout.setErrorEnabled(false);
                    }

                    //call method
                    change_Password(strOldPass, strNewPass);


                } else {
                    if (oldpassLayout.isErrorEnabled()) {
                        oldpassLayout.setErrorEnabled(false);
                    }
                    if (newPassLayout.isErrorEnabled()) {
                        newPassLayout.setErrorEnabled(false);
                    }
                    confirmpassLayout.setError("Password doesn't match.");
                }
                break;
        }
    }

    public void change_Password(final String strOldPass, final String strNewPass) {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.changePassword_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressDialog.dismiss();
                            String status, message, str_userName, str_password;
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                status = jsonObject.getString("status");

                                if (status.equalsIgnoreCase("1")) {

                                    message = jsonObject.getString("message");
                                    str_userName = jsonObject.getString("mobile");
                                    str_password = jsonObject.getString("password");

                                    //Save data for next time login
                                    editor = getSharedPreferences("LogDIN", 0).edit();
                                    editor.putBoolean("Is_Login", true);
                                    editor.putString("N", str_userName);
                                    editor.putString("P", str_password);
                                    editor.commit();

                                    //Show Success Dialog
                                    CommonUtils.AwesomeSuccessDialog(ChangePassword.this, message);

                                    edtOldPass.setText("");
                                    edtNewPass.setText("");
                                    edtConfirmPass.setText("");
                                    edtOldPass.requestFocus();

                                } else if (status.equalsIgnoreCase("0")) {

                                    message = jsonObject.getString("message");

                                    //Show Error Dialog
                                    CommonUtils.AwesomeErrorDialog(ChangePassword.this, message);

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

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", CommonUtils.userID);
                    params.put("old_pass", strOldPass);
                    params.put("new_pass", strNewPass);

                    return params;
                }
            };

            queue.add(request);

        } catch (Exception e) {

        }
    }
}
