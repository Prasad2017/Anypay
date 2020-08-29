package com.digi.anypayments.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.andreabaccega.widget.FormEditText;
import com.digi.anypayments.Activity.ConfirmationAnyOrder;
import com.digi.anypayments.Activity.MainPage;
import com.digi.anypayments.Adapter.CircleAdapter;
import com.digi.anypayments.Adapter.ContactAdapter;
import com.digi.anypayments.Adapter.OperatorAdapter;
import com.digi.anypayments.Extra.DetectConnection;
import com.digi.anypayments.Model.AssignResponse;
import com.digi.anypayments.Model.Operator;
import com.digi.anypayments.Model.OperatorResponse;
import com.digi.anypayments.Model.RechargeResponse;
import com.digi.anypayments.R;
import com.digi.anypayments.Retrofit.Api;
import com.digi.anypayments.Retrofit.ApiMobile;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRechargeMobile extends Fragment {

    View view;
    @BindViews({R.id.customerName, R.id.customerMobile, R.id.recharge})
    List<TextView> textViews;
    public static TextView circle, operator;
    @BindView(R.id.customerImage)
    ImageView imageView;
    @BindView(R.id.amount)
    FormEditText formEditText;
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
    public static String customerName, customerNumber, operatorId, operatorName, circleName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_recharge_mobile, container, false);
        ButterKnife.bind(this, view);
        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));

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
                ((MainPage) getActivity()).loadFragment(new CustomerContactList(), true);
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

                        OperatorAdapter operatorAdapter = new OperatorAdapter(getActivity(), operatorResponseList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(operatorAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        operatorAdapter.notifyDataSetChanged();
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

                    CircleAdapter circleAdapter = new CircleAdapter(getActivity(), assignResponseList);
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


                    if (Float.parseFloat(MainPage.walletBalance) > Float.parseFloat(formEditText.getText().toString())) {


                        Call<RechargeResponse> call = Api.getClient().rechargeMobile(textViews.get(1).getText().toString(), formEditText.getText().toString().trim(), operatorId, MainPage.userId);
                        call.enqueue(new Callback<RechargeResponse>() {
                            @Override
                            public void onResponse(Call<RechargeResponse> call, Response<RechargeResponse> response) {

                                Log.e("response", ""+response.body());

                                if (response.isSuccessful()) {

                                    if (response.body().getSuccess().booleanValue() == true) {

                                    /*    Intent intent = new Intent(getActivity(), ConfirmationAnyOrder.class);
                                        startActivity(intent);*/

                                    Toasty.success(getActivity(), "Recharge is in process", Toasty.LENGTH_SHORT,true).show();

                                    } else if (response.body().getSuccess().booleanValue() == false) {
                                        Toasty.success(getActivity(), "Recharge failed", Toasty.LENGTH_SHORT,true).show();
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

        Call<Operator> call = Api.getClient().prepaidOperator("Prepaid");
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