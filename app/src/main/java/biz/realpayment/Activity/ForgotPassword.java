package biz.realpayment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import biz.realpayment.Extra.DetectConnection;
import biz.realpayment.Model.LoginResponse;
import biz.realpayment.R;
import biz.realpayment.Retrofit.Api;
import biz.realpayment.helper.AppSignatureHelper;
import biz.realpayment.receiver.SmsBroadcastReceiver;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.mobileNumber)
    FormEditText formEditText;
    @BindViews({R.id.sendotpLayout, R.id.verifyotpLayout})
    List<LinearLayout> linearLayouts;
    @BindView(R.id.forgotLayout)
    LinearLayout forgotLayout;
    @BindView(R.id.otpView)
    OtpView otpView;
    GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    private String HASH_KEY, OTP, otpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        //Permission's .....
        requestPermission();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        forgotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                try {
                    otpCode = otp;
                    int length = otpCode.trim().length();
                    if (length == 4) {

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @OnClick({R.id.submit, R.id.verify, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:

                Intent intent = new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
                finishAffinity();

                break;

            case R.id.submit:

                if (formEditText.testValidity()) {
                    if (DetectConnection.checkInternetConnection(ForgotPassword.this)) {
                        SendOTP(formEditText.getText().toString().trim());
                    } else {
                        Toasty.warning(ForgotPassword.this, "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
                    }
                }

                break;

            case R.id.verify:

                if (otpCode != null) {
                    if (otpCode.equalsIgnoreCase(OTP)) {
                        Success();
                    } else {
                        Toast.makeText(ForgotPassword.this, "OTP Not Match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ForgotPassword.this, "Please Enter OTP", Toast.LENGTH_LONG).show();
                }

                break;

        }
    }

    private void SendOTP(String mobileNumber) {

        ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Mobile number is checking");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<LoginResponse> call = Api.getClient().checkMobileNumber(mobileNumber);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().booleanValue()==true){
                    progressDialog.dismiss();
                    if (DetectConnection.checkInternetConnection(ForgotPassword.this)) {
                        sendOTP(mobileNumber);
                    } else {
                        Toasty.warning(ForgotPassword.this, "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
                    }

                } else if (response.body().getSuccess().booleanValue()==false){
                    progressDialog.dismiss();
                    Toasty.error(ForgotPassword.this, "Please enter registered number").show();
                    linearLayouts.get(0).setVisibility(View.VISIBLE);
                    linearLayouts.get(1).setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("MobileNumberError", ""+t.getMessage());
                linearLayouts.get(0).setVisibility(View.VISIBLE);
                linearLayouts.get(1).setVisibility(View.GONE);
            }
        });

    }

    private void sendOTP(String mobileNumber) {

        ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("OTP is sending");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        HASH_KEY = (String) new AppSignatureHelper(this).getAppSignatures().get(0);
        HASH_KEY = HASH_KEY.replace("+", "%252B");
        OTP= new DecimalFormat("000000").format(new Random().nextInt(999999));
        String message = "<#> Your Realpayment verification OTP code is "+ OTP +". Please DO NOT share this OTP with anyone.\n" + HASH_KEY;
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "MYRPAY";

        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;
        //encoding message
        String encoded_message=URLEncoder.encode(message);
        //Send SMS API
        String mainUrl="http://103.10.234.154/vendorsms/pushsms.aspx?";
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("user=ANYPAY");
        sbPostData.append("&password=realpay");
        sbPostData.append("&msisdn="+mobileNumber);
        sbPostData.append("&sid="+senderId);
        sbPostData.append("&msg="+message);
        sbPostData.append("&fl=0");
        sbPostData.append("&gwid=2");
        //final string
        mainUrl = sbPostData.toString();
        Log.e("url",""+mainUrl);
        try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response
            String response;
            while ((response = reader.readLine()) != null) {
                //print response
                Log.d("RESPONSE", "" + response.substring(response.indexOf("=") + 1));

                response = response.substring(response.indexOf("=") + 1);


                if (response.equalsIgnoreCase("SUCCESS")) {
                    progressDialog.dismiss();

                        otpView.setVisibility(View.GONE);
                        linearLayouts.get(1).setVisibility(View.VISIBLE);
                        linearLayouts.get(0).setVisibility(View.GONE);

                        Toasty.normal(ForgotPassword.this, "Something went wrong").show();

                    } else {

                    progressDialog.dismiss();

                    otpView.setVisibility(View.GONE);
                    linearLayouts.get(0).setVisibility(View.VISIBLE);
                    linearLayouts.get(1).setVisibility(View.GONE);

                    Toasty.normal(ForgotPassword.this, "Something went wrong").show();
                }

            }

            //finally close connection
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void Success() {

        ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Verifying OTP");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        Intent intent = new Intent(ForgotPassword.this, ChangePassword.class);
        intent.putExtra("OTP", OTP);
        intent.putExtra("mobileNumber", formEditText.getText().toString());
        startActivity(intent);

        progressDialog.dismiss();


    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

    }

    private void showSettingsDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ForgotPassword.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ForgotPassword.this, Login.class);
        startActivity(intent);
        finishAffinity();

    }


}