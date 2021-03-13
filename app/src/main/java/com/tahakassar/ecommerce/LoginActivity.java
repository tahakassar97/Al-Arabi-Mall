package com.tahakassar.ecommerce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.tahakassar.ecommerce.controllers.LoginController;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginActivity extends AppCompatActivity {

    private TextView needRegister;
    private Button loginBTN;
    private EditText username;
    private EditText password;
    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getViews();
        needRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        loginBTN.setOnClickListener(v -> {
            if (!checkData()) {
                Snackbar.make(loginBTN, "Please enter full information", Snackbar.LENGTH_LONG)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                        .setBackgroundTint(getResources().getColor(R.color.colorPrimary))
                        .show();
            } else {
                final RequestBody service = RequestBody
                        .create(MediaType.parse("text/plain"), "login");
                final RequestBody username_ = RequestBody
                        .create(MediaType.parse("text/plain"), username.getText().toString().trim());
                final RequestBody password_ = RequestBody
                        .create(MediaType.parse("text/plain"), password.getText().toString().trim());
                LoginController controller = new LoginController();
                controller.start(this, service, username_, password_);
            }
        });
    }

    private boolean checkData() {
        return !username.getText().toString().isEmpty() && !password.getText().toString().isEmpty();
    }

    private void getViews() {
        needRegister = findViewById(R.id.signUp);
        loginBTN = findViewById(R.id.loginBTN);
        username = findViewById(R.id.usernameET);
        password = findViewById(R.id.passwordET);
        loginActivity = this;
    }
}