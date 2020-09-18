package in.realpayment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.ChangePass;
import in.realpayment.Model.LoginResponse;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

    @BindViews({R.id.password, R.id.confirmPassword})
    List<FormEditText> formEditTexts;
    String OTP, mobileNumber;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        formEditTexts.get(0).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        formEditTexts.get(1).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        formEditTexts.get(0).setSelection(formEditTexts.get(0).getText().toString().length());
        formEditTexts.get(1).setSelection(formEditTexts.get(1).getText().toString().length());

        try {
            Intent intent = getIntent();
            OTP = intent.getStringExtra("OTP");
            mobileNumber = intent.getStringExtra("mobileNumber");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.back, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:

                Intent intent = new Intent(ChangePassword.this, Login.class);
                startActivity(intent);
                finishAffinity();

                break;

            case R.id.submit:

                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity()) {
                    if (validatePassword(formEditTexts.get(0)) && validatePassword(formEditTexts.get(1))) {
                        if (formEditTexts.get(1).getText().toString().equals(formEditTexts.get(0).getText().toString())) {
                            if (DetectConnection.checkInternetConnection(ChangePassword.this)) {
                                changePassword(formEditTexts.get(0).getText().toString());
                            } else {
                                Toasty.warning(ChangePassword.this, "No Internet Connection", Toasty.LENGTH_SHORT, true).show();
                            }
                        } else {
                            Toast.makeText(ChangePassword.this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;
        }
    }

    private void changePassword(String password) {

        ProgressDialog progressDialog = new ProgressDialog(ChangePassword.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Changing Password");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        ChangePass changePass = new ChangePass();
        changePass.setPassword(password);
        changePass.setConfirmPassword(password);
        changePass.setMobileNo(mobileNumber);

        Call<LoginResponse> call = Api.getClient().changePassword(changePass);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();
                Log.e("Response", ""+response.body());

                   /* if (response.body().getSuccess().booleanValue()==true) {
                        Toast.makeText(ChangePassword.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePassword.this, Login.class);
                        startActivity(intent);
                        finishAffinity();
                    } else if (response.body().getSuccess().booleanValue()==false) {
                        Toast.makeText(ChangePassword.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }*/

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("PassError", "" + t.getMessage());
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ChangePassword.this, Login.class);
        startActivity(intent);
        finishAffinity();

    }

    private boolean validatePassword(FormEditText editText) {
        if (editText.getText().toString().trim().length() > 7) {
            return true;
        } else if (editText.getText().toString().trim().length() > 0) {
            editText.setError("Password must be of 8 characters");
            editText.requestFocus();
            return false;
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }
}