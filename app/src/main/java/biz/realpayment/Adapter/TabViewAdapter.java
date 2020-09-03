package biz.realpayment.Adapter;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabViewAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private int resultTab;
    private Fragment[] tabs;


    public TabViewAdapter(FragmentManager fm, int NumOfTabs, Fragment[] tabs, int resultTab) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = NumOfTabs;
        this.tabs = tabs;
        this.resultTab = resultTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void forwardActivityResult(int requestCode, int resultCode, Intent data) {
        tabs[resultTab].onActivityResult(requestCode, resultCode, data);
    }

    public void forwardPermissionResult(int requestCode,
                                        @NonNull String[] permissions,
                                        @NonNull int[] grantResults) {

        tabs[resultTab].onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
