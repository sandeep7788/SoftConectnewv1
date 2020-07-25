package com.exam.softconect.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.R;
import com.exam.softconect.Services.LoginVolleyService;
import com.exam.softconect.Utils.ServerUtils;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.android.volley.Request.Method.POST;

public class SpleshActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;
    private static int SPLASH_TIME_OUT = 1500;
    ImageView img_logo, img_logo_txt;
    String version, newVersion, oldVersion, Na, Pa, institute_id;
    boolean Is;
    LoginVolleyService mVolleyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);

        //get Reference
        img_logo = findViewById(R.id.img_logo);
        img_logo_txt = findViewById(R.id.img_logo_txt);

        //Set Animation
        img_logo.animate().alpha(1).setDuration(2000).start();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.wobble);
        img_logo_txt.startAnimation(animation);




        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            CommonUtils.oldversion = version;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                VersionCheck();

            }
        }, SPLASH_TIME_OUT);

        mVolleyService = new LoginVolleyService(this);

    }

    public void VersionCheck() {


        if (isNetworkAvailable()) {
            JsonArrayRequest request = new JsonArrayRequest(POST, ServerUtils.Base_url + ServerUtils.Version_check_url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);

                        oldVersion = jsonObject.getString("oldVersion");
                        newVersion = jsonObject.getString("newVersion");

                        oldVersion = CommonUtils.oldversion;
                        if (Double.valueOf(Double.parseDouble(newVersion)).doubleValue() > Double.valueOf(Double.parseDouble(oldVersion)).doubleValue()) {

                            //Show Update Dialog
                            new FancyAlertDialog.Builder(SpleshActivity.this)
                                    .setTitle("Update available")
                                    .setMessage("A quick update is available. Would you like  to update it ?")
                                    .setNegativeBtnText("Ignor")
                                    .setPositiveBtnText("Update")
                                    .setAnimation(com.shashank.sony.fancydialoglib.Animation.POP)
                                    .isCancellable(false)
                                    .setIcon(R.drawable.ic_info_white, Icon.Visible)
                                    .OnPositiveClicked(new FancyAlertDialogListener() {
                                        @Override
                                        public void OnClick() {

                                            //Update using play store
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.exam.softconect"));
                                            startActivity(intent);

                                        }
                                    })
                                    .OnNegativeClicked(new FancyAlertDialogListener() {
                                        @Override
                                        public void OnClick() {

                                            login();
                                        }
                                    })
                                    .build();

                            return;
                        } else {
                            login();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SpleshActivity.this, R.string.error + "Version update verify", Toast.LENGTH_SHORT).show();

                }
            });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } else {
            showAlertDialog();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.e("@@intermnet", String.valueOf(activeNetworkInfo));
        return activeNetworkInfo != null;

    }

    void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle((CharSequence) "No Internet").setMessage((CharSequence) "Turn On Your Internet and Try Again")
                .setPositiveButton((CharSequence) "Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VersionCheck();
                    }
                });
        alertDialog.show();
    }

    void login() {
        if (isNetworkAvailable()) {
            SharedPreferences sharedPreferences = getSharedPreferences("LogDIN", Context.MODE_PRIVATE);
            Is = sharedPreferences.getBoolean("Is_Login", false);
            Na = sharedPreferences.getString("N", "");
            Pa = sharedPreferences.getString("P", "");
            institute_id = sharedPreferences.getString("institute_id", "");
            CommonUtils.userName = Na;
            Log.e("@@login", String.valueOf(Is));
            if (Is) {

                //For Login
                mVolleyService.postDataVolley(this, ServerUtils.Base_url + ServerUtils.Login_url, Na, Pa, institute_id);

                return;
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
            return;
        }
        showAlertDialog();
    }

}
