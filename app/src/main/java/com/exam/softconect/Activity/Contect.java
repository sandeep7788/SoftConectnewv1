package com.exam.softconect.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class Contect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect);


        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        progressDialog.show();

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(Request.Method.POST,"https://www.softconect.com/api/getContact",
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

                                    TextView t_number=findViewById(R.id.t_number);
                                    TextView t_email=findViewById(R.id.t_email);
                                    message = jsonObject.getString("contact");
                                    str_userName = jsonObject.getString("email");
                                    t_number.setText(message);
                                    t_email.setText(str_userName);


                                    //Save data for next time login


                                    //



                                } else if (status.equalsIgnoreCase("0")) {

                                    message = jsonObject.getString("message");


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