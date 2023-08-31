package com.orwa.gatherin.User.SettingsUser.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.R;
import com.orwa.gatherin.SplashAndTypeOfLogin.SplashActivity.View.SplashActivity;
import com.orwa.gatherin.SplashAndTypeOfLogin.TypeOfLogin.View.TypeOfLoginActivity;
import com.orwa.gatherin.Teacher.ChangePasswordTeacher.View.ChangePasswordActivity;
import com.orwa.gatherin.Teacher.EditProfileTeacher.View.EditProfileTeacherActivity;
import com.orwa.gatherin.Teacher.SubscriptionsList.View.SubscriptionsListActivity;
import com.orwa.gatherin.User.AboutApp.View.AboutAppActivity;
import com.orwa.gatherin.User.EditUserProfile.View.EditProfileUserActivity;
import com.orwa.gatherin.User.LeaderChatHistoryUser.View.LeaderChatHistoryUserActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivitySettingsUserBinding;

import spencerstudios.com.bungeelib.Bungee;

public class SettingsUserActivity extends ParentClass {
    ActivitySettingsUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        if (getIntent().getStringExtra("type").equals("teacher")) {
            binding.rlEditPassword.setVisibility(View.VISIBLE);
            binding.rlSubscription.setVisibility(View.VISIBLE);
        }
        if (getLang(SettingsUserActivity.this).equals("ar")) {
            binding.rlEnglish.setBackgroundColor(Color.parseColor("#0DFFFFFF"));
            binding.tvEnglish.setTextColor(Color.parseColor("#9b9b9b"));
            binding.rlArabic.setBackgroundResource(R.drawable.drawable_app_color_5_radious);
            binding.tvArabic.setTextColor(Color.parseColor("#FFFFFF"));
            binding.ivBack.setImageResource(R.drawable.ab_back);
        } else {
            binding.rlEnglish.setBackgroundResource(R.drawable.drawable_app_color_5_radious);
            binding.tvEnglish.setTextColor(Color.parseColor("#FFFFFF"));
            binding.rlArabic.setBackgroundColor(Color.parseColor("#0DFFFFFF"));
            binding.tvArabic.setTextColor(Color.parseColor("#9b9b9b"));
            binding.ivBack.setImageResource(R.drawable.ab_back);
        }
    }

    private void initEventDriven() {
        binding.rlEditPassword.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsUserActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            Bungee.split(SettingsUserActivity.this);
        });
        binding.rlSubscription.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsUserActivity.this, SubscriptionsListActivity.class);
            startActivity(intent);
            Bungee.split(SettingsUserActivity.this);
        });
        binding.rlAbout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsUserActivity.this, AboutAppActivity.class);
            startActivity(intent);
            Bungee.split(SettingsUserActivity.this);
        });
        binding.rlEditProfile.setOnClickListener(v -> {
            if (getIntent().getStringExtra("type").equals("teacher")) {
                Intent intent = new Intent(SettingsUserActivity.this, EditProfileTeacherActivity.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
                Bungee.split(SettingsUserActivity.this);
            } else {
                Intent intent = new Intent(SettingsUserActivity.this, EditProfileUserActivity.class);
                startActivity(intent);
                Bungee.split(SettingsUserActivity.this);
            }

        });
        binding.rlLeaderChats.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsUserActivity.this, LeaderChatHistoryUserActivity.class);
            startActivity(intent);
            Bungee.split(SettingsUserActivity.this);
        });
        binding.rlLogout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsUserActivity.this, TypeOfLoginActivity.class);
            startActivity(intent);
            Bungee.split(SettingsUserActivity.this);
        });
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(SettingsUserActivity.this);
        });
        binding.rlEnglish.setOnClickListener(v -> {
            ParentClass.storeLang("en",SettingsUserActivity.this);
            Intent intent = new Intent(SettingsUserActivity.this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        binding.tvArabic.setOnClickListener(v -> {
            ParentClass.storeLang("ar",SettingsUserActivity.this);
            Intent intent = new Intent(SettingsUserActivity.this,SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
    }
}