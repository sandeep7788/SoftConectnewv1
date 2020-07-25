package com.exam.softconect.Activity;

import android.app.Dialog;
import android.app.Activity;

import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;

import android.content.IntentFilter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Adapter.DeashBoardRankAdapter;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.Helper.TestPanelHelper;
import com.exam.softconect.R;
import com.exam.softconect.Services.LoginVolleyService;
import com.exam.softconect.Utils.ServerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DeshBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Boolean exit = Boolean.valueOf(false);
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    boolean isDrawerOpen = false;
    private Toolbar toolbar;
    Intent intent;
    TextView txt_test_panel_test_name, txt_test_panel_total_question, txt_test_panel_total_marks, txt_test_panel_total_time, txt_package_test_name, txt_package_total_amount, txt_package_discount_amount, txt_package_total_time, txt_result_name, txt_result_min_marks, txt_result_max_marks, txt_test_panel_error, txt_package_error, txt_result_error, txt_penal_view_all, txt_package_view_all, txt_result_view_all;
    LinearLayout lay_test_panel, lay_package, lay_result, btn_test_start, btn_test_resume, btn_test_view, lay_package_buy, lay_result_view, lay_rank, lay_discount_amount;
    ImageView img_feedback;
    CardView card_test, card_package, card_result;

    //Feedback
    Dialog dialog_feedback;
    EditText edt_feedback;
    Button btn_feedback, btn_view_all_rank;
    String str_feedback, str_testId, str_testId_Test, str_test_btn_status, str_test_name, str_total_time, str_Package_id, str_Package_name, str_amount, str_discount_price, str_package_status;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    Timer swipeTimer;

    LoginVolleyService mVolleyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desh_board);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Get Reference for Toolbar
        toolbar = findViewById(R.id.toolbar);
        card_test = findViewById(R.id.card_test);
        card_package = findViewById(R.id.card_package);
        card_result = findViewById(R.id.card_result);
        img_feedback = findViewById(R.id.img_feedback);
        txt_test_panel_test_name = findViewById(R.id.txt_test_panel_test_name);
        txt_test_panel_total_question = findViewById(R.id.txt_test_panel_total_question);
        txt_test_panel_total_marks = findViewById(R.id.txt_test_panel_total_marks);
        txt_test_panel_total_time = findViewById(R.id.txt_test_panel_total_time);
        txt_package_test_name = findViewById(R.id.txt_package_test_name);
        txt_package_total_amount = findViewById(R.id.txt_package_total_amount);
        txt_package_total_time = findViewById(R.id.txt_package_total_time);
        txt_package_discount_amount = findViewById(R.id.txt_package_discount_amount);
        txt_result_name = findViewById(R.id.txt_result_name);
        txt_result_min_marks = findViewById(R.id.txt_result_min_marks);
        txt_result_max_marks = findViewById(R.id.txt_result_max_marks);
        txt_test_panel_error = findViewById(R.id.txt_test_panel_error);
        txt_package_error = findViewById(R.id.txt_package_error);
            txt_result_error = findViewById(R.id.txt_result_error);
        txt_penal_view_all = findViewById(R.id.txt_penal_view_all);
        txt_package_view_all = findViewById(R.id.txt_package_view_all);
        txt_result_view_all = findViewById(R.id.txt_result_view_all);
        lay_rank = findViewById(R.id.lay_rank);
        lay_test_panel = findViewById(R.id.lay_test_panel);
        lay_package = findViewById(R.id.lay_package);
        lay_result = findViewById(R.id.lay_result);
        lay_discount_amount = findViewById(R.id.lay_discount_amount);
        btn_test_start = findViewById(R.id.btn_test_start);
        btn_test_resume = findViewById(R.id.btn_test_resume);
        btn_test_view = findViewById(R.id.btn_test_view);
        btn_view_all_rank = findViewById(R.id.btn_view_all_rank);
        lay_package_buy = findViewById(R.id.lay_package_buy);
        lay_result_view = findViewById(R.id.lay_result_view);
        mPager = (ViewPager) findViewById(R.id.pager);

        //set visibily card
        if (CommonUtils.roleID.equalsIgnoreCase("2")) {

            card_test.setVisibility(View.VISIBLE);
            card_package.setVisibility(View.VISIBLE);
            card_result.setVisibility(View.VISIBLE);

        } else if (CommonUtils.roleID.equalsIgnoreCase("5")) {

            card_test.setVisibility(View.VISIBLE);
            card_package.setVisibility(View.GONE);
            card_result.setVisibility(View.VISIBLE);
        }


        //Get Reference for DrawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                isDrawerOpen = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true;
            }
        };
        drawerLayout.setDrawerListener(this.actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //Set Listner
        navigationView.setNavigationItemSelectedListener(this);
        img_feedback.setOnClickListener(this);
        txt_penal_view_all.setOnClickListener(this);
        txt_package_view_all.setOnClickListener(this);
        txt_result_view_all.setOnClickListener(this);
        btn_test_start.setOnClickListener(this);
        btn_test_resume.setOnClickListener(this);
        btn_test_view.setOnClickListener(this);
        btn_view_all_rank.setOnClickListener(this);
        lay_package_buy.setOnClickListener(this);
        lay_result_view.setOnClickListener(this);

        dialog_feedback = new Dialog(this);






        card_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_result_view_all.performClick();
            }
        });

        card_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_penal_view_all.performClick();
            }
        });

        card_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_package_view_all.performClick();
            }
        });

        //call method
        Get_deasboard_data();

        //set listner on view pager
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                currentPage = i;

                // if (currentPage==imageModelArrayList.size())

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mVolleyService = new LoginVolleyService(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.deshboard) {

        } else if (id == R.id.tests) {
            Intent intent = new Intent(this, TestPanelActivity.class);
            startActivity(intent);
        } else if (id == R.id.reports) {
            Intent intent = new Intent(this, AllViewResultActivity.class);
            startActivity(intent);
        } else if (id == R.id.change_password) {

            intent = new Intent(this, ChangePassword.class);
            startActivity(intent);

        } else if (id == R.id.profile) {

            intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout) {
            if (Build.VERSION.SDK_INT >= 16) {
                SharedPreferences.Editor editor = getSharedPreferences("LogDIN", MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, LoginActivity.class));
                finishAffinity();
            }
        }
        else if (id == R.id.contect) {

            intent = new Intent(this, Contect.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Get_deasboard_data();
    }


    //method for get data
    public void Get_deasboard_data() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(this);

        Log.e("@@dashbord_api",ServerUtils.Base_url + ServerUtils.dashboard_data_url + CommonUtils.userID);
        Log.e("@@userid",CommonUtils.userID+" ");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.dashboard_data_url + CommonUtils.userID,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("@@status",response.toString());
                        progressDialog.dismiss();
                        Log.e("@@response",response.toString());
                        try {

                            JSONObject jsonObject = response.getJSONObject(0);
                            String str_test_penal = jsonObject.getString("test_details");
                            String str_package = jsonObject.getString("package");
                            String str_result = jsonObject.getString("result");
                            String str_rank_list = jsonObject.getString("rankList");
                            str_package_status = jsonObject.getString("package_status");
                            Log.e("@@news",jsonObject.getString("news"));

                            byte[] data = Base64.decode(jsonObject.getString("news"), Base64.DEFAULT);
                            String question = new String(data, StandardCharsets.UTF_8);
                            question = question.replace("\\", "\\\\");
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

                            TextView txt = findViewById(R.id.t_news);
                            txt.setText(question.trim());
                            txt.setSelected(true);

                            //set text
                            if (!str_test_penal.equalsIgnoreCase("")) {

                                JSONObject jsonObject1 = jsonObject.getJSONObject("test_details");

                                str_testId_Test = jsonObject1.getString("test_id");
                                str_test_name = jsonObject1.getString("test_name");
                                str_total_time = jsonObject1.getString("total_time");
                                str_test_btn_status = jsonObject1.getString("btn_status");


                                txt_test_panel_test_name.setText(jsonObject1.getString("test_name"));
                                txt_test_panel_total_question.setText(jsonObject1.getString("total_question"));
                                txt_test_panel_total_marks.setText(jsonObject1.getString("total_marks"));
                                txt_test_panel_total_time.setText(jsonObject1.getString("total_time") + " Min");

                                if (str_test_btn_status.equalsIgnoreCase("0")) {

                                    btn_test_start.setVisibility(View.VISIBLE);
                                    btn_test_resume.setVisibility(View.GONE);
                                    btn_test_view.setVisibility(View.GONE);

                                } else if (str_test_btn_status.equalsIgnoreCase("1")) {

                                    btn_test_start.setVisibility(View.GONE);
                                    btn_test_resume.setVisibility(View.VISIBLE);
                                    btn_test_view.setVisibility(View.GONE);

                                } else if (str_test_btn_status.equalsIgnoreCase("2")) {

                                    btn_test_start.setVisibility(View.GONE);
                                    btn_test_resume.setVisibility(View.GONE);
                                    btn_test_view.setVisibility(View.VISIBLE);

                                }


                            } else {
                                lay_test_panel.setVisibility(View.GONE);
//                                txt_penal_view_all.setVisibility(View.GONE);
                                txt_test_panel_error.setVisibility(View.VISIBLE);
                            }

                            if (!str_package.equalsIgnoreCase("")) {

                                JSONObject jsonObject_package = jsonObject.getJSONObject("package");

                                txt_package_test_name.setText(jsonObject_package.getString("package_name"));
                                txt_package_total_time.setText(jsonObject_package.getString("total_time"));

                                str_Package_id = jsonObject_package.getString("package_id");
                                str_Package_name = jsonObject_package.getString("package_name");
                                str_amount = jsonObject_package.getString("price");
                                str_discount_price = jsonObject_package.getString("discount_price");

                                if (str_discount_price.equalsIgnoreCase("0")) {

                                    txt_package_total_amount.setText(str_amount + " /-");
                                    lay_discount_amount.setVisibility(View.GONE);
                                } else {
                                    lay_discount_amount.setVisibility(View.VISIBLE);

                                    txt_package_total_amount.setText(str_amount + " /-");
                                    txt_package_total_amount.setPaintFlags(txt_package_total_amount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    txt_package_discount_amount.setText(str_discount_price + " /-");

                                    str_amount = str_discount_price;
                                }

                            } else {
                                lay_package.setVisibility(View.GONE);
                                txt_package_view_all.setVisibility(View.GONE);
                                txt_package_error.setVisibility(View.VISIBLE);
                            }

                            if (!str_result.equalsIgnoreCase("")) {

                                JSONObject jsonObject_result = jsonObject.getJSONObject("result");

                                str_testId = jsonObject_result.getString("test_id");
                                txt_result_name.setText(jsonObject_result.getString("test_name"));
                                txt_result_min_marks.setText(jsonObject_result.getString("total_score"));
                                txt_result_max_marks.setText(jsonObject_result.getString("total_test_marks"));

                            } else {
                                lay_result.setVisibility(View.GONE);
//                                txt_result_view_all.setVisibility(View.GONE);
                                txt_result_view_all.setClickable(false);
                                txt_result_view_all.setClickable(false);
                                txt_result_error.setVisibility(View.VISIBLE);
                            }

                            //get rank list
                            JSONArray jsonArray = new JSONArray(str_rank_list);

                            //set rankList
                            if (jsonArray.length() != 0) {

                                ArrayList<TestPanelHelper> list = new ArrayList<>();

                                lay_rank.setVisibility(View.VISIBLE);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject_ranklist = jsonArray.getJSONObject(i);

                                    TestPanelHelper testPanelHelper = new TestPanelHelper();

                                    testPanelHelper.setDeashboard_name(jsonObject_ranklist.getString("student_name"));
                                    testPanelHelper.setDeashboard_test(jsonObject_ranklist.getString("test_name"));
                                    testPanelHelper.setDeashboard_rank(jsonObject_ranklist.getString("rank"));
                                    testPanelHelper.setDeashboard_image_profile(jsonObject_ranklist.getString("photo"));
                                    testPanelHelper.setDeashboard_city(jsonObject_ranklist.getString("city"));
                                    testPanelHelper.setDeashboard_district(jsonObject_ranklist.getString("district"));
                                    list.add(testPanelHelper);

                                }

                                DeashBoardRankAdapter deashBoardRankAdapter = new DeashBoardRankAdapter(DeshBoardActivity.this, list);
                                mPager.setAdapter(deashBoardRankAdapter);

                                NUM_PAGES = list.size();

                                // Auto start of viewpager
                                final Handler handler = new Handler();
                                final Runnable Update = new Runnable() {
                                    public void run() {
                                        if (currentPage == NUM_PAGES) {
                                            currentPage = 0;
                                        }
                                        mPager.setCurrentItem(currentPage++, true);
                                    }
                                };
                                swipeTimer = new Timer();
                                swipeTimer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        handler.post(Update);
                                    }
                                }, 5000, 5000);


                            } else {
                                lay_rank.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Log.e("@@response",error.getMessage()+" ");
                        progressDialog.dismiss();
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        queue.add(jsonArrayRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isDrawerOpen) {
            drawerLayout.closeDrawer(8388611);
        } else if (exit.booleanValue()) {
            finish();
        } else {
            // Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
            exit = Boolean.valueOf(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = Boolean.valueOf(false);

                }
            }, 3000);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_feedback:
                Feedback_dialog();

                break;
            case R.id.btn_view_all_rank:

                intent = new Intent(this, AllRankListActivity.class);
                startActivity(intent);

                break;

            case R.id.btn_test_start:

                if (str_package_status.equalsIgnoreCase("1")) {
                    //call method
                    CommonUtils.getInstruction(this, str_testId_Test, str_total_time);
                } else {
                    CommonUtils.AwesomeWarningPackageDialog(this, "Sorry ! You are not authorized to access this test, Please purchase package.");

                }
                break;
            case R.id.btn_test_resume:

                if (str_package_status.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(this, ResumeTestActivity.class);
                    intent.putExtra("title", str_test_name);
                    intent.putExtra("testId", str_testId_Test);
                    startActivity(intent);
                } else {
                    CommonUtils.AwesomeWarningPackageDialog(this, "Sorry ! You are not authorized to access this test, Please purchase package.");

                }


                break;
            case R.id.btn_test_view:

                if (str_package_status.equalsIgnoreCase("1")) {
                    intent = new Intent(this, ViewResultActivity.class);
                    intent.putExtra("testId", str_testId_Test);
                    startActivity(intent);
                } else {
                    CommonUtils.AwesomeWarningPackageDialog(this, "Sorry ! You are not authorized to access this test, Please purchase package.");
                }


                break;

            case R.id.lay_package_buy:
                str_Package_name = "purchase " + str_Package_name;
                callInstamojoPay(CommonUtils.email_common, CommonUtils.mobile_common, str_amount, str_Package_name, CommonUtils.name_common);

                break;

            case R.id.lay_result_view:
                intent = new Intent(this, ViewResultActivity.class);
                intent.putExtra("testId", str_testId);
                startActivity(intent);
                break;

            case R.id.txt_penal_view_all:
                intent = new Intent(this, TestPanelActivity.class);
                startActivity(intent);
                break;

            case R.id.txt_package_view_all:
                intent = new Intent(this, PackageActivity.class);
                startActivity(intent);
                break;

            case R.id.txt_result_view_all:
                intent = new Intent(this, AllViewResultActivity.class);
                startActivity(intent);
                break;
        }
    }

    //Method for Show Feedback Dialog
    public void Feedback_dialog() {

        ImageView img_close;

        dialog_feedback.setContentView(R.layout.feedback_popup);
        dialog_feedback.setCancelable(false);

        img_close = dialog_feedback.findViewById(R.id.img_close);
        edt_feedback = dialog_feedback.findViewById(R.id.edt_feedback);
        btn_feedback = dialog_feedback.findViewById(R.id.btn_feedback);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_feedback.dismiss();
            }
        });

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_feedback = edt_feedback.getText().toString().trim();

                if (str_feedback.equalsIgnoreCase("")) {
                    edt_feedback.setError("Please enter your Feedback.");
                } else {

                    //call send feedback method
                    Send_feedback(str_feedback);
                }
            }
        });


        dialog_feedback.show();


    }

    public void Send_feedback(final String str_feedback) {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.feedback_url,
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
                                    dialog_feedback.dismiss();

                                    message = jsonObject.getString("message");

                                    //Show Success Dialog
                                    CommonUtils.AwesomeSuccessDialog(DeshBoardActivity.this, message);

                                } else if (status.equalsIgnoreCase("0")) {

                                    message = jsonObject.getString("message");

                                    //Show Error Dialog
                                    CommonUtils.AwesomeErrorDialog(DeshBoardActivity.this, message);
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
                    params.put("feedback", str_feedback);

                    return params;
                }
            };

            queue.add(request);

        } catch (Exception e) {

        }
    }

    //payment
    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
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


    private void
    initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {

                mVolleyService.sendPaymentResponse(DeshBoardActivity.this, CommonUtils.userID, response, str_Package_id);

            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }
}
