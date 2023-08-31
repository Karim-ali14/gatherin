package com.orwa.gatherin.User.NotificationsUser.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.NotificationsModel.NotificationsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.User.Adapter.NotificationsAdapter;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityNotificationUserBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class NotificationUserActivity extends ParentClass {
    ActivityNotificationUserBinding binding;
    NotificationsAdapter notificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getLang(this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        } else {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        }
        getStudentsDepartments();
        initEventDriven();

    }


    private void initEventDriven() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(NotificationUserActivity.this);
        });
    }
    private void getStudentsDepartments() {
        showFlipDialog();
        notificationsAdapter = new NotificationsAdapter(NotificationUserActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationUserActivity.this, RecyclerView.VERTICAL, false);
        binding.rvNotification.setLayoutManager(linearLayoutManager);
        Observable observable = RetroWeb.getClient().create(AppServices.class).getStudentNotification(sharedPrefManager.getUserDate().getId(), sharedPrefManager.getAccessToken())
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
                        errorToast(NotificationUserActivity.this, homeModel.getMessage());
                        binding.rvNotification.setVisibility(View.GONE);
                        binding.rlNoNotifications.setVisibility(View.VISIBLE);
                    }

                } else {
                    errorToast(NotificationUserActivity.this, getString(R.string.somethingWentWrong));
                    binding.rvNotification.setVisibility(View.GONE);
                    binding.rlNoNotifications.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Throwable t) {
                dismissFlipDialog();
                handleApiException(NotificationUserActivity.this, t);
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