package com.orwa.gatherin.User.SectionGroups.View;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.UserGroupsModel.UserGroupsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.User.Adapter.StudentGroupsAdapter;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivitySectionGroupsUserBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class SectionGroupsUserActivity extends ParentClass {

    ActivitySectionGroupsUserBinding binding;
    StudentGroupsAdapter subscriptionPackagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySectionGroupsUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getLang(this).equals("ar")) {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        } else {
            binding.ivBack.setImageResource(R.drawable.ab_back);
        }
        initUi();
        initEventDriven();
    }


    private void initUi() {
        subscriptionPackagesAdapter = new StudentGroupsAdapter(SectionGroupsUserActivity.this,binding.rvSectionGroups);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SectionGroupsUserActivity.this,RecyclerView.VERTICAL,false);
        binding.rvSectionGroups.setLayoutManager(linearLayoutManager);


        Observable observable = RetroWeb.getClient().create(AppServices.class).getStudentGroups(sharedPrefManager.getUserDate().getId(),
                sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<UserGroupsModel> observer = new Observer<UserGroupsModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UserGroupsModel homeModel) {
                if (homeModel != null) {
                    if (homeModel.isStatus()) {

                        binding.rvSectionGroups.hideShimmerAdapter();
                        subscriptionPackagesAdapter.addAll(homeModel.getData());
                        subscriptionPackagesAdapter.notifyDataSetChanged();


                    } else {
                        errorToast(SectionGroupsUserActivity.this,homeModel.getMessage());
                        binding.rvSectionGroups.setVisibility(View.GONE);
                    }

                } else {
                    errorToast(SectionGroupsUserActivity.this,getString(R.string.somethingWentWrong));
                    binding.rvSectionGroups.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t) {
                handleApiException(SectionGroupsUserActivity.this,t);
                binding.rvSectionGroups.setVisibility(View.GONE);

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvSectionGroups.setAdapter(subscriptionPackagesAdapter);

    }

    private void initEventDriven() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(SectionGroupsUserActivity.this);
        });

    }

}