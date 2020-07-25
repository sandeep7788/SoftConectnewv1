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
import com.exam.softconect.Adapter.AllRankAdapter;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllRankListActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    RecyclerView recyclearview;
    AllRankAdapter adapter;
    List<TestPanelHelper> rankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rank_list);

        //get references
        img_back = findViewById(R.id.img_back);
        recyclearview = (RecyclerView) findViewById(R.id.recyclearview_all_rank);
        recyclearview.setHasFixedSize(true);
        recyclearview.setLayoutManager(new LinearLayoutManager(this));

        rankList = new ArrayList<>();

        adapter = new AllRankAdapter(this, rankList);
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

        StringRequest request = new StringRequest(Request.Method.GET, ServerUtils.Base_url + ServerUtils.rankDetail_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String status;

                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                JSONArray jsonArray1 = jsonObject.getJSONArray("rankList");


                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    try {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        TestPanelHelper rankList_ = new TestPanelHelper();

                                        rankList_.setRank_list_image(jsonObject1.getString("photo"));
                                        rankList_.setRank_list_name(jsonObject1.getString("student_name"));
                                        rankList_.setRank_list_address(jsonObject1.getString("address"));
                                        rankList_.setRank_list_test_name(jsonObject1.getString("test_name"));
                                        rankList_.setRank_list_get_marks(jsonObject1.getString("total_marks"));
                                        rankList_.setRank_list_total_marks(jsonObject1.getString("test_total_marks"));
                                        rankList_.setRank_list_rank(jsonObject1.getString("rank"));

                                        rankList.add(rankList_);

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
