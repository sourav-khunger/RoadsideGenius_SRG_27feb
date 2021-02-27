package com.doozycod.roadsidegenius.Adapter.CustomerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Model.JobList.Job;
import com.doozycod.roadsidegenius.R;

import java.util.ArrayList;
import java.util.List;

public class UnassignCustomerAdapter extends RecyclerView.Adapter<UnassignCustomerAdapter.RecyclerHolder> {
    Context context;

    public UnassignCustomerAdapter(Context context, List<Job> jobs) {
        this.context = context;
        this.jobs = jobs;
    }

    List<Job> jobs = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unassign_recyclerview_customer, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        Job job = jobs.get(position);
        holder.customerNameTxt.setText(job.getCustomerName());
        holder.phoneTxt.setText("+" + job.getCustomerNumber());
        holder.emailTxt.setText(job.getCustomerEmail());
        holder.serviceTypeTxt.setText(job.getServiceNeeded());
        holder.amount_quoted.setText("$"+job.getAmountQuoted());
        holder.pickupTxt.setText(job.getCustomerPickup());
        holder.dropoffTxt.setText(job.getCustomerDropoff());
        holder.notesTxt.setText(job.getCustomerNotes());
//        holder.assignButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AssignJobActivity.class);
//                intent.putExtra("job", (Serializable) job);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt, phoneTxt, emailTxt, serviceTypeTxt, amount_quoted, pickupTxt, dropoffTxt, notesTxt;
        Button assignButton;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
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
