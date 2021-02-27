package com.doozycod.roadsidegenius.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doozycod.roadsidegenius.Activities.Admin.AssignJobActivity;
import com.doozycod.roadsidegenius.Adapter.AdminPaymentsAdapter;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.DriverList.Driver;
import com.doozycod.roadsidegenius.Model.DriverList.DriversListModel;
import com.doozycod.roadsidegenius.Model.PaymentList.Payment;
import com.doozycod.roadsidegenius.Model.PaymentList.PaymentListModel;
import com.doozycod.roadsidegenius.R;
import com.doozycod.roadsidegenius.Service.ApiService;
import com.doozycod.roadsidegenius.Service.ApiUtils;
import com.doozycod.roadsidegenius.Utils.CustomProgressBar;
import com.doozycod.roadsidegenius.Utils.SharedPreferenceMethod;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminPaymentsFragment extends Fragment {

    private SimpleDateFormat month_date;
    private YearMonth yearMonthObject;
    private List<Payment> payments = new ArrayList<>();

    public AdminPaymentsFragment() {
        // Required empty public constructor
    }

    Calendar calendar;
    private List<String> monthList;
    private List<String> dateList;
    private List<String> yearList;
    private Spinner monthSpinner, yearSpinner;
    String year;
    int month;
    SharedPreferenceMethod sharedPreferenceMethod;
    CustomProgressBar customProgressBar;
    ApiService apiService;
    List<Driver> driverList = new ArrayList<>();
    List<String> driverNameList = new ArrayList<>();
    List<String> driverIdList = new ArrayList<>();
    Spinner driverSpinner;
    ArrayAdapter aa;
    Button searchButton;
    RecyclerView recyclerView;
    TextView text, paymentTotalTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context contextThemeWrapper;
        if (sharedPreferenceMethod != null) {
            contextThemeWrapper = new ContextThemeWrapper(getActivity(),
                    sharedPreferenceMethod.getTheme().equals("light") ? R.style.LightTheme : R.style.DarkTheme);
        } else {
            contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.LightTheme);

        }
        // create ContextThemeWrapper from the original Activity Context with the custom theme

// clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // Inflate the layout for this fragment
        View view = localInflater.inflate(R.layout.fragment_admin_payments, container, false);
        month_date = new SimpleDateFormat("MMMM");
        text = view.findViewById(R.id.text);
        paymentTotalTxt = view.findViewById(R.id.paymentTotalTxt);
        driverSpinner = view.findViewById(R.id.driverSpinner);
        searchButton = view.findViewById(R.id.searchButton);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        recyclerView = view.findViewById(R.id.recyclerView);
