package com.tahakassar.ecommerce.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tahakassar.ecommerce.APIInterface;
import com.tahakassar.ecommerce.BrandDetailsActivity;
import com.tahakassar.ecommerce.models.Brand;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tahakassar.ecommerce.BasicData.BASE_URL;

public class BrandDetailsController implements Callback<JsonObject> {

    Context context;
    ProgressDialog dialog;

    public void start(Context context, RequestBody service, RequestBody brandID) {
        this.context = context;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS);
        httpClient.addInterceptor(loggingInterceptor);
        Gson gson = new GsonBuilder()
                .setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        APIInterface apiInterface = retrofit.create(APIInterface.class);

        dialog = new ProgressDialog(context);
        dialog.setMessage("Please waiting ...");
        dialog.setCancelable(false);
        dialog.show();
        Call<JsonObject> call = apiInterface.getBrandDetails(service, brandID);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if (response.isSuccessful()) {
            JsonObject result = response.body();
            ArrayList<String> urls = new ArrayList<>();
            JsonArray pics = result.get("data").getAsJsonObject().get("pics").getAsJsonArray();
            for (int i = 0; i < pics.size(); i++) {
                urls.add(pics.get(i).getAsString());
            }
            float rating = 0f;
            try {
                if (result.getAsJsonObject("data").get("rating") != null) {
                    rating = result.getAsJsonObject("data").get("rating").getAsFloat();
                }
            } catch (Exception e) {
                Log.e("ExceptionE", e.getMessage());
            }

            Brand brand = new Brand(result.getAsJsonObject("data").get("id").getAsString(),
                    result.getAsJsonObject("data").get("title").getAsString(),
                    result.getAsJsonObject("data").get("icon").getAsString(),
                    rating, urls,
                    result.getAsJsonObject("data").get("description").getAsString(),
                    result.getAsJsonObject("data").get("phone").getAsString(),
                    result.getAsJsonObject("data").get("lat").getAsString(),
                    result.getAsJsonObject("data").get("lng").getAsString(),
                    result.getAsJsonObject("data").get("location").getAsString());
            BrandDetailsActivity.initData(context, brand);
            dialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
