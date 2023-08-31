package com.orwa.gatherin.Authentication.LoginTeacher.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.orwa.gatherin.Authentication.RegisterTeacher.View.RegisterTeacherActivity;
import com.orwa.gatherin.Models.UserModelNew.UserModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityLoginBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class LoginTeacherActivity extends ParentClass {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
    }

    private void initEventDriven() {
        binding.ivButtonActive.setOnClickListener(v -> {
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(binding.etPassword.getText().toString())) {
                binding.etPassword.setError(getString(R.string.password));
                focusView = binding.etPassword;
                cancel = true;
            }

            if (TextUtils.isEmpty(binding.ettMail.getText().toString())) {
                binding.ettMail.setError(getString(R.string.email));
                focusView = binding.ettMail;
                cancel = true;
            }

            if (cancel) {

            } else {
//                Log.e("email",binding.ettMail.getText().toString());
                startLoading();


                Observable observable = RetroWeb.getClient().create(AppServices.class).teacher_signin(binding.ettMail.getText().toString(),
                        binding.etPassword.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                Observer<UserModel> observer = new Observer<UserModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserModel userModel) {
                        stopLoading();
                        if (userModel != null) {
                            if (userModel.isStatus()) {
                                ParentClass.sharedPrefManager.setUserDate(userModel.getData());
                                ParentClass.sharedPrefManager.setAccessToken(userModel.getAccessToken());
                                ParentClass.sharedPrefManager.setLoginStatus(true);
                                ParentClass.sharedPrefManager.setLoginType("teacher");
                                Intent intent = new Intent(LoginTeacherActivity.this,HomeTeacherActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Bungee.split(LoginTeacherActivity.this);
                                successToast(LoginTeacherActivity.this,userModel.getMessage());


                            } else {
                                errorToast(LoginTeacherActivity.this,userModel.getMessage());
                            }
                        } else {
                            errorToast(LoginTeacherActivity.this,getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiException(LoginTeacherActivity.this,e);
                        stopLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.subscribe(observer);
            }

        });

        binding.tvCreateNewSection.setOnClickListener(v -> {
            Intent intent = new Intent(LoginTeacherActivity.this,RegisterTeacherActivity.class);
            startActivity(intent);
            Bungee.split(LoginTeacherActivity.this);

        });

    }
}