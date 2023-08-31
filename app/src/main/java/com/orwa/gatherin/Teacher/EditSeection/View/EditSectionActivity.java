package com.orwa.gatherin.Teacher.EditSeection.View;

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
import com.orwa.gatherin.databinding.ActivityEditSectionBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class EditSectionActivity extends ParentClass {

    ActivityEditSectionBinding binding;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditSectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        sharedPrefManager = new SharedPrefManager(EditSectionActivity.this);
    }

    private void initEventDriven() {
        binding.etSectionName.setText(getIntent().getStringExtra("name"));
        binding.etSectionCode.setText(getIntent().getStringExtra("code"));
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(EditSectionActivity.this);
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
                startLoading();
                Observable observable = RetroWeb.getClient().create(AppServices.class).updateDepartment(
                        getIntent().getStringExtra("id"),binding.etSectionName.getText().toString(),binding.etSectionCode.getText().toString(),
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
                                Intent intent = new Intent(EditSectionActivity.this,HomeTeacherActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                Bungee.split(EditSectionActivity.this);
                                successToast(EditSectionActivity.this,userModel.getMessage());

                            } else {
                                errorToast(EditSectionActivity.this,userModel.getMessage());
                            }
                        } else {
                            errorToast(EditSectionActivity.this,getString(R.string.somethingWentWrong));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiException(EditSectionActivity.this,e);
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