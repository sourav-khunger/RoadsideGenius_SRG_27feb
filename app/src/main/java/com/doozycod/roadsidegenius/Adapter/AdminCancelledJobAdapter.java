package com.doozycod.roadsidegenius.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Model.AdminJobListModel.CancelledJob;
import com.doozycod.roadsidegenius.Model.AdminJobListModel.GoneAllArrivalJob;
import com.doozycod.roadsidegenius.R;

import java.util.ArrayList;
import java.util.List;

public class AdminCancelledJobAdapter extends RecyclerView.Adapter<AdminCancelledJobAdapter.RecyclerHolder> {
    Context context;
    List<CancelledJob> cancelledJobs = new ArrayList<>();

    public AdminCancelledJobAdapter(Context context, List<CancelledJob> cancelledJob) {
        this.context = context;
        this.cancelledJobs = cancelledJob;
    }

    @NonNull
    @Override
    public AdminCancelledJobAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gone_arrival_listview, parent, false);
        return new RecyclerHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdminCancelledJobAdapter.RecyclerHolder holder, int position) {
        CancelledJob cancelledJob = cancelledJobs.get(position);
        holder.customerNameTxt.setText(cancelledJob.getCustomerName());
        holder.phoneTxt.setText("+" + cancelledJob.getCustomerNumber());
        holder.amountTxt.setText("$"+cancelledJob.getAmountQuoted());
        holder.emailTxt.setText(cancelledJob.getCustomerEmail());
        holder.pickupTxt.setText(cancelledJob.getCustomerPickup());
        holder.dropoffTxt.setText(cancelledJob.getCustomerDropoff());
    }

    @Override
    public int getItemCount() {
        return cancelledJobs.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt, phoneTxt, emailTxt, pickupTxt, dropoffTxt,amountTxt;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            pickupTxt = itemView.findViewById(R.id.pickupTxt);
            dropoffTxt = itemView.findViewById(R.id.dropoffTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);

        }
    }
}
