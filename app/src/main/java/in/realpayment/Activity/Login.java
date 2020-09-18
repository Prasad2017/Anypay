package in.realpayment.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import in.realpayment.Extra.Common;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.CustomerResponse;
import in.realpayment.Model.LoginResponse;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;
import in.realpayment.helper.AppSignatureHelper;
import in.realpayment.interfaces.OtpReceivedInterface;
import in.realpayment.receiver.SmsBroadcastReceiver;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.io.BufferedReader;
import java.io.File;
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

import android.provider.Settings.Secure;


public class Login extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {

    @BindViews({R.id.fullName, R.id.mobileNumber, R.id.email, R.id.password, R.id.confirmPassword, R.id.referenceId})
    List<FormEditText> formEditTexts;
    @BindViews({R.id.nameLayout, R.id.mobileLayout, R.id.emailLayout, R.id.passwordLayout, R.id.confirmPasswordLayout, R.id.referenceLayout})
    List<CardView> cardViews;
    @BindView(R.id.signUp)
    TextView textView;
    @BindView(R.id.forgotPassword)
    TextView forgotPassword;
    @BindView(R.id.otpView)
    OtpView otpView;
    GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    private String HASH_KEY, OTP, otpCode, referCode, androidId;
    SharedPreferences pref;
    SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        formEditTexts.get(0).setSelection(formEditTexts.get(0).getText().toString().length());
        formEditTexts.get(1).setSelection(formEditTexts.get(1).getText().toString().length());
        formEditTexts.get(2).setSelection(formEditTexts.get(2).getText().toString().length());
        formEditTexts.get(3).setSelection(formEditTexts.get(3).getText().toString().length());
        formEditTexts.get(4).setSelection(formEditTexts.get(4).getText().toString().length());
        formEditTexts.get(5).setSelection(formEditTexts.get(5).getText().toString().length());

