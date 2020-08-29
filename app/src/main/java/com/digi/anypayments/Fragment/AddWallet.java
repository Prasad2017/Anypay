package com.digi.anypayments.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreabaccega.widget.FormEditText;
import com.digi.anypayments.Activity.Login;
import com.digi.anypayments.Activity.MainPage;
import com.digi.anypayments.Extra.DetectConnection;
import com.digi.anypayments.Model.BankAmountResponse;
import com.digi.anypayments.Model.BankResponse;
import com.digi.anypayments.Model.LoginResponse;
import com.digi.anypayments.Model.Profile;
import com.digi.anypayments.R;
import com.digi.anypayments.Retrofit.Api;
import com.digi.anypayments.Retrofit.ApiBank;
import com.google.android.material.snackbar.Snackbar;
import com.open.open_web_sdk.OpenPayment;
import com.open.open_web_sdk.listener.PaymentStatusListener;
import com.open.open_web_sdk.model.TransactionDetails;
import com.razorpay.PaymentResultListener;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.Crypt;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddWallet extends Fragment implements PaymentStatusListener {

    View view;
    @BindView(R.id.amount)
    FormEditText formEditText;
    Handler mHandler = new Handler(Looper.getMainLooper());
    public static String mtx, paymentToken, timeStamp;
    private static String mSecretKey = "ddb5dc2837d7784092fa60123a4ee314680ef3de";
    private static String mAccessKey = "00ab8450-bb76-11ea-973f-85c936500fe2";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_wallet, container, false);
        ButterKnife.bind(this, view);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.blue_grey_900));

        return view;

    }

    @OnClick({R.id.topUpWallet})
    public void onClick(View view){

        switch (view.getId()){

            case R.id.topUpWallet:

                if (formEditText.testValidity()){

                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                    try {
                        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                        String date = df.format(Calendar.getInstance().getTime());

                        Date mDate = sdf.parse(date);
                        long timeInMilliseconds = mDate.getTime();
                        timeStamp = String.valueOf(timeInMilliseconds/1000);
                        System.out.println("Date in milli :: " + timeInMilliseconds);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Random rnd = new Random();
                    int number = rnd.nextInt(999999);
                    mtx = String.format("%06d", number);

                    HMACKey();

                }

                break;
        }

    }

    private void topUpWallet(String amount) {

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));


    }


    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {

        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(final String s) {
      /*  getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mTextViewResponseText.setText(String.format("onError: \n%s", s));
            }
        });*/

      mHandler.post(new Runnable() {
          @Override
          public void run() {
              Toasty.normal(getActivity(), "", Toasty.LENGTH_SHORT).show();
          }
      });


    }

    @Override
    public void onTransactionCompleted(@NotNull TransactionDetails transactionDetails) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                String response = "On Transaction Completed:\n" +
                        "\n Payment Id: " + transactionDetails.paymentId +
                        "\n Payment Token Id: " + transactionDetails.paymentTokenId +
                        "\n Status: " + transactionDetails.status;

                System.out.println("Response :: " + response);

                addWallet(transactionDetails.paymentId, transactionDetails.paymentTokenId, transactionDetails.status);
                
            }
        });

    }

    private void addWallet(String paymentId, String paymentTokenId, String status) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));

        Profile profile = new Profile();
        profile.setWalletBalance(""+String.format(java.util.Locale.US,"%.2f", Float.parseFloat(formEditText.getText().toString().trim())));
        profile.setUserName(MainPage.userName);
        profile.setEmail(MainPage.userEmail);
        profile.setPassword(MainPage.password);
        profile.setMobileNo(MainPage.userNumber);
        profile.setEc("10001");
        profile.setStatus("Success");
        profile.setRemark("wallet balance update successfully");
        profile.setReqid(paymentId);

        Call<LoginResponse> call = Api.getClient().addWallet(stringStringMap, MainPage.userId, profile);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()){

                    Log.e("response", ""+response.body());

                    if (response.body().getSuccess().booleanValue()==true){
                        progressDialog.dismiss();
                        ((MainPage) getActivity()).loadFragment(new Home(), false);
                    } else {
                        progressDialog.dismiss();
                        ((MainPage) getActivity()).loadFragment(new Home(), false);
                    }

                } else {
                    progressDialog.dismiss();
                    ((MainPage) getActivity()).loadFragment(new Home(), false);

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("payemntError", ""+t.getMessage());
            }
        });

    }

    public void HMACKey(){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "Bearer "+ mAccessKey +":"+mSecretKey);
        stringStringMap.put("X-O-Timestamp", timeStamp);

        BankAmountResponse bankAmountResponse = new BankAmountResponse();
        bankAmountResponse.setAmount(""+String.format(java.util.Locale.US,"%.2f", Float.parseFloat(formEditText.getText().toString().trim())));
        bankAmountResponse.setMtx(mtx);
        bankAmountResponse.setCurrency("INR");
        bankAmountResponse.setContactNumber(MainPage.userNumber);
        bankAmountResponse.setEmailId(MainPage.userEmail);

        Call<BankResponse> call = ApiBank.getClient().getToken(stringStringMap, bankAmountResponse);
        call.enqueue(new Callback<BankResponse>() {
            @Override
            public void onResponse(Call<BankResponse> call, Response<BankResponse> response) {

                try {

                    if (response.isSuccessful()){
                        progressDialog.dismiss();
                        paymentToken = response.body().getId();
                        Log.e("paymentToken", ""+paymentToken);
                        if (paymentToken!=null) {
                            progressDialog.dismiss();
                            setPaymentView(paymentToken, mAccessKey);
                        } else {
                            progressDialog.dismiss();
                            Toasty.normal(getActivity(), "Token is invalid", Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        paymentToken = "";
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<BankResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("tokenError", ""+t.getMessage());
            }
        });

    }

    private void setPaymentView(String paymentToken, String accessKey) {
        OpenPayment openPayment = new OpenPayment.Builder()
                .with(getActivity())
                .setPaymentToken(paymentToken)
                .setAccessKey(accessKey)
                .setEnvironment(OpenPayment.Environment.LIVE)
                .build();

        openPayment.setPaymentStatusListener(this);

        openPayment.startPayment();
    }

    public static class HMAC {

        public static byte[] calcHmacSha256(byte[] secretKey, byte[] message) {

            byte[] hmacSha256 = null;
            try {

                Mac mac = Mac.getInstance("HmacSHA256");
                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
                mac.init(secretKeySpec);
                hmacSha256 = mac.doFinal(message);

            } catch (Exception e) {
                throw new RuntimeException("Failed to calculate hmac-sha256", e);
            }
            return hmacSha256;
        }
    }
}