package in.realpayment.Fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.realpayment.Activity.MainPage;
import in.realpayment.Adapter.HistoryAdapter;
import in.realpayment.Extra.DetectConnection;
import in.realpayment.Model.HistoryResponse;
import in.realpayment.Model.HistoryResponseList;
import in.realpayment.R;
import in.realpayment.Retrofit.Api;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyHistory extends Fragment {


    View view;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<HistoryResponse> historyResponseList = new ArrayList<>();
    HistoryAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_history, container, false);
        ButterKnife.bind(this, view);

        MainPage.titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
        MainPage.title.setText("Transaction History");

        return view;

    }

    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        ((MainPage) getActivity()).lockUnlockDrawer(1);
        if (DetectConnection.checkInternetConnection(getActivity())) {
           getHistory();
        } else {
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }

    private void getHistory() {

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("Authorization", "bearer " + MainPage.accessToken.replace("\"", ""));


        Call<HistoryResponseList> call = Api.getClient().getHistoryList(stringStringMap, "1", "1000000", MainPage.userId);
        call.enqueue(new Callback<HistoryResponseList>() {
            @Override
            public void onResponse(Call<HistoryResponseList> call, Response<HistoryResponseList> response) {

                if (response.body().getSuccess().booleanValue() == true) {

                    try {

                        historyResponseList.clear();
                        recyclerView.clearOnScrollListeners();

                        historyResponseList = response.body().getHistoryResponseList();

                        adapter = new HistoryAdapter(getActivity(), historyResponseList);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                       // recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_size));
                        recyclerView.addItemDecoration(itemDecorator);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemInserted(historyResponseList.size() - 1);
                        recyclerView.setHasFixedSize(true);


                    } catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    historyResponseList = null;
                }

            }

            @Override
            public void onFailure(Call<HistoryResponseList> call, Throwable t) {
                Log.e("historyError", ""+t.getMessage());
            }
        });

    }
}