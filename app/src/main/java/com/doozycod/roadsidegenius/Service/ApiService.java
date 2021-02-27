package com.doozycod.roadsidegenius.Service;


import com.doozycod.roadsidegenius.Model.AddCustomerNumberModel.VerifyOTPModel;
import com.doozycod.roadsidegenius.Model.AdminJobListModel.AdminListModel;
import com.doozycod.roadsidegenius.Model.AdminRegisterModel;
import com.doozycod.roadsidegenius.Model.AssignedJobs.AssignJobsListModel;
import com.doozycod.roadsidegenius.Model.Company.CompanyModel;
import com.doozycod.roadsidegenius.Model.CompletedJobModel.CompletedJobListModel;
import com.doozycod.roadsidegenius.Model.Customer.CustomerLoginModel;
import com.doozycod.roadsidegenius.Model.DriverActiveJob.DriverActiveJobModel;
import com.doozycod.roadsidegenius.Model.DriverList.DriversListModel;
import com.doozycod.roadsidegenius.Model.DriverLogin.DriverLoginModel;
import com.doozycod.roadsidegenius.Model.ImageListModel.ImagesListModel;
import com.doozycod.roadsidegenius.Model.JobList.JobsListModel;
import com.doozycod.roadsidegenius.Model.JobRequestModel.JobRequestModel;
import com.doozycod.roadsidegenius.Model.Notification.NotificationListModel;
import com.doozycod.roadsidegenius.Model.OTP.OTPModel;
import com.doozycod.roadsidegenius.Model.PaymentList.PaymentListModel;
import com.doozycod.roadsidegenius.Model.ServiceList.ServiceListModel;
import com.doozycod.roadsidegenius.Model.WalletData.WalletDataModel;
import com.doozycod.roadsidegenius.Model.WithdrawList.WithdrawListModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

//  api Interface for Retrofit
public interface ApiService {
    
    //     get Suggested drivers list
    @POST("admin/get-suggested-drivers.php")
    @Multipart
    Call<DriversListModel> getSuggestedDriversList(
            @Field("jwt") String jwt,
            @Field("job_id") String job_id);
    //     Add Image
    @POST("job/add-image.php")
    @Multipart
    Call<ImagesListModel> addImageBeforeJob(
            @Part("jwt") RequestBody jwt,
            @Part("job_id") RequestBody job_id,
            @Part MultipartBody.Part image);


    //    Images delete
    @POST("job/delete-image.php")
    @FormUrlEncoded
    Call<ImagesListModel> deleteImages(
            @Field("jwt") String jwt,
            @Field("job_id") String job_id,
            @Field("imid") String imageId);

    //    update location
    @POST("driver/update-location.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> updateLocation(
            @Field("jwt") String jwt,
            @Field("location_lat") String location_lat,
            @Field("location_long") String location_long);


    //    delete company
    @POST("company/delete.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> deleteCompany(
            @Field("jwt") String jwt,
            @Field("company_id") String company_id);

    //    admin Login
    @POST("admin/login.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> adminLogin(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_id") String device_id,
            @Field("push_token") String push_token);

    //      withdraw list api request
    @POST("withdrawl/list.php")
    @FormUrlEncoded
    Call<WithdrawListModel> getWithdrawList(
            @Field("jwt") String jwt);

    @POST("job/completed.php")
    @FormUrlEncoded
    Call<CompletedJobListModel> completedJobsList(
            @Field("jwt") String jwt,
            @Field("driver_id") String driver_id,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date,
            @Field("customer_id") String customer_id);

    @POST("job/completed.php")
    @FormUrlEncoded
    Call<CompletedJobListModel> completedJobsAdminList(
            @Field("jwt") String jwt,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date);


    @POST("notification/list.php")
    @FormUrlEncoded
    Call<NotificationListModel> getNotifications(
            @Field("jwt") String jwt);

    @POST("notification/update-status.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> updateNotificationStatus(
            @Field("jwt") String jwt,
            @Field("id") String id,
            @Field("is_read") String is_read);


    @POST("withdrawl/update_status.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> withdrawRequest(
            @Field("jwt") String jwt,
            @Field("id") String id,
            @Field("status") String status);


    //    admin Login
    @POST("payment/save.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> paymentResponse(
            @Field("jwt") String jwt,
            @Field("job_id") String job_id,
            @Field("amount") String amount,
            @Field("bonus_amount") String bonus_amount,
            @Field("payment_method") String payment_method,
            @Field("client_secret") String client_secret,
            @Field("created") String created,
            @Field("currency") String currency,
            @Field("status") String status,
            @Field("payment_method_id") String payment_method_id,
            @Field("transaction_id") String transaction_id
    );

    //    admin Login
    @POST("payment/list.php")
    @FormUrlEncoded
    Call<PaymentListModel> paymentList(
            @Field("jwt") String jwt,
            @Field("start_date") String start_date,
            @Field("end_date") String end_date,
            @Field("customer_id") String customer_id,
            @Field("driver_id") String driver_id,
            @Field("job_id") String job_id);

    //    driver
    @POST("job/update-status.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> updateStatusForDriverJob(
            @Field("jwt") String jwt,
            @Field("job_id") String job_id,
            @Field("status") String status,
            @Field("start_timestamps") String start_timestamps,
            @Field("timezone") String timezone);

    //    admin Login
    @POST("job/update-status.php")
