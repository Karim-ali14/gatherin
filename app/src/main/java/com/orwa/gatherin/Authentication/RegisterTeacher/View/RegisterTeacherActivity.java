package com.orwa.gatherin.Authentication.RegisterTeacher.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.orwa.gatherin.Authentication.VerificationCodeActivity.VerificationCodeActivity;
import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse2;
import com.orwa.gatherin.Models.UserModelNew.UserModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityRegisterTeacherBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class RegisterTeacherActivity extends ParentClass {
    ActivityRegisterTeacherBinding binding;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterTeacherBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
    }


    private void initEventDriven() {

        if (getLang(RegisterTeacherActivity.this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.back);
        } else {
            binding.ivBack.setImageResource(R.drawable.back_en);

        }
        binding.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.tvRegister.setOnClickListener(v -> {

            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
                binding.etEmail.setError(this.getString(R.string.email));
                focusView = binding.etEmail;
                cancel = true;
            }

            if (TextUtils.isEmpty(binding.etUserName.getText().toString())) {
                binding.etUserName.setError(this.getString(R.string.username));
                focusView = binding.etUserName;
                cancel = true;
            }

            if (TextUtils.isEmpty(binding.etPassword.getText().toString())) {
                binding.etPassword.setError(this.getString(R.string.password));
                focusView = binding.etPassword;
                cancel = true;
            }


            if (cancel) {

            } else {

                startLoading();
//                Log.e("here1", "here");

                Observable observable = RetroWeb.getClient().create(AppServices.class).teacherRegister(binding.etEmail.getText().toString(),
                        binding.etPassword.getText().toString(), binding.etUserName.getText().toString(), "plan-1")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
//                Log.e("here2", "here");

                Observer<UserModel> observer = new Observer<UserModel>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserModel userModel) {
                        stopLoading();
                        if (userModel != null) {
//                            Log.e("here", "here");
                            if (userModel.isStatus()) {
//                                ParentClass.sharedPrefManager.setUserDate(userModel);
//                                ParentClass.sharedPrefManager.setLoginStatus(true);
//                                ParentClass.sharedPrefManager.setLoginType("teacher");
//                                Intent intent = new Intent(RegisterTeacherActivity.this, VerificationCodeActivity.class);
//                                intent.putExtra("email",binding.etEmail.getText().toString());
//                                intent.putExtra("token",userModel.getAccessToken());
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                startActivity(intent);
//                                Bungee.split(RegisterTeacherActivity.this);
                                token = userModel.getAccessToken();
                            } else {
                                errorToast(RegisterTeacherActivity.this, userModel.getMessage());
                            }
                        } else {
                            errorToast(RegisterTeacherActivity.this, getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiException(RegisterTeacherActivity.this, e);
//                        Log.e("error", e.toString() + "goood");
                        stopLoading();
                    }

                    @Override
                    public void onComplete() {
                        sendVerification();

                    }
                };
                observable.subscribe(observer);
            }
        });
    }

    void sendVerification() {
        startLoading();
        Observable observable = RetroWeb.getClient().create(AppServices.class).sendVerificationCode(binding.etEmail.getText().toString(), token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<GeneralResponse2> observer = new Observer<GeneralResponse2>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GeneralResponse2 userModel) {
                stopLoading();
                if (userModel != null) {
//                    Log.e("here", "here");
                    if (userModel.getStatus()) {
//                                ParentClass.sharedPrefManager.setUserDate(userModel);
//                                ParentClass.sharedPrefManager.setLoginStatus(true);
//                                ParentClass.sharedPrefManager.setLoginType("teacher");
                        Intent intent = new Intent(RegisterTeacherActivity.this, VerificationCodeActivity.class);
                        intent.putExtra("code", userModel.getData());
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        Bungee.split(RegisterTeacherActivity.this);
                    } else {
                        errorToast(RegisterTeacherActivity.this, userModel.getMessage());
                    }
                } else {
                    errorToast(RegisterTeacherActivity.this, getString(R.string.somethingWentWrong));
                }

            }

            @Override
            public void onError(Throwable e) {
                handleApiException(RegisterTeacherActivity.this, e);
//                Log.e("error", e.toString() + "goood");
                stopLoading();
            }

            @Override
            public void onComplete() {


            }
        };
        observable.subscribe(observer);
    }
}