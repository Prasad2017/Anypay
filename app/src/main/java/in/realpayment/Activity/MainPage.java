package in.realpayment.Activity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import in.realpayment.Extra.Common;
import in.realpayment.Fragment.AddWallet;
import in.realpayment.Fragment.Home;
import in.realpayment.Fragment.MyAccount;
import in.realpayment.Fragment.MyHistory;
import in.realpayment.Fragment.MyWalletBalance;
import in.realpayment.Fragment.QRCodePayment;
import in.realpayment.Fragment.QrCodeScan;
import in.realpayment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class MainPage extends AppCompatActivity {

    public static ImageView menu, back, qrCode, walletIcon;
    public static DrawerLayout drawerLayout;
    public static TextView title, userNameTxt, userMobileNumberTxt, walletBalanceTxt;
    public static LinearLayout toolbarContainer;
    public static RelativeLayout titleLayout;
    public static String userId, cartId, currency = "₹", accessToken, userName, userNumber, userEmail, walletBalance, password;
    boolean doubleBackToExitPressedOnce = false;
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


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


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
                        loadFragment(new MyHistory(), true);
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
        walletBalanceTxt = findViewById(R.id.walletBalance);
        titleLayout = findViewById(R.id.titleLayout);
        menu = (ImageView) findViewById(R.id.menu);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        back = findViewById(R.id.back);
        qrCode = findViewById(R.id.qrCodeScan);
        walletIcon = findViewById(R.id.walletIcon);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

    }

    @SuppressLint("RtlHardcoded")
    @OnClick({R.id.menu, R.id.back, R.id.qrCodeScan, R.id.walletBalance, R.id.walletIcon})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.menu:
                in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);

                loadFragment(new MyAccount(), true);

                break;

            case R.id.back:
                in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                removeCurrentFragmentAndMoveBack();
                break;

            case R.id.qrCodeScan:
                loadFragment(new QrCodeScan(), true);
                break;

            case R.id.walletBalance:
            case R.id.walletIcon:
                loadFragment(new AddWallet(), true);
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
            qrCode.setVisibility(View.GONE);
            walletBalanceTxt.setVisibility(View.GONE);
            walletIcon.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        } else {
            menu.setVisibility(View.VISIBLE);
            qrCode.setVisibility(View.VISIBLE);
            walletBalanceTxt.setVisibility(View.VISIBLE);
            walletIcon.setVisibility(View.VISIBLE);
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
        transaction.commitAllowingStateLoss();

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
                File file1 = new File("data/data/in.realpayment/shared_prefs/user.xml");
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