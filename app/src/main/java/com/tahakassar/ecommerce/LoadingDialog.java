package com.tahakassar.ecommerce;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.marcok.stepprogressbar.StepProgressBar;

public class LoadingDialog extends Dialog {

    private static LoadingDialog loadingDialog = null;
    private static StepProgressBar stepProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
    }

    private LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public static LoadingDialog getInstance(Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
            stepProgressBar = loadingDialog.findViewById(R.id.stepProgressBar);
            loadingDialog.setCancelable(false);
            return loadingDialog;
        }
        return loadingDialog;
    }

    @Override
    public void show() {
        super.show();
        handler();
    }

    private void handler() {
        new Handler().postDelayed(() -> stepProgressBar.next(), 500);
    }

}