package in.realpayment.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.awt.font.TextAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import in.realpayment.Activity.MainPage;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.HistoryResponse;
import in.realpayment.Model.HistoryResponseList;
import in.realpayment.R;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.realpayment.Retrofit.Api;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConfirmationAnyOrder extends Fragment {


    View view;
    @BindViews({R.id.paymentText, R.id.RechargeDate, R.id.transactionId, R.id.paidTo, R.id.userName, R.id.debitedFrom, R.id.paidAmount, R.id.amount})
    List<TextView> textView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    Bitmap layoutBitmap;
    String requestId="";
    List<HistoryResponse> historyResponseList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_confirmation_any_order, container, false);
        ButterKnife.bind(this, view);

        try{

            layoutBitmap = convertLayoutToImage();

            Bundle bundle = getArguments();
            requestId = bundle.getString("requestId");

        } catch (Exception e){
            e.printStackTrace();
        }

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.green_600));
        MainPage.title.setText("Transaction Details");


        return view;

    }

    private Bitmap convertLayoutToImage() {

        NestedScrollView nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView); //you can pass your xml layout

        nestedScrollView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        nestedScrollView.layout(0, 0, nestedScrollView.getMeasuredWidth(), nestedScrollView.getMeasuredHeight());

        nestedScrollView.setDrawingCacheEnabled(true);
        nestedScrollView.buildDrawingCache();
        return nestedScrollView.getDrawingCache();// creates bitmap and returns the same

    }

    @OnClick({R.id.share, R.id.goToHome})
    public void onCLick(View view){
        switch (view.getId()){

            case R.id.share:

                String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), layoutBitmap,"RealPayment Transaction Details", null);
                Uri bitmapUri = Uri.parse(bitmapPath);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                intent.putExtra(Intent.EXTRA_TEXT, "Paid through RealPayment");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Share"));

                break;

            case R.id.goToHome:
                ((MainPage) getActivity()).loadFragment(new Home(), false);
                break;

        }

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getRequestIdDetails(requestId);
        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void getRequestIdDetails(String requestId) {

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));

        Call<HistoryResponseList> call = Api.getClient().getRequestIdDetails(stringStringMap, "1", "10000", requestId);
        call.enqueue(new Callback<HistoryResponseList>() {
            @Override
            public void onResponse(Call<HistoryResponseList> call, Response<HistoryResponseList> response) {

                if (response.isSuccessful()){

                    if (response.body().getSuccess().booleanValue()==true){

                        historyResponseList = response.body().getHistoryResponseList();

                        if (historyResponseList.size()>0){

                            for (int i=0;i<historyResponseList.size();i++){

                                HistoryResponse historyResponse = historyResponseList.get(i);

                                if (historyResponseList.get(i).getStatus().equalsIgnoreCase("SUCCESS")) {
                                    textView.get(0).setText("Payment Successfully");
                                } else if (historyResponseList.get(i).getStatus().equalsIgnoreCase("FAILED")) {
                                    textView.get(0).setText("Payment Failed");
                                } else {
                                    textView.get(0).setText("Payment in Process");
                                }

                                try {

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                                    Date sourceDate = null;
                                    try {
                                        sourceDate = dateFormat.parse(historyResponseList.get(i).getCreatedDate().substring(0,10));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy");
                                    String targetDate = targetFormat.format(sourceDate);

                                    textView.get(1).setText(""+targetDate);

                                    try {

                                        textView.get(6).setText(MainPage.currency+" "+historyResponseList.get(i).getAmt());
                                        textView.get(7).setText(MainPage.currency+" "+historyResponseList.get(i).getAmt());

                                        textView.get(2).setText(historyResponseList.get(i).getReqid());


                                        if (historyResponse.getRemark().substring(0, 14).equalsIgnoreCase("Received from:")) {

                                            textView.get(3).setText("Received from ");
                                            textView.get(5).setText("Credited to ");
                                            textView.get(4).setText(historyResponseList.get(i).getRemark().substring(14, historyResponseList.get(i).getRemark().length()));

                                        } else {

                                            textView.get(3).setText("Paid to");
                                            textView.get(5).setText("Debited from ");

                                            textView.get(4).setText(historyResponseList.get(i).getRemark().substring(20, historyResponseList.get(i).getRemark().length()));

                                        }

                                        if (historyResponseList.get(i).getStatus().equalsIgnoreCase("SUCCESS")){

                                            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.success_tick));
                                            linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.green_600));

                                            MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.green_600));
                                            MainPage.title.setText("Transaction Details");

                                        } else if (historyResponseList.get(i).getStatus().equalsIgnoreCase("FAILED")){

                                            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.warning));
                                            linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.red));

                                            MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
                                            MainPage.title.setText("Transaction Details");

                                        } else {

                                            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.pending));
                                            linearLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow_800));
                                            RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                            rotate.setDuration(5000);
                                            rotate.setRepeatCount(Animation.INFINITE);
                                            rotate.setInterpolator(new LinearInterpolator());
                                            imageView.startAnimation(rotate);

                                            MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.yellow_800));
                                            MainPage.title.setText("Transaction Details");

                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }



                                } catch (Exception e){
                                    e.printStackTrace();

                                }

                            }

                        }


                    } else {

                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<HistoryResponseList> call, Throwable t) {
                Log.e("requestIdError", ""+t.getMessage());
            }
        });

    }

}