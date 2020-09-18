package com.instacloudhost.extremes.remote;

import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.model.Mgraph;
import com.instacloudhost.extremes.model.Mlogin;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    /*
        Check users
     */
    @POST("/api/login")
    @FormUrlEncoded
    Call <Mlogin> checkUser(@Field("username") String username,
                            @Field("password") String password);

    /*
        get all customers
     */
    @POST("/api/graph")
    @FormUrlEncoded
    Call <Mgraph> graph(@Field("agent_id") String agent);

    /*
        get all customers
     */
    @POST("/api/tracking")
    @FormUrlEncoded
    Call <MStatus> tracking(@Field("latitude") String lat,
                            @Field("longitude") String longi,
                            @Field("agent_id") String agents);

    /*
        get all customers
     */
    @POST("/api/addcustomer")
    @Multipart
    Call <MStatus> addCustomer(@Part("agent_id") RequestBody agentId,
                               @Part("customer_name") RequestBody customerName,
                               @Part("mobile") RequestBody mobile,
                               @Part("location") RequestBody location,
                               @Part("category") RequestBody category,
                               @Part("customs") RequestBody customs,
                               @Part MultipartBody.Part selfie,
                               @Part MultipartBody.Part proof);

    @GET("/api/checkversion")
    Call <String> check_version();

    /*
        get all customers
     */
    @POST("/api/cmobile")
    @FormUrlEncoded
    Call <String> customerMobile(@Field("mobile") String mobile);


}
