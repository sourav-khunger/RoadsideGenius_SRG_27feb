package com.doozycod.roadsidegenius.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DriverPaymentsAdapter extends RecyclerView.Adapter<DriverPaymentsAdapter.RecyclerHolder> {
    @NonNull
    @Override
    public DriverPaymentsAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DriverPaymentsAdapter.RecyclerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
