package com.doozycod.roadsidegenius.Adapter.AdminAdapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Activities.CompanyDetailsActvity;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.Company.Company;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.RecyclerHolder> {

    Context context;
    List<Company> companyList = new ArrayList<>();
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;

    public CompanyAdapter(Context context, List<Company> companyList, ApiService apiService) {
        this.context = context;
        this.companyList = companyList;
        this.apiService = apiService;
        sharedPreferenceMethod = new SharedPreferenceMethod(context);
        customProgressBar = new CustomProgressBar(context);
    }


    @NonNull
    @Override
    public CompanyAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_list_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.RecyclerHolder holder, int position) {

        holder.companyID.setText(companyList.get(position).getVendorId());
        holder.companyName.setText(companyList.get(position).getCompanyName());
        holder.companyAddress.setText(companyList.get(position).getCompanyAddress()
                + "\n" + companyList.get(position).getCompanyCity() + "\n" +
                companyList.get(position).getCompanyState());
        holder.companyEmail.setText(companyList.get(position).getCompanyEmail());
        holder.companyNumber.setText(companyList.get(position).getCompanyNumber());
        holder.editCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompanyDetailsActvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("company", companyList.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.deleteCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCompanyDialog(position, companyList.get(position).getId());
            }
        });
    }

    private void deleteCompanyDialog(int position, String id) {
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
                companyList.remove(position);
                deleteCompany(id);

            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView companyID, companyName, companyNumber, companyEmail, companyAddress;
        RelativeLayout getDetails;
        ImageView editCompanyButton, deleteCompanyButton;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            deleteCompanyButton = itemView.findViewById(R.id.deleteCompanyButton);
            editCompanyButton = itemView.findViewById(R.id.editCompanyButton);
            getDetails = itemView.findViewById(R.id.getDetails);
            companyEmail = itemView.findViewById(R.id.companyEmail);
            companyAddress = itemView.findViewById(R.id.companyAddress);
            companyNumber = itemView.findViewById(R.id.companyNumber);
            companyName = itemView.findViewById(R.id.companyTXT);
            companyID = itemView.findViewById(R.id.companyID);
        }
    }

    void deleteCompany(String id) {
        customProgressBar.showProgress();
        apiService.deleteCompany(sharedPreferenceMethod.getTokenJWT(), id).enqueue(new Callback<AdminRegisterModel>() {
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
                Log.e("TAG", "onFailure: " + t.getMessage());

            }
        });
    }

}
