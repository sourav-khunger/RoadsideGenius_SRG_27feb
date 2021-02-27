package com.doozycod.roadsidegenius.Adapter.CustomerAdapter;

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

import com.doozycod.roadsidegenius.Activities.Customer.CustomerCompleteJobActivity;
import com.doozycod.roadsidegenius.Model.JobList.Active;
import com.doozycod.roadsidegenius.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActiveJobsCustomerAdapter extends RecyclerView.Adapter<ActiveJobsCustomerAdapter.RecyclerHolder> {
    Context context;
    List<Active> actives = new ArrayList<>();

    public ActiveJobsCustomerAdapter(Context context, List<Active> actives) {
        this.context = context;
        this.actives = actives;
    }

    @NonNull
    @Override
    public ActiveJobsCustomerAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_request_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveJobsCustomerAdapter.RecyclerHolder holder, int position) {
        Active activejob = actives.get(position);
        holder.driverNameTxt.setText(activejob.getDriver());
        holder.customerNameTxt.setText(activejob.getCustomerName());
        holder.emailTxt.setText(activejob.getCustomerEmail());
        holder.phoneTxt.setText("+" + activejob.getCustomerNumber());
        holder.serviceTypeTxt.setText(activejob.getService());
        holder.pickupTxt.setText(activejob.getCustomerPickup());
        holder.dropoffTxt.setText(activejob.getCustomerDropoff());
        holder.amountTxt.setText("$"+activejob.getInvoiceTotal());
        if (activejob.getStatus().toLowerCase().equals("started")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.startedText));
        }
        if (activejob.getStatus().toLowerCase().equals("in progress")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.inprogress));
        }
        holder.statusTxt.setText(activejob.getStatus());

        holder.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomerCompleteJobActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("customer", (Serializable) activejob);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return actives.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt,amountTxt, phoneTxt, emailTxt, statusTxt, driverNameTxt, serviceTypeTxt, pickupTxt, dropoffTxt;
        Button payButton;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            payButton = itemView.findViewById(R.id.payButton);
            dropoffTxt = itemView.findViewById(R.id.dropoffTxt);
            pickupTxt = itemView.findViewById(R.id.pickupTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
            driverNameTxt = itemView.findViewById(R.id.DriverNameTxt);
            serviceTypeTxt = itemView.findViewById(R.id.serviceTypeTxt);
        }
    }
}
