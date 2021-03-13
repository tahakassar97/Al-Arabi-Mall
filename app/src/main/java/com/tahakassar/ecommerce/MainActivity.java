package com.tahakassar.ecommerce;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.tahakassar.ecommerce.models.User;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    @SuppressLint("StaticFieldLeak")
    public static Activity mainActivity;
    BasicData basicData;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadFragment(new HomeFragment());

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            Fragment fragment;
            switch (id) {
                case R.id.home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    break;
                case R.id.profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    break;
                case R.id.aboutUs:
                    fragment = new AboutUsFragment();
                    loadFragment(fragment);
                    break;
                case R.id.logout:
                    basicData.setUserData(null);
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    break;
                default:
                    return true;
            }
            return true;
        });
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toolBar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open, R.string.close);
        setToggleColor();
        drawerLayout.addDrawerListener(toggle);
        navigationView.getMenu().getItem(0).setChecked(true);
        toggle.syncState();
        mainActivity = this;
        basicData = BasicData.getInstance(this);
    }

    private void setToggleColor() {
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("");
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();
        drawerLayout.closeDrawers();
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("Sure to logout?")
                    .setPositiveButton("Confirm", ((dialog, which) -> {
                        this.finish();
                    }))
                    .setNegativeButton("No", null)
                    .create()
                    .show();
        } else {
            getFragmentManager().popBackStack();
        }
    }

}