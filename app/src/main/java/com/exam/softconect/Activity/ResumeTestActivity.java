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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
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
import com.exam.softconect.Adapter.ReviewAdapter;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;
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
import java.util.regex.Pattern;

import com.nishant.math.MathView;



public class ResumeTestActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    TextView txt_title_quiz, txt_quiz_time, txt_question_number;
    MathView txt_question,rb_1_Answer,rb_2_Answer,rb_3_Answer,rb_4_Answer;
    LinearLayout btn_review, btn_next, btn_clear, btn_finish;
    RadioGroup groupradio;
    RadioButton rb_1, rb_2, rb_3, rb_4;
    String str_testID, str_answer = "", is_completed, str_questionId, str_studentAnswer;
    String[] str_question_id, str_question_number_arr, str_question_arr, str_student_answer;
    String[][] str_options_1_arr;
    int count_question_number = 0;

    private static CountDownTimer countDownTimer;

    //for review dialog
    Dialog dialog_review;
    RecyclerView recyclearview_review;
    List<TestPanelHelper> reviewList;
    ReviewAdapter adapter;
    ProgressBar progress_bar;
    int count = 1;
    static Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_test);

        //get references
        img_back = findViewById(R.id.img_back);
        txt_title_quiz = findViewById(R.id.txt_title_quiz);
        txt_quiz_time = findViewById(R.id.txt_quiz_time);
        txt_question_number = findViewById(R.id.txt_question_number);
        txt_question =(MathView)findViewById(R.id.txt_question);
        rb_1_Answer=(MathView)findViewById(R.id.rb_1_Answer);
        rb_2_Answer=(MathView)findViewById(R.id.rb_2_Answer);
        rb_3_Answer=(MathView)findViewById(R.id.rb_3_Answer);
        rb_4_Answer=(MathView)findViewById(R.id.rb_4_Answer);
        groupradio = findViewById(R.id.groupradio);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        btn_review = findViewById(R.id.btn_review);
        btn_next = findViewById(R.id.btn_next);
        btn_clear = findViewById(R.id.btn_clear);
        btn_finish = findViewById(R.id.btn_finish);

        //set title
        Intent intent = getIntent();
        txt_title_quiz.setText(intent.getStringExtra("title"));
        str_testID = intent.getStringExtra("testId");

        //set listner
        img_back.setOnClickListener(this);
        btn_review.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_finish.setOnClickListener(this);

        //call method
        getResumeTestData();

        //set listner on radio buttons
        groupradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rb_1:
                        str_answer = "";
                        str_answer = "1";
                        break;
                    case R.id.rb_2:
                        str_answer = "";
                        str_answer = "2";
                        break;
                    case R.id.rb_3:
                        str_answer = "";
                        str_answer = "3";
                        break;
                    case R.id.rb_4:
                        str_answer = "";
                        str_answer = "4";
                        break;
                }

            }
        });

        dialog_review = new Dialog(this);

    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        txt_question = (MathView)findViewById(R.id.txt_question);
//        question=txt_question.toString();
//        txt_question.setText(question);
//        Log.d("myApp", "Erroronquestion: "+question);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:

                CommonUtils.AwesomeWarningDialog(this, "Do You want to leave this Test");
                break;
            case R.id.btn_clear:

                //clear readio button
                groupradio.clearCheck();

                str_answer = "0";

                break;
            case R.id.btn_finish:

                finishTest();

                break;
            case R.id.btn_review:

                Review_dialog();

                break;
            case R.id.btn_next:

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
                        question = question.replace("\\", "\\\\");
                        Log.d("myApp", "Erroronquestion:"+testmath(question));
//                        if(testmath(question)=="Yes")
//                        {
//                            Log.d("myApp", "Erroronquestion: yes have in math");
//                        }
//                        else
//                        {
//                            Log.d("myApp", "Erroronquestion:"+testmath(question));
//                        }
                        Log.e("@@data1",data.toString());
                        Log.e("@@data2",question.toString());
                        Log.e("@@option_get _bu_url",str_question_arr[count_question_number]);
                        txt_question.setText(question.trim());


                        //convert options
                        data = Base64.decode(str_options_1_arr[count_question_number][0], Base64.DEFAULT);
                        String options_1 = new String(data, StandardCharsets.UTF_8);
                        options_1=options_1.replace("\\", "\\\\");

