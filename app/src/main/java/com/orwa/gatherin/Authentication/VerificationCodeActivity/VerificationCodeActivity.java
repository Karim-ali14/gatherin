package com.orwa.gatherin.Authentication.VerificationCodeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.Authentication.LoginTeacher.View.LoginTeacherActivity;
import com.orwa.gatherin.R;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.databinding.ActivityVerificationCodeBinding;

import spencerstudios.com.bungeelib.Bungee;

public class VerificationCodeActivity extends ParentClass {
    ActivityVerificationCodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationCodeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEveryThing();
    }

    private void initEveryThing() {
//        Log.e("code",getIntent().getStringExtra("code")+" Good");
        binding.tvRegister.setOnClickListener(v -> {
            if (getIntent().getStringExtra("code").equals(binding.etCode.getText().toString())) {
                Intent intent = new Intent(VerificationCodeActivity.this, LoginTeacherActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                Bungee.split(VerificationCodeActivity.this);
            }else {
                makeErrorToast(VerificationCodeActivity.this,getString(R.string.wrongCode));
            }
        });

    }
}