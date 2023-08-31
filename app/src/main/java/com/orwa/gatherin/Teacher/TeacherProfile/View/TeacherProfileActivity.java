package com.orwa.gatherin.Teacher.TeacherProfile.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.orwa.gatherin.Models.TeacherProfileModel.TeacherProfileModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.ChatHistoryLeaders.View.ChatHistoryLeadersActivity;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.Teacher.NotificationTeacher.View.NotificationTeacherActivity;
import com.orwa.gatherin.User.SettingsUser.View.SettingsUserActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityTeacherProfileBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class TeacherProfileActivity extends ParentClass {
    ActivityTeacherProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        startLoading();
        Observable observable = RetroWeb.getClient().create(AppServices.class).teacherProfile(sharedPrefManager.getUserDate().getId(),
                sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<TeacherProfileModel> observer = new Observer<TeacherProfileModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(TeacherProfileModel userModel) {
                stopLoading();
                if (userModel != null) {
                    if (userModel.isStatus()) {
                        binding.tvEmail.setText(userModel.getData().getEmail());
                        binding.tvUserName.setText(userModel.getData().getUsername());
                        binding.tvCode.setText(userModel.getData().getId());
                    } else {
                        errorToast(TeacherProfileActivity.this,userModel.getMessage());
                    }
                } else {
                    errorToast(TeacherProfileActivity.this,getString(R.string.somethingWentWrong));
                }

            }

            @Override
            public void onError(Throwable e) {
                handleApiException(TeacherProfileActivity.this,e);
                stopLoading();
            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
    }


    private void initEventDriven() {
        binding.relativeMsg.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherProfileActivity.this,ChatHistoryLeadersActivity.class);
            startActivity(intent);
            Bungee.split(TeacherProfileActivity.this);
        });
        binding.relativeNotification.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherProfileActivity.this,NotificationTeacherActivity.class);
            startActivity(intent);
            Bungee.split(TeacherProfileActivity.this);
        });
        binding.relativeHome.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherProfileActivity.this,HomeTeacherActivity.class);
            startActivity(intent);
            Bungee.split(TeacherProfileActivity.this);
        });
        binding.ivSettings.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherProfileActivity.this,SettingsUserActivity.class);
            intent.putExtra("type","teacher");
            intent.putExtra("name",binding.tvUserName.getText().toString());
            intent.putExtra("email",binding.tvEmail.getText().toString());
            startActivity(intent);
            Bungee.split(TeacherProfileActivity.this);
        });

    }
}