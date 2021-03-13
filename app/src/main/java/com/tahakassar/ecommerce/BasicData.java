package com.tahakassar.ecommerce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tahakassar.ecommerce.models.User;

import java.lang.reflect.Type;

public class BasicData {

    @SuppressLint("StaticFieldLeak")
    private static BasicData basicData = null;
    private Context context;
    private final String USER_DATA = "USER_DATA";
    public static String BASE_URL = "https://neuronprograms.com/clients/";

    private Gson gson = null;
    private BasicData() {

    }

    public static BasicData getInstance(Context context) {
        if (basicData == null) {
            basicData = new BasicData();
            basicData.context = context;
            basicData.gson = new Gson();
            return basicData;
        }
        return basicData;
    }

    public void setRatingBarColor(RatingBar ratingBar) {
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.gray),
                PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP);
    }


    public void setGradientColor(TextView welcomeTV) {
        TextPaint paint = welcomeTV.getPaint();
        float width = paint.measureText(welcomeTV.getText().toString());
        Shader textShader = new LinearGradient(0, 0, width, welcomeTV.getTextSize(),
                new int[]{
                        Color.parseColor("#C9E364"),
                        Color.parseColor("#0097D1"),
                }, null, Shader.TileMode.CLAMP);
        welcomeTV.getPaint().setShader(textShader);
    }

    public User getUserData() {
        android.content.SharedPreferences pref = context
                .getSharedPreferences(context.getApplicationInfo().name, Context.MODE_PRIVATE);
        return gson.fromJson(pref.getString(USER_DATA, null), (Type) User.class);
    }

    public void setUserData(JsonElement user) {
        String serializableUser = gson.toJson(user);
        android.content.SharedPreferences.Editor pref = context
                .getSharedPreferences(context.getApplicationInfo().name, Context.MODE_PRIVATE).edit();
        pref.putString(USER_DATA, serializableUser).apply();
    }
}
