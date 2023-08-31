package com.orwa.gatherin.Teacher.NotificationTeacher.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.NotificationsModel.NotificationsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.ChatHistoryLeaders.View.ChatHistoryLeadersActivity;
import com.orwa.gatherin.Teacher.TeacherProfile.View.TeacherProfileActivity;
import com.orwa.gatherin.User.Adapter.NotificationsAdapter;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityNotificationTeacherBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class NotificationTeacherActivity extends ParentClass {
    ActivityNotificationTeacherBinding binding;
    NotificationsAdapter notificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationTeacherBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getStudentsDepartments();
        initEventDriven();
    }

    private void initEventDriven() {
        binding.relativeHome.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationTeacherActivity.this,ChatHistoryLeadersActivity.class);
            startActivity(intent);
            Bungee.split(NotificationTeacherActivity.this);
        });
        binding.relativeMsg.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationTeacherActivity.this,ChatHistoryLeadersActivity.class);
            startActivity(intent);
            Bungee.split(NotificationTeacherActivity.this);
        });
        binding.relativeProfile.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationTeacherActivity.this,TeacherProfileActivity.class);
            startActivity(intent);
            Bungee.split(NotificationTeacherActivity.this);
        });
    }
    private void getStudentsDepartments() {
        showFlipDialog();
        notificationsAdapter = new NotificationsAdapter(NotificationTeacherActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationTeacherActivity.this, RecyclerView.VERTICAL, false);
        binding.rvNotification.setLayoutManager(linearLayoutManager);
        Observable observable = RetroWeb.getClient().create(AppServices.class).getTeacherNotification(sharedPrefManager.getUserDate().getId(), sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<NotificationsModel> observer = new Observer<NotificationsModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(NotificationsModel homeModel) {
                dismissFlipDialog();
                if (homeModel != null) {
                    if (homeModel.isStatus()) {
                        notificationsAdapter.addAll(homeModel.getData());
                        notificationsAdapter.notifyDataSetChanged();
                        if (homeModel.getData().size() > 0) {
                            binding.rvNotification.setVisibility(View.VISIBLE);
                            binding.rlNoNotifications.setVisibility(View.GONE);
                        } else {
                            binding.rvNotification.setVisibility(View.GONE);
                            binding.rlNoNotifications.setVisibility(View.VISIBLE);
                        }
                        Log.e("homeModel.getData()0", homeModel.getData().size() + "GOOD");
                        Log.e("homeModel.getData()", homeModel.getData() + "GOOD");
                        Log.e("homeModel.getData()12", homeModel + "GOOD");
                        Log.e("homeModel.getData()1", homeModel.getMessage() + "GOOD");
                    } else {
                        errorToast(NotificationTeacherActivity.this, homeModel.getMessage());
                        binding.rvNotification.setVisibility(View.GONE);
                        binding.rlNoNotifications.setVisibility(View.VISIBLE);
                    }

                } else {
                    errorToast(NotificationTeacherActivity.this, getString(R.string.somethingWentWrong));
                    binding.rvNotification.setVisibility(View.GONE);
                    binding.rlNoNotifications.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Throwable t) {
                dismissFlipDialog();
                handleApiException(NotificationTeacherActivity.this, t);
                binding.rvNotification.setVisibility(View.GONE);
                binding.rlNoNotifications.setVisibility(View.VISIBLE);

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvNotification.setAdapter(notificationsAdapter);
    }

}