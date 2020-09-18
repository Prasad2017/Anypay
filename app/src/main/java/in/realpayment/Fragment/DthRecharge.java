package in.realpayment.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.andreabaccega.widget.FormEditText;
import com.google.android.material.snackbar.Snackbar;
import com.open.open_web_sdk.OpenPayment;
import com.open.open_web_sdk.listener.PaymentStatusListener;
import com.open.open_web_sdk.model.TransactionDetails;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import in.realpayment.Activity.MainPage;
import in.realpayment.Adapter.DTHOperatorAdapter;
import in.realpayment.Adapter.OperatorAdapter;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.BankAmountResponse;
import in.realpayment.Model.BankResponse;
import in.realpayment.Model.Operator;
import in.realpayment.Model.OperatorResponse;
import in.realpayment.Model.RechargeResponse;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;
import in.realpayment.Retrofit.ApiBank;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DthRecharge extends Fragment implements PaymentStatusListener {


    View view;
    @BindView(R.id.customerId)
    FormEditText customerId;
    @BindView(R.id.amount)
    FormEditText amount;
    @BindView(R.id.recharge)
    TextView recharge;
    @BindView(R.id.paymentMethodsGroup)
    RadioGroup paymentMethodsGroup;
    public static TextView operator;
    private List<OperatorResponse> operatorResponseList = new ArrayList<>();
    public static String operatorId, operatorName;
    //Payment Gateway
    Handler mHandler = new Handler(Looper.getMainLooper());
    public static String mtx, paymentToken, timeStamp, paymentMode="";
    private static String mSecretKey = "ddb5dc2837d7784092fa60123a4ee314680ef3de";
    private static String mAccessKey = "00ab8450-bb76-11ea-973f-85c936500fe2";
    //OperatorList
    public static Dialog dialog;
    RecyclerView recyclerView;
    TextView close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dth_recharge, container, false);
        ButterKnife.bind(this, view);

        initViews();

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        MainPage.title.setText("DTH Recharge");


        customerId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (customerId.getText().toString().trim().length()>0){
                    if (amount.getText().toString().trim().length()>0){
                        recharge.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        recharge.setClickable(true);
                    } else {
                        recharge.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_40));
                        recharge.setClickable(false);
                    }

                } else {
                    recharge.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_40));
                    recharge.setClickable(false);
                }


            }
        });

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (customerId.getText().toString().trim().length()>0){
                    if (amount.getText().toString().trim().length()>0){
                        recharge.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        recharge.setClickable(true);
                    } else {
                        recharge.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_40));
                        recharge.setClickable(false);
                    }
                } else {
                    recharge.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_40));
                    recharge.setClickable(false);
                }

            }
        });

        paymentMethodsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                    if (rb.getText().toString().equalsIgnoreCase("   Pay Using Debit Card/Net Banking/UPI")){
                        paymentMode = "upi";
                    } else {
                        paymentMode = "wallet";
                    }

                }

            }
        });

        return view;

    }

    private void initViews() {

        operator = view.findViewById(R.id.operator);
    }

    @OnClick({R.id.recharge, R.id.operator})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.operator:

                if (operatorResponseList.size()>0) {

                    dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.circle_list_item);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    recyclerView = dialog.findViewById(R.id.recyclerView);
                    close = dialog.findViewById(R.id.close);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });

                    try {

                        DTHOperatorAdapter dthOperatorAdapter = new DTHOperatorAdapter(getActivity(), operatorResponseList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(dthOperatorAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        dthOperatorAdapter.notifyDataSetChanged();
                        recyclerView.setHasFixedSize(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.show();

                } else {



                }

                break;

            case R.id.recharge:

                if(customerId.testValidity() && amount.testValidity()){

                    Log.e("customerNumber", ""+MainPage.userId);

                    if (!operator.getText().toString().equalsIgnoreCase("")) {

                            if (!paymentMode.equalsIgnoreCase("")) {

                                if (paymentMode.equalsIgnoreCase("wallet")) {

                                    if (Float.parseFloat(MainPage.walletBalance) > Float.parseFloat(amount.getText().toString())) {


                                        Call<RechargeResponse> call = Api.getClient().rechargeMobile(customerId.getText().toString().trim(), amount.getText().toString().trim(), operatorId, MainPage.userId, "DTH");
                                        call.enqueue(new Callback<RechargeResponse>() {
                                            @Override
                                            public void onResponse(Call<RechargeResponse> call, Response<RechargeResponse> response) {

                                                Log.e("response", "" + response.body());

                                                if (response.isSuccessful()) {

                                                    if (response.body().getSuccess().booleanValue() == true) {
                                                        Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                                        ((MainPage) getActivity()).loadFragment(new Home(), false);
                                                    } else if (response.body().getSuccess().booleanValue() == false) {
                                                        Toasty.normal(getActivity(), "Recharge failed", Toasty.LENGTH_SHORT).show();
                                                    }

                                                } else {

                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<RechargeResponse> call, Throwable t) {
                                                Log.e("rechargeError", "" + t.getMessage());
                                            }
                                        });

                                    } else {
                                        ((MainPage) getActivity()).loadFragment(new AddWallet(), true);
                                        Toasty.error(getActivity(), "Your wallet balance is low", Toasty.LENGTH_SHORT, true).show();
                                    }
                                } else {
                                    if (!MainPage.userNumber.equalsIgnoreCase("") || !MainPage.userEmail.equalsIgnoreCase("")) {

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
                                    } else {
                                        Toasty.normal(getActivity(), "Update Your Profile", Toasty.LENGTH_SHORT).show();
                                    }
                                }

                            } else {
                                Toasty.normal(getActivity(), "Select Payment Method", Toasty.LENGTH_SHORT).show();
                            }
                    } else {
                        Toasty.normal(getActivity(), "Select Operator", Toasty.LENGTH_SHORT).show();
                    }

                }

                break;

        }
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
        bankAmountResponse.setAmount(""+String.format(java.util.Locale.US,"%.2f", Float.parseFloat(amount.getText().toString().trim())));
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
                Toasty.normal(getActivity(), ""+String.format("onError: \n%s", s), Toasty.LENGTH_SHORT).show();
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

                if (transactionDetails.status.equalsIgnoreCase("cancelled")){
                    Toasty.normal(getActivity(), "payment canceled", Toasty.LENGTH_SHORT).show();
                } else {
                    rechargeMobile();
                }
            }
        });

    }

    private void rechargeMobile() {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Call<RechargeResponse> call = Api.getClient().upiRechargeMobile(customerId.getText().toString(), amount.getText().toString().trim(), operatorId, MainPage.userId, "DTH");
        call.enqueue(new Callback<RechargeResponse>() {
            @Override
            public void onResponse(Call<RechargeResponse> call, Response<RechargeResponse> response) {

                Log.e("response", "" + response.body());

                if (response.isSuccessful()) {

                    if (response.body().getSuccess().booleanValue() == true) {
                        progressDialog.dismiss();
                        Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                        ((MainPage) getActivity()).loadFragment(new Home(), false);
                    } else if (response.body().getSuccess().booleanValue() == false) {
                        progressDialog.dismiss();
                        Toasty.success(getActivity(), "Recharge failed", Toasty.LENGTH_SHORT, true).show();
                    }

                } else {
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<RechargeResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("rechargeError", "" + t.getMessage());
            }
        });

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getPrepaid();
        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void getPrepaid() {

        Call<Operator> call = Api.getClient().prepaidOperator("DTH");
        call.enqueue(new Callback<Operator>() {
            @Override
            public void onResponse(Call<Operator> call, Response<Operator> response) {

                if (response.isSuccessful()){

                    operatorResponseList = response.body().getData();

                    if (operatorResponseList!=null){

                        for (int i=0;i<operatorResponseList.size();i++){

                            if (operatorResponseList.get(i).getOprator().equalsIgnoreCase(operatorName)) {
                                operator.setText(operatorResponseList.get(i).getOprator());
                                operatorId = operatorResponseList.get(i).getOpratorcode();
                            }

                        }


                    } else {

                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<Operator> call, Throwable t) {
                Log.e("OpratorError", ""+t.getMessage());
            }
        });


    }
}