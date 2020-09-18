package in.realpayment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.realpayment.Fragment.CustomerRechargeMobile;
import in.realpayment.Fragment.PostPaidRecharge;
import in.realpayment.Model.AssignResponse;
import in.realpayment.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostCircleAdapter extends RecyclerView.Adapter<PostCircleAdapter.MyViewHolder> {

    Context context;
    List<AssignResponse> assignResponseList;

    public PostCircleAdapter(Context context, List<AssignResponse> assignResponseList) {

        this.context =  context;
        this.assignResponseList =  assignResponseList;

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

        holder.countryName.setText(assignResponseList.get(position).getName());

        holder.countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PostPaidRecharge.circle.setText(assignResponseList.get(position).getName());
                PostPaidRecharge.dialog.dismiss();

            }
        });

    }

    @Override
    public int getItemCount() {
        return assignResponseList.size();
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

