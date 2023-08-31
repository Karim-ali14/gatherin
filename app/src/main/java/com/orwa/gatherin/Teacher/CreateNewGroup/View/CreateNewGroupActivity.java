package com.orwa.gatherin.Teacher.CreateNewGroup.View;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.DepartmentMembersModel.DepartmentMembersModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Adapter.DepartmentMembersAdapter;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityCreateNewGroupBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class CreateNewGroupActivity extends ParentClass {
    ActivityCreateNewGroupBinding binding;
    DepartmentMembersAdapter departmentMembersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateNewGroupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {

        if (getIntent().getStringExtra("type").equals("updateGroup")) {
            binding.etGroupName.setText(getIntent().getStringExtra("name"));
            binding.tvCreateEditGroupTitle.setText(getString(R.string.updateGroup));
        }
        departmentMembersAdapter = new DepartmentMembersAdapter(CreateNewGroupActivity.this,binding.rvMembers,sharedPrefManager,
                getIntent().getStringExtra("id"),getIntent().getStringExtra("sectionId"),getIntent().getStringExtra("type"),
                binding.tvRegister,binding.etGroupName);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CreateNewGroupActivity.this,RecyclerView.VERTICAL,false);
        binding.rvMembers.setLayoutManager(linearLayoutManager);


        Observable observable = RetroWeb.getClient().create(AppServices.class).get_department_members(getIntent().getStringExtra("sectionId"),
                sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<DepartmentMembersModel> observer = new Observer<DepartmentMembersModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DepartmentMembersModel homeModel) {
                if (homeModel != null) {
                    if (homeModel.isStatus()) {
                        binding.rvMembers.hideShimmerAdapter();
                        departmentMembersAdapter.addAll(homeModel.getData());
                        departmentMembersAdapter.notifyDataSetChanged();


                    } else {
                        errorToast(CreateNewGroupActivity.this,homeModel.getMessage());
                        binding.rvMembers.setVisibility(View.GONE);
                    }

                } else {
                    errorToast(CreateNewGroupActivity.this,getString(R.string.somethingWentWrong));
                    binding.rvMembers.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t) {
                handleApiException(CreateNewGroupActivity.this,t);
                binding.rvMembers.setVisibility(View.GONE);

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvMembers.setAdapter(departmentMembersAdapter);


//        binding.tvRegister.setOnClickListener(v -> {
//            boolean cancel = false;
//            View focusView = null;
//
//
//            if (TextUtils.isEmpty(binding.etGroupName.getText().toString())) {
//                binding.etGroupName.setError(getString(R.string.groupName));
//                focusView = binding.etGroupName;
//                cancel = true;
//            }
//
//            if (cancel) {
//
//            } else {
//                Log.e("token",sharedPrefManager.getAccessToken() + "good");
//                Log.e("name",binding.etGroupName.getText().toString() + "good");
//                Log.e("sectionId",getIntent().getStringExtra("sectionId") + "good");
//                Log.e("id",getIntent().getStringExtra("id") + "good");
//                startLoading();
//
//                if (getIntent().getStringExtra("type").equals("updateGroup")) {
//                    Observable observable = RetroWeb.getClient().create(AppServices.class).updateGroup(
//                            getIntent().getStringExtra("id"),binding.etGroupName.getText().toString(),
//                            sharedPrefManager.getAccessToken())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread());
//                    Observer<GeneralResponse> observer = new Observer<GeneralResponse>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(GeneralResponse userModel) {
//                            stopLoading();
//                            if (userModel != null) {
//                                if (userModel.getStatus()) {
//                                    Intent intent = new Intent(CreateNewGroupActivity.this,HomeTeacherActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                    startActivity(intent);
//                                    Bungee.split(CreateNewGroupActivity.this);
//                                    successToast(CreateNewGroupActivity.this,userModel.getMessage());
//
//                                } else {
//                                    errorToast(CreateNewGroupActivity.this,userModel.getMessage());
//                                }
//                            } else {
//                                errorToast(CreateNewGroupActivity.this,getString(R.string.somethingWentWrong));
//                            }
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            handleApiException(CreateNewGroupActivity.this,e);
//                            stopLoading();
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    };
//                    observable.subscribe(observer);
//                } else {
//                    Observable observable = RetroWeb.getClient().create(AppServices.class).addGroup(
//                            getIntent().getStringExtra("sectionId"),binding.etGroupName.getText().toString(),
//                            sharedPrefManager.getAccessToken())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread());
//                    Observer<GeneralResponse> observer = new Observer<GeneralResponse>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(GeneralResponse userModel) {
//                            stopLoading();
//                            if (userModel != null) {
//                                if (userModel.getStatus()) {
//                                    Intent intent = new Intent(CreateNewGroupActivity.this,HomeTeacherActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                    startActivity(intent);
//                                    Bungee.split(CreateNewGroupActivity.this);
//                                    successToast(CreateNewGroupActivity.this,userModel.getMessage());
//
//                                } else {
//                                    errorToast(CreateNewGroupActivity.this,userModel.getMessage());
//                                }
//                            } else {
//                                errorToast(CreateNewGroupActivity.this,getString(R.string.somethingWentWrong));
//                            }
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            handleApiException(CreateNewGroupActivity.this,e);
//                            stopLoading();
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    };
//                    observable.subscribe(observer);
//                }
//
//            }
//        });

    }


    private void initEventDriven() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(CreateNewGroupActivity.this);
        });
    }


}