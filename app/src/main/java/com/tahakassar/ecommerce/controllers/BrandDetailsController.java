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
            Log.e("pics", String.valueOf(result.get("pics")));
            ArrayList<String> urls = new ArrayList<>();
            JsonArray pics = result.get("data").getAsJsonObject().get("pics").getAsJsonArray();
            Log.e("pics", String.valueOf(pics.size()));
            for (int i = 0; i < pics.size(); i++) {
                Log.e("pics", pics.get(i).getAsString());
                urls.add(pics.get(i).getAsString());
            }
            Brand brand = new Brand(result.getAsJsonObject("data").get("id").getAsString(),
                    result.getAsJsonObject("data").get("title").getAsString(),
                    result.getAsJsonObject("data").get("icon").getAsString(),
                    result.getAsJsonObject("data").get("rating").getAsFloat(), urls,
                    result.getAsJsonObject("data").get("description").getAsString(),
                    result.getAsJsonObject("data").get("phone").getAsString(),
                    result.getAsJsonObject("data").get("lat").getAsString(),
                    result.getAsJsonObject("data").get("lng").getAsString());
            BrandDetailsActivity.initData(context, brand);
            Log.e("pics", brand.getIcon());
            dialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
