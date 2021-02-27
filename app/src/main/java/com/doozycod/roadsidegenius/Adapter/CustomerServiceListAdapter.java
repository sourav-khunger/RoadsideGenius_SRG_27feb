package com.doozycod.roadsidegenius.Adapter;

import android.app.Dialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doozycod.roadsidegenius.Activities.RequestActivity;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.ServiceList.Service;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Utils.Constants;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerServiceListAdapter extends RecyclerView.Adapter<CustomerServiceListAdapter.RecyclerHolder> {
    Context context;
    List<Service> services = new ArrayList<>();
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;

    public CustomerServiceListAdapter(Context context, List<Service> services, ApiService apiService) {
        this.context = context;
        this.services = services;
        customProgressBar = new CustomProgressBar(context);
        this.apiService = apiService;
        sharedPreferenceMethod = new SharedPreferenceMethod(context);

    }

    @NonNull
    @Override
    public CustomerServiceListAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_service_list_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerServiceListAdapter.RecyclerHolder holder, int position) {
        Service service = services.get(position);
        boolean isExpanded = service.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.serviceCost.setText("$"+service.getServiceCost());
        holder.serviceTypeTXT.setText(service.getServiceType());
//        holder.serviceID.setText(service.getServiceId());
        Glide.with(context)
                .load(Constants.FILES_BASE_URL + service.getServiceImage())
                .placeholder(R.drawable.plac)
                .into(holder.serviceImage);
        holder.descriptionTxt.setText(service.getServiceDescription());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RequestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("service", service);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return services.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView serviceID, serviceTypeTXT, serviceCost, descriptionTxt;
        ImageView editDriverButton,serviceDetails,  deleteDriverButton;
        CardView cardView;
        ConstraintLayout expandableLayout;
        CircleImageView serviceImage;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            serviceDetails = itemView.findViewById(R.id.serviceDetails);
            cardView = itemView.findViewById(R.id.cardView);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
            serviceCost = itemView.findViewById(R.id.serviceCost);
            serviceTypeTXT = itemView.findViewById(R.id.serviceTypeTXT);
            serviceImage = itemView.findViewById(R.id.serviceImage);
//            serviceID = itemView.findViewById(R.id.serviceID);
            deleteDriverButton = itemView.findViewById(R.id.deleteDriverButton);
            serviceDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Service service = services.get(getAdapterPosition());
                    service.setExpanded(!service.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
