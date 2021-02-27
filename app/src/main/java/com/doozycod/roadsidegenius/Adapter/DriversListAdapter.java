package com.doozycod.roadsidegenius.Adapter;

import android.app.Dialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Activities.Driver.EditActivity;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.DriverList.Driver;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriversListAdapter extends RecyclerView.Adapter<DriversListAdapter.RecyclerHolder> {
    Context context;
    List<Driver> driverList = new ArrayList<>();
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;

    public DriversListAdapter(Context context, List<Driver> driverList, ApiService apiService) {
        this.context = context;
        this.driverList = driverList;
        customProgressBar = new CustomProgressBar(context);
        this.apiService = apiService;
        sharedPreferenceMethod = new SharedPreferenceMethod(context);

    }

    @NonNull
    @Override
    public DriversListAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_list_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriversListAdapter.RecyclerHolder holder, int position) {
        Driver driver = driverList.get(position);
        holder.vendorId.setText(driver.getVendorId());
        holder.driverNumber.setText(driver.getDriverNumber());
        holder.driverNameTxt.setText(driver.getDriverName());
        holder.serviceArea.setText(driver.getServiceArea());
        holder.payperjobTxt.setText(driver.getPayPerJob());
        holder.editDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AddDriverFragment myFragment = new AddDriverFragment();
                Bundle args = new Bundle();
                args.putSerializable("driver", (Serializable) driver);
                Intent intent = new Intent(context, EditActivity.class);

                intent.putExtras(args);
                context.startActivity(intent);

            }
        });
        holder.deleteDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDriverDialog(position, driver.getId());
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
                driverList.remove(position);
                deleteDriver(id);

            }
        });
        dialog.show();
    }

    void deleteDriver(String id) {
        customProgressBar.showProgress();
        apiService.deleteDriver(sharedPreferenceMethod.getTokenJWT(), id).enqueue(new Callback<AdminRegisterModel>() {
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
    public int getItemCount() {
        return driverList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView vendorId, driverNumber, driverNameTxt, serviceArea, payperjobTxt;
        ImageView editDriverButton, deleteDriverButton;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            deleteDriverButton = itemView.findViewById(R.id.deleteDriverButton);
            editDriverButton = itemView.findViewById(R.id.editDriverButton);
            vendorId = itemView.findViewById(R.id.vendorId);
            driverNumber = itemView.findViewById(R.id.driverNumber);
            driverNameTxt = itemView.findViewById(R.id.driverNameTxt);
            serviceArea = itemView.findViewById(R.id.serviceArea);
            payperjobTxt = itemView.findViewById(R.id.payperjobTxt);
        }
    }
}
