package com.orwa.gatherin.User.SendMessageToLeader.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.User.HomeUser.View.HomeUserActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivitySendMessageToLeaderBinding;

import spencerstudios.com.bungeelib.Bungee;

public class SendMessageToLeaderActivity extends ParentClass {

    ActivitySendMessageToLeaderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendMessageToLeaderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
    }

    private void initEventDriven() {
        binding.tvSend.setOnClickListener(v -> {
            Intent intent = new Intent(SendMessageToLeaderActivity.this,HomeUserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            Bungee.split(SendMessageToLeaderActivity.this);
        });
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(SendMessageToLeaderActivity.this);
        });
    }
}