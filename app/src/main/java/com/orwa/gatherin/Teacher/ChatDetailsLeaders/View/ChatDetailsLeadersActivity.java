package com.orwa.gatherin.Teacher.ChatDetailsLeaders.View;

import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivityChatDetailsLeadersBinding;

public class ChatDetailsLeadersActivity extends ParentClass {
    ActivityChatDetailsLeadersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailsLeadersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }
}