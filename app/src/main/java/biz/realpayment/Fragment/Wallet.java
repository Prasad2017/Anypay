package biz.realpayment.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biz.realpayment.Activity.MainPage;
import biz.realpayment.Adapter.TabViewAdapter;
import biz.realpayment.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.ButterKnife;


public class Wallet extends Fragment {

    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabViewAdapter adapter;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        ButterKnife.bind(this, view);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.blue_grey_900));

        initComponent();

        return view;
    }

    private void initComponent() {

        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("WITHDRAWAL"));
        tabLayout.addTab(tabLayout.newTab().setText("TOPUP"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.white), ContextCompat.getColor(getActivity(), R.color.white));

        Fragment[] tabs = new Fragment[2];
        tabs[0] = new WithdrawalWallet();
        tabs[1] = new AddWallet();
        adapter = new TabViewAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(), tabs, 1);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.forwardActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


}