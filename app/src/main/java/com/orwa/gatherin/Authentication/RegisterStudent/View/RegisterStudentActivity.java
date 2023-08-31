package com.orwa.gatherin.Authentication.RegisterStudent.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.orwa.gatherin.Authentication.LoginUser.View.LoginUserActivity;
import com.orwa.gatherin.Models.UserModelNew.UserModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityConfirmationCodeBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class RegisterStudentActivity extends ParentClass {
    ActivityConfirmationCodeBinding binding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmationCodeBinding.inflate(getLayoutInflater());
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


//                Log.e("email", binding.etEmail.getText().toString());
                startLoading();


                Observable observable = RetroWeb.getClient().create(AppServices.class).studentSignUp( binding.etEmail.getText().toString(), binding.etPassword.getText().toString(), binding.etUserName.getText().toString())
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

                                Intent intent = new Intent(RegisterStudentActivity.this, LoginUserActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Bungee.split(RegisterStudentActivity.this);
                                successToast(RegisterStudentActivity.this, userModel.getMessage());


                            } else {
                                errorToast(RegisterStudentActivity.this, userModel.getMessage());
                            }
                        } else {
                            errorToast(RegisterStudentActivity.this, getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiException(RegisterStudentActivity.this, e);
//                        Log.e("e", e.toString());
                        stopLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.subscribe(observer);
            }
        });
        binding.tvAlreadyHaveAnAccount.setOnClickListener(v -> {
            finish();
            Bungee.split(RegisterStudentActivity.this);
        });
    }
}