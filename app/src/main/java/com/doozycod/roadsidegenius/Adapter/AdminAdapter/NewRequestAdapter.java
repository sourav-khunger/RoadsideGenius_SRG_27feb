package com.doozycod.roadsidegenius.Adapter.AdminAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Activities.Admin.AssignJobActivity;
import com.doozycod.roadsidegenius.Activities.CustomerDetailsActivity;
import com.doozycod.roadsidegenius.Activities.NewRequestDetailsActivity;
import com.doozycod.roadsidegenius.Model.JobList.Job;
import com.doozycod.roadsidegenius.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewRequestAdapter extends RecyclerView.Adapter<NewRequestAdapter.RecyclerHolder> {
    Context context;

    public NewRequestAdapter(Context context, List<Job> jobs) {
        this.context = context;
        this.jobs = jobs;
    }

    List<Job> jobs = new ArrayList<>();

    @NonNull
    @Override
    public NewRequestAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unassign_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewRequestAdapter.RecyclerHolder holder, int position) {
        Job job = jobs.get(position);
        holder.customerNameTxt.setText(job.getCustomerName());
        holder.phoneTxt.setText("+" + job.getCustomerNumber());
        holder.emailTxt.setText(job.getCustomerEmail());
        holder.serviceTypeTxt.setText(job.getServiceNeeded());
        holder.amount_quoted.setText("$"+job.getAmountQuoted());
        holder.pickupTxt.setText(job.getCustomerPickup());
        holder.dropoffTxt.setText(job.getCustomerDropoff());
        holder.notesTxt.setText(job.getCustomerNotes());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewRequestDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("driver", job);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });holder.assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AssignJobActivity.class);
                intent.putExtra("job", (Serializable) job);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt, phoneTxt, emailTxt, serviceTypeTxt, amount_quoted, pickupTxt, dropoffTxt, notesTxt;
        Button assignButton;
        CardView cardView;
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            notesTxt = itemView.findViewById(R.id.notesTxt);
            dropoffTxt = itemView.findViewById(R.id.dropoffTxt);
            pickupTxt = itemView.findViewById(R.id.pickupTxt);
            amount_quoted = itemView.findViewById(R.id.amount_quoted);
            serviceTypeTxt = itemView.findViewById(R.id.serviceTypeTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
            assignButton = itemView.findViewById(R.id.assignButton);
        }
    }
}
