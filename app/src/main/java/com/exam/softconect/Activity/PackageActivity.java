package com.exam.softconect.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Adapter.PackageAdapter;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.exam.softconect.Services.LoginVolleyService;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class PackageActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    RecyclerView recyclearview;
    PackageAdapter adapter;
    List<TestPanelHelper> testList;
    LoginVolleyService mVolleyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        //get references
        img_back = findViewById(R.id.img_back);
        recyclearview = (RecyclerView) findViewById(R.id.recyclearview_package);
        recyclearview.setHasFixedSize(true);
        recyclearview.setLayoutManager(new LinearLayoutManager(this));

        testList = new ArrayList<>();

        adapter = new PackageAdapter(this, testList, this);
        recyclearview.setAdapter(adapter);

        //call method
        loadRecyclerViewData();

        //set listner
        img_back.setOnClickListener(this);

        mVolleyService = new LoginVolleyService(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    public void loadRecyclerViewData() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.packageList_url + "/" + CommonUtils.userID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("package_detail");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    TestPanelHelper testList_ = new TestPanelHelper();

                                    testList_.setPackageID(jsonObject.getString("packageID"));
                                    testList_.setPackage_name(jsonObject.getString("package_name"));
                                    testList_.setTotal_amount(jsonObject.getString("total_amount"));
                                    testList_.setDiscount_amount(jsonObject.getString("discount_price"));
                                    testList_.setTotal_time(jsonObject.getString("total_time"));

                                    testList.add(testList_);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                }
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        queue.add(req);
    }

    public void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {

                mVolleyService.sendPaymentResponse(PackageActivity.this, CommonUtils.userID, response, CommonUtils.packageID);

            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

}
