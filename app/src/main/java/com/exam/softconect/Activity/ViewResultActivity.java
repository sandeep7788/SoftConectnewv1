package com.exam.softconect.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Adapter.ResultViewAdapter;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewResultActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    String str_testID;
    ResultViewAdapter adapter;
    List<TestPanelHelper> resultList;
    RecyclerView recyclearview;
    TextView txt_test_name, txt_total_question, txt_total_marks, txt_total_correct, txt_total_incorrect, txt_not_attempted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        //get data from Intent
        Intent intent = getIntent();
        str_testID = intent.getStringExtra("testId");

        //get references
        img_back = findViewById(R.id.img_back);
        txt_test_name = findViewById(R.id.txt_test_name);
        txt_total_question = findViewById(R.id.txt_total_question);
        txt_total_marks = findViewById(R.id.txt_total_marks);
        txt_total_correct = findViewById(R.id.txt_total_correct);
        txt_total_incorrect = findViewById(R.id.txt_total_incorrect);
        txt_not_attempted = findViewById(R.id.txt_not_attempted);
        recyclearview = (RecyclerView) findViewById(R.id.recyclearview_result);
        recyclearview.setHasFixedSize(true);
        recyclearview.setLayoutManager(new LinearLayoutManager(this));

        resultList = new ArrayList<>();

        adapter = new ResultViewAdapter(this, resultList);
        recyclearview.setAdapter(adapter);

        //call method
        getResult();

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

    public void getResult() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.viewResult_url + "/" + str_testID + "/" + CommonUtils.userID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status;
                        try {

                            status = response.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                JSONObject jsonObject = response.getJSONObject("test_details");

                                txt_test_name.setText(jsonObject.getString("test_name"));
                                txt_total_question.setText("Total Question - " + jsonObject.getString("total_question"));
                                txt_total_marks.setText("Total Marks - " + jsonObject.getString("total_marks"));
                                txt_total_correct.setText("Total Correct - " + jsonObject.getString("total_correct"));
                                txt_total_incorrect.setText("Total InCorrect - " + jsonObject.getString("total_incorrect"));
                                txt_not_attempted.setText("Not Attempted - " + jsonObject.getString("total_omitted"));


                                JSONArray jsonArray = response.getJSONArray("question_wise_Result");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject_result = jsonArray.getJSONObject(i);

                                        TestPanelHelper testList_ = new TestPanelHelper();

                                        testList_.setQuestion_number(jsonObject_result.getString("ques_no"));
                                        testList_.setQuestion_status(jsonObject_result.getString("is_attempt"));
                                        testList_.setYour_answer(jsonObject_result.getString("student_answer"));
                                        testList_.setCorrect_answer(jsonObject_result.getString("correct_answer"));
                                        testList_.setYour_scored(jsonObject_result.getString("correct_marks"));

                                        resultList.add(testList_);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                    }
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
}
