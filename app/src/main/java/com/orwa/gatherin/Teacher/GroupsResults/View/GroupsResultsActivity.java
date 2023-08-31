package com.orwa.gatherin.Teacher.GroupsResults.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GroupAnswersModel.GroupsAnswersModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Adapter.GroupsAnswersAdapter;
import com.orwa.gatherin.Teacher.SendQuestionsToGroups.View.SendQuestionsToGroupsActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityGroupsResultsBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class GroupsResultsActivity extends ParentClass {
    ActivityGroupsResultsBinding binding;
    GroupsAnswersAdapter groupsAnswersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupsResultsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        groupsAnswersAdapter = new GroupsAnswersAdapter(GroupsResultsActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupsResultsActivity.this, RecyclerView.VERTICAL, false);
        binding.rvAnswers.setLayoutManager(linearLayoutManager);
        showFlipDialog();
        Observable observable = RetroWeb.getClient().create(AppServices.class).getـgroupsـanswers(sharedPrefManager.getUserDate().getId(),
                sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<GroupsAnswersModel> observer = new Observer<GroupsAnswersModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GroupsAnswersModel homeModel) {
                dismissFlipDialog();
                if (homeModel != null) {
                    if (homeModel.isStatus()) {
//                        Log.e("id", getIntent().getStringExtra("id"));
                        groupsAnswersAdapter.addAll(homeModel.getData());
                        groupsAnswersAdapter.notifyDataSetChanged();


                    } else {
                        errorToast(GroupsResultsActivity.this, homeModel.getMessage());
                        binding.rvAnswers.setVisibility(View.GONE);
                    }

                } else {
                    errorToast(GroupsResultsActivity.this, getString(R.string.somethingWentWrong));
                    binding.rvAnswers.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t) {
                dismissFlipDialog();
                handleApiException(GroupsResultsActivity.this, t);
                binding.rvAnswers.setVisibility(View.GONE);

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvAnswers.setAdapter(groupsAnswersAdapter);

    }

    private void initEventDriven() {
        if (getLang(this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        } else {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        }
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(GroupsResultsActivity.this);
        });
        binding.rlSendQuestion.setOnClickListener(v -> {
            Intent intent = new Intent(GroupsResultsActivity.this, SendQuestionsToGroupsActivity.class);
            startActivity(intent);
            Bungee.split(GroupsResultsActivity.this);
        });
    }
}