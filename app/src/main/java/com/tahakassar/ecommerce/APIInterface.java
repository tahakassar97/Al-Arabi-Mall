package com.tahakassar.ecommerce;

import com.google.gson.JsonObject;
import com.tahakassar.ecommerce.models.User;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @POST("sfew/")
    @Headers({"Accept: application/json"})
    @Multipart
    Call<JsonObject> register(@Part("service") RequestBody service, @Part("username") RequestBody username,
                                @Part("password") RequestBody password, @Part("name") RequestBody name,
                                @Part("lat") RequestBody lat, @Part("lng") RequestBody lng);

    @POST("sfew/")
    @Headers({"Accept: application/json"})
    @Multipart
    Call<JsonObject> login(@Part("service") RequestBody service, @Part("username") RequestBody username,
                           @Part("password") RequestBody password);

    @POST("sfew/")
    @Headers({"Accept: application/json"})
    @Multipart
    Call<JsonObject> aboutUs(@Part("service") RequestBody service);

    @POST("sfew/")
    @Headers({"Accept: application/json"})
    @Multipart
    Call<JsonObject> updateProfile(@Part("service") RequestBody service, @Part("username") RequestBody username,
                                   @Part("password") RequestBody password, @Part("fullname") RequestBody name,
                                   @Part("lat") RequestBody lat, @Part("lng") RequestBody lng,
                                   @Part("user_id") RequestBody userID);

    @POST("sfew/")
    @Headers({"Accept: application/json"})
    @Multipart
    Call<JsonObject> getCategories(@Part("service") RequestBody service);

    @POST("sfew/")
    @Headers({"Accept: application/json"})
    @Multipart
    Call<JsonObject> getBrands(@Part("service") RequestBody service, @Part("cat_id") RequestBody categoryID);

    @POST("sfew/")
    @Headers({"Accept: application/json"})
    @Multipart
    Call<JsonObject> getBrandDetails(@Part("service") RequestBody service, @Part("id") RequestBody brandID);

    @POST("sfew/")
    @Headers({"Accept: application/json"})
    @Multipart
    Call<JsonObject> setRate(@Part("service") RequestBody service, @Part("shop_id") RequestBody shopID,
                             @Part("user_id") RequestBody userID, @Part("stars") Float rating);

}