//        calendar = Calendar.getInstance();
        sharedPreferenceMethod = new SharedPreferenceMethod(getActivity());
        customProgressBar = new CustomProgressBar(getActivity());
        apiService = ApiUtils.getAPIService();


        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);
        year = String.valueOf(calendar.get(Calendar.YEAR));


        calListMethod();
        // Creating adapter for spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, monthList);
        // Drop down layout style - list view with radio button
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);
        // Creating adapter for spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, yearList);
        // Drop down layout style - list view with radio button
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
        calendar.set(Calendar.MONTH, month);
        String month_name = month_date.format(calendar.getTime());

        for (int i = 0; i < yearList.size(); i++) {
            if (year.equals(yearList.get(i))) {
                yearSpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < monthList.size(); i++) {
            if (month_name.equals(monthList.get(i))) {
                monthSpinner.setSelection(i);
//                Log.e("Selected", "month " + i);
                break;
            }
        }

//        recyclerview INIT
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getPaymentList();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (monthSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Select month", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (yearSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Select month", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (driverSpinner.getSelectedItemPosition() == 0) {
                        getPaymentList();
                    } else {
                        getPaymentList(driverSpinner.getSelectedItemPosition());
                    }
                }
            }
        });
        return view;
    }

    void getPaymentList() {
        int iYear = Integer.parseInt(yearList.get(yearSpinner.getSelectedItemPosition()));
        int iMonth = Integer.parseInt(dateList.get(monthSpinner.getSelectedItemPosition()));

// Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(iYear, iMonth - 1, 1);
// Get the number of days in that month
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        String startDate = 1 + "-" + iMonth + "-" + iYear;
        String endDate = daysInMonth + "-" + iMonth + "-" + iYear;


        Log.e("TAG", "getPaymentList: " + startDate + "  " + endDate);
//        yearMonthObject = YearMonth.of(2000, 2);
//        daysInMonth = yearMonthObject.lengthOfMonth();
        customProgressBar.showProgress();
        apiService.paymentList(sharedPreferenceMethod.getTokenJWT(), startDate, endDate, "",
                "", "").enqueue(new Callback<PaymentListModel>() {
            @Override
            public void onResponse(Call<PaymentListModel> call, Response<PaymentListModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    payments = response.body().getResponse().getPayments();
                    if (payments.size() > 0) {
                        text.setVisibility(View.GONE);
                    } else {
                        text.setVisibility(View.VISIBLE);
                        paymentTotalTxt.setText("$0.00");
                    }
                    int paymentAmount = 0;
                    for (int i = 0; payments.size() > i; i++) {
                        paymentAmount = paymentAmount + Integer.parseInt(payments.get(i).getAmount());
                    }
                    paymentTotalTxt.setText("$" + paymentAmount);
                    recyclerView.setAdapter(new AdminPaymentsAdapter(getActivity(), payments));
                }
            }

            @Override
            public void onFailure(Call<PaymentListModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                customProgressBar.hideProgress();

            }
        });
    }

    void getPaymentList(int driver) {
        int iYear = Integer.parseInt(yearList.get(yearSpinner.getSelectedItemPosition()));
        int iMonth = Integer.parseInt(dateList.get(monthSpinner.getSelectedItemPosition()));

// Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(iYear, iMonth - 1, 1);
// Get the number of days in that month
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

        String startDate = 1 + "-" + iMonth + "-" + iYear;
        String endDate = daysInMonth + "-" + iMonth + "-" + iYear;


        Log.e("TAG", "getPaymentList: " + startDate + "  " + endDate);
//        yearMonthObject = YearMonth.of(2000, 2);
//        daysInMonth = yearMonthObject.lengthOfMonth();
        customProgressBar.showProgress();
        apiService.paymentList(sharedPreferenceMethod.getTokenJWT(), startDate, endDate, "",
                driverIdList.get(driver), "").enqueue(new Callback<PaymentListModel>() {
            @Override
            public void onResponse(Call<PaymentListModel> call, Response<PaymentListModel> response) {
                customProgressBar.hideProgress();

                if (response.body().getResponse().getStatus().equals("Success")) {
                    payments = response.body().getResponse().getPayments();
                    if (payments.size() > 0) {
                        text.setVisibility(View.GONE);
//                        for(int i)
                    } else {
                        text.setVisibility(View.VISIBLE);
                        paymentTotalTxt.setText("$0.00");

                    }

                    int paymentAmount = 0;
                    for (int i = 0; payments.size() > i; i++) {
                        paymentAmount = paymentAmount + Integer.parseInt(payments.get(i).getAmount());
                    }
                    paymentTotalTxt.setText("$" + paymentAmount);
                    recyclerView.setAdapter(new AdminPaymentsAdapter(getActivity(), payments));
                }
            }

            @Override
            public void onFailure(Call<PaymentListModel> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage());
                customProgressBar.hideProgress();

            }
        });
    }

    private void calListMethod() {
        monthList = new ArrayList<>();
        yearList = new ArrayList<>();
        dateList = new ArrayList<>();
        monthList.add("Month");
        dateList.add("Month");
        for (int i = 0; i < 12; i++) {
            if (i < 10) {
                dateList.add("0" + (i + 1));
            } else {
                dateList.add("" + (i + 1));

            }
            calendar.set(Calendar.MONTH, i);
            String month_name = month_date.format(calendar.getTime());
            monthList.add(month_name);
        }
        yearList.add("Year");
        for (int i = 2019; i < 2100; i++) {
            yearList.add(i + "");
        }
        getDriverList();
    }

    void getDriverList() {
        apiService.getDriverList(sharedPreferenceMethod.getTokenJWT()).enqueue(new Callback<DriversListModel>() {
            @Override
            public void onResponse(Call<DriversListModel> call, Response<DriversListModel> response) {
                if (response.body().getResponse().getStatus().equals("Success")) {
                    driverIdList.add("Select");
                    driverNameList.add("All Payments");
                    if (response.body().getResponse().getDrivers().size() > 0) {
                        driverList = response.body().getResponse().getDrivers();
                        for (int i = 0; i < driverList.size(); i++) {
                            driverIdList.add(driverList.get(i).getId());
                            driverNameList.add(driverList.get(i).getDriverName());
                        }
                        aa = new ArrayAdapter(getActivity(),
                                android.R.layout.simple_spinner_item, driverNameList);
                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        driverSpinner.setAdapter(aa);

                    }
                }
            }

            @Override
            public void onFailure(Call<DriversListModel> call, Throwable t) {

            }
        });
    }

}