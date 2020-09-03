package biz.realpayment.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biz.realpayment.Activity.MainPage;
import biz.realpayment.Extra.DetectConnection;
import biz.realpayment.R;
import com.google.android.material.snackbar.Snackbar;

import butterknife.ButterKnife;


public class WithdrawalWallet extends Fragment {

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_withdrawal_wallet, container, false);
        ButterKnife.bind(this, view);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.blue_grey_900));

        return view;

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