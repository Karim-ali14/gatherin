package com.orwa.gatherin.Teacher.SubscriptionsList.View;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Teacher.Adapter.SubscriptionPackagesAdapter;
import com.orwa.gatherin.Models.SubscriptionPackagesModel.SubscriptionPlansModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivitySubscriptionsListBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class SubscriptionsListActivity extends ParentClass {
    ActivitySubscriptionsListBinding binding;
    SubscriptionPackagesAdapter subscriptionPackagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionsListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        subscriptionPackagesAdapter = new SubscriptionPackagesAdapter(SubscriptionsListActivity.this,binding.rvSubscriptionsList,binding.tvAssure);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SubscriptionsListActivity.this,RecyclerView.VERTICAL,false);
        binding.rvSubscriptionsList.setLayoutManager(linearLayoutManager);


        Observable observable = RetroWeb.getClient().create(AppServices.class).subscriptionPackages(sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<SubscriptionPlansModel> observer = new Observer<SubscriptionPlansModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SubscriptionPlansModel homeModel) {
                if (homeModel != null) {
                    if (homeModel.isStatus()) {

                        binding.rvSubscriptionsList.hideShimmerAdapter();
                        subscriptionPackagesAdapter.addAll(homeModel.getData());
                        subscriptionPackagesAdapter.notifyDataSetChanged();


                    } else {
                        errorToast(SubscriptionsListActivity.this,homeModel.getMessage());
                        binding.rvSubscriptionsList.setVisibility(View.GONE);
                    }

                } else {
                    errorToast(SubscriptionsListActivity.this,getString(R.string.somethingWentWrong));
                    binding.rvSubscriptionsList.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t) {
                handleApiException(SubscriptionsListActivity.this,t);
                binding.rvSubscriptionsList.setVisibility(View.GONE);

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvSubscriptionsList.setAdapter(subscriptionPackagesAdapter);

    }

    private void initEventDriven() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(SubscriptionsListActivity.this);
        });

    }
}