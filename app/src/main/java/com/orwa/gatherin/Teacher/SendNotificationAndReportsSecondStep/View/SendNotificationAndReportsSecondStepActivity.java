package com.orwa.gatherin.Teacher.SendNotificationAndReportsSecondStep.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivitySendNotificationAndReportsSecondStepBinding;

import spencerstudios.com.bungeelib.Bungee;

public class SendNotificationAndReportsSecondStepActivity extends ParentClass {
    ActivitySendNotificationAndReportsSecondStepBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendNotificationAndReportsSecondStepBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
    }

    private void initEventDriven() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(SendNotificationAndReportsSecondStepActivity.this);
        });
        binding.ivSend.setOnClickListener(v -> {
            Intent intent = new Intent(SendNotificationAndReportsSecondStepActivity.this,HomeTeacherActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            Bungee.split(SendNotificationAndReportsSecondStepActivity.this);
        });
    }
}