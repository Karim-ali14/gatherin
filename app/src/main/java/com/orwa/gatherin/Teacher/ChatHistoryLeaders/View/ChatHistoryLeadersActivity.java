package com.orwa.gatherin.Teacher.ChatHistoryLeaders.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.Teacher.NotificationTeacher.View.NotificationTeacherActivity;
import com.orwa.gatherin.Teacher.TeacherProfile.View.TeacherProfileActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivityChatHistoryLeadersBinding;

import spencerstudios.com.bungeelib.Bungee;

public class ChatHistoryLeadersActivity extends ParentClass {
    ActivityChatHistoryLeadersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatHistoryLeadersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();

    }

    private void initEventDriven() {
        binding.relativeHome.setOnClickListener(v -> {
            Intent intent = new Intent(ChatHistoryLeadersActivity.this,HomeTeacherActivity.class);
            startActivity(intent);
            Bungee.split(ChatHistoryLeadersActivity.this);
        });
        binding.relativeNotification.setOnClickListener(v -> {
            Intent intent = new Intent(ChatHistoryLeadersActivity.this,NotificationTeacherActivity.class);
            startActivity(intent);
            Bungee.split(ChatHistoryLeadersActivity.this);
        });
        binding.relativeProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ChatHistoryLeadersActivity.this,TeacherProfileActivity.class);
            startActivity(intent);
            Bungee.split(ChatHistoryLeadersActivity.this);
        });
    }
}