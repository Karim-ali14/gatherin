package com.orwa.gatherin.Teacher.CreateNewSection.View;

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
import com.orwa.gatherin.utils.SharedPrefManager;
import com.orwa.gatherin.databinding.ActivityCreateNewSectionBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class CreateNewSectionActivity extends ParentClass {
    ActivityCreateNewSectionBinding binding;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNewSectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        sharedPrefManager = new SharedPrefManager(CreateNewSectionActivity.this);
    }

    private void initEventDriven() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(CreateNewSectionActivity.this);
        });
        binding.tvRegister.setOnClickListener(v -> {
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(binding.etSectionName.getText().toString())) {
                binding.etSectionName.setError(getString(R.string.newSectionName));
                focusView = binding.etSectionName;
                cancel = true;
            }

            if (TextUtils.isEmpty(binding.etSectionCode.getText().toString())) {
                binding.etSectionCode.setError(getString(R.string.createPrivateCode));
                focusView = binding.etSectionCode;
                cancel = true;
            }

            if (cancel) {

            } else {
//                Log.e("teacher",sharedPrefManager.getUserDate().getId());
//                Log.e("name",binding.etSectionName.getText().toString());
//                Log.e("code",binding.etSectionCode.getText().toString());
                startLoading();
                Observable observable = RetroWeb.getClient().create(AppServices.class).addDepartment(
                        sharedPrefManager.getUserDate().getId(),binding.etSectionName.getText().toString(),binding.etSectionCode.getText().toString(),
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
                                Intent intent = new Intent(CreateNewSectionActivity.this,HomeTeacherActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Bungee.split(CreateNewSectionActivity.this);
                                successToast(CreateNewSectionActivity.this,userModel.getMessage());

                            } else {
                                errorToast(CreateNewSectionActivity.this,userModel.getMessage());
                            }
                        } else {
                            errorToast(CreateNewSectionActivity.this,getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiException(CreateNewSectionActivity.this,e);
                        stopLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.subscribe(observer);
            }
        });
    }


}