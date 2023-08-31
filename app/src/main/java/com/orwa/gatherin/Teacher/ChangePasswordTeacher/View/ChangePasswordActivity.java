package com.orwa.gatherin.Teacher.ChangePasswordTeacher.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityChangePasswordBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class ChangePasswordActivity extends ParentClass {

    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
    }

    private void initEventDriven() {
        if (getLang(ChangePasswordActivity.this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.back);
        } else {
            binding.ivBack.setImageResource(R.drawable.back_en);
        }

        binding.tvConfirm.setOnClickListener(v -> {

            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(binding.etOldPassword.getText().toString())) {
                binding.etOldPassword.setError(getString(R.string.password));
                focusView = binding.etOldPassword;
                cancel = true;
            }

            if (TextUtils.isEmpty(binding.etNewPassword.getText().toString())) {
                binding.etNewPassword.setError(getString(R.string.password));
                focusView = binding.etNewPassword;
                cancel = true;
            }
            if (TextUtils.isEmpty(binding.etConfirmPassword.getText().toString())) {
                binding.etConfirmPassword.setError(getString(R.string.password));
                focusView = binding.etConfirmPassword;
                cancel = true;
            }

            if (cancel) {

            } else {
                startLoading();
                Observable observable = RetroWeb.getClient().create(AppServices.class).updateTeacherPassword(
                        sharedPrefManager.getUserDate().getId(),binding.etNewPassword.getText().toString(),
                        sharedPrefManager.getAccessToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                Observer<GeneralResponse> observer = new Observer<GeneralResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GeneralResponse userModel) {
                        stopLoading();
                        if (userModel != null) {
                            if (userModel.getStatus()) {
                                Intent intent = new Intent(ChangePasswordActivity.this,HomeTeacherActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Bungee.split(ChangePasswordActivity.this);
                                successToast(ChangePasswordActivity.this,userModel.getMessage());

                            } else {
                                errorToast(ChangePasswordActivity.this,userModel.getMessage());
                            }
                        } else {
                            errorToast(ChangePasswordActivity.this,getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiException(ChangePasswordActivity.this,e);
                        stopLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.subscribe(observer);
            }
        });

        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(ChangePasswordActivity.this);
        });
    }
}