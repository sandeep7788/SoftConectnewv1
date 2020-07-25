package com.exam.softconect.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.exam.softconect.Adapter.ReviewQuizAdapter;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;
import com.nishant.math.MathView;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    TextView txt_title_quiz, txt_quiz_time, txt_question_number;
    MathView txt_question;
    LinearLayout btn_review, btn_next, btn_clear, btn_finish;
    RadioGroup groupradio;
    RadioButton rb_1, rb_2, rb_3, rb_4;
    String str_testID, str_answer = "", is_completed, str_questionId, str_total_time, str_studentAnswer, type;
    String[] str_question_id, str_question_number_arr, str_question_arr, str_student_answer;
    String[][] str_options_1_arr;
    int count_question_number = 0;
    com.nishant.math.MathView rb_1_Answer,rb_2_Answer,rb_3_Answer,rb_4_Answer;
    private static CountDownTimer countDownTimer;
    //for review dialog
    Dialog dialog_review;
    RecyclerView recyclearview_review;
    List<TestPanelHelper> reviewList;
    ReviewQuizAdapter adapter;
    ProgressBar progress_bar;
    int count = 1;
    static Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //get references
        img_back = findViewById(R.id.img_back);
        txt_title_quiz = findViewById(R.id.txt_title_quiz);
        txt_quiz_time = findViewById(R.id.txt_quiz_time);
        txt_question_number = findViewById(R.id.txt_question_number);
        groupradio = findViewById(R.id.groupradio);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        btn_review = findViewById(R.id.btn_review);
        btn_next = findViewById(R.id.btn_next);
        btn_clear = findViewById(R.id.btn_clear);
        btn_finish = findViewById(R.id.btn_finish);
        txt_question =(com.nishant.math.MathView)findViewById(R.id.txt_question);
        rb_1_Answer=(com.nishant.math.MathView)findViewById(R.id.rb_1_Answer);
        rb_2_Answer=(com.nishant.math.MathView)findViewById(R.id.rb_2_Answer);
        rb_3_Answer=(com.nishant.math.MathView)findViewById(R.id.rb_3_Answer);
        rb_4_Answer=(com.nishant.math.MathView)findViewById(R.id.rb_4_Answer);

        //set title
        Intent intent = getIntent();
        txt_title_quiz.setText(intent.getStringExtra("title"));
        str_testID = intent.getStringExtra("test_id");
        str_total_time = intent.getStringExtra("total_time");

        //start timer
        startTimer(str_total_time);

        //set listner
        img_back.setOnClickListener(this);
        btn_review.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_finish.setOnClickListener(this);

        //call method
        getTestData();
        type = "Before";

        //set listner on radio buttons
        groupradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rb_1:
                        str_answer = "1";
                        break;
                    case R.id.rb_2:
                        str_answer = "2";
                        break;
                    case R.id.rb_3:
                        str_answer = "3";
                        break;
                    case R.id.rb_4:
                        str_answer = "4";
                        break;
                }
            }
        });

        dialog_review = new Dialog(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                CommonUtils.AwesomeWarningDialog(this, "Do You want to leave this Test");
                break;
            case R.id.btn_clear:

                //clear readio button
                groupradio.clearCheck();


                break;
            case R.id.btn_finish:

                finishTest();
                break;
            case R.id.btn_review:

                type = "After";
                Review_dialog();

                break;
            case R.id.btn_next:


                if (type.equalsIgnoreCase("Before")) {

                    int question_length = str_question_number_arr.length - 1;

                    if (count_question_number <= question_length) {

                        is_completed = "0";
                        str_questionId = str_question_id[count_question_number];

                        //call method
                        sendAnswerServer();

                        if (count_question_number == question_length) {

                            Toast.makeText(this, "Question Complete. Click on Finish", Toast.LENGTH_SHORT).show();

                        }

                        if (count_question_number < question_length) {

                            count_question_number++;

                            //clear readio button
                            groupradio.clearCheck();


                            txt_question_number.setText("Q " + str_question_number_arr[count_question_number] + ".");

                            //convert question
                            byte[] data = Base64.decode(str_question_arr[count_question_number], Base64.DEFAULT);
                            String question = new String(data, StandardCharsets.UTF_8);
                            txt_question.setText(question);

                            //convert options
                            data = Base64.decode(str_options_1_arr[count_question_number][0], Base64.DEFAULT);
                            String options_1 = new String(data, StandardCharsets.UTF_8);
                            rb_1_Answer.setText(options_1);


                            data = Base64.decode(str_options_1_arr[count_question_number][1], Base64.DEFAULT);
                            String options_2 = new String(data, StandardCharsets.UTF_8);
                            rb_2_Answer.setText(options_2);

                            data = Base64.decode(str_options_1_arr[count_question_number][2], Base64.DEFAULT);
                            String options_3 = new String(data, StandardCharsets.UTF_8);
                            rb_3_Answer.setText(options_3);

                            data = Base64.decode(str_options_1_arr[count_question_number][3], Base64.DEFAULT);
                            String options_4 = new String(data, StandardCharsets.UTF_8);
                            rb_4_Answer.setText(options_4);
                        }
                    } else {
                        Toast.makeText(this, "Question Complete. Click on Finish", Toast.LENGTH_SHORT).show();

                    }
                } else if (type.equalsIgnoreCase("After")) {

                    int question_length = str_question_number_arr.length - 1;

                    if (count_question_number <= question_length) {

                        is_completed = "0";
                        str_questionId = str_question_id[count_question_number];

                        //call method
                        sendAnswerServer(str_testID, str_questionId, str_answer);

                        if (count_question_number == question_length) {

                            Toast.makeText(this, "Question Complete. Click on Finish", Toast.LENGTH_SHORT).show();

                        }

                        if (count_question_number < question_length) {

                            //set questions and  options
                            count_question_number++;

                            txt_question_number.setText("Q " + str_question_number_arr[count_question_number] + ".");

                            //convert question
                            byte[] data = Base64.decode(str_question_arr[count_question_number], Base64.DEFAULT);
                            String question = new String(data, StandardCharsets.UTF_8);
                            txt_question.setText(question);

                            //convert options
                            data = Base64.decode(str_options_1_arr[count_question_number][0], Base64.DEFAULT);
                            String options_1 = new String(data, StandardCharsets.UTF_8);
                            rb_1.setText(options_1.trim());

                            data = Base64.decode(str_options_1_arr[count_question_number][1], Base64.DEFAULT);
                            String options_2 = new String(data, StandardCharsets.UTF_8);
                            rb_2.setText(options_2);

                            data = Base64.decode(str_options_1_arr[count_question_number][2], Base64.DEFAULT);
                            String options_3 = new String(data, StandardCharsets.UTF_8);
                            rb_3.setText(options_3);

                            data = Base64.decode(str_options_1_arr[count_question_number][3], Base64.DEFAULT);
                            String options_4 = new String(data, StandardCharsets.UTF_8);
                            rb_4.setText(options_4);

                            //set radio button checked
                            str_studentAnswer = str_student_answer[count_question_number];

                            str_answer = "";
                            if (str_studentAnswer.equalsIgnoreCase("1")) {
                                str_answer = "1";
                                rb_1.setChecked(true);
                            } else if (str_studentAnswer.equalsIgnoreCase("2")) {
                                str_answer = "2";
                                rb_2.setChecked(true);
                            } else if (str_studentAnswer.equalsIgnoreCase("3")) {
                                str_answer = "3";
                                rb_3.setChecked(true);
                            } else if (str_studentAnswer.equalsIgnoreCase("4")) {
                                str_answer = "4";
                                rb_4.setChecked(true);
                            } else if (str_studentAnswer.equalsIgnoreCase("0")) {

                                groupradio.clearCheck();
                                str_answer = "0";

                            }
                        }

                    } else {
                        Toast.makeText(this, "Question Complete.", Toast.LENGTH_SHORT).show();

                    }
                }

                break;
        }
    }

    //Start Countodwn method
    private void startTimer(String getMinutes) {

        int noOfMinutes = Integer.parseInt(getMinutes) * 60 * 1000;//Convert minutes into milliseconds
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txt_quiz_time.setText(hms);//set text
                Log.e(">>1",hms);
                Log.e(">>2", String.valueOf(millis));

                /*if(millis==0)
                {
                    Toast.makeText(QuizActivity.this, "Please wait....", Toast.LENGTH_LONG).show();
                    finishTest();
                }*/

            }

            public void onFinish() {

                countDownTimer = null;//set CountDownTimer to null
                //CommonUtils.AwesomeWarningDialog(QuizActivity.this, "Time's up!");
                AwesomeWarningDialog(QuizActivity.this, "Time's up!");

            }
        }.start();

    }

    public void AwesomeWarningDialog(final Activity activity, String msg) {
        dialog = new AwesomeWarningDialog(activity)
                .setTitle("Conformation")
                .setMessage(msg)
                .setCancelable(true)
                .setButtonText("Yes")
                .setWarningButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        Toast.makeText(QuizActivity.this, "Please wait....", Toast.LENGTH_LONG).show();
                        finishTest();

                    }
                })
                .show();
    }

    public void getTestData() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(this);
