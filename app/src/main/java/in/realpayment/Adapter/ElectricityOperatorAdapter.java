package in.realpayment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.realpayment.Fragment.DthRecharge;
import in.realpayment.Fragment.ElectricityBill;
import in.realpayment.Model.OperatorResponse;
import in.realpayment.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ElectricityOperatorAdapter extends RecyclerView.Adapter<ElectricityOperatorAdapter.MyViewHolder> {

    Context context;
    List<OperatorResponse> operatorResponseList;


    public ElectricityOperatorAdapter(Context context, List<OperatorResponse> operatorResponseList) {

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

                ElectricityBill.operator.setText(operatorResponseList.get(position).getOprator());
                ElectricityBill.operatorId = operatorResponseList.get(position).getOpratorcode();

                ElectricityBill.dialog.dismiss();

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