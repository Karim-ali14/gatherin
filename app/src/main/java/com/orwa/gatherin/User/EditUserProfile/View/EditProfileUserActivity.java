package com.orwa.gatherin.User.EditUserProfile.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.UserModelNew.Data;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.User.HomeUser.View.HomeUserActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityEditProfileUserBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class EditProfileUserActivity extends ParentClass {
    ActivityEditProfileUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getLang(this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        } else {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        }
        initEventDriven();
    }

    private void initEventDriven() {
        binding.tvRegister.setOnClickListener(v -> {
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(binding.etUserName.getText().toString())) {
                binding.etUserName.setError(getString(R.string.username));
                focusView = binding.etUserName;
                cancel = true;
            }

            if (TextUtils.isEmpty(binding.etEmail.getText().toString())) {
                binding.etEmail.setError(getString(R.string.email));
                focusView = binding.etEmail;
                cancel = true;
            }


            if (cancel) {

            } else {


                Log.e("email", binding.etEmail.getText().toString());
                startLoading();


                Observable observable = RetroWeb.getClient().create(AppServices.class).updateStudent(sharedPrefManager.getUserDate().getId(), sharedPrefManager.getAccessToken(), binding.etUserName.getText().toString(), binding.etEmail.getText().toString())
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
                                Data data = new Data();
                                data.setUsername(binding.etUserName.getText().toString());
                                data.setEmail(binding.etEmail.getText().toString());
                                data.setId(sharedPrefManager.getUserDate().getId());
                                sharedPrefManager.setUserDate(data);
                                Intent intent = new Intent(EditProfileUserActivity.this, HomeUserActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Bungee.split(EditProfileUserActivity.this);
                                successToast(EditProfileUserActivity.this, userModel.getMessage());

                            } else {
                                errorToast(EditProfileUserActivity.this, userModel.getMessage());
                            }
                        } else {
                            errorToast(EditProfileUserActivity.this, getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiException(EditProfileUserActivity.this, e);
                        Log.e("e", e.toString());
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
            Bungee.split(EditProfileUserActivity.this);
        });
    }
}