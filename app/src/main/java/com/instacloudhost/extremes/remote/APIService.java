package com.instacloudhost.extremes.remote;

import com.instacloudhost.extremes.model.MStatus;
import com.instacloudhost.extremes.model.MWindForm;
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

    @GET("/api/states")
    Call <MStatus> states();

    /*
        get all customers
     */
    @POST("/api/cmobile")
    @FormUrlEncoded
    Call <String> customerMobile(@Field("mobile") String mobile);

    /*
        add Winds
     */
    @POST("/api/addwindcustomer")
    @FormUrlEncoded
    Call <MStatus> addWinds(@Field("shopname") String shopName,
                              @Field("aoro") String aoro,
                              @Field("pan") String pan,
                              @Field("mobile") String mobile,
                              @Field("email") String email,
                              @Field("ahn") String ahn,
                              @Field("bankname") String bankname,
                              @Field("bankaddress") String baddress,
                              @Field("accountno") String acno,
                              @Field("ifsc") String ifsc,
                              @Field("peraddress") String padd,
                              @Field("dob") String dob,
                              @Field("pin") String pin,
                              @Field("fhname") String fhname,
                              @Field("vidano") String vidno,
                              @Field("nominee") String nominee,
                              @Field("customer_name") String customername,
                              @Field("state") String state,
                              @Field("district") String district,
                              @Field("gender") String gender,
                              @Field("relation") String relation,
                              @Field("agent_id") String agentId
    );

    /*
        Winds Files Upload
     */
    @POST("/api/windsupload")
    @Multipart
    Call <MStatus> WindsUpload(@Part("customer_id") RequestBody customerID,
                               @Part MultipartBody.Part adhrf,
                               @Part MultipartBody.Part adhrb,
                               @Part MultipartBody.Part pan,
                               @Part MultipartBody.Part shop_pic,
                               @Part MultipartBody.Part pass,
                               @Part MultipartBody.Part selfie);


    @POST("/api/listdistrict")
    @FormUrlEncoded
    Call <String> listDistrict(@Field("state") String state);
}