//                        rb_1_Answer.loadData("<html><body  align='left'><h3> "+'{' + options_1 + '}'+" </h3></body></html>", "text/html", null);
                        rb_1_Answer.setText(options_1);

                        data = Base64.decode(str_options_1_arr[count_question_number][1], Base64.DEFAULT);
                        String options_2 = new String(data, StandardCharsets.UTF_8);
                        options_2=options_2.replace("\\", "\\\\");
                        rb_2_Answer.setText(options_2);
                        Log.e("@@d", String.valueOf(data));
                        Log.e("@@d", options_2);
                        Log.e("@@code",str_options_1_arr[count_question_number][1]);
//                        rb_2_Answer.loadData("<html><body  align='left'><b> "+'{' + options_2 + '}'+"</b></body></html>", "text/html", null);

                        data = Base64.decode(str_options_1_arr[count_question_number][2], Base64.DEFAULT);
                        String options_3 = new String(data, StandardCharsets.UTF_8);
                        options_3=options_3.replace("\\", "\\\\");

                        rb_3_Answer.setText(options_3.trim());
//                        rb_3_Answer.loadData("<html><body  align='left'><b> "+'{' + options_3 + '}'+"$a+ </b></body></html>", "text/html", null);

                        data = Base64.decode(str_options_1_arr[count_question_number][3], Base64.DEFAULT);
                        String options_4 = new String(data, StandardCharsets.UTF_8);
                        options_4=options_4.replace("\\", "\\\\");
                        rb_4_Answer.setText(options_4);
