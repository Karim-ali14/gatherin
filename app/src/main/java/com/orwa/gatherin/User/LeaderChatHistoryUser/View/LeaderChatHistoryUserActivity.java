package com.orwa.gatherin.User.LeaderChatHistoryUser.View;

import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.R;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivityLeaderChatHistoryUserBinding;

public class LeaderChatHistoryUserActivity extends ParentClass {

    ActivityLeaderChatHistoryUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderChatHistoryUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getLang(this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        } else {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        }
    }
}