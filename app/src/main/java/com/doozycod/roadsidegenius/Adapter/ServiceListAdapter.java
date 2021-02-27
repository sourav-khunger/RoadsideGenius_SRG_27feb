package com.doozycod.roadsidegenius.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.doozycod.roadsidegenius.Activities.Admin.EditServiceActivity;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.ServiceList.Service;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Utils.Constants;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.RecyclerHolder> {
    Context context;
    List<Service> services = new ArrayList<>();
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;

    public ServiceListAdapter(Context context, List<Service> services, ApiService apiService) {
        this.context = context;
        this.services = services;
        customProgressBar = new CustomProgressBar(context);
        this.apiService = apiService;
        sharedPreferenceMethod = new SharedPreferenceMethod(context);

    }

    @NonNull
    @Override
    public ServiceListAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListAdapter.RecyclerHolder holder, int position) {
        Service service = services.get(position);

        boolean isExpanded = service.isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.serviceCost.setText("$" + service.getServiceCost());
        holder.serviceTypeTXT.setText(service.getServiceType());
//        holder.serviceID.setText(service.getServiceId());
        holder.descriptionTxt.setText(service.getServiceDescription());
//        if (!service.getServiceImage().equals("")) {
        Glide.with(context)
                .load(Constants.FILES_BASE_URL + service.getServiceImage())
                .placeholder(R.drawable.plac)
//                .dontAnimate()
//                .dontTransform()
//                .priority(Priority.IMMEDIATE)
//                .encodeFormat(Bitmap.CompressFormat.PNG)
//                .signature(new MediaStoreSignature(service.getServiceImage(), System.currentTimeMillis(), 1))
                .into(holder.serviceImage);
//        Picasso.get()
//                .load(Constants.FILES_BASE_URL + service.getServiceImage())
//                .placeholder(R.drawable.plac)
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .into(holder.serviceImage);
//        }
        holder.deleteDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDriverDialog(position, service.getId());
            }
        });
        holder.editServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditServiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("service", service);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    private void deleteDriverDialog(int position, String id) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_driver_record_dialog);
        Button yesButton = dialog.findViewById(R.id.yesButton);
        Button noButton = dialog.findViewById(R.id.cancelDeletion);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                services.remove(position);
                deleteDriver(id);

            }
        });
        dialog.show();
    }

    void deleteDriver(String id) {
        customProgressBar.showProgress();
        apiService.deleteService(sharedPreferenceMethod.getTokenJWT(), id).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    notifyDataSetChanged();
                    Toast.makeText(context, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                customProgressBar.hideProgress();

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView serviceID, serviceTypeTXT, serviceCost, descriptionTxt;
        ImageView editServiceButton, serviceDetails, deleteDriverButton;
        ConstraintLayout expandableLayout;
        CircleImageView serviceImage;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            editServiceButton = itemView.findViewById(R.id.editServiceButton);
            serviceDetails = itemView.findViewById(R.id.serviceDetails);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
            serviceCost = itemView.findViewById(R.id.serviceCost);
            serviceTypeTXT = itemView.findViewById(R.id.serviceTypeTXT);
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
