package in.realpayment.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import in.realpayment.Activity.Login;
import in.realpayment.Activity.MainPage;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.RechargeResponse;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QRCodePayment extends Fragment {

    View view;
    @BindView(R.id.amount)
    FormEditText formEditText;
    @BindView(R.id.walletBalance)
    TextView textView;
    @BindView(R.id.topUpWallet)
    TextView topUpWallet;
    @BindView(R.id.receiverName)
    TextView receiverNameTxt;
    @BindView(R.id.receiverNumber)
    TextView receiverNumberTxt;
    public static String receiverId="0", receiverName="", receiverNumber="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q_r_code_payment, container, false);
        ButterKnife.bind(this, view);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        MainPage.title.setText("");

        Bundle bundle = getArguments();
        receiverId = bundle.getString("receiverId");
        receiverName = bundle.getString("receiverName");
        receiverNumber = bundle.getString("receiverNumber").trim();

        textView.setText(""+MainPage.walletBalance);
        receiverNameTxt.setText(""+receiverName.trim().replace(" ", "")+"@rp"+receiverNumber);
        receiverNumberTxt.setText(""+receiverNumber);

        formEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {

                    if (formEditText.getText().toString().trim().length()>0){
                        topUpWallet.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        topUpWallet.setClickable(true);
                    } else {
                        topUpWallet.setBackgroundColor(getActivity().getResources().getColor(R.color.grey_40));
                        topUpWallet.setClickable(false);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    @OnClick({R.id.topUpWallet})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.topUpWallet:

                if (formEditText.testValidity()){

                    if (Float.parseFloat(MainPage.walletBalance) > Float.parseFloat(formEditText.getText().toString())) {

                        ProgressDialog progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("Loading...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);

                        Call<RechargeResponse> call = Api.getClient().qrCodePayment(MainPage.userId, receiverId, MainPage.userName, receiverName, formEditText.getText().toString());
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
                                        Toasty.normal(getActivity(), "Amount Transfer failed", Toasty.LENGTH_SHORT).show();
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

        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

}