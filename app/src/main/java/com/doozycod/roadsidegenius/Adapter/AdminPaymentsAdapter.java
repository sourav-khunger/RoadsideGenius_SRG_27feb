package com.doozycod.roadsidegenius.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Activities.PaymentDetailsActivity;
import com.doozycod.roadsidegenius.Model.PaymentList.Payment;
import com.doozycod.roadsidegenius.R;

import java.util.ArrayList;
import java.util.List;

public class AdminPaymentsAdapter extends RecyclerView.Adapter<AdminPaymentsAdapter.RecyclerHolder> {

    Context context;
    List<Payment> paymentList = new ArrayList<>();

    public AdminPaymentsAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public AdminPaymentsAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_listview_admin, parent, false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPaymentsAdapter.RecyclerHolder holder, int position) {
        Payment payment = paymentList.get(position);
        holder.customerNameTxt.setText(payment.getCustomerName());
        holder.driverNameTxt.setText(payment.getDriver());
        holder.statusTxt.setText(payment.getStatus());
        if (payment.getStatus().toLowerCase().equals("completed")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.quantum_googgreen));
        }
        holder.paymentMethodTxt.setText(payment.getPaymentMethod());
        holder.amountTxt.setText(payment.getAmount());
        holder.bonusAmountTxt.setText(payment.getBonusAmount());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PaymentDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("payment", payment);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView customerNameTxt, statusTxt, driverNameTxt, paymentMethodTxt, amountTxt, bonusAmountTxt;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            bonusAmountTxt = itemView.findViewById(R.id.bonusAmountTxt);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            paymentMethodTxt = itemView.findViewById(R.id.paymentMethodTxt);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            driverNameTxt = itemView.findViewById(R.id.driverNameTxt);
        }
    }
}