//                        rb_4_Answer.loadData("<html><body  align='left'><b> "+'{' + options_4 + '}'+"$a+ </b></body></html>", "text/html", null);
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


                break;
        }
    }

    //Start Countodwn method
    private void startTimer(String getMiliseconds) {

        int noOfMiliseconds = Integer.parseInt(getMiliseconds);

        countDownTimer = new CountDownTimer(noOfMiliseconds, 1000) {
            public void onTick(long millisUntilFinished) {

                txt_quiz_time.setText("" + String.format("%d:%d:%d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes(millisUntilFinished))));

            }

            public void onFinish() {

                countDownTimer = null;//set CountDownTimer to null
                //CommonUtils.AwesomeWarningDialog(ResumeTestActivity.this, "Time's up!");
                AwesomeWarningDialog(ResumeTestActivity.this, "Time's up!");
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
                        Toast.makeText(ResumeTestActivity.this, "Please wait....", Toast.LENGTH_LONG).show();
                        finishTest();

                    }
                })
                .show();
    }

    public void getResumeTestData() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("@@testurl",ServerUtils.Base_url + ServerUtils.testResume_url + CommonUtils.userID + "/" + str_testID);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.testResume_url + CommonUtils.userID + "/" + str_testID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status, str_miliseconds;

                            status = response.getString("status");


                            if (status.equalsIgnoreCase("1")) {

                                //start timer
                                str_miliseconds = response.getString("miliseconds");
                                startTimer(str_miliseconds);

                                //get data
                                JSONArray jsonArray = response.getJSONArray("question_data");

                                str_question_id = new String[jsonArray.length()];
                                str_question_arr = new String[jsonArray.length()];
                                str_question_number_arr = new String[jsonArray.length()];
                                str_student_answer = new String[jsonArray.length()];
                                str_options_1_arr = new String[jsonArray.length()][4];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //Log.d("myApp", "Erroronquestion: "+jsonObject.toString());
                                    str_question_id[i] = jsonObject.getString("ques_id");
                                    str_question_number_arr[i] = jsonObject.getString("order_no");
                                    str_question_arr[i] = jsonObject.getString("question");
                                    str_student_answer[i] = jsonObject.getString("student_answer");

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
//                                Log.d("myApp", "Erroronquestion: "+str_question_number_arr[0]);

                                txt_question = (MathView) findViewById(R.id.txt_question);
                                question = question.replace("\\", "\\\\");
                                Log.d("myApp", "Erroronquestion:"+testmath(question));
//                                if(testmath(question)=="Yes")
//                                {
//                                    Log.d("myApp", "Erroronquestion: yes have in math");
//                                }
//                                else
//                                {
//                                    Log.d("myApp", "Erroronquestion: no have in math");
//                                }
                                txt_question.setText(question.trim());


                                //convert options
                                data = Base64.decode(str_options_1_arr[0][0], Base64.DEFAULT);
                                String options_1 = new String(data, StandardCharsets.UTF_8);
                                //rb_1.setText(options_1);
                                options_1=options_1.replace("\\", "\\\\");
                                rb_1_Answer.setText(options_1);
//                                rb_1_Answer.setForegroundGravity(Gravity.NO_GRAVITY);
                                data = Base64.decode(str_options_1_arr[0][1], Base64.DEFAULT);
                                String options_2 = new String(data, StandardCharsets.UTF_8);
                                options_2=options_2.replace("\\", "\\\\");
                                rb_2_Answer.setText(options_2);

                                data = Base64.decode(str_options_1_arr[0][2], Base64.DEFAULT);
                                String options_3 = new String(data, StandardCharsets.UTF_8);
                                options_3=options_3.replace("\\", "\\\\");
                                rb_3_Answer.setText(options_3.trim());


                                data = Base64.decode(str_options_1_arr[0][3], Base64.DEFAULT);
                                String options_4 = new String(data, StandardCharsets.UTF_8);
                                //rb_4.setText(options_4);
                                options_4=options_4.replace("\\", "\\\\");
                                rb_4_Answer.setText(options_4);

                                //set radio button checked
                                str_studentAnswer = str_student_answer[0];

                                if (str_studentAnswer.equalsIgnoreCase("1")) {
                                    rb_1.setChecked(true);
                                    str_answer = "1";
                                } else if (str_studentAnswer.equalsIgnoreCase("2")) {
                                    rb_2.setChecked(true);
                                    str_answer = "2";
                                } else if (str_studentAnswer.equalsIgnoreCase("3")) {
                                    rb_3.setChecked(true);
                                    str_answer = "3";
                                } else if (str_studentAnswer.equalsIgnoreCase("4")) {
                                    rb_4.setChecked(true);
                                    str_answer = "4";
                                } else if (str_studentAnswer.equalsIgnoreCase("0")) {

                                    groupradio.clearCheck();
                                    str_answer = "0";
                                }


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

    public String testmath(String myString)
    {
        final String Digits     = "(\\p{Digit}+)";
        final String HexDigits  = "(\\p{XDigit}+)";

        final String Exp        = "[eE][+-]?"+Digits;
        final String fpRegex    =
                ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
                        "[+-]?(" +         // Optional sign character
                        "NaN|" +           // "NaN" string
                        "Infinity|" +      // "Infinity" string



                        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                        "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                        // . Digits ExponentPart_opt FloatTypeSuffix_opt
                        "(\\.("+Digits+")("+Exp+")?)|"+

                        // Hexadecimal strings
                        "((" +
                        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "(\\.)?)|" +

                        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                        ")[pP][+-]?" + Digits + "))" +
                        "[fFdD]?))" +
                        "[\\x00-\\x20]*");// Optional trailing "whitespace"

        if (Pattern.matches(fpRegex, myString)){
            return "Yes";
        } else {
            return "no";
        }

    }

    public void sendAnswerServer(final String str_testID_, final String str_questionId_, final String str_answer_) {

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
                                new FancyAlertDialog.Builder(ResumeTestActivity.this)
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

                                                Intent intent = new Intent(ResumeTestActivity.this, ViewResultActivity.class);
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

        adapter = new ReviewAdapter(this, reviewList, dialog_review, this);
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

                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    try {
                                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);

                                        TestPanelHelper reviewList_ = new TestPanelHelper();

                                        reviewList_.setReview_list_ques_id(jsonObject1.getString("ques_id"));
                                        reviewList_.setReview_list_student_ans(jsonObject1.getString("student_ans"));
                                        reviewList_.setReview_list_btn_status(jsonObject1.getString("btn_status"));
                                        reviewList_.setReview_list_ques_ordert(jsonObject1.getString("ques_order"));


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

        byte[] data;
        data=Base64.decode(str_question_arr[str_ques_order], Base64.DEFAULT);
        String question = new String(data, StandardCharsets.UTF_8);
        txt_question.setText(question);
        //Log.d("myApp", "Erroronquestion:"+question);
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
}

