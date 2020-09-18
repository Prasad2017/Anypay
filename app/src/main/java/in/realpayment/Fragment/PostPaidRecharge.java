package in.realpayment.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import in.realpayment.Activity.MainPage;
import in.realpayment.Adapter.CircleAdapter;
import in.realpayment.Adapter.ContactAdapter;
import in.realpayment.Adapter.OperatorAdapter;
import in.realpayment.Adapter.PostCircleAdapter;
import in.realpayment.Adapter.PostPaidOperatorAdapter;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.AssignResponse;
import in.realpayment.Model.BankAmountResponse;
import in.realpayment.Model.BankResponse;
import in.realpayment.Model.Operator;
import in.realpayment.Model.OperatorResponse;
import in.realpayment.Model.RechargeResponse;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;
import in.realpayment.Retrofit.ApiBank;
import in.realpayment.Retrofit.ApiMobile;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostPaidRecharge extends Fragment implements PaymentStatusListener {

    View view;
    @BindViews({R.id.customerName, R.id.customerMobile, R.id.recharge})
    List<TextView> textViews;
    public static TextView circle, operator;
    @BindView(R.id.customerImage)
    ImageView imageView;
    @BindView(R.id.amount)
    FormEditText formEditText;
    @BindView(R.id.paymentMethodsGroup)
    RadioGroup paymentMethodsGroup;
    public static Dialog dialog;
    RecyclerView recyclerView;
    TextView close;
    private List<String> rootFilters;
    private List<String> operatorIdList;
    private List<AssignResponse> assignResponseList = new ArrayList<>();
    private List<OperatorResponse> operatorResponseList = new ArrayList<>();
    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    public static String customerName, customerNumber, operatorId, operatorName, circleName, paymentMode="";
    //Payment Gateway
    Handler mHandler = new Handler(Looper.getMainLooper());
    public static String mtx, paymentToken, timeStamp;
    private static String mSecretKey = "ddb5dc2837d7784092fa60123a4ee314680ef3de";
    private static String mAccessKey = "00ab8450-bb76-11ea-973f-85c936500fe2";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_recharge_mobile, container, false);
        ButterKnife.bind(this, view);
        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        MainPage.title.setText("Postpaid Recharge");
        initViews();
        rootFilters = Arrays.asList(this.getResources().getStringArray(R.array.circle));

        for (int i = 0; i < rootFilters.size(); i++) {
            AssignResponse assignResponse = new AssignResponse();
            assignResponse.setName(rootFilters.get(i));
            assignResponseList.add(assignResponse);
        }

        try {

            textViews.get(0).setText(ContactAdapter.customerName);
            textViews.get(1).setText(ContactAdapter.customerNumber);

            String[] splitStr = ContactAdapter.customerName.trim().split("\\s+");
            String firstName, lastName = "";
            if (splitStr.length==2) {
                firstName = splitStr[0].replace(" ", "");
                lastName = splitStr[1].replace(" ", "");
            } else {
                firstName = splitStr[0].replace(" ", "");
            }

            if (lastName.isEmpty()){

                mDrawableBuilder = TextDrawable.builder().roundRect(100);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(50) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(String.valueOf(firstName.charAt(0)), mColorGenerator.getColor(firstName), 100);

                // drawable = mDrawableBuilder.build(String.valueOf(firstName.charAt(0)), mColorGenerator.getColor(firstName)).fontSize(30);
                imageView.setImageDrawable(drawable);
            } else {

                mDrawableBuilder = TextDrawable.builder().roundRect(100);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(50) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(String.valueOf(firstName.charAt(0)+" "+lastName.charAt(0)), mColorGenerator.getColor(firstName), 100);

                imageView.setImageDrawable(drawable);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

        formEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try{

                    if (formEditText.testValidity()){
                        if (formEditText.getText().toString().trim().length()>1){
                            textViews.get(2).setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                            textViews.get(2).setClickable(true);
                        } else {
                            textViews.get(2).setBackgroundColor(getActivity().getResources().getColor(R.color.grey_40));
                            textViews.get(2).setClickable(false);
                        }
                    }

                } catch (Exception e){
                    e.printStackTrace();
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

        circle = view.findViewById(R.id.circle);
        operator = view.findViewById(R.id.operator);

    }

    @OnClick({R.id.changeMobile, R.id.operator, R.id.circle, R.id.recharge})
    public void onClick(View view){
        switch (view.getId()) {

            case R.id.changeMobile:
                ((MainPage) getActivity()).removeCurrentFragmentAndMoveBack();
                ((MainPage) getActivity()).loadFragment(new PostPaidContactList(), true);
                break;

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

                        PostPaidOperatorAdapter postPaidOperatorAdapter = new PostPaidOperatorAdapter(getActivity(), operatorResponseList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(postPaidOperatorAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        postPaidOperatorAdapter.notifyDataSetChanged();
                        recyclerView.setHasFixedSize(true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.show();

                } else {



                }

                break;

            case R.id.circle:

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

                    PostCircleAdapter circleAdapter = new PostCircleAdapter(getActivity(), assignResponseList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(circleAdapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                    circleAdapter.notifyDataSetChanged();
                    recyclerView.setHasFixedSize(true);

                }catch (Exception e){
                    e.printStackTrace();
                }

                dialog.show();

                break;

            case R.id.recharge:

                Log.e("customerNumber", ""+MainPage.userId);

                if (formEditText.testValidity()) {

                    if (!operator.getText().toString().equalsIgnoreCase("")) {

                        if (!circle.getText().toString().equalsIgnoreCase("")) {

                            if (!paymentMode.equalsIgnoreCase("")) {

                                if (paymentMode.equalsIgnoreCase("wallet")) {

                                    if (Float.parseFloat(MainPage.walletBalance) > Float.parseFloat(formEditText.getText().toString())) {


                                        Call<RechargeResponse> call = Api.getClient().rechargeMobile(textViews.get(1).getText().toString(), formEditText.getText().toString().trim(), operatorId, MainPage.userId, "Postpaid");
                                        call.enqueue(new Callback<RechargeResponse>() {
                                            @Override
                                            public void onResponse(Call<RechargeResponse> call, Response<RechargeResponse> response) {

                                                Log.e("response", "" + response.body());

                                                if (response.isSuccessful()) {

                                                    if (response.body().getSuccess().booleanValue() == true) {
                                                        Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                                                        ((MainPage) getActivity()).loadFragment(new Home(), true);
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
                            Toasty.normal(getActivity(), "Select Circle", Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        Toasty.normal(getActivity(), "Select Operator", Toasty.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }
    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getOCMobile(textViews.get(1).getText().toString().trim());
            getPrepaid();
        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void getOCMobile(String mobileNumber) {

        Call<OperatorResponse> call = ApiMobile.getClient().getOCMobile(mobileNumber);
        call.enqueue(new Callback<OperatorResponse>() {
            @Override
            public void onResponse(Call<OperatorResponse> call, Response<OperatorResponse> response) {

                if (response.isSuccessful()){

                    operatorName = response.body().getOperator();
                    circleName = response.body().getCircle();

                } else {

                }

            }

            @Override
            public void onFailure(Call<OperatorResponse> call, Throwable t) {
                Log.e("operatorError", ""+t.getMessage());
            }
        });

    }

    private void getPrepaid() {

        Call<Operator> call = Api.getClient().prepaidOperator("Postpaid");
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

        Call<RechargeResponse> call = Api.getClient().upiRechargeMobile(textViews.get(1).getText().toString(), formEditText.getText().toString().trim(), operatorId, MainPage.userId, "Postpaid");
        call.enqueue(new Callback<RechargeResponse>() {
            @Override
            public void onResponse(Call<RechargeResponse> call, Response<RechargeResponse> response) {

                Log.e("response", "" + response.body());

                if (response.isSuccessful()) {

                    if (response.body().getSuccess().booleanValue() == true) {
                        progressDialog.dismiss();
                        Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                        ((MainPage) getActivity()).loadFragment(new Home(), true);
                    } else if (response.body().getSuccess().booleanValue() == false) {
                        progressDialog.dismiss();
                        Toasty.normal(getActivity(), "Recharge failed", Toasty.LENGTH_SHORT).show();
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
}