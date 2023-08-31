package com.orwa.gatherin.Teacher.ChooseGroupCaptain.View;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GroupMembersModel.GroupStusentsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Adapter.ChooseLeaderAdapter;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivityChooseGroupCaptainBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class ChooseGroupCaptainActivity extends ParentClass {
    ActivityChooseGroupCaptainBinding binding;
    ChooseLeaderAdapter subscriptionPackagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseGroupCaptainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        subscriptionPackagesAdapter = new ChooseLeaderAdapter(ChooseGroupCaptainActivity.this,binding.rvMembers,sharedPrefManager,
                getIntent().getStringExtra("id"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChooseGroupCaptainActivity.this,RecyclerView.VERTICAL,false);
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


                    } else {
                        errorToast(ChooseGroupCaptainActivity.this,homeModel.getMessage());
                        binding.rvMembers.setVisibility(View.GONE);
                    }

                } else {
                    errorToast(ChooseGroupCaptainActivity.this,getString(R.string.somethingWentWrong));
                    binding.rvMembers.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t) {
                handleApiException(ChooseGroupCaptainActivity.this,t);
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

        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(ChooseGroupCaptainActivity.this);
        });
    }
}