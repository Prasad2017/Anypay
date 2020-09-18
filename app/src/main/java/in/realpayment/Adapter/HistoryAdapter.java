package in.realpayment.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import in.realpayment.Fragment.ConfirmationAnyOrder;
import in.realpayment.Activity.MainPage;
import in.realpayment.Model.HistoryResponse;
import in.realpayment.R;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    List<HistoryResponse> historyResponseList;


    public HistoryAdapter(Context context, List<HistoryResponse> historyResponseList) {

        this.context = context;
        this.historyResponseList = historyResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HistoryResponse historyResponse = historyResponseList.get(position);

        holder.textViews.get(0).setText(MainPage.currency+""+historyResponseList.get(position).getAmt());

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date sourceDate = null;
            try {
                sourceDate = dateFormat.parse(historyResponseList.get(position).getModifiedDate().substring(0,10));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy");
            String targetDate = targetFormat.format(sourceDate);

            holder.textViews.get(1).setText(""+targetDate);

            try {

                if (historyResponse.getRemark().substring(0, 14).equalsIgnoreCase("Received from:")) {

                    holder.textViews.get(2).setText("Received from ");
                    holder.textViews.get(3).setText("Credited to ");
                    holder.imageView.setImageDrawable(context.getDrawable(R.drawable.upcross));

                } else {

                    holder.textViews.get(2).setText("Paid to");
                    holder.textViews.get(3).setText("Debited from ");
                    holder.imageView.setImageDrawable(context.getDrawable(R.drawable.downcross));

                }

            }catch (Exception e){
                e.printStackTrace();
            }



        } catch (Exception e){
            e.printStackTrace();

        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConfirmationAnyOrder confirmationAnyOrder = new ConfirmationAnyOrder();
                Bundle bundle = new Bundle();
                bundle.putString("requestId", historyResponseList.get(position).getReqid());
                confirmationAnyOrder.setArguments(bundle);
                ((MainPage) context).loadFragment(confirmationAnyOrder, true);

            }
        });

    }

    @Override
    public int getItemCount() {
        return historyResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.amount, R.id.date, R.id.paidTo, R.id.walletTxt})
        List<TextView> textViews;
        @BindView(R.id.imageSender)
        ImageView imageView;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
