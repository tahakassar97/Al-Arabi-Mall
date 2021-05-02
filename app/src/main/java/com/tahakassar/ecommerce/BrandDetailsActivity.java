package com.tahakassar.ecommerce;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marcok.stepprogressbar.StepProgressBar;
import com.squareup.picasso.Picasso;
import com.tahakassar.ecommerce.adapters.ViewPagerAdapter;
import com.tahakassar.ecommerce.controllers.BrandDetailsController;
import com.tahakassar.ecommerce.controllers.RatingController;
import com.tahakassar.ecommerce.models.Brand;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BrandDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static ArrayList<String> urls;
    private static ViewPager pager;
    private static Button rateBTN;
    private static RatingBar ratingBar;
    private static BasicData basicData;
    private static StepProgressBar stepProgressBar;
    private static ImageView back;
    private static ImageView next;
    private static int currentPosition;
    public static  Brand brand;
    private static TextView title;
    private static TextView description;
    private static TextView address;
    private static TextView phone;
    private static GoogleMap map;
    private static SupportMapFragment mapFragment;
    private String id;
    private static CircleImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_details);

        id = (String) getIntent().getSerializableExtra("id");
        getViews();
        BrandDetailsController controller = new BrandDetailsController();
        final RequestBody service = RequestBody
                .create(MediaType.parse("text/plain"), "get_shop_by_id");
        final RequestBody brandID = RequestBody
                .create(MediaType.parse("text/plain"), id);
        controller.start(this, service, brandID);
        rateBTN.setOnClickListener(v -> showRatingDialog());
        back.setOnClickListener(v -> pager.setCurrentItem(pager.getCurrentItem() - 1));
        next.setOnClickListener(v -> pager.setCurrentItem(pager.getCurrentItem() + 1));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (currentPosition < position) {
                    currentPosition = position;
                    stepProgressBar.next();
                } else if (currentPosition > position && currentPosition != 0) {
                    currentPosition = position;
                    stepProgressBar.previous();
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    back.setVisibility(View.GONE);
                } else {
                    back.setVisibility(View.VISIBLE);
                }
                if (position == urls.size() - 1) {
                    next.setVisibility(View.GONE);
                } else {
                    next.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    public static void initData(Context context, Brand brand) {
        currentPosition = 0;
        try {
            stepProgressBar.setNumDots(brand.getPics().size());
            stepProgressBar.setCurrentProgressDot(0);
        } catch (Exception e) {
            Log.e("Exception e", e.getMessage());
        }
        title.setText(brand.getTitle());
        ratingBar.setRating(brand.getRating());
        description.setText(brand.getDescription());
        address.setText(brand.getLocation());
        phone.setText(brand.getPhone());
        Picasso.get().load(brand.getIcon()).into(logo);
        urls.addAll(brand.getPics());
        pager.setAdapter(new ViewPagerAdapter(context, urls));
        LatLng latLng = new LatLng(Double.parseDouble(brand.getLat()),
                Double.parseDouble(brand.getLng()));
        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude)).title("Al-Arabi Mall"));
        map.animateCamera(CameraUpdateFactory
                .newLatLngZoom(latLng, 12.0f));
    }

    private void showRatingDialog() {
        Dialog dialog = new Dialog(BrandDetailsActivity.this);
        dialog.setContentView(R.layout.rating_layout);
        Objects.requireNonNull(dialog.getWindow())
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.secondAnimation;
        dialog.show();
        final RequestBody service = RequestBody
                .create(MediaType.parse("text/plain"), "rate_shop");
        final RequestBody brandID = RequestBody
                .create(MediaType.parse("text/plain"), id);
        final RequestBody userID = RequestBody
                .create(MediaType.parse("text/plain"), basicData.getUserData().getId());
        RatingBar ratingBarLayout = dialog.findViewById(R.id.ratingLayout);
        basicData.setRatingBarColor(ratingBarLayout);
        ratingBarLayout.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            RatingController controller = new RatingController();
            controller.start(this, service, brandID, userID, rating);
            dialog.dismiss();
        });
    }

    private void getViews() {
        basicData = BasicData.getInstance(this);
        urls = new ArrayList<>();
        pager = findViewById(R.id.viewpager);
        rateBTN = findViewById(R.id.rateBTN);
        ratingBar = findViewById(R.id.ratingBar);
        stepProgressBar = findViewById(R.id.stepProgressBar);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        logo = findViewById(R.id.logo);
        basicData.setRatingBarColor(ratingBar);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}