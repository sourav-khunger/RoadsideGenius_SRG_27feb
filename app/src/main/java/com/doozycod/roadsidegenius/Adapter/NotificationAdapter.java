package com.doozycod.roadsidegenius.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.Notification.Notification;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecyclerHolder> {
    Context context;
    List<Notification> notifications;
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;


    public NotificationAdapter(Context context, List<Notification> notifications, ApiService apiService) {
        this.context = context;
        this.notifications = notifications;
        this.apiService = apiService;
        sharedPreferenceMethod = new SharedPreferenceMethod(context);
    }


    @NonNull
    @Override
    public NotificationAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list, parent, false);
        Log.e("TAG", "onCreateViewHolder: ");
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.RecyclerHolder holder, int position) {
        Notification notification = notifications.get(position);

        if (notification.getIsRead().equals("1")) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.quantum_grey300));
        }
        holder.titleTxt.setText(notification.getTitle());
        if (notification.getUserType().equals("Customer")) {
            holder.contentTxt.setText(notification.getData().getDriverName() + " is assigned for Job");

        }

        boolean isExpanded = notification.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.contentTxt.setText(notification.getData().getDriverName() + " is assigned for Job");
        holder.driverNameTxt.setText(notification.getData().getDriverName());
        holder.truckTxt.setText(notification.getData().getTruck());
        holder.dispatchTxt.setText(notification.getData().getDipatch());
        holder.invoiceTotalTxt.setText(notification.getData().getInvoiceTotal());
        holder.totalMilesTxt.setText(notification.getData().getTotalMiles());
        holder.totalJobTimeTxt.setText(notification.getData().getTotalJobTime());
        holder.dispatchTimeTxt.setText(notification.getData().getDispatchDate());
        holder.vehicleColorTxt.setText(notification.getData().getVehicleColor());
        holder.vehicleModelTxt.setText(notification.getData().getVehicleModel());
        holder.vehicleMakeTxt.setText(notification.getData().getVehicleMake());
        holder.statusTxt.setText(notification.getData().getStatus());
        holder.siteTxt.setText(notification.getData().getSite());


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleTxt, contentTxt, driverNameTxt, dispatchTxt, truckTxt, siteTxt, etaTxt, statusTxt,
                vehicleMakeTxt, vehicleModelTxt, vehicleColorTxt, dispatchTimeTxt, totalJobTimeTxt, totalMilesTxt,
                invoiceTotalTxt, customerNameTxt, customerNoTxt, emailTxt, pickupTxt, dropOffTxt, amountTxt;
        LinearLayout customerLayout, adminLayout;
        ConstraintLayout expandableLayout;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            dropOffTxt = itemView.findViewById(R.id.dropOffTxt);
            pickupTxt = itemView.findViewById(R.id.pickupTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            customerNoTxt = itemView.findViewById(R.id.customerNoTxt);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);

            invoiceTotalTxt = itemView.findViewById(R.id.invoiceTotalTxt);
            totalMilesTxt = itemView.findViewById(R.id.totalMilesTxt);
            totalJobTimeTxt = itemView.findViewById(R.id.totalJobTimeTxt);
            dispatchTimeTxt = itemView.findViewById(R.id.dispatchTimeTxt);
            vehicleColorTxt = itemView.findViewById(R.id.vehicleColorTxt);
            vehicleModelTxt = itemView.findViewById(R.id.vehicleModelTxt);
            vehicleMakeTxt = itemView.findViewById(R.id.vehicleMakeTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            etaTxt = itemView.findViewById(R.id.etaTxt);
            siteTxt = itemView.findViewById(R.id.siteTxt);
            truckTxt = itemView.findViewById(R.id.truckTxt);
            dispatchTxt = itemView.findViewById(R.id.dispatchTxt);
            driverNameTxt = itemView.findViewById(R.id.driverNameTxt);
            adminLayout = itemView.findViewById(R.id.adminLayout);
//            driverLayout = itemView.findViewById(R.id.driverLayout);
            customerLayout = itemView.findViewById(R.id.customerLayout);
            cardView = itemView.findViewById(R.id.notificationCardView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            titleTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Notification notification = notifications.get(getAdapterPosition());
                    notification.setExpanded(!notification.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                    if (notification.getIsRead().equals("0")) {
                        updateStatusOnClick(notification.getId());
                    }
                }
            });
        }
    }

    void updateStatusOnClick(String id) {

        apiService.updateNotificationStatus(sharedPreferenceMethod.getTokenJWT(), id, "1").enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                Log.e("TAG", "onResponse: " + response.body().getResponse().getMessage());
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());

            }
        });
    }
}
