package com.orwa.gatherin.Teacher.Home.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Teacher.Adapter.TeacherHomeAdapter;
import com.orwa.gatherin.Models.HomeTeacherModel.HomeTeacherModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.ChatHistoryLeaders.View.ChatHistoryLeadersActivity;
import com.orwa.gatherin.Teacher.CreateNewSection.View.CreateNewSectionActivity;
import com.orwa.gatherin.Teacher.GroupsResults.View.GroupsResultsActivity;
import com.orwa.gatherin.Teacher.NotificationTeacher.View.NotificationTeacherActivity;
import com.orwa.gatherin.Teacher.SendNotificationAndReportsFirstStep.View.SendNotificationAndReportsFirstStepActivity;
import com.orwa.gatherin.Teacher.SubscriptionsList.View.SubscriptionsListActivity;
import com.orwa.gatherin.Teacher.TeacherProfile.View.TeacherProfileActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityHomeTeacherBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class HomeTeacherActivity extends ParentClass {
    ActivityHomeTeacherBinding binding;
    TeacherHomeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeTeacherBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        adapter = new TeacherHomeAdapter(HomeTeacherActivity.this,binding.rvHomeTeacher);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeTeacherActivity.this,RecyclerView.VERTICAL,false);
        binding.rvHomeTeacher.setLayoutManager(linearLayoutManager);


        Observable observable = RetroWeb.getClient().create(AppServices.class).teacherHome(
                sharedPrefManager.getUserDate().getId(),
                sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
//        Log.e("user",sharedPrefManager.getUserDate().getId());
        Observer<HomeTeacherModel> observer = new Observer<HomeTeacherModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(HomeTeacherModel homeModel) {
//                Log.e("homeModel",homeModel.getData() + "good");
//                Log.e("homeModelMessage",homeModel.getMessage() + "good");
                if (homeModel != null) {
                    if (homeModel.isStatus()) {
                        binding.tvUserName.setText(homeModel.getData().getUsername());

                        if (homeModel.getData().getDepartments().isEmpty()) {
                            binding.rvHomeTeacher.setVisibility(View.GONE);
                        } else {
                            binding.rvHomeTeacher.hideShimmerAdapter();
                            adapter.addAll(homeModel.getData().getDepartments());
                            adapter.notifyDataSetChanged();
                        }


                    } else {
                        errorToast(HomeTeacherActivity.this,homeModel.getMessage());
                        binding.rvHomeTeacher.setVisibility(View.GONE);
                    }

                } else {
                    errorToast(HomeTeacherActivity.this,getString(R.string.somethingWentWrong));
                    binding.rvHomeTeacher.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t) {
                handleApiException(HomeTeacherActivity.this,t);
                binding.rvHomeTeacher.setVisibility(View.GONE);

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvHomeTeacher.setAdapter(adapter);

    }

    private void initEventDriven() {
        binding.rlSendNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(HomeTeacherActivity.this,SendNotificationAndReportsFirstStepActivity.class);
            startActivity(intent);
            Bungee.split(HomeTeacherActivity.this);
        });
        binding.rlGroupResults.setOnClickListener(v -> {
            Intent intent = new Intent(HomeTeacherActivity.this,GroupsResultsActivity.class);
            startActivity(intent);
            Bungee.split(HomeTeacherActivity.this);
        });
        binding.tvUpgrade.setOnClickListener(v -> {
            Intent intent = new Intent(HomeTeacherActivity.this,SubscriptionsListActivity.class);
            startActivity(intent);
            Bungee.split(HomeTeacherActivity.this);
        });
        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeTeacherActivity.this,CreateNewSectionActivity.class);
            startActivity(intent);
            Bungee.split(HomeTeacherActivity.this);
        });
        binding.relativeMsg.setOnClickListener(v -> {
            Intent intent = new Intent(HomeTeacherActivity.this,ChatHistoryLeadersActivity.class);
            startActivity(intent);
            Bungee.split(HomeTeacherActivity.this);
        });
        binding.relativeNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HomeTeacherActivity.this,NotificationTeacherActivity.class);
            startActivity(intent);
            Bungee.split(HomeTeacherActivity.this);
        });
        binding.relativeProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeTeacherActivity.this,TeacherProfileActivity.class);
            startActivity(intent);
            Bungee.split(HomeTeacherActivity.this);
        });

    }
}