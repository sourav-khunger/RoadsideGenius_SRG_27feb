package com.doozycod.roadsidegenius.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Activities.ApprovePendingRequestActivity;
import com.doozycod.roadsidegenius.Model.WithdrawList.Pending;
import com.doozycod.roadsidegenius.R;

import java.util.ArrayList;
import java.util.List;

public class WithdrawPendingRequestAdapter extends RecyclerView.Adapter<WithdrawPendingRequestAdapter.RecyclerHolder> {
    Context context;
    List<Pending> pendings = new ArrayList<>();
//    SharedPreferenceMethod sharedPreferenceMethod;
//    ApiService apiService;
//    CustomProgressBar customProgressBar;


    public WithdrawPendingRequestAdapter(Context context, List<Pending> pendings) {
        this.context = context;
        this.pendings = pendings;
//        sharedPreferenceMethod = new SharedPreferenceMethod(context);
//        apiService = ApiUtils.getAPIService();
//        customProgressBar = new CustomProgressBar(context);
    }

    @NonNull
    @Override
    public WithdrawPendingRequestAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_listview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawPendingRequestAdapter.RecyclerHolder holder, int position) {
        Pending pending = pendings.get(position);
        holder.driverNameTxt.setText(pending.getDriverName());
        holder.emailTxt.setText(pending.getDriverEmail());
        if (pending.getStatus().equals("Requested")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.quantum_orange));
        }
        holder.statusTxt.setText(pending.getStatus());
        holder.amountTxt.setText("$" + pending.getAmount());
        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ApprovePendingRequestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("pending", pending);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendings.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView driverNameTxt, emailTxt, statusTxt, amountTxt;
        Button approveButton;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            approveButton = itemView.findViewById(R.id.approveButton);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            driverNameTxt = itemView.findViewById(R.id.driverNameTxt);
        }
    }
}
