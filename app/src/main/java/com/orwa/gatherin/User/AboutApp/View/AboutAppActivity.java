package com.orwa.gatherin.User.AboutApp.View;

import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivityAboutAppBinding;

import spencerstudios.com.bungeelib.Bungee;

public class AboutAppActivity extends ParentClass {
    ActivityAboutAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutAppBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
    }

    private void initEventDriven() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(AboutAppActivity.this);
        });
    }
}