Log.e("@@",ServerUtils.Base_url+ServerUtils.testStart_url+CommonUtils.userID);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.testStart_url + CommonUtils.userID + "/" + str_testID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
Log.e("@@",response.toString());
                        try {
                            String status;

                            status = response.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                JSONArray jsonArray = response.getJSONArray("question_data");

                                str_question_id = new String[jsonArray.length()];
                                str_question_arr = new String[jsonArray.length()];
                                str_question_number_arr = new String[jsonArray.length()];
                                str_options_1_arr = new String[jsonArray.length()][4];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    str_question_id[i] = jsonObject.getString("ques_id");
                                    str_question_number_arr[i] = jsonObject.getString("order_no");
                                    str_question_arr[i] = jsonObject.getString("question");

                                    //get options
                                    JSONArray jsonArray_options = jsonObject.getJSONArray("options");


                                    for (int j = 0; j < jsonArray_options.length(); j++) {

                                        JSONObject jsonObject_options = jsonArray_options.getJSONObject(j);

                                        String option = jsonObject_options.getString("answer_option");

                                        str_options_1_arr[i][j] = option;

                                    }
                                }


                                //set first question
                                is_completed = "0";
                                str_questionId = str_question_id[0];

                                txt_question_number.setText("Q " + str_question_number_arr[0] + ".");

                                //convert question
                                byte[] data = Base64.decode(str_question_arr[0], Base64.DEFAULT);
                                String question = new String(data, StandardCharsets.UTF_8);

                                txt_question.setText(question);

                                //convert options
                                data = Base64.decode(str_options_1_arr[0][0], Base64.DEFAULT);
                                String options_1 = new String(data, StandardCharsets.UTF_8);
                                rb_1.setText(options_1);


                                data = Base64.decode(str_options_1_arr[0][1], Base64.DEFAULT);
                                String options_2 = new String(data, StandardCharsets.UTF_8);
                                rb_2.setText(options_2);

                                data = Base64.decode(str_options_1_arr[0][2], Base64.DEFAULT);
                                String options_3 = new String(data, StandardCharsets.UTF_8);
                                rb_3.setText(options_3);

                                data = Base64.decode(str_options_1_arr[0][3], Base64.DEFAULT);
                                String options_4 = new String(data, StandardCharsets.UTF_8);
                                rb_4.setText(options_4);


                            }

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

    public void sendAnswerServer() {

        StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.saveTestResponse_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status;

                            status = jsonObject.getString("status");
                            Log.d("myApp", "Error: "+status);
                            if (status.equalsIgnoreCase("1")) {
                                str_answer = "";
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", CommonUtils.userID);
                params.put("test_id", str_testID);
                params.put("ques_id", str_questionId);
                params.put("answer_option", str_answer);
                params.put("is_completed", is_completed);

                Calendar rightNow = Calendar.getInstance();
                long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                        rightNow.get(Calendar.DST_OFFSET);
                long sinceMidnight = (rightNow.getTimeInMillis() + offset) %
                        (24 * 60 * 60 * 1000);
                System.out.println("@@ "+ sinceMidnight + " milliseconds since midnight");
                params.put("test_start_time", String.valueOf(sinceMidnight));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void finishTest() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.finishTest_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status;

                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                //Show Update Dialog
                                new FancyAlertDialog.Builder(QuizActivity.this)
                                        .setTitle("Test Complete")
                                        .setMessage("Congratulations ! Your result has been generated, Please click on View Report.")
                                        .setNegativeBtnText("Close")
                                        .setPositiveBtnText("View Report")
                                        .setAnimation(Animation.POP)
                                        .isCancellable(false)
                                        .setIcon(R.drawable.ic_right_green, Icon.Visible)
                                        .OnPositiveClicked(new FancyAlertDialogListener() {
                                            @Override
                                            public void OnClick() {

                                                Intent intent = new Intent(QuizActivity.this, ViewResultActivity.class);
                                                intent.putExtra("testId", str_testID);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .OnNegativeClicked(new FancyAlertDialogListener() {
                                            @Override
                                            public void OnClick() {

                                                finish();
                                            }
                                        })
                                        .build();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", CommonUtils.userID);
                params.put("test_id", str_testID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //moveTaskToBack(true);
            CommonUtils.AwesomeWarningDialog(this, "Do You want to leave this Test");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        CommonUtils.quitTest(this, str_testID);
    }

    //Method for Show Review Dialog
    public void Review_dialog() {

        ImageView img_close;
        RelativeLayout lay_answer, lay_without_answer;

        dialog_review.setContentView(R.layout.review_popup);
        dialog_review.setCancelable(false);

        img_close = dialog_review.findViewById(R.id.img_close);
        lay_answer = dialog_review.findViewById(R.id.lay_answer);
        lay_without_answer = dialog_review.findViewById(R.id.lay_without_answer);
        progress_bar = dialog_review.findViewById(R.id.progress_bar);
        recyclearview_review = dialog_review.findViewById(R.id.recyclearview_review);
        recyclearview_review.setHasFixedSize(true);
        recyclearview_review.setLayoutManager(new GridLayoutManager(this, 4));

        reviewList = new ArrayList<>();

        adapter = new ReviewQuizAdapter(this, reviewList, dialog_review, this);
        recyclearview_review.setAdapter(adapter);

        //set color
        GradientDrawable lay_answer_ = (GradientDrawable) lay_answer.getBackground().getCurrent();
        lay_answer_.setColor(Color.parseColor("#4AA641"));

        GradientDrawable lay_without_answer_ = (GradientDrawable) lay_without_answer.getBackground().getCurrent();
        lay_without_answer_.setColor(Color.parseColor("#EF473A"));

        //call method
        loadRecyclerViewData_Review();


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_review.dismiss();
            }
        });


        dialog_review.show();


    }

    public void loadRecyclerViewData_Review() {

        //show process dialog
        count = 1;

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, ServerUtils.Base_url + ServerUtils.review_url + CommonUtils.userID + "/" + str_testID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String status;

                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {

                                JSONArray jsonArray1 = jsonObject.getJSONArray("quesDetail");

                                str_student_answer = new String[jsonArray1.length()];
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    try {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        TestPanelHelper reviewList_ = new TestPanelHelper();

                                        reviewList_.setReview_list_ques_id(jsonObject1.getString("ques_id"));
                                        reviewList_.setReview_list_student_ans(jsonObject1.getString("student_ans"));
                                        reviewList_.setReview_list_btn_status(jsonObject1.getString("btn_status"));
                                        reviewList_.setReview_list_ques_ordert(jsonObject1.getString("ques_order"));

                                        str_student_answer[i] = jsonObject1.getString("student_ans");

                                        reviewList_.setReview_list_count(String.valueOf(count));
                                        count++;

                                        reviewList.add(reviewList_);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                progress_bar.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress_bar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        progress_bar.setVisibility(View.GONE);
                    }
                }
        );

        queue.add(request);


    }

    public void setReview_Questions(String str_student_ans, int str_ques_order) {


        count = 1;

        txt_question_number.setText("Q " + str_question_number_arr[str_ques_order] + ".");

        //convert question
        byte[] data = Base64.decode(str_question_arr[str_ques_order], Base64.DEFAULT);
        String question = new String(data, StandardCharsets.UTF_8);
        txt_question.setText(question);

        //convert options
        data = Base64.decode(str_options_1_arr[str_ques_order][0], Base64.DEFAULT);
        String options_1 = new String(data, StandardCharsets.UTF_8);
        rb_1.setText(options_1);

        data = Base64.decode(str_options_1_arr[str_ques_order][1], Base64.DEFAULT);
        String options_2 = new String(data, StandardCharsets.UTF_8);
        rb_2.setText(options_2);

        data = Base64.decode(str_options_1_arr[str_ques_order][2], Base64.DEFAULT);
        String options_3 = new String(data, StandardCharsets.UTF_8);
        rb_3.setText(options_3);

        data = Base64.decode(str_options_1_arr[str_ques_order][3], Base64.DEFAULT);
        String options_4 = new String(data, StandardCharsets.UTF_8);
        rb_4.setText(options_4);

        //set radio button checked
        str_studentAnswer = str_student_ans;

        str_answer = "";
        if (str_studentAnswer.equalsIgnoreCase("1")) {
            str_answer = "1";
            rb_1.setChecked(true);
        } else if (str_studentAnswer.equalsIgnoreCase("2")) {
            str_answer = "2";
            rb_2.setChecked(true);
        } else if (str_studentAnswer.equalsIgnoreCase("3")) {
            str_answer = "3";
            rb_3.setChecked(true);
        } else if (str_studentAnswer.equalsIgnoreCase("4")) {
            str_answer = "4";
            rb_4.setChecked(true);
        } else if (str_studentAnswer.equalsIgnoreCase("0")) {

            groupradio.clearCheck();
            str_answer = "0";

        }

        //set questions and  options
        count_question_number = str_ques_order;

    }
    public void  sendAnswerServer(final String str_testID_, final String str_questionId_, final String str_answer_) {

        StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.saveTestResponse_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String status;

                            status = jsonObject.getString("status");

                            if (status.equalsIgnoreCase("1")) {
                                //str_answer = "";
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
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", CommonUtils.userID);
                params.put("test_id", str_testID_);
                params.put("ques_id", str_questionId_);
                params.put("answer_option", str_answer_);
                params.put("is_completed", is_completed);

                Calendar rightNow = Calendar.getInstance();
                long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                        rightNow.get(Calendar.DST_OFFSET);
                long sinceMidnight = (rightNow.getTimeInMillis() + offset) %
                        (24 * 60 * 60 * 1000);
                System.out.println("@@ "+ sinceMidnight + " milliseconds since midnight");
                params.put("test_start_time", String.valueOf(sinceMidnight));

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

}
