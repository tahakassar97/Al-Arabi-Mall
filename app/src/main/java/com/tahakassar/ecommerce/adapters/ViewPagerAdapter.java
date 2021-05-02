package com.tahakassar.ecommerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.tahakassar.ecommerce.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    Context c;
    private final List<String> _imagePaths;

    public ViewPagerAdapter(Context c, List<String> imagePaths) {
        this._imagePaths = imagePaths;
        this.c = c;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imgDisplay;

        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.pager_item, container,
                false);

        imgDisplay = viewLayout.findViewById(R.id.imageIV);

        Picasso.get().load(_imagePaths.get(position)).into(imgDisplay);
        (container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((CardView) object);
    }
}