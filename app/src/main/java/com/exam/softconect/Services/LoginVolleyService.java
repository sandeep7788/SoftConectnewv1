package com.exam.softconect.Services;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.exam.softconect.Activity.DeshBoardActivity;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginVolleyService {

    Context mContext;
    Dialog dialog;

    public LoginVolleyService(Context context) {
        mContext = context;
    }


    public void postDataVolley(final Activity activity, String url, final String userName, final String password, final String instituteID) {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressDialog.dismiss();
                            String status, message;
                            SharedPreferences.Editor editor;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                status = jsonObject.getString("status");

                                if (status.equalsIgnoreCase("1")) {

                                    CommonUtils.userID = jsonObject.getString("userID");
                                    CommonUtils.name_common = jsonObject.getString("name");
                                    CommonUtils.email_common = jsonObject.getString("email");
                                    CommonUtils.mobile_common = jsonObject.getString("mobile");
                                    CommonUtils.roleID = jsonObject.getString("roleID");

                                    //Save data for next time login
                                    editor = mContext.getSharedPreferences("LogDIN", 0).edit();
                                    editor.putBoolean("Is_Login", true);
                                    editor.putString("N", userName);
                                    editor.putString("P", password);
                                    editor.putString("institute_id", instituteID);
                                    editor.commit();

                                    Intent intent = new Intent(mContext, DeshBoardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    mContext.startActivity(intent);

                                } else if (status.equalsIgnoreCase("0")) {

                                    message = jsonObject.getString("message");

                                    //Show Error Dialog
                                    CommonUtils.AwesomeErrorLoginDialog(activity, message);
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
                    params.put("username", userName);
                    params.put("password", password);
                    params.put("instituteID", instituteID);

                    return params;
                }
            };

            queue.add(request);

        } catch (Exception e) {

        }
    }

   /* public void getDataVolley(final String requestType, String url) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            });

            queue.add(jsonObj);

        } catch (Exception e) {

        }
    }*/

    public void sendPaymentResponse(final Activity activity, final String userID, final String response, final String package_id) {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.purchaseAuth_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            progressDialog.dismiss();
                            String status, message;

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                status = jsonObject.getString("status");

                                if (status.equalsIgnoreCase("1")) {

                                    CommonUtils.packageID = "";

                                    dialog = new AwesomeSuccessDialog(activity)
                                            .setTitle("Success")
                                            .setMessage("Payment Success")
                                            .setCancelable(false)
                                            .setDoneButtonText("Ok")
                                            .setDoneButtonClick(new Closure() {
                                                @Override
                                                public void exec() {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();

                                } else if (status.equalsIgnoreCase("0")) {

                                    message = jsonObject.getString("message");

                                    //Show Error Dialog
                                    CommonUtils.AwesomeErrorDialog(activity, message);
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
                    params.put("user_id", userID);
                    params.put("response", response);
                    params.put("package_id", package_id);

                    return params;
                }
            };

            queue.add(request);

        } catch (Exception e) {

        }
    }
}
