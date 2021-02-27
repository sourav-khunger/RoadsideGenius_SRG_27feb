package com.doozycod.roadsidegenius.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Activities.CustomerDetailsActivity;
import com.doozycod.roadsidegenius.Fragments.TaskListFragment;
import com.doozycod.roadsidegenius.Activities.JobDetailsDriverActivity;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.AssignedJobs.Job;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignJobDriverAdapter extends RecyclerView.Adapter<AssignJobDriverAdapter.RecyclerHolder> {

    Context context;
    List<Job> jobs = new ArrayList<>();
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    String timeZone = "";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    CustomProgressBar customProgressBar;

    public AssignJobDriverAdapter(Context context, List<Job> jobs, ApiService apiService) {
        this.context = context;
        this.jobs = jobs;
        this.apiService = apiService;
        sharedPreferenceMethod = new SharedPreferenceMethod(context);
        customProgressBar = new CustomProgressBar(context);
        TimeZone tz = TimeZone.getDefault();
        timeZone = tz.getDisplayName(false, TimeZone.SHORT);
    }

    @NonNull
    @Override
    public AssignJobDriverAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_job_recyclerview_driver, parent, false);
//        Log.e("TAG", "startJobAPI: " + simpleDateFormat.format(new Date()));

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignJobDriverAdapter.RecyclerHolder holder, int position) {
        holder.customerNameTxt.setText(jobs.get(position).getCustomerName());
        holder.phoneTxt.setText("+" + jobs.get(position).getCustomerNumber());
        holder.emailTxt.setText(jobs.get(position).getCustomerEmail());
        holder.dispatcherNameTxt.setText(jobs.get(position).getDispatcherName());
        holder.DriverNameTxt.setText(jobs.get(position).getDriver());
        holder.serviceTypeTxt.setText(jobs.get(position).getService());
        holder.pickupTxt.setText(jobs.get(position).getCustomerPickup());
        holder.dropoffTxt.setText(jobs.get(position).getCustomerDropoff());
        holder.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startJobAPI(position);
            }
        });
        holder.viewJobDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomerDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("driver", jobs.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    void startJobAPI(int pos) {
        customProgressBar.showProgress();
        apiService.updateStatusForDriverJob(sharedPreferenceMethod.getTokenJWT(), jobs.get(pos).getJobId(),
                "Started", simpleDateFormat.format(new Date()), timeZone).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().toLowerCase().equals("success")) {
                    Toast.makeText(context, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    AppCompatActivity activity = (AppCompatActivity) context;

                    TaskListFragment fragment = new TaskListFragment();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();

                } else {
                    Toast.makeText(context, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e("TAG", "startJobAPI: " + t.getMessage());
                customProgressBar.hideProgress();

            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt, phoneTxt, emailTxt, dispatcherNameTxt, DriverNameTxt,
                serviceTypeTxt, amount_quoted, pickupTxt, dropoffTxt;
        Button startButton;
        RelativeLayout viewJobDetails;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            startButton = itemView.findViewById(R.id.startButton);
            dropoffTxt = itemView.findViewById(R.id.dropoffTxt);
            pickupTxt = itemView.findViewById(R.id.pickupTxt);
            viewJobDetails = itemView.findViewById(R.id.viewJobDetails);
            serviceTypeTxt = itemView.findViewById(R.id.serviceTypeTxt);
            DriverNameTxt = itemView.findViewById(R.id.DriverNameTxt);
            dispatcherNameTxt = itemView.findViewById(R.id.dispatcherNameTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
        }
    }
}
