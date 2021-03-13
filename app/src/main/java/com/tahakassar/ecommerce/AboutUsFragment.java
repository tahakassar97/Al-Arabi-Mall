package com.tahakassar.ecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.marcok.stepprogressbar.StepProgressBar;
import com.tahakassar.ecommerce.controllers.AboutUsController;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AboutUsFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static TextView description;
    StepProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
        description = rootView.findViewById(R.id.descriptionTV);
        progressBar = rootView.findViewById(R.id.stepProgressBar);
        getAboutUs();
        return rootView;
    }

    private void getAboutUs() {
        final RequestBody service = RequestBody
                .create(MediaType.parse("text/plain"), "get_about");
        AboutUsController aboutUs = new AboutUsController();
        aboutUs.start(getContext(), service);

    }

}