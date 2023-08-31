package com.orwa.gatherin.User.HomeUser.View;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.StudentsDepartmentsModel.StudentDepartmentsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.User.Adapter.StudentDepartmentsAdapter;
import com.orwa.gatherin.User.NotificationsUser.View.NotificationUserActivity;
import com.orwa.gatherin.User.SettingsUser.View.SettingsUserActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityHomeUserBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class HomeUserActivity extends ParentClass {

    ActivityHomeUserBinding binding;
    StudentDepartmentsAdapter studentDepartmentsAdapter;

    Dialog popupJoinSectionDialog;
    ImageView ivClose;
    EditText etCode;
    TextView tvConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
        initUi();
    }

    private void initUi() {
        binding.tvUserName.setText(sharedPrefManager.getUserDate().getUsername());
        popupJoinSectionDialog = new Dialog(this, android.R.style.Theme_Dialog);
        popupJoinSectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popupJoinSectionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        popupJoinSectionDialog.setContentView(R.layout.popup_join_section);
        // to set width and height
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        lp1.copyFrom(popupJoinSectionDialog.getWindow().getAttributes());
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ivClose = (ImageView) popupJoinSectionDialog.findViewById(R.id.ivClose);
        etCode = (EditText) popupJoinSectionDialog.findViewById(R.id.etCode);
        tvConfirm = (TextView) popupJoinSectionDialog.findViewById(R.id.tvConfirm);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupJoinSectionDialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCode.getText().toString())) {
                    etCode.setError(getString(R.string.enterCode));
                } else {
                    showFlipDialog();
                    Observable observable = RetroWeb.getClient().create(AppServices.class).enrollStudentInDepartment(sharedPrefManager.getUserDate().getId(), sharedPrefManager.getAccessToken(), etCode.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    Observer<GeneralResponse> observer = new Observer<GeneralResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(GeneralResponse userModel) {
                            dismissFlipDialog();
                            if (userModel != null) {
                                if (userModel.getStatus()) {
                                    successToast(HomeUserActivity.this, userModel.getMessage());
                                    popupJoinSectionDialog.dismiss();
                                } else {
                                    errorToast(HomeUserActivity.this, userModel.getMessage());
                                }
                            } else {
                                errorToast(HomeUserActivity.this, getString(R.string.somethingWentWrong));
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissFlipDialog();
                            handleApiException(HomeUserActivity.this, e);
                            Log.e("e", e.toString());

                        }

                        @Override
                        public void onComplete() {

                        }
                    };
                    observable.subscribe(observer);

                }
            }

        });


        getStudentsDepartments();


    }

    private void initEventDriven() {
        binding.rlJoinToSection.setOnClickListener(v -> {
            popupJoinSectionDialog.show();

        });
        binding.ivJoinCategory.setOnClickListener(v -> {
            popupJoinSectionDialog.show();

        });
        binding.ivNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HomeUserActivity.this, NotificationUserActivity.class);
            startActivity(intent);
            Bungee.split(HomeUserActivity.this);
        });
        binding.ivSettings.setOnClickListener(v -> {
            Intent intent = new Intent(HomeUserActivity.this, SettingsUserActivity.class);
            intent.putExtra("type", "user");
            startActivity(intent);
            Bungee.split(HomeUserActivity.this);
        });
    }

    private void getStudentsDepartments() {
        showFlipDialog();
        studentDepartmentsAdapter = new StudentDepartmentsAdapter(HomeUserActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeUserActivity.this, RecyclerView.VERTICAL, false);
        binding.rvSections.setLayoutManager(linearLayoutManager);
        Observable observable = RetroWeb.getClient().create(AppServices.class).getStudentDepartments(sharedPrefManager.getUserDate().getId(), sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<StudentDepartmentsModel> observer = new Observer<StudentDepartmentsModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(StudentDepartmentsModel homeModel) {
                dismissFlipDialog();
                if (homeModel != null) {
                    if (homeModel.isStatus()) {
                        studentDepartmentsAdapter.addAll(homeModel.getData());
                        studentDepartmentsAdapter.notifyDataSetChanged();
                        if (homeModel.getData().size() > 0) {
                            binding.rlSections.setVisibility(View.VISIBLE);
                            binding.rlJoinToSection.setVisibility(View.GONE);
                        } else {
                            binding.rlSections.setVisibility(View.GONE);
                            binding.rlJoinToSection.setVisibility(View.VISIBLE);
                        }
                        Log.e("homeModel.getData()0", homeModel.getData().size() + "GOOD");
                        Log.e("homeModel.getData()", homeModel.getData() + "GOOD");
                        Log.e("homeModel.getData()12", homeModel + "GOOD");
                        Log.e("homeModel.getData()1", homeModel.getMessage() + "GOOD");
                    } else {
                        errorToast(HomeUserActivity.this, homeModel.getMessage());
                        binding.rlSections.setVisibility(View.GONE);
                        binding.rlJoinToSection.setVisibility(View.VISIBLE);
                    }

                } else {
                    errorToast(HomeUserActivity.this, getString(R.string.somethingWentWrong));
                    binding.rlSections.setVisibility(View.GONE);
                    binding.rlJoinToSection.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Throwable t) {
                dismissFlipDialog();
                handleApiException(HomeUserActivity.this, t);
                binding.rlSections.setVisibility(View.GONE);
                binding.rlJoinToSection.setVisibility(View.VISIBLE);

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvSections.setAdapter(studentDepartmentsAdapter);
    }
}