//    @FormUrlEncoded
    @Multipart
    Call<AdminRegisterModel> completedJobDriverAPIwithIMAGE(
            @Part("jwt") RequestBody jwt,
            @Part("job_id") RequestBody job_id,
            @Part("status") RequestBody status,
            @Part("comments") RequestBody comments,
            @Part("amount") RequestBody amount,
            @Part("payment_method") RequestBody payment_method,
            @Part("completed_timestamps") RequestBody completed_timestamps,
            @Part("time_taken") RequestBody time_taken,
            @Part MultipartBody.Part image);

    @POST("job/update-status.php")
    @Multipart
    Call<AdminRegisterModel> completedJobDriverAPI(
            @Part("jwt") RequestBody jwt,
            @Part("job_id") RequestBody job_id,
            @Part("status") RequestBody status,
            @Part("comments") RequestBody comments,
            @Part("amount") RequestBody amount,
            @Part("payment_method") RequestBody payment_method,
            @Part("completed_timestamps") RequestBody completed_timestamps,
            @Part("time_taken") RequestBody time_taken);

    //    admin complete Job
    @POST("job/update-status.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> completedJobAdmin(
            @Field("jwt") String jwt,
            @Field("job_id") String job_id,
            @Field("status") String status,
            @Field("payment_method") String payment_method);


    //    Customer Login
    @POST("customer/login.php")
    @FormUrlEncoded
    Call<CustomerLoginModel> customerLogin(
            @Field("phone") String number);

    //    Wallet data
    @POST("wallet/data.php")
    @FormUrlEncoded
    Call<WalletDataModel> driverWalletData(
            @Field("jwt") String jwt);

    //    Wallet data
    @POST("job/list-all.php")
    @FormUrlEncoded
    Call<AdminListModel> adminJobsList(
            @Field("jwt") String jwt);

    //    Wallet data
    @POST("withdrawl/request.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> requestPaymentDriver(
            @Field("jwt") String jwt,
            @Field("amount") String amount);

    //   Logout
    @POST("admin/logout.php")
    @FormUrlEncoded
    Call<CustomerLoginModel> adminLogout(
            @Field("jwt") String jwt,
            @Field("device_id") String device_id);

    //   Logout
    @POST("customer/logout.php")
    @FormUrlEncoded
    Call<CustomerLoginModel> customerLogout(
            @Field("jwt") String jwt,
            @Field("device_id") String device_id);

    //   Logout
    @POST("driver/logout.php")
    @FormUrlEncoded
    Call<CustomerLoginModel> driverLogout(
            @Field("jwt") String jwt,
            @Field("device_id") String device_id);

    //    reset Forget Admin
    @POST("admin/recover-password.php")
    @FormUrlEncoded
    Call<CustomerLoginModel> recoverPassword(
            @Field("email") String email);


    //    change password driver
    @POST("driver/update-password.php")
    @FormUrlEncoded
    Call<CustomerLoginModel> changeDriverPassword(
            @Field("jwt") String jwt,
            @Field("current_password") String current_password,
            @Field("new_password") String new_password);


    //    change password Admin
    @POST("admin/update-password.php")
    @FormUrlEncoded
    Call<CustomerLoginModel> changeAdminPassword(
            @Field("jwt") String jwt,
            @Field("current_password") String current_password,
            @Field("new_password") String new_password);

    //    reset Forget Admin
    @POST("driver/recover-password.php")
    @FormUrlEncoded
    Call<CustomerLoginModel> recoverDriverPassword(
            @Field("email") String email);

    //    get unassigned jobs
    @POST("job/unassigned.php")
    @FormUrlEncoded
    Call<JobsListModel> getJobsList(
            @Field("jwt") String jwt);

    //    Customer Login
    @POST("customer/add-number.php")
    @FormUrlEncoded
    Call<VerifyOTPModel> addCustomerNumber(
            @Field("jwt") String jwt,
            @Field("number") String number);

    //    Service List
    @POST("service/list-all.php")
    @FormUrlEncoded
    Call<ServiceListModel> serviceList(
            @Field("jwt") String jwt);

    //    Assign List
    @POST("job/assigned.php")
    @FormUrlEncoded
    Call<AssignJobsListModel> assignJobsList(
            @Field("jwt") String jwt);

    //    active job for driver
    @POST("driver/active-job.php")
    @FormUrlEncoded
    Call<DriverActiveJobModel> activeJobForDriver(
            @Field("jwt") String jwt);


    //    Customer Login
    @POST("customer/verify.php")
    @FormUrlEncoded
    Call<OTPModel> verifyOTP(
            @Field("jwt") String jwt,
            @Field("otp") String otp,
            @Field("device_id") String device_id,
            @Field("push_token") String push_token);

    //    Service Add
    @POST("service/add.php")
    @Multipart
    Call<AdminRegisterModel> addService(
            @Part("jwt") RequestBody jwt,
            @Part("type") RequestBody type,
            @Part("cost") RequestBody cost,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image);

    //    Service Add
    @POST("service/edit.php")
    @Multipart
    Call<AdminRegisterModel> editService(
            @Part("jwt") RequestBody jwt,
            @Part("id") RequestBody id,
            @Part("type") RequestBody type,
            @Part("cost") RequestBody cost,
            @Part("description") RequestBody description);

    //    Service Add
    @POST("service/edit.php")
    @Multipart
    Call<AdminRegisterModel> editServiceWithFile(
            @Part("jwt") RequestBody jwt,
            @Part("id") RequestBody id,
            @Part("type") RequestBody type,
            @Part("cost") RequestBody cost,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image);


    //    Service Add
    @POST("job/create.php")
    @FormUrlEncoded
    Call<JobRequestModel> createJobRequest(
            @Field("jwt") String jwt,
            @Field("customer_id") String customer_id,
            @Field("customer_name") String customer_name,
            @Field("customer_number") String customer_number,
            @Field("customer_pickup") String customer_pickup,
            @Field("customer_dropoff") String customer_dropoff,
            @Field("customer_email") String customer_email,
            @Field("service_needed") String service_needed,
            @Field("customer_notes") String customer_notes,
            @Field("amount_quoted") String amount_quoted,
            @Field("vehicle_make") String vehicle_make,
            @Field("vehicle_model") String vehicle_model,
            @Field("vehicle_color") String vehicle_color,
            @Field("vehicle_year") String vehicle_year);

    //    Service Add
    @POST("job/create.php")
    @FormUrlEncoded
    Call<JobRequestModel> createJob(
            @Field("jwt") String jwt,
//            @Field("customer_id") String customer_id,
            @Field("customer_name") String customer_name,
            @Field("customer_number") String customer_number,
            @Field("customer_pickup") String customer_pickup,
            @Field("customer_dropoff") String customer_dropoff,
            @Field("vehicle_make") String vehicle_make,
            @Field("vehicle_model") String vehicle_model,
            @Field("vehicle_color") String vehicle_color,
            @Field("vehicle_year") String vehicle_year,
            @Field("customer_email") String customer_email,
            @Field("service_needed") String service_needed,
            @Field("customer_notes") String customer_notes,
            @Field("amount_quoted") String amount_quoted);


    //    Assign new Job
    @POST("admin/assign-job.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> assignJob(
            @Field("jwt") String jwt,
            @Field("job_id") String job_id,
            @Field("driver_id") String driver_id,
            @Field("dispatch_date") String dispatch_date,
            @Field("site") String site,
            @Field("eta") String eta,
            @Field("status") String status,
            @Field("vehicle_make") String vehicle_make,
            @Field("vehicle_model") String vehicle_model,
            @Field("vehicle_color") String vehicle_color,
            @Field("dispatched") String dispatched,
            @Field("total_job_time") String total_job_time,
            @Field("total_miles") String total_miles,
            @Field("invoice_total") String invoice_total,
            @Field("comments") String comments,
            @Field("truck") String truck
    );

    //    Create Assign new Job
    @POST("job/create-assign.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> createAssignJob(
            @Field("jwt") String jwt,
//            @Field("job_id") String job_id,
            @Field("driver_id") String driver_id,
            @Field("dispatch_date") String dispatch_date,
            @Field("site") String site,
            @Field("eta") String eta,
            @Field("customer_name") String customer_name,
            @Field("customer_number") String customer_number,
            @Field("customer_pickup") String customer_pickup,
            @Field("customer_dropoff") String customer_dropoff,
            @Field("customer_email") String customer_email,
            @Field("service_needed") String service_needed,
            @Field("status") String status,
            @Field("vehicle_make") String vehicle_make,
            @Field("vehicle_model") String vehicle_model,
            @Field("vehicle_color") String vehicle_color,
            @Field("vehicle_year") String vehicle_year,
            @Field("dispatched") String dispatched,
            @Field("total_job_time") String total_job_time,
            @Field("total_miles") String total_miles,
            @Field("invoice_total") String invoice_total,
            @Field("comments") String comments,
            @Field("truck") String truck
    );


    //    upload SCA
    @Multipart
    @POST("driver/upload-docs.php")
    Call<AdminRegisterModel> driverUploadSCAFiles(
            @Part("jwt") RequestBody jwt,
            @Part MultipartBody.Part sca);

    //    upload COI
    @POST("driver/upload-docs.php")
