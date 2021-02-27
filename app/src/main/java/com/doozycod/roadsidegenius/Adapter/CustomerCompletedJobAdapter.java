package com.doozycod.roadsidegenius.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.R;

public class CustomerCompletedJobAdapter extends RecyclerView.Adapter<CustomerCompletedJobAdapter.RecyclerHolder> {

    @NonNull
    @Override
    public CustomerCompletedJobAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_completed_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerCompletedJobAdapter.RecyclerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
