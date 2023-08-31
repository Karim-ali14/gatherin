package com.orwa.gatherin.Teacher.EditProfileTeacher.View;

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
import com.orwa.gatherin.databinding.ActivityEditProfileTeacherBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class EditProfileTeacherActivity extends ParentClass {
    ActivityEditProfileTeacherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileTeacherBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        if (getLang(EditProfileTeacherActivity.this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.back);
        } else {
            binding.ivBack.setImageResource(R.drawable.back_en);
        }
        binding.etUserName.setText(getIntent().getStringExtra("name"));
        binding.etEmail.setText(getIntent().getStringExtra("email"));
    }

    private void initEventDriven() {

        binding.tvRegister.setOnClickListener(v -> {
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
                binding.etEmail.setError(getString(R.string.email));
                focusView = binding.etEmail;
                cancel = true;
            }

            if (TextUtils.isEmpty(binding.etUserName.getText().toString())) {
                binding.etUserName.setError(getString(R.string.username));
                focusView = binding.etUserName;
                cancel = true;
            }

            if (cancel) {

            } else {
                startLoading();
                Observable observable = RetroWeb.getClient().create(AppServices.class).updateTeacherProfile(
                        sharedPrefManager.getUserDate().getId(),binding.etUserName.getText().toString(),binding.etEmail.getText().toString(),
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
                                Intent intent = new Intent(EditProfileTeacherActivity.this,HomeTeacherActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Bungee.split(EditProfileTeacherActivity.this);
                                successToast(EditProfileTeacherActivity.this,userModel.getMessage());

                            } else {
                                errorToast(EditProfileTeacherActivity.this,userModel.getMessage());
                            }
                        } else {
                            errorToast(EditProfileTeacherActivity.this,getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiException(EditProfileTeacherActivity.this,e);
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
            Bungee.split(EditProfileTeacherActivity.this);
        });
    }

}