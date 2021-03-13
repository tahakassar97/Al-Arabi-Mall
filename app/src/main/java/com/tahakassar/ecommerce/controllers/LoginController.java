package com.tahakassar.ecommerce.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tahakassar.ecommerce.APIInterface;
import com.tahakassar.ecommerce.BasicData;
import com.tahakassar.ecommerce.LoginActivity;
import com.tahakassar.ecommerce.MainActivity;

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


public class LoginController implements Callback<JsonObject> {

    Context context;
    ProgressDialog dialog;

    public void start(Context context, RequestBody service, RequestBody username, RequestBody password) {
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
        Call<JsonObject> call = apiInterface.login(service, username, password);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if (response.isSuccessful()) {
            BasicData basicData = BasicData.getInstance(context);
            JsonObject user = response.body();
            Log.e("loginn", String.valueOf(user.get("data")));
            if (user.get("status").getAsInt() != -1) {
                basicData.setUserData(user.get("data"));
                Log.e("loginn", String.valueOf(user));
                context.startActivity(new Intent(context, MainActivity.class));
                LoginActivity.loginActivity.finish();
            } else {
                dialog.dismiss();
                Toast.makeText(context, "Please check your information", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
    }
}
