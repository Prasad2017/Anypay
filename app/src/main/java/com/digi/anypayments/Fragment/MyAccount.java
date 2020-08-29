package com.digi.anypayments.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.digi.anypayments.Activity.Login;
import com.digi.anypayments.Activity.MainPage;
import com.digi.anypayments.Extra.Common;
import com.digi.anypayments.Extra.DetectConnection;
import com.digi.anypayments.R;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyAccount extends Fragment {

    View view;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.mobileNumber)
    TextView mobileNumber;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, view);
        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));

        userName.setText(MainPage.userName);
        mobileNumber.setText(MainPage.userNumber);

        return view;

    }

    @OnClick({R.id.walletLayout, R.id.logoutLayout, R.id.shareLayout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.walletLayout:
                ((MainPage) getActivity()).loadFragment(new AddWallet(), true);
                break;

            case R.id.logoutLayout:
                logout();
                break;

            case R.id.shareLayout:
                break;

        }

    }

    private void logout() {

        final Dialog dialog = new Dialog(getActivity());
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
                Common.saveUserData(getActivity(), "userId", "");
                File file1 = new File("data/data/com.digi.anypayments/shared_prefs/user.xml");
                if (file1.exists()) {
                    file1.delete();
                }

                Intent intent = new Intent(getActivity(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        dialog.show();

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