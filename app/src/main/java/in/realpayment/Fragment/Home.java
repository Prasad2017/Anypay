package in.realpayment.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import in.realpayment.Activity.Login;
import in.realpayment.Activity.MainPage;
import in.realpayment.Adapter.BannerImageAdapter;
import in.realpayment.Extra.Common;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.BannerResponse;
import in.realpayment.Model.BannerResponseList;
import in.realpayment.Model.Profile;
import in.realpayment.Model.ProfileResponse;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;


public class Home extends Fragment {

    View view;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    CirclePageIndicator indicator;
    public static ViewPager mPager;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    List<BannerResponseList> bannerResponseList = new ArrayList<>();
    // Creates an instance of the manager.
    private AppUpdateManager mAppUpdateManager;
    // Returns an intent object that you use to check for an update.
    private Task<AppUpdateInfo> appUpdateInfo;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private int MY_REQUEST_CODE = 700;
    String appUrl=" One app for all your payments Join Me on RealPayment, a secure app for Mobile Recharge,DTH, Utility ,Bill, Payments etc..\n" +
            "Enter My Referral Code and to earn Rs.10 to Rs.100 CashBack first Transaction.";
    String appLink="https://play.google.com/store/apps/details?id=";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.indicator);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        MainPage.title.setText("");

        mAppUpdateManager = AppUpdateManagerFactory.create(getActivity());
        appUpdateInfo = mAppUpdateManager.getAppUpdateInfo();
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState state) {
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                } else if (state.installStatus() == InstallStatus.INSTALLED) {
                    if (mAppUpdateManager != null) {
                        mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                    }

                } else {
                    Log.i("TAG", "InstallStateUpdatedListener: state: " + state.installStatus());
                }
            }
        };

        mAppUpdateManager.registerListener(installStateUpdatedListener);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, getActivity(), MY_REQUEST_CODE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("", "checkForAppUpdateAvailability: something else");
            }
        });

        return view;

    }

    @OnClick({R.id.toContactLayout, R.id.toAccountLayout, R.id.mobileRechargeLayout, R.id.electricityLayout, R.id.walletLayout, R.id.postpaidLayout, R.id.dthLayout, R.id.shareLayout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.mobileRechargeLayout:
                ((MainPage) getActivity()).loadFragment(new CustomerContactList(), true);
                break;

            case R.id.walletLayout:
                ((MainPage) getActivity()).loadFragment(new AddWallet(), true);
                break;

            case R.id.postpaidLayout:
                ((MainPage) getActivity()).loadFragment(new PostPaidContactList(), true);
                break;

            case R.id.dthLayout:
                ((MainPage) getActivity()).loadFragment(new DthRecharge(), true);
                break;

            case R.id.electricityLayout:
                ((MainPage) getActivity()).loadFragment(new ElectricityBill(), true);
                break;

            case R.id.shareLayout:
                // share app with your friends
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/*");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                shareIntent.putExtra(Intent.EXTRA_TEXT, appUrl+" Download RealPayment APP Link :- "+appLink+ getActivity().getPackageName()+"&referrer="+MainPage.userNumber);
                startActivity(Intent.createChooser(shareIntent, "Share Using"));

                break;

            case R.id.toContactLayout:
               // ((MainPage) getActivity()).loadFragment(new ToContact(), true);
                Toasty.warning(getActivity(), "In working", Toasty.LENGTH_SHORT, true).show();
                break;

        }
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(linearLayout, "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });

        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }


    // Checks that the update is not stalled during 'onResume()'.
    // However, you should execute this check at all entry points into the app.
    @Override
    public void onResume() {
        super.onResume();

        mAppUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    mAppUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            IMMEDIATE,
                                            getActivity(),
                                            MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(0);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            requestPermission();
            getProfile();
            getBannerList();
        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void getBannerList() {

        try {
            bannerResponseList.clear();
        } catch (Exception e){
            e.printStackTrace();
        }

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));

        Call<BannerResponse> call = Api.getClient().getBannerList(stringStringMap, "1", "100");
        call.enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getSuccess().booleanValue() == true) {

                        try {

                            bannerResponseList.clear();
                            bannerResponseList = response.body().getBannerResponseList();

                            if (bannerResponseList.size() > 0) {
                                mPager.setAdapter(new BannerImageAdapter(getActivity(), bannerResponseList));
                                indicator.setViewPager(mPager);
                            } else {

                            }

                            final float density = getResources().getDisplayMetrics().density;
                            indicator.setRadius(5 * density);
                            //Set circle indicator radius
                            indicator.setRadius(5 * density);

                            NUM_PAGES = bannerResponseList.size();
                            // Auto start of viewpager
                            final Handler handler = new Handler();
                            final Runnable Update = new Runnable() {
                                public void run() {

                                    if (currentPage == NUM_PAGES) {
                                        currentPage = 0;
                                    }
                                    mPager.setCurrentItem(currentPage++, true);
                                }
                            };
                            Timer swipeTimer = new Timer();
                            swipeTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handler.post(Update);
                                }
                            }, 5000, 5000);

                            // Pager listener over indicator
                            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                @Override
                                public void onPageSelected(int position) {
                                    currentPage = position;
                                }

                                @Override
                                public void onPageScrolled(int position, float arg1, int arg2) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int position) {

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        bannerResponseList = null;
                    }

                } else {

                    bannerResponseList = null;
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                Log.e("bannerError", ""+t.getMessage());
            }
        });


    }

    private void getProfile() {

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));

        Call<ProfileResponse> call = Api.getClient().getProfile(stringStringMap);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                if (response.isSuccessful()) {

                    if (response.body().getSuccess().booleanValue() == true) {

                        Profile profile = response.body().getProfile();

                        MainPage.userEmail = profile.getEmail();
                        MainPage.userNumber = profile.getMobileNo();
                        MainPage.walletBalance = profile.getWalletBalance();
                        MainPage.password = profile.getPassword();

                        MainPage.walletBalanceTxt.setText(MainPage.currency + " " + MainPage.walletBalance);

                        Common.saveUserData(getActivity(), "userId", profile.getUserId());
                        Common.saveUserData(getActivity(), "userName", profile.getUserName());
                        Common.saveUserData(getActivity(), "userNumber", profile.getMobileNo());
                        Common.saveUserData(getActivity(), "userEmail", profile.getEmail());
                        Common.saveUserData(getActivity(), "walletBalance", profile.getWalletBalance());
                        Common.saveUserData(getActivity(), "password", profile.getPassword());

                    } else if (response.body().getSuccess().booleanValue() == false) {

                        Common.saveUserData(getActivity(), "userId", "");
                        File file1 = new File("data/data/in.realpayment/shared_prefs/user.xml");
                        if (file1.exists()) {
                            file1.delete();
                        }

                        Intent intent = new Intent(getActivity(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                } else {

                    Intent intent = new Intent(getActivity(), Login.class);
                    getActivity().startActivity(intent);
                    getActivity().finishAffinity();

                }

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("profileError", ""+t.getMessage());
            }
        });


    }

    private void requestPermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            getBannerList();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

}