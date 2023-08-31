package com.orwa.gatherin.SplashAndTypeOfLogin.TypeOfLogin.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.Authentication.LoginTeacher.View.LoginTeacherActivity;
import com.orwa.gatherin.Authentication.LoginUser.View.LoginUserActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivityTypeOfLoginBinding;

import spencerstudios.com.bungeelib.Bungee;

public class TypeOfLoginActivity extends ParentClass {
    ActivityTypeOfLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTypeOfLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
    }

    private void initEventDriven() {
        binding.rlUser.setOnClickListener(v -> {
            Intent intent = new Intent(TypeOfLoginActivity.this,LoginUserActivity.class);
            startActivity(intent);
            Bungee.split(TypeOfLoginActivity.this);
        });
        binding.rlTeacher.setOnClickListener(v -> {
            Intent intent = new Intent(TypeOfLoginActivity.this,LoginTeacherActivity.class);
            startActivity(intent);
            Bungee.split(TypeOfLoginActivity.this);
        });


    }
}