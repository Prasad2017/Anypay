package biz.realpayment.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import biz.realpayment.R;

import butterknife.ButterKnife;


public class ConfirmationAnyOrder extends Activity {



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_confirmation_any_order);
        ButterKnife.bind(this);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ConfirmationAnyOrder.this, MainPage.class);
        startActivity(intent);
        finishAffinity();

    }
}