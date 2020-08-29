package com.digi.anypayments.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.digi.anypayments.Activity.MainPage;
import com.digi.anypayments.Fragment.CustomerRechargeMobile;
import com.digi.anypayments.Model.ContactResponse;
import com.digi.anypayments.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    Context context;
    List<ContactResponse> contactResponseList;
    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    public static String customerName, customerNumber;



    public ContactAdapter(Context context, List<ContactResponse> contactResponseList) {

        this.context = context;
        this.contactResponseList = contactResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.contact_item_list, null);
        MyViewHolder CartListViewHolder = new MyViewHolder(view, contactResponseList);
        return CartListViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ContactResponse contactResponse = contactResponseList.get(position);

        holder.textViews.get(0).setText(contactResponseList.get(position).getCustomerName());
        holder.textViews.get(1).setText(contactResponseList.get(position).getCustomerNumber());

        try {

            String[] splitStr = contactResponseList.get(position).getCustomerName().trim().split("\\s+");
            String firstName, lastName = "";
            if (splitStr.length==2) {
                firstName = splitStr[0].replace(" ", "");
                lastName = splitStr[1].replace(" ", "");
            } else {
                firstName = splitStr[0].replace(" ", "");
            }

            if (lastName.isEmpty()){

                mDrawableBuilder = TextDrawable.builder().roundRect(100);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(50) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(String.valueOf(firstName.charAt(0)), mColorGenerator.getColor(firstName), 100);

               // drawable = mDrawableBuilder.build(String.valueOf(firstName.charAt(0)), mColorGenerator.getColor(firstName)).fontSize(30);
                holder.imageView.setImageDrawable(drawable);
            } else {

                mDrawableBuilder = TextDrawable.builder().roundRect(100);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(50) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()
                        .buildRoundRect(String.valueOf(firstName.charAt(0)+" "+lastName.charAt(0)), mColorGenerator.getColor(firstName), 100);

                holder.imageView.setImageDrawable(drawable);
            }


            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    customerName = contactResponseList.get(position).getCustomerName();
                    customerNumber = contactResponseList.get(position).getCustomerNumber();
                    Log.e("customerName", ""+customerName);

                    ((MainPage) context).loadFragment(new CustomerRechargeMobile(), true);

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return contactResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.customerName, R.id.customerMobile})
        List<TextView> textViews;
        @BindView(R.id.customerImage)
        ImageView imageView;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView, List<ContactResponse> contactResponseList) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
