package biz.realpayment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import biz.realpayment.Fragment.CustomerRechargeMobile;
import biz.realpayment.Model.OperatorResponse;
import biz.realpayment.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperatorAdapter extends RecyclerView.Adapter<OperatorAdapter.MyViewHolder> {

    Context context;
    List<OperatorResponse> operatorResponseList;


    public OperatorAdapter(Context context, List<OperatorResponse> operatorResponseList) {

        this.context = context;
        this.operatorResponseList = operatorResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recharge_item_list, null);
        MyViewHolder CartListViewHolder = new MyViewHolder(view);
        return CartListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.countryName.setText(operatorResponseList.get(position).getOprator());

        holder.countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomerRechargeMobile.operator.setText(operatorResponseList.get(position).getOprator());
                CustomerRechargeMobile.operatorId = operatorResponseList.get(position).getOpratorcode();

                CustomerRechargeMobile.dialog.dismiss();

            }
        });

    }

    @Override
    public int getItemCount() {
        return operatorResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.countryName)
        TextView countryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
