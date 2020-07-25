package com.exam.softconect.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.exam.softconect.Helper.CommonUtils;
import com.exam.softconect.R;
import com.exam.softconect.Utils.ServerUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back, img_edit;
    CircleImageView profile_image;
    TextInputLayout til_user_name, til_father_name, til_address,txt_city,txt_distict;
    TextInputEditText edt_user_name, edt_father_name, edt_email, edt_mobile, edt_address,edt_txt_city,edt_txt_distict;
    String str_user_name, str_father_name, str_address, encodedImageData="",str_city,str_district;
    Button btn_profile_update;
    private int CAMERA = 2;
    private int GALLERY = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get references
        img_back = findViewById(R.id.img_back);
        img_edit = findViewById(R.id.img_edit);
        profile_image = findViewById(R.id.profile_image);
        til_user_name = findViewById(R.id.til_user_name);
        til_father_name = findViewById(R.id.til_father_name);
        til_address = findViewById(R.id.til_address);
        txt_city= findViewById(R.id.txt_city);
        txt_distict= findViewById(R.id.txt_distict);
        edt_user_name = findViewById(R.id.edt_user_name);
        edt_father_name = findViewById(R.id.edt_father_name);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_address = findViewById(R.id.edt_address);
        edt_txt_city= findViewById(R.id.edt_txt_city);
        edt_txt_distict= findViewById(R.id.edt_txt_distict);

        btn_profile_update = findViewById(R.id.btn_profile_update);

        //set disable edittext
        edt_user_name.setEnabled(false);
        edt_father_name.setEnabled(false);
        edt_email.setEnabled(false);
        edt_mobile.setEnabled(false);
        edt_address.setEnabled(false);
        profile_image.setEnabled(false);

        //set listner
        img_back.setOnClickListener(this);
        img_edit.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        btn_profile_update.setOnClickListener(this);

        //call method
        get_profile_data();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_edit:

                edt_user_name.setEnabled(true);
                edt_father_name.setEnabled(true);
                edt_address.setEnabled(true);
                profile_image.setEnabled(true);

                edt_user_name.setFocusable(true);
                btn_profile_update.setVisibility(View.VISIBLE);

                break;
            case R.id.profile_image:
                showPictureDialog();
                break;
            case R.id.btn_profile_update:

                str_user_name = edt_user_name.getText().toString().trim();
                str_father_name = edt_father_name.getText().toString().trim();
                str_address = edt_address.getText().toString().trim();
                str_city = edt_txt_city.getText().toString().trim();
//                str_district = edt_txt_distict.getText().toString().trim();

                if (str_user_name.equalsIgnoreCase("")) {
                    til_user_name.setError("Please Enter Your Name");
                    til_father_name.setErrorEnabled(false);
                    til_address.setErrorEnabled(false);
                    edt_user_name.requestFocus();
                } else if (str_father_name.equalsIgnoreCase("")) {
                    til_father_name.setError("Please Enter Your Father Name");
                    til_user_name.setErrorEnabled(false);
                    til_address.setErrorEnabled(false);
                    edt_father_name.requestFocus();
                } else if (str_address.equalsIgnoreCase("")) {
                    til_address.setError("Please Enter Your Address");
                    til_user_name.setErrorEnabled(false);
                    til_father_name.setErrorEnabled(false);
                    edt_address.requestFocus();
                } else {

                    if (isNetworkAvailable()) {
                        Update_profile();
                    } else {
                        Snackbar.make(v, "You are now offline.", Snackbar.LENGTH_LONG).show();
                    }
                }
                break;
        }

    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle((CharSequence) "Select Action");
        pictureDialog.setItems(new String[]{"Select photo from gallery", "Capture photo from camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choosePhotoFromGallary();
                        return;
                    case 1:
                        takePhotoFromCamera();
                        return;
                    default:
                        return;
                }
            }
        });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GALLERY);
    }

    private void takePhotoFromCamera() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            if (data != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    profile_image.setImageBitmap(bitmap);
                    encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            bitmap = (Bitmap) data.getExtras().get("data");
            profile_image.setImageBitmap(bitmap);
            encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return Base64.encodeToString(stream.toByteArray(), 2);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void get_profile_data() {

        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                ServerUtils.Base_url + ServerUtils.profileDetail_url + CommonUtils.userID,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status, photo;
                            status = response.getString("status");

                            if (status.equalsIgnoreCase("1")) {
                                edt_user_name.setText(response.getString("name"));
//                                edt_father_name.setText(response.getString("father_name"));
                                edt_email.setText(response.getString("email"));
                                edt_mobile.setText(response.getString("mobile"));
                                edt_address.setText(response.getString("address"));
                                edt_txt_city.setText(response.getString("city"));
//                                edt_txt_distict.setText(response.getString("district"));
                                //get photo url
                                photo = response.getString("photo");
                                if (!photo.equalsIgnoreCase("")) {
                                    Picasso.with(ProfileActivity.this).load(photo).fit().centerCrop().into(profile_image);

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

    public void Update_profile() {
        //show process dialog
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_loader);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            RequestQueue queue = Volley.newRequestQueue(this);

            Log.e("@@profil;e_update",ServerUtils.Base_url + ServerUtils.updateProfile_url);
            StringRequest request = new StringRequest(Request.Method.POST, ServerUtils.Base_url + ServerUtils.updateProfile_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response

                            progressDialog.dismiss();

                            String status, message, str_userName, str_password;
                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                status = jsonObject.getString("status");

                                if (status.equalsIgnoreCase("1")) {

                                    message = jsonObject.getString("message");


                                    //Show Success Dialog
                                    CommonUtils.AwesomeSuccessDialog(ProfileActivity.this, message);


                                } else if (status.equalsIgnoreCase("0")) {

                                    message = jsonObject.getString("message");

                                    //Show Error Dialog
                                    CommonUtils.AwesomeErrorDialog(ProfileActivity.this, message);

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
                    params.put("name", str_user_name);
                    params.put("fname", str_father_name);
                    params.put("address", str_address);
                    params.put("profilePhoto", encodedImageData);
                    params.put("city", str_city);
//                    params.put("district", str_district);

                    return params;
                }
            };

            queue.add(request);

        } catch (Exception e) {

        }
    }

}
