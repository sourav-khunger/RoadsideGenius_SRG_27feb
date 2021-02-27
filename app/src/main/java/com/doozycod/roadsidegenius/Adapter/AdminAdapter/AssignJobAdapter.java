package com.doozycod.roadsidegenius.Adapter.AdminAdapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.AssignedJobs.Job;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignJobAdapter extends RecyclerView.Adapter<AssignJobAdapter.RecyclerHolder> {

    Context context;
    List<Job> jobs = new ArrayList<>();
    ApiService apiService;
    SharedPreferenceMethod sharedPreferenceMethod;
    OnJobFinishCallback onJobFinishCallback;

    public AssignJobAdapter(Context context, List<Job> jobs, ApiService apiService, OnJobFinishCallback onJobFinishCallback) {
        this.context = context;
        this.jobs = jobs;
        this.apiService = apiService;
        this.onJobFinishCallback = onJobFinishCallback;
        sharedPreferenceMethod = new SharedPreferenceMethod(context);

    }

    public interface OnJobFinishCallback {
        void onJobFinish(boolean refresh);
    }


    @NonNull
    @Override
    public AssignJobAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_job_recyclerview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignJobAdapter.RecyclerHolder holder, int position) {
        holder.completeButton.setVisibility(sharedPreferenceMethod.getLogin().equals("admin") ? View.VISIBLE : View.GONE);

        holder.customerNameTxt.setText(jobs.get(position).getCustomerName());
        holder.phoneTxt.setText("+" + jobs.get(position).getCustomerNumber());
        holder.emailTxt.setText(jobs.get(position).getCustomerEmail());
        holder.dispatcherNameTxt.setText(jobs.get(position).getDispatcherName());
        holder.DriverNameTxt.setText(jobs.get(position).getDriver());
        holder.serviceTypeTxt.setText(jobs.get(position).getService());
        holder.pickupTxt.setText(jobs.get(position).getCustomerPickup());
        holder.dropoffTxt.setText(jobs.get(position).getCustomerDropoff());
        holder.amountTxt.setText("$"+jobs.get(position).getInvoiceTotal());

        TimeZone tz = TimeZone.getDefault();
//        Log.e("TAG", "onBindViewHolder: "+tz.getDisplayName(false, TimeZone.SHORT));
        holder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatusDialog(context, jobs.get(position));
            }
        });
    }


    void showStatusDialog(Context context, Job job) {

        Spinner statusSpinner;
        List<String> statusList = new ArrayList<>();
        ArrayAdapter arrayAdapter;
        Button finishButton;
        final String TAG = "AdminCompleteJob";
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.finish_status_dialog);

        statusSpinner = dialog.findViewById(R.id.statusSpinner);
        finishButton = dialog.findViewById(R.id.completeButton);
        statusList.add("Completed");
        statusList.add("Gone_On_Arrival");
        statusList.add("Cancelled");

        arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, statusList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(arrayAdapter);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = statusSpinner.getSelectedItemPosition();
                if (status == 0) {
                    completeJobByAdmin("Online", statusList.get(status), job, dialog);
                } else {
                    completeJobByAdmin("", statusList.get(status), job, dialog);
                }
            }
        });
        dialog.show();
    }


    private void completeJobByAdmin(String payment, String status, Job job, Dialog dialog) {
        apiService.completedJobAdmin(sharedPreferenceMethod.getTokenJWT(),
                job.getJobId(), status, payment).enqueue(new Callback<AdminRegisterModel>() {
            @Override
            public void onResponse(Call<AdminRegisterModel> call, Response<AdminRegisterModel> response) {
                if (response.body().getResponse().getStatus().equals("Success")) {
                    Toast.makeText(context, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    onJobFinishCallback.onJobFinish(true);
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AdminRegisterModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView customerNameTxt, phoneTxt, emailTxt, dispatcherNameTxt, DriverNameTxt,
                serviceTypeTxt, amountTxt, pickupTxt, dropoffTxt;
        Button completeButton;
        CardView cardView;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            completeButton = itemView.findViewById(R.id.completeButton);
            dropoffTxt = itemView.findViewById(R.id.dropoffTxt);
            pickupTxt = itemView.findViewById(R.id.pickupTxt);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            serviceTypeTxt = itemView.findViewById(R.id.serviceTypeTxt);
            DriverNameTxt = itemView.findViewById(R.id.DriverNameTxt);
            dispatcherNameTxt = itemView.findViewById(R.id.dispatcherNameTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            customerNameTxt = itemView.findViewById(R.id.customerNameTxt);
        }
    }
}
