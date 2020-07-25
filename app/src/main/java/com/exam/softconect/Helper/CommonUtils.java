package com.exam.softconect.Helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.exam.softconect.Activity.LoginActivity;
import com.exam.softconect.Activity.QuizActivity;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;
import com.tapadoo.alerter.Alerter;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonUtils {

    public static String oldversion;
    public static String userName, userID, name_common, email_common, mobile_common, roleID = "", packageID = "";
    static Dialog dialog;
    static Dialog dialog_instruction;
    static String str_test_name;


    //Error Msg Dialog
    public static void AwesomeErrorDialog(Activity activity, String msg) {
        dialog = new AwesomeErrorDialog(activity)
                .setTitle("Error !")
                .setMessage(msg)
                .setCancelable(false)
                .setButtonText("Ok")
                .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //Error Msg login Dialog
    public static void AwesomeErrorLoginDialog(final Activity activity, String msg) {
        dialog = new AwesomeErrorDialog(activity)
                .setTitle("Error !")
                .setMessage(msg)
                .setCancelable(false)
                .setButtonText("Ok")
                .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        dialog.dismiss();

                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                    }
                })
                .show();
    }

    //Hint Msg Dialog
    public static void AwesomeWarningDialog(final Activity activity, String msg) {
        dialog = new AwesomeWarningDialog(activity)
                .setTitle("Conformation")
                .setMessage(msg)
                .setCancelable(true)
                .setButtonText("Yes")
                .setWarningButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        activity.finish();

                    }
                })
                .show();
    }

    //Hint Msg Dialog
    public static void AwesomeWarningPackageDialog(final Activity activity, String msg) {
        dialog = new AwesomeWarningDialog(activity)
                .setTitle("Conformation")
                .setMessage(msg)
                .setCancelable(true)
                .setButtonText("Yes")
                .setWarningButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        dialog.dismiss();

                    }
                })
                .show();
    }

    //Success Msg Dialog
    public static void AwesomeSuccessDialog(Activity activity, String msg) {
        dialog = new AwesomeSuccessDialog(activity)
                .setTitle("Success")
                .setMessage(msg)
                .setCancelable(false)
                .setDoneButtonText("Ok")
                .setDoneButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //Error Msg Alerter
    public static void Alerter_Error(Activity activity, String msg) {
        Alerter.create(activity)
                .setTitle("Error !")
                .setText(msg)
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setBackgroundColor(R.color.colorAccent)
                .show();
    }

    public static void getInstruction(final Context mctx, final String str_test_id, final String str_total_time) {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(mctx, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(mctx);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.testDetail_url + str_test_id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        ImageView img_close;
                        Button btn_start_quiz;
                        TextView txt_test_name, txt_total_question, txt_total_marks, txt_start_date, txt_end_date, txt_start_time, txt_total_time;


                        dialog_instruction = new Dialog(mctx);

                        //show dialog
                        dialog_instruction.setContentView(R.layout.instruction_popup);
                        dialog_instruction.setCancelable(false);

                        img_close = dialog_instruction.findViewById(R.id.img_close);
                        btn_start_quiz = dialog_instruction.findViewById(R.id.btn_start_quiz);
                        txt_test_name = dialog_instruction.findViewById(R.id.txt_test_name);
                        txt_total_question = dialog_instruction.findViewById(R.id.txt_total_question);
                        txt_total_marks = dialog_instruction.findViewById(R.id.txt_total_marks);
                        txt_start_date = dialog_instruction.findViewById(R.id.txt_start_date);
                        txt_end_date = dialog_instruction.findViewById(R.id.txt_end_date);
                        txt_start_time = dialog_instruction.findViewById(R.id.txt_start_time);
                        txt_total_time = dialog_instruction.findViewById(R.id.txt_total_time);

                        img_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_instruction.dismiss();
                            }
                        });

                        //set data
                        try {
                            String str_status = response.getString("status");

                            if (str_status.equalsIgnoreCase("1")) {

                                str_test_name = response.getString("test_name");

                                txt_test_name.setText(response.getString("test_name"));
                                txt_total_question.setText(response.getString("total_questions"));
                                txt_total_marks.setText(response.getString("total_marks"));
                                txt_start_date.setText(response.getString("start_date"));
                                txt_end_date.setText(response.getString("end_date"));
                                txt_start_time.setText(response.getString("start_time"));
                                txt_total_time.setText(response.getString("total_time"));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //set listner on button
                        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(mctx, QuizActivity.class);
                                intent.putExtra("title", str_test_name);
                                intent.putExtra("test_id", str_test_id);
                                intent.putExtra("total_time", str_total_time);
                                mctx.startActivity(intent);

                                //dismiss dialog
                                dialog_instruction.dismiss();
                            }
                        });


                        dialog_instruction.show();

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

    public static void quitTest(final Context mctx, final String str_test_id) {

        RequestQueue queue = Volley.newRequestQueue(mctx);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.quitTest_url + CommonUtils.userID + "/" + str_test_id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        queue.add(req);
    }

}