        formEditTexts.get(0).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        formEditTexts.get(3).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        formEditTexts.get(4).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        // init broadcast receiver
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();

        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);

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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        File file = new File("data/data/in.realpayment/shared_prefs/user.xml");
        if (file.exists()) {
            Intent intent = new Intent(Login.this, MainPage.class);
            startActivity(intent);
            finish();
        }

        try{

            referCode = Common.getSavedUserData(Login.this, "referCode");
            formEditTexts.get(5).setText(""+referCode);

            androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        } catch (Exception e){
            e.printStackTrace();
        }

        androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

    }


    @OnClick({R.id.login_button_card_view, R.id.forgotPassword})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.forgotPassword:
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                break;


            case R.id.login_button_card_view:
                if (DetectConnection.checkInternetConnection(Login.this)) {

                    if (textView.getText().toString().equalsIgnoreCase("Send OTP")) {
                        if (formEditTexts.get(1).testValidity()) {
                            CheckMobile(formEditTexts.get(1).getText().toString().trim());
                        }
                    } else if (textView.getText().toString().equalsIgnoreCase("Verify")) {
                        if (otpCode.equalsIgnoreCase(OTP)) {

                            cardViews.get(0).setVisibility(View.VISIBLE);
                            cardViews.get(2).setVisibility(View.VISIBLE);
                            cardViews.get(3).setVisibility(View.VISIBLE);
                            cardViews.get(4).setVisibility(View.VISIBLE);
                            cardViews.get(5).setVisibility(View.VISIBLE);
                            otpView.setVisibility(View.GONE);
                            textView.setText("Sign Up");
                        } else {
                            Toasty.error(Login.this, "Enter valid otp", Toasty.LENGTH_SHORT).show();
                            cardViews.get(0).setVisibility(View.GONE);
                            cardViews.get(2).setVisibility(View.GONE);
                            cardViews.get(3).setVisibility(View.GONE);
                            cardViews.get(4).setVisibility(View.GONE);
                            cardViews.get(5).setVisibility(View.GONE);
                            otpView.setVisibility(View.VISIBLE);
                        }
                    } else if (textView.getText().toString().equalsIgnoreCase("Login")) {
                        if (formEditTexts.get(1).testValidity() && formEditTexts.get(3).testValidity()) {
                            login(formEditTexts.get(1).getText().toString(), formEditTexts.get(3).getText().toString());
                        }
                    } else if (textView.getText().toString().equalsIgnoreCase("Sign Up")) {
                        if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity()
                                && formEditTexts.get(3).testValidity() && formEditTexts.get(4).testValidity()) {
                            if (formEditTexts.get(4).getText().toString().equalsIgnoreCase(formEditTexts.get(3).getText().toString())) {
                                registration(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString(), formEditTexts.get(2).getText().toString(), formEditTexts.get(3).getText().toString(), formEditTexts.get(5).getText().toString());
                            } else {
                                formEditTexts.get(5).setError("Password not matched");
                                formEditTexts.get(5).requestFocus();
                            }
                        }
                    }
                } else {
                    Toasty.warning(Login.this, "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
                }

                break;
        }
    }

    private void registration(String fullName, String mobileNumber, String email, String password, String referenceId) {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Account is creating");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        String android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setUserName(fullName);
        customerResponse.setMobileNo(mobileNumber);
        customerResponse.setEmail(email);
        customerResponse.setPassword(password);
        customerResponse.setMacAddress(androidId);

        Call<LoginResponse> call = Api.getClient().Registration(customerResponse);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().booleanValue()==true){
                    progressDialog.dismiss();
                    Toasty.success(Login.this, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                    cardViews.get(0).setVisibility(View.GONE);
                    cardViews.get(2).setVisibility(View.GONE);
                    cardViews.get(4).setVisibility(View.GONE);
                    cardViews.get(3).setVisibility(View.VISIBLE);
                    cardViews.get(5).setVisibility(View.GONE);

                    textView.setText("Login");
                } else if (response.body().getSuccess().booleanValue()==false){
                    progressDialog.dismiss();
                    Toasty.error(Login.this, ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("MobileNumber", ""+t.getMessage());
            }
        });

    }

    private void CheckMobile(String mobileNumber) {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
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
                    cardViews.get(0).setVisibility(View.GONE);
                    cardViews.get(2).setVisibility(View.GONE);
                    cardViews.get(4).setVisibility(View.GONE);
                    cardViews.get(3).setVisibility(View.VISIBLE);
                    forgotPassword.setVisibility(View.VISIBLE);
                    cardViews.get(5).setVisibility(View.GONE);
                    textView.setText("Login");
                } else if (response.body().getSuccess().booleanValue()==false){
                    progressDialog.dismiss();
                    if (DetectConnection.checkInternetConnection(Login.this)) {
                        sendOTP(mobileNumber);
                    } else {
                        Toasty.warning(Login.this, "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("MobileNumberError", ""+t.getMessage());
            }
        });

    }

    private void sendOTP(String mobileNumber) {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("OTP is sending");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        HASH_KEY = (String) new AppSignatureHelper(this).getAppSignatures().get(0);
        HASH_KEY = HASH_KEY.replace("+", "%252B");
        OTP= new DecimalFormat("000000").format(new Random().nextInt(999999));
        String message = "Your Realpayment verification OTP code is "+ OTP +". Please DO NOT share this OTP with anyone.\n" + HASH_KEY;
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
                Log.d("RESPONSE", "" + response.substring(0,7));

                response = response.substring(0,7);

                if (response.equalsIgnoreCase("SUCCESS")) {

                    progressDialog.dismiss();

                    otpView.setVisibility(View.VISIBLE);

                    cardViews.get(1).setClickable(false);
                    cardViews.get(1).setFocusable(false);

                    cardViews.get(0).setVisibility(View.GONE);
                    cardViews.get(2).setVisibility(View.GONE);
                    cardViews.get(3).setVisibility(View.GONE);
                    cardViews.get(4).setVisibility(View.GONE);
                    cardViews.get(5).setVisibility(View.GONE);
                    forgotPassword.setVisibility(View.GONE);

                    startSMSListener();
                    textView.setText("Verify");

                } else {

                    progressDialog.dismiss();

                    otpView.setVisibility(View.GONE);

                    cardViews.get(0).setVisibility(View.GONE);
                    cardViews.get(2).setVisibility(View.GONE);
                    cardViews.get(3).setVisibility(View.GONE);
                    cardViews.get(4).setVisibility(View.GONE);
                    cardViews.get(5).setVisibility(View.GONE);
                    forgotPassword.setVisibility(View.GONE);

                }
            }

            //finally close connection
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



    }

    private void login(String mobileNumber, String password) {

        ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Account is in verification");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<JsonObject> call = Api.getClient().Login(mobileNumber, password, "password");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();

                Log.e("response", "=" + response.body());

                if (response.body() != null) {
                    if (response.body().has("access_token")) {
                        progressDialog.dismiss();

                        pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("UserLogin", "UserLoginSuccessful");
                        editor.commit();

                        Common.saveUserData(Login.this, "accessToken", response.body().get("access_token").toString().replace("\"", ""));

                        Intent intent = new Intent(Login.this, MainPage.class);
                        startActivity(intent);
                        finishAffinity();

                    } else if (response.body().has("error")) {
                        progressDialog.dismiss();
                        Toasty.error(Login.this, "Login Failed", Toasty.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toasty.error(Login.this, "Login Failed", Toasty.LENGTH_SHORT).show();
                    }

                } else {

                    progressDialog.dismiss();
                    Log.e("TAG", "" + response.body());
                    Toast.makeText(Login.this, "Login Failed...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                progressDialog.dismiss();
                Log.e("TAG", "Server Error");
            }
        });

    }

    public void startSMSListener() {

        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {

                cardViews.get(1).setClickable(false);
                cardViews.get(1).setFocusable(false);

                cardViews.get(0).setVisibility(View.GONE);
                cardViews.get(2).setVisibility(View.GONE);
                cardViews.get(3).setVisibility(View.GONE);
                cardViews.get(4).setVisibility(View.GONE);
                cardViews.get(5).setVisibility(View.GONE);
                forgotPassword.setVisibility(View.GONE);
                textView.setText("Verify");

            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onOtpReceived(String otp) {
        otpView.setText(otp);
    }

    @Override
    public void onOtpTimeout() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}