//    @FormUrlEncoded
    @Multipart
    Call<AdminRegisterModel> driverUploadCOIFiles(
            @Part("jwt") RequestBody jwt,
            @Part MultipartBody.Part coi);

    //    upload W9 forms
    @POST("driver/upload-docs.php")
    @Multipart
    Call<AdminRegisterModel> driverUploadW9Files(
            @Part("jwt") RequestBody jwt,
            @Part MultipartBody.Part w_9_forms);

    //    company register
    @POST("company/register.php")
//    @FormUrlEncoded
    @Multipart
    Call<AdminRegisterModel> registerCompany(
            @Part("jwt") RequestBody jwt,
            @Part("email") RequestBody email,
            @Part("number") RequestBody number,
            @Part("name") RequestBody name,
            @Part("address") RequestBody address,
            @Part("city") RequestBody city,
            @Part("state") RequestBody state,
            @Part("zipcode") RequestBody zipcode,
            @Part("primary_phone") RequestBody primary_phone,
            @Part("secondary_phone") RequestBody secondary_phone,
            @Part("primary_service_area") RequestBody primary_service_area,
            @Part MultipartBody.Part i_9_forms,
            @Part MultipartBody.Part w_9_forms,
            @Part MultipartBody.Part coi);

    //    company register
    @POST("company/edit.php")
