package com.tahakassar.ecommerce;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tahakassar.ecommerce.adapters.BrandAdapter;
import com.tahakassar.ecommerce.models.Brand;
import com.tahakassar.ecommerce.models.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tahakassar.ecommerce.BasicData.BASE_URL;

public class HomeFragment extends Fragment {

    private View rootView;
    private RecyclerView brands;
    public ArrayList<Brand> paginigBrands;
    private Chip chip1;
    private Chip chip2;
    private Chip chip3;
    private Chip chip4;

    public ChipGroup chipGroup;
    public Map<Integer, String> typeID;
    private final static int limit = 3;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initViews();
        final RequestBody service = RequestBody
                .create(MediaType.parse("text/plain"), "get_categories");
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please waiting...");
        dialog.setCancelable(false);
        dialog.show();
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
        Call<JsonObject> call = apiInterface.getCategories(service);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject result = response.body();
                    ArrayList<Category> categoriesData = new ArrayList<>();
                    assert result != null;
                    JsonArray categories = result.getAsJsonArray("data");
                    for (int i = 0; i < categories.size(); i++) {
                        categoriesData.add(new Category(categories.get(i).getAsJsonObject().get("id").toString()
                                .replace("\"", ""),
                                categories.get(i).getAsJsonObject().get("name").toString()
                                        .replace("\"", "")));
                    }
                    for (int i = 0; i < categoriesData.size(); i++) {
                        @SuppressLint("InflateParams") Chip chip = (Chip)
                                getLayoutInflater().inflate(R.layout.chip_layout,
                                        null, false);
                        chip.setId(Integer.parseInt(categoriesData.get(i).getId()));
                        typeID.put(Integer
                                .parseInt(categoriesData.get(i).getId()), categoriesData.get(i).getName());
                        chip.setText(categoriesData.get(i).getName());
                        chipGroup.addView(chip);
                    }
                    chipGroup.check(Integer.parseInt(categoriesData.get(0).getId()));
                    paginigBrands.clear();
                    final RequestBody service2 = RequestBody
                            .create(MediaType.parse("text/plain"), "get_shops_by_cat");
                    final RequestBody id = RequestBody
                            .create(MediaType.parse("text/plain"), categoriesData.get(0).getId());
                    Call<JsonObject> call2 = apiInterface.getBrands(service2, id);
                    call2.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    JsonObject result = response.body();
                                    ArrayList<Brand> brandsData = new ArrayList<>();
                                    assert result != null;
                                    JsonArray brands = result.getAsJsonArray("data");
                                    for (int i = 0; i < brands.size(); i++) {
                                        ArrayList<String> urls = new ArrayList<>();
                                        JsonArray pics = brands.get(i).getAsJsonObject()
                                                .get("pics").getAsJsonArray();
                                        Log.e("image", String.valueOf(pics.size()));
                                        for (int j = 0; j < pics.size(); j++) {
                                            urls.add(pics.get(i).toString());
                                        }
                                        Brand brand = new Brand(brands.get(i).getAsJsonObject()
                                                .get("id").toString()
                                                .replace("\"", ""),
                                                brands.get(i).getAsJsonObject().get("title").toString()
                                                        .replace("\"", ""),
                                                brands.get(i).getAsJsonObject().get("icon").toString(),
                                                brands.get(i).getAsJsonObject().get("rating").getAsFloat(),
                                                brands.get(i).getAsJsonObject().get("description").toString()
                                                        .replace("\"", ""),
                                                brands.get(i).getAsJsonObject().get("phone").toString()
                                                        .replace("\"", ""), urls,
                                                brands.get(i).getAsJsonObject().get("location").toString()
                                        );
                                        brandsData.add(brand);
                                    }
                                    initBrands(getContext(), brandsData);
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    dialog.dismiss();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            brands.setAdapter(new BrandAdapter(getContext(), new ArrayList<>()));
            final RequestBody service3 = RequestBody
                    .create(MediaType.parse("text/plain"), "get_categories");
            ProgressDialog dialog2 = new ProgressDialog(getContext());
            dialog2.setMessage("Please waiting...");
            dialog2.setCancelable(false);
            dialog2.show();
            HttpLoggingInterceptor loggingInterceptor2 = new HttpLoggingInterceptor();
            loggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS);
            httpClient2.addInterceptor(loggingInterceptor2);
            Gson gson2 = new GsonBuilder()
                    .setLenient().create();
            Retrofit retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson2))
                    .client(httpClient2.build())
                    .build();
            APIInterface apiInterface2 = retrofit2.create(APIInterface.class);
            Call<JsonObject> call2 = apiInterface2.getCategories(service3);
            call2.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        JsonObject result = response.body();
                        ArrayList<Category> categoriesData = new ArrayList<>();
                        chipGroup = new ChipGroup(getContext());
                        assert result != null;
                        JsonArray categories = result.getAsJsonArray("data");
                        for (int i = 0; i < categories.size(); i++) {
                            categoriesData.add(new Category(categories.get(i).getAsJsonObject().get("id").toString()
                                    .replace("\"", ""),
                                    categories.get(i).getAsJsonObject().get("name").toString()
                                            .replace("\"", "")));
                        }
                        for (int i = 0; i < categoriesData.size(); i++) {
                            @SuppressLint("InflateParams") Chip chip = (Chip)
                                    getLayoutInflater().inflate(R.layout.chip_layout,
                                            null, false);
                            chip.setId(Integer.parseInt(categoriesData.get(i).getId()));
                            typeID.put(Integer
                                    .parseInt(categoriesData.get(i).getId()), categoriesData.get(i).getName());
                            chip.setText(categoriesData.get(i).getName());
                            chipGroup.addView(chip);
                        }
                        chipGroup.check(Integer.parseInt(categoriesData.get(0).getId()));
                        paginigBrands.clear();
                        final RequestBody service4 = RequestBody
                                .create(MediaType.parse("text/plain"), "get_shops_by_cat");
                        final RequestBody id = RequestBody
                                .create(MediaType.parse("text/plain"), String.valueOf(checkedId));
                        Call<JsonObject> call2 = apiInterface.getBrands(service4, id);
                        call2.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        JsonObject result = response.body();
                                        ArrayList<Brand> brandsData = new ArrayList<>();
                                        assert result != null;
                                        JsonArray brands = result.getAsJsonArray("data");
                                        for (int i = 0; i < brands.size(); i++) {
                                            ArrayList<String> urls = new ArrayList<>();
                                            JsonArray pics = brands.get(i).getAsJsonObject()
                                                    .get("pics").getAsJsonArray();
                                            Log.e("image", String.valueOf(pics.size()));
                                            for (int j = 0; j < pics.size(); j++) {
                                                urls.add(pics.get(i).toString()
                                                        .replace("\"", ""));
                                                Log.e("image", pics.get(i).toString());
                                            }
                                            Brand brand = new Brand(brands.get(i).getAsJsonObject()
                                                    .get("id").toString()
                                                    .replace("\"", ""),
                                                    brands.get(i).getAsJsonObject().get("title").toString()
                                                            .replace("\"", ""),
                                                    brands.get(i).getAsJsonObject().get("icon").toString()
                                                            .replace("\"", ""),
                                                    brands.get(i).getAsJsonObject().get("rating").getAsFloat(),
                                                    brands.get(i).getAsJsonObject().get("description").toString()
                                                            .replace("\"", ""),
                                                    brands.get(i).getAsJsonObject().get("phone").toString()
                                                            .replace("\"", ""), urls,
                                                    brands.get(i).getAsJsonObject().get("location").toString()
                                            );
                                            brandsData.add(brand);
                                        }
                                        initBrands(getContext(), brandsData);
                                        dialog2.dismiss();
                                    } catch (Exception e) {
                                        dialog2.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                dialog2.dismiss();
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        chip1.setOnClickListener(v -> setPagination(0, limit, getContext()));
        chip2.setOnClickListener(v -> setPagination(limit, limit * 2, getContext()));
        chip3.setOnClickListener(v -> setPagination(limit * 2, limit * 3, getContext()));
        chip4.setOnClickListener(v -> setPagination(limit * 3, limit * 4, getContext()));
        return rootView;
    }


    public void initBrands(Context context, ArrayList<Brand> brands_) {
        Log.e("images", brands_.get(0).getIcon());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        brands.setLayoutManager(layoutManager);
        brands.setHasFixedSize(true);
        paginigBrands.clear();
        paginigBrands.addAll(brands_);
        brands.setAdapter(new BrandAdapter(context, brands_));
        showChips(brands_.size());
        setPagination(0, limit, context);
    }

    private void setPagination(int skip, int limit, Context context) {
        ArrayList<Brand> finalBrands;
        try {
            if (limit >= paginigBrands.size()) {
                finalBrands = new ArrayList<>(paginigBrands.subList(skip, paginigBrands.size()));
            } else {
                finalBrands = new ArrayList<>(paginigBrands.subList(skip, limit));
            }
            brands.setAdapter(new BrandAdapter(context, finalBrands));
        } catch (Exception e) {
            Log.e("catchs", e.getMessage());
        }
    }

    @SuppressLint("InflateParams")
    private void initViews() {
        brands = rootView.findViewById(R.id.brands);
        chip1 = rootView.findViewById(R.id.firstPage);
        chip1.setChecked(true);
        chip2 = rootView.findViewById(R.id.secondPage);
        chip3 = rootView.findViewById(R.id.thirdPage);
        chip4 = rootView.findViewById(R.id.fourthPage);
        chipGroup = rootView.findViewById(R.id.chip_group);

        typeID = new HashMap<>();
        paginigBrands = new ArrayList<>();
    }


    private void showChips(int size) {
        hideChips();
        if (size > 3 && size <= 6) {
            chip2.setVisibility(View.VISIBLE);
        } else if (size > 6 && size <= 9) {
            chip3.setVisibility(View.VISIBLE);
        } else if (size > 9) {
            chip4.setVisibility(View.VISIBLE);
        }
    }

    private void hideChips() {
        chip2.setVisibility(View.GONE);
        chip3.setVisibility(View.GONE);
        chip4.setVisibility(View.GONE);
    }
}