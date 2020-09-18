package in.realpayment.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.realpayment.Activity.Login;
import in.realpayment.Activity.MainPage;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.LoginResponse;
import in.realpayment.Model.Profile;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfile extends Fragment {


    View view;
    @BindViews({R.id.fullName, R.id.mobileNumber, R.id.email})
    List<FormEditText> formEditTexts;
    @BindView(R.id.update)
    TextView textView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);

        formEditTexts.get(0).setSelection(formEditTexts.get(0).getText().toString().length());
        formEditTexts.get(1).setSelection(formEditTexts.get(1).getText().toString().length());
        formEditTexts.get(2).setSelection(formEditTexts.get(2).getText().toString().length());

        formEditTexts.get(0).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        try {

            formEditTexts.get(0).setText(MainPage.userName);
            formEditTexts.get(1).setText(MainPage.userNumber);
            formEditTexts.get(2).setText(MainPage.userEmail);

        } catch (Exception e){
            e.printStackTrace();
        }

        return view;

    }

    @OnClick({R.id.login_button_card_view})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_button_card_view:

                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity()){

                    updateProfile(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString().trim(), formEditTexts.get(2).getText().toString());

                }

                break;
        }

    }

    private void updateProfile(String userName, String mobileNumber, String emailId) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Profile is updating");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));

        Profile profile = new Profile();
        profile.setWalletBalance("0");
        profile.setUserName(formEditTexts.get(0).getText().toString());
        profile.setEmail(formEditTexts.get(2).getText().toString());
        profile.setPassword(MainPage.password);
        profile.setMobileNo(formEditTexts.get(1).getText().toString());
        profile.setCategory("profile");

        Log.e("userId", ""+MainPage.userId);
        Call<LoginResponse> call = Api.getClient().updateProfile(stringStringMap, MainPage.userId, profile);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.body().getSuccess().booleanValue() == true){
                    progressDialog.dismiss();
                    Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                } else  if (response.body().getSuccess().booleanValue() == false){
                    progressDialog.dismiss();
                    Toasty.normal(getActivity(), ""+response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("profileError", ""+t.getMessage());
            }
        });

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