//    @FormUrlEncoded
    @Multipart
    Call<AdminRegisterModel> editCompany(
            @Part("jwt") RequestBody jwt,
            @Part("company_id") RequestBody company_id,
            @Part("email") RequestBody email,
            @Part("number") RequestBody number,
            @Part("name") RequestBody name,
            @Part("address") RequestBody address,
            @Part("city") RequestBody city,
            @Part("state") RequestBody state,
            @Part("zipcode") RequestBody zipcode,
            @Part("primary_phone") RequestBody primary_phone,
            @Part("secondary_phone") RequestBody secondary_phone,
            @Part("primary_service_area") RequestBody primary_service_area,
            @Part MultipartBody.Part i_9_forms,
            @Part MultipartBody.Part w_9_forms,
            @Part MultipartBody.Part coi);


    //    company register
    @POST("driver/login.php")
    @FormUrlEncoded
    Call<DriverLoginModel> loginDriver(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_id") String device_id,
            @Field("push_token") String push_token);

    //    driver register
    @POST("driver/register.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> registerDriver(
            @Field("jwt") String jwt,
            @Field("email") String email,
            @Field("number") String number,
            @Field("name") String name,
            @Field("vendor_id") String vendor_id,
//            @Field("driver_id") String driver_id,
            @Field("driver_address") String driver_address,
            @Field("driver_zipcode") String driver_zipcode,
            @Field("service_area") String service_area,
            @Field("pay_per_job") String pay_per_job,
            @Field("service_vehicle_type") String service_vehicle_type,
            @Field("service_vehicle_model") String service_vehicle_model,
            @Field("service_vehicle_year") String service_vehicle_year,
            @Field("service_vehicle_make") String service_vehicle_make
    );

    //    driver register
    @POST("driver/edit.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> editDriverDetails(
            @Field("jwt") String jwt,
            @Field("id") String id,
            @Field("email") String email,
            @Field("number") String number,
            @Field("name") String name,
            @Field("vendor_id") String vendor_id,
            @Field("driver_address") String driver_address,
            @Field("driver_zipcode") String driver_zipcode,
            @Field("service_area") String service_area,
            @Field("pay_per_job") String pay_per_job,
            @Field("service_vehicle_type") String service_vehicle_type,
            @Field("service_vehicle_model") String service_vehicle_model,
            @Field("service_vehicle_year") String service_vehicle_year,
            @Field("service_vehicle_make") String service_vehicle_make
    );

    //    get Company list
    @POST("driver/delete.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> deleteDriver(
            @Field("jwt") String jwt,
            @Field("id") String id);

    //    delete service
    @POST("service/delete.php")
    @FormUrlEncoded
    Call<AdminRegisterModel> deleteService(
            @Field("jwt") String jwt,
            @Field("id") String id);

    //    get Company list
    @POST("company/list-all.php")
    @FormUrlEncoded
    Call<CompanyModel> getCompanyLists(
            @Field("jwt") String jwt);

    //    get Drivers List
    @POST("driver/list-all.php")
    @FormUrlEncoded
    Call<DriversListModel> getDriverList(
            @Field("jwt") String jwt);

}
