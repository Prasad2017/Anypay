package in.realpayment.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import in.realpayment.Extra.Common;

public class InstallReferrerReceiver extends BroadcastReceiver {

    public String referCode;

    @Override
    public void onReceive(Context context, Intent intent) {

        referCode = intent.getStringExtra("referrer").trim();
        if(referCode.equals("utm_source=google-play&utm_medium=organic")){
            Common.saveUserData(context, "referCode", referCode);
        }else {
            Common.saveUserData(context, "referCode", referCode);
        }

    }
}