package com.exam.softconect.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Adapter.TestPanelAdapter;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TestPanelActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    RecyclerView recyclearview;
    TestPanelAdapter adapter;
    List<TestPanelHelper> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_panel);

        //get references
        img_back = findViewById(R.id.img_back);
        recyclearview = (RecyclerView) findViewById(R.id.recyclearview_test_list);
        recyclearview.setHasFixedSize(true);
        recyclearview.setLayoutManager(new LinearLayoutManager(this));

        testList = new ArrayList<>();

        adapter = new TestPanelAdapter(this, testList,this);
        recyclearview.setAdapter(adapter);

        //call method
        loadRecyclerViewData();

        //set listner
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        testList.clear(); //clear list
        adapter.notifyDataSetChanged();
        //call method
        loadRecyclerViewData();
    }



    public void loadRecyclerViewData() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.test_list_url + CommonUtils.userID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("@@testpanel",response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("testDetail");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    TestPanelHelper testList_ = new TestPanelHelper();

                                    testList_.setTestID(jsonObject.getString("testID"));
                                    testList_.setTest_name(jsonObject.getString("test_name"));
                                    testList_.setTotal_question(jsonObject.getString("total_question"));
                                    testList_.setTotal_marks(jsonObject.getString("total_marks"));
                                    testList_.setTotal_time(jsonObject.getString("total_time"));
                                    testList_.setBtn_status(jsonObject.getString("btn_status"));
                                    testList_.setPackage_status(jsonObject.getString("package_status"));

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
                Log.e("@@testpanel_error",error.getMessage().toString());
            }
        });
        queue.add(req);
    }
}
