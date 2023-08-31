package com.orwa.gatherin.Teacher.SendNotificationAndReportsFirstStep.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GroupsModel.GroupsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Adapter.GroupsInNotificationsAdapter;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivitySendNotificationAndReportsBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class SendNotificationAndReportsFirstStepActivity extends ParentClass {
    ActivitySendNotificationAndReportsBinding binding;
    GroupsInNotificationsAdapter groupsInQuestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendNotificationAndReportsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initEventDriven();
        initUi();
    }

    private void initUi() {
        showFlipDialog();
        groupsInQuestionsAdapter = new GroupsInNotificationsAdapter(SendNotificationAndReportsFirstStepActivity.this,binding.ivSend,sharedPrefManager,binding.etNote);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SendNotificationAndReportsFirstStepActivity.this, RecyclerView.VERTICAL, false);
        binding.rvGroupsNames.setLayoutManager(linearLayoutManager);
        Observable observable = RetroWeb.getClient().create(AppServices.class).getTeacherGroups(sharedPrefManager.getUserDate().getId(), sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<GroupsModel> observer = new Observer<GroupsModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GroupsModel homeModel) {
                dismissFlipDialog();
                if (homeModel != null) {
                    if (homeModel.isStatus()) {
                        groupsInQuestionsAdapter.addAll(homeModel.getData());
                        groupsInQuestionsAdapter.notifyDataSetChanged();
                        Log.e("homeModel.getData()0", homeModel.getData().size() + "GOOD");
                        Log.e("homeModel.getData()", homeModel.getData() + "GOOD");
                        Log.e("homeModel.getData()12", homeModel + "GOOD");
                        Log.e("homeModel.getData()1", homeModel.getMessage() + "GOOD");
                    } else {
                        errorToast(SendNotificationAndReportsFirstStepActivity.this, homeModel.getMessage());
                    }

                } else {
                    errorToast(SendNotificationAndReportsFirstStepActivity.this, getString(R.string.somethingWentWrong));
                }

            }

            @Override
            public void onError(Throwable t) {
                dismissFlipDialog();
                handleApiException(SendNotificationAndReportsFirstStepActivity.this, t);
            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvGroupsNames.setAdapter(groupsInQuestionsAdapter);
    }

    private void initEventDriven() {
        if (getLang(this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        } else {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        }
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(SendNotificationAndReportsFirstStepActivity.this);
        });
        binding.ivSend.setOnClickListener(v -> {

        });
    }
}