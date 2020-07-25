package com.exam.softconect.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Adapter.AllResultAdapter;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllViewResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    RecyclerView recyclearview;
    AllResultAdapter adapter;
    List<TestPanelHelper> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_view_result);

        //get references
        img_back = findViewById(R.id.img_back);
        recyclearview = (RecyclerView) findViewById(R.id.recyclearview_all_result);
        recyclearview.setHasFixedSize(true);
        recyclearview.setLayoutManager(new LinearLayoutManager(this));

        testList = new ArrayList<>();

        adapter = new AllResultAdapter(this, testList);
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

    public void loadRecyclerViewData() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ServerUtils.Base_url + ServerUtils.resultList_url + CommonUtils.userID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status;

                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                JSONArray jsonArray1 = jsonObject.getJSONArray("result");

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    try {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        TestPanelHelper testList_ = new TestPanelHelper();

                                        testList_.setTest_Id_result(jsonObject1.getString("test_id"));
                                        testList_.setTest_name_result(jsonObject1.getString("test_name"));
                                        testList_.setTotal_score(jsonObject1.getString("total_score"));
                                        testList_.setTotal_test_marks(jsonObject1.getString("total_test_marks"));

                                        testList.add(testList_);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            progressDialog.dismiss();
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
        );

        queue.add(request);


    }
}
