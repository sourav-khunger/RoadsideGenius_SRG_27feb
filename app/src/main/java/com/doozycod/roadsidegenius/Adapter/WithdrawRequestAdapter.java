package com.doozycod.roadsidegenius.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doozycod.roadsidegenius.Model.WithdrawList.Approved;
import com.doozycod.roadsidegenius.R;

import java.util.ArrayList;
import java.util.List;

public class WithdrawRequestAdapter extends RecyclerView.Adapter<WithdrawRequestAdapter.RecyclerHolder> {
    Context context;
    List<Approved> pendings = new ArrayList<>();
//    SharedPreferenceMethod sharedPreferenceMethod;
//    ApiService apiService;
//    CustomProgressBar customProgressBar;


    public WithdrawRequestAdapter(Context context, List<Approved> pendings) {
        this.context = context;
        this.pendings = pendings;
//        sharedPreferenceMethod = new SharedPreferenceMethod(context);
//        apiService = ApiUtils.getAPIService();
//        customProgressBar = new CustomProgressBar(context);
    }

    @NonNull
    @Override
    public WithdrawRequestAdapter.RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_listview, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawRequestAdapter.RecyclerHolder holder, int position) {
        Approved pending = pendings.get(position);
        holder.approveButton.setVisibility(View.GONE);
        holder.driverNameTxt.setText(pending.getDriverName());
        holder.emailTxt.setText(pending.getDriverEmail());
        if (pending.getStatus().equals("Approved")) {
            holder.statusTxt.setTextColor(context.getResources().getColor(R.color.quantum_googgreen));
        }
        holder.statusTxt.setText(pending.getStatus());
        holder.amountTxt.setText("$" + pending.getAmount());
//        holder.approveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ApprovePendingRequestActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("pending", pending);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return pendings.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView driverNameTxt, emailTxt, statusTxt, amountTxt;
        Button approveButton;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            approveButton = itemView.findViewById(R.id.approveButton);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            driverNameTxt = itemView.findViewById(R.id.driverNameTxt);
        }
    }
}
