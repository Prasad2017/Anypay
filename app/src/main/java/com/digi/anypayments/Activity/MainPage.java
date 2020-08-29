package com.digi.anypayments.Activity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.digi.anypayments.Extra.Common;
import com.digi.anypayments.Fragment.AddWallet;
import com.digi.anypayments.Fragment.Home;
import com.digi.anypayments.Fragment.MyAccount;
import com.digi.anypayments.Fragment.MyWalletBalance;
import com.digi.anypayments.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class MainPage extends AppCompatActivity {

    public static ImageView menu, back;
    public static DrawerLayout drawerLayout;
    public static TextView title, userNameTxt, userMobileNumberTxt;
    public static LinearLayout toolbarContainer;
    public static RelativeLayout titleLayout;
    public static String userId, cartId, currency = "₹", accessToken, userName, userNumber, userEmail, walletBalance, password;
    boolean doubleBackToExitPressedOnce = false;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    public static BottomNavigationView bottomNavigationView;
    public static ProgressBar progressBar;
    InputMethodManager in;

    
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbarContainer = findViewById(R.id.toolbar_container);
        initViews();

        loadFragment(new Home(), false);

        userId = Common.getSavedUserData(MainPage.this, "userId");
        accessToken = Common.getSavedUserData(MainPage.this, "accessToken");
        userName = Common.getSavedUserData(MainPage.this, "userName");
        userNumber = Common.getSavedUserData(MainPage.this, "userNumber");
        userEmail = Common.getSavedUserData(MainPage.this, "userEmail");
        walletBalance = Common.getSavedUserData(MainPage.this, "walletBalance");
        password = Common.getSavedUserData(MainPage.this, "password");

        View header = navigationView.getHeaderView(0);
        userNameTxt = (TextView) header.findViewById(R.id.userName);
        userMobileNumberTxt = (TextView) header.findViewById(R.id.userMobileNumber);

        userNameTxt.setText(userName);
        userMobileNumberTxt.setText(userNumber);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        loadFragment(new Home(), false);
                        break;

                    case R.id.mymoney:
                        loadFragment(new MyWalletBalance(), true);
                        break;

                }

                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        loadFragment(new Home(), false);
                        break;

                    case R.id.mymoney:
                        loadFragment(new AddWallet(), true);
                        break;

                    case R.id.history:
                        break;

                    case R.id.account:
                        loadFragment(new MyAccount(), true);
                        break;

                }
                return false;
            }
        });



    }

    @SuppressLint("CutPasteId")
    private void initViews() {

        drawerLayout = findViewById(R.id.drawer_layout);
        title = findViewById(R.id.title);
        titleLayout = findViewById(R.id.titleLayout);
        menu = (ImageView) findViewById(R.id.menu);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        back = findViewById(R.id.back);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

    }

    @SuppressLint("RtlHardcoded")
    @OnClick({R.id.menu, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.menu:
                in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (!MainPage.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    MainPage.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;

            case R.id.back:
                in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                removeCurrentFragmentAndMoveBack();
                break;

        }
    }


    @Override
    public void onBackPressed() {
        // double press to exit
        if (menu.getVisibility() == View.VISIBLE) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
        } else {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toasty.normal(MainPage.this, "Press back once more to exit", Toasty.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    public void lockUnlockDrawer(int lockMode) {

        drawerLayout.setDrawerLockMode(lockMode);
        if (lockMode == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            menu.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        } else {
            menu.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
        }

    }

    public void removeCurrentFragmentAndMoveBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private void logout() {

        final Dialog dialog = new Dialog(MainPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView txtyes = dialog.findViewById(R.id.yes);
        TextView txtno = dialog.findViewById(R.id.no);

        txtno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Common.saveUserData(MainPage.this, "userId", "");
                File file1 = new File("data/data/com.digi.anypayments/shared_prefs/user.xml");
                if (file1.exists()) {
                    file1.delete();
                }

                Intent intent = new Intent(MainPage.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        dialog.show();

    }

}