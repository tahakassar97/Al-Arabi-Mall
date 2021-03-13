package com.tahakassar.ecommerce.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.tahakassar.ecommerce.BasicData;
import com.tahakassar.ecommerce.BrandDetailsActivity;
import com.tahakassar.ecommerce.R;
import com.tahakassar.ecommerce.models.Brand;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    Context context;
    ArrayList<Brand> brands;
    BasicData basicData;
    public BrandAdapter(Context context, ArrayList<Brand> brands) {
        this.context = context;
        this.brands = brands;
        Log.e("BRanrd", String.valueOf(brands.size()));
        basicData = BasicData.getInstance(context);
    }

    @NonNull
    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.brand_item,
                                parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Brand brand = brands.get(position);
        holder.titleTV.setText(brand.getTitle());
        holder.ratingBar.setRating(brand.getRating());
        holder.description.setText(brand.getDescription());
        holder.address.setText("Al-Arabi Mall");
        holder.number.setText(brand.getPhone());
        basicData.setRatingBarColor(holder.ratingBar);
        Picasso.get().load(brand.getIcon())
                .into(holder.logo);
        Picasso.get().load(brand.getPics().get(0))
                .into(holder.firstImage);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BrandDetailsActivity.class);
            intent.putExtra("id", brand.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircularImageView logo;
        TextView titleTV;
        RatingBar ratingBar;
        ImageView firstImage;
        TextView description;
        TextView address;
        TextView number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.logoCIV);
            firstImage = itemView.findViewById(R.id.firstImage);
            titleTV = itemView.findViewById(R.id.titleTV);
            ratingBar = itemView.findViewById(R.id.rating);
            description = itemView.findViewById(R.id.description);
            address = itemView.findViewById(R.id.address);
            number = itemView.findViewById(R.id.number);

        }
    }
}
