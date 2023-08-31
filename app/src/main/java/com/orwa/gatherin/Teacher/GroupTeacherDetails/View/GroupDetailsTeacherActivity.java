package com.orwa.gatherin.Teacher.GroupTeacherDetails.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GroupMembersModel.GroupStusentsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.ChooseGroupCaptain.View.ChooseGroupCaptainActivity;
import com.orwa.gatherin.User.Adapter.GroupMembersAdapter;
import com.orwa.gatherin.User.ChatUser.View.ChatUserActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityGroupDetailsTeacherBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class GroupDetailsTeacherActivity extends ParentClass {
    ActivityGroupDetailsTeacherBinding binding;
    GroupMembersAdapter subscriptionPackagesAdapter;
    String studentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupDetailsTeacherBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        subscriptionPackagesAdapter = new GroupMembersAdapter(GroupDetailsTeacherActivity.this, binding.rvMembers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupDetailsTeacherActivity.this, RecyclerView.VERTICAL, false);
        binding.rvMembers.setLayoutManager(linearLayoutManager);


        Observable observable = RetroWeb.getClient().create(AppServices.class).getGroupMembers(getIntent().getStringExtra("id"),
                sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<GroupStusentsModel> observer = new Observer<GroupStusentsModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GroupStusentsModel homeModel) {
                if (homeModel != null) {
                    if (homeModel.isStatus()) {
                        binding.tvTitle.setText(homeModel.getData().getName());
                        binding.rvMembers.hideShimmerAdapter();
                        subscriptionPackagesAdapter.addAll(homeModel.getData().getStudents());
                        subscriptionPackagesAdapter.notifyDataSetChanged();
                        studentId = homeModel.getData().getLeader();

                    } else {
                        errorToast(GroupDetailsTeacherActivity.this, homeModel.getMessage());
                        binding.rvMembers.setVisibility(View.GONE);
                    }

                } else {
                    errorToast(GroupDetailsTeacherActivity.this, getString(R.string.somethingWentWrong));
                    binding.rvMembers.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t) {
                handleApiException(GroupDetailsTeacherActivity.this, t);
                binding.rvMembers.setVisibility(View.GONE);

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvMembers.setAdapter(subscriptionPackagesAdapter);

    }

    private void initEventDriven() {
        if (getLang(this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        } else {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        }
        binding.rlChooseLeader.setOnClickListener(v -> {
            Intent intent = new Intent(GroupDetailsTeacherActivity.this, ChooseGroupCaptainActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
            Bungee.split(GroupDetailsTeacherActivity.this);
        });
        binding.relativeChat.setOnClickListener(v -> {
            Intent intent = new Intent(GroupDetailsTeacherActivity.this, ChatUserActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            intent.putExtra("tech_id", sharedPrefManager.getUserDate().getId());
            intent.putExtra("type", "tech");
            startActivity(intent);
            Bungee.split(GroupDetailsTeacherActivity.this);
        });
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(GroupDetailsTeacherActivity.this);
        });
    }
}