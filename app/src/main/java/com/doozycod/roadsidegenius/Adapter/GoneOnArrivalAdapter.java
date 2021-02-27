package com.doozycod.roadsidegenius.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Model.AdminJobListModel.GoneAllArrivalJob;
import com.doozycod.roadsidegenius.R;

import java.util.ArrayList;
import java.util.List;

public class GoneOnArrivalAdapter extends RecyclerView.Adapter<GoneOnArrivalAdapter.RecyclerHolder> {
    Context context;
    List<GoneAllArrivalJob> goneAllArrivalJobs = new ArrayList<>();

    public GoneOnArrivalAdapter(Context context, List<GoneAllArrivalJob> goneAllArrivalJobs) {
        this.context = context;
        this.goneAllArrivalJobs = goneAllArrivalJobs;
    }

    @NonNull
    @Override
    public GoneOnArrivalAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gone_arrival_listview, parent, false);
        return new RecyclerHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GoneOnArrivalAdapter.RecyclerHolder holder, int position) {
        GoneAllArrivalJob goneAllArrivalJob = goneAllArrivalJobs.get(position);
        holder.customerNameTxt.setText(goneAllArrivalJob.getCustomerName());
        holder.phoneTxt.setText("+" + goneAllArrivalJob.getCustomerNumber());
        holder.amountTxt.setText("$"+goneAllArrivalJob.getAmountQuoted());
        holder.emailTxt.setText(goneAllArrivalJob.getCustomerEmail());
        holder.pickupTxt.setText(goneAllArrivalJob.getCustomerPickup());
        holder.dropoffTxt.setText(goneAllArrivalJob.getCustomerDropoff());
    }

    @Override
    public int getItemCount() {
        return goneAllArrivalJobs.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt, phoneTxt, emailTxt, pickupTxt, dropoffTxt,amountTxt;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            pickupTxt = itemView.findViewById(R.id.pickupTxt);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            dropoffTxt = itemView.findViewById(R.id.dropoffTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);

        }
    }
}
