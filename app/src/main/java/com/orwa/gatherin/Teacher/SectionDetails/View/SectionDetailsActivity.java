package com.orwa.gatherin.Teacher.SectionDetails.View;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.TeacherGroupsModel.TeacherGroupsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Adapter.SectionGroupsAdapter;
import com.orwa.gatherin.Teacher.CreateNewGroup.View.CreateNewGroupActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivitySectionDetailsBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class SectionDetailsActivity extends ParentClass {
    ActivitySectionDetailsBinding binding;
    SectionGroupsAdapter subscriptionPackagesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySectionDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initUi();
        initEventDriven();
    }

    private void initUi() {
        subscriptionPackagesAdapter = new SectionGroupsAdapter(SectionDetailsActivity.this,binding.rvMembers,getIntent().getStringExtra("id"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SectionDetailsActivity.this,RecyclerView.VERTICAL,false);
        binding.rvMembers.setLayoutManager(linearLayoutManager);


        Observable observable = RetroWeb.getClient().create(AppServices.class).getSectionDetails(getIntent().getStringExtra("id"),
                sharedPrefManager.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        Observer<TeacherGroupsModel> observer = new Observer<TeacherGroupsModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(TeacherGroupsModel homeModel) {
                if (homeModel != null) {
                    if (homeModel.isStatus()) {
//                        Log.e("id",getIntent().getStringExtra("id"));
                        binding.tvCode.setText(getIntent().getStringExtra("name"));
                        binding.tvLink.setText(getIntent().getStringExtra("link"));

                        binding.rvMembers.hideShimmerAdapter();
                        subscriptionPackagesAdapter.addAll(homeModel.getData().getGroups());
                        subscriptionPackagesAdapter.notifyDataSetChanged();


                    } else {
                        errorToast(SectionDetailsActivity.this,homeModel.getMessage());
                        binding.rvMembers.setVisibility(View.GONE);
                    }

                } else {
                    errorToast(SectionDetailsActivity.this,getString(R.string.somethingWentWrong));
                    binding.rvMembers.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(Throwable t) {
                handleApiException(SectionDetailsActivity.this,t);
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
        binding.rlCopy.setOnClickListener(v -> {
            ParentClass.makeSuccessToast(SectionDetailsActivity.this,getString(R.string.copied));
            ClipboardManager clipboard = (ClipboardManager) SectionDetailsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("code",getIntent().getStringExtra("link"));
            clipboard.setPrimaryClip(clip);
        });
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(SectionDetailsActivity.this);
        });
        binding.floatingActionButton.setOnClickListener(v -> {
            startLoading();
            Observable observable = RetroWeb.getClient().create(AppServices.class).check_adding_group(getIntent().getStringExtra("id"),
                    sharedPrefManager.getAccessToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            Observer<GeneralResponse> observer = new Observer<GeneralResponse>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(GeneralResponse homeModel) {
                    if (homeModel != null) {
                        stopLoading();
                        if (homeModel.getStatus()) {
                            Intent intent = new Intent(SectionDetailsActivity.this,CreateNewGroupActivity.class);
                            intent.putExtra("type","newGroup");
                            intent.putExtra("sectionId",getIntent().getStringExtra("id"));
                            startActivity(intent);
                            Bungee.split(SectionDetailsActivity.this);

                        } else {
                            errorToast(SectionDetailsActivity.this,homeModel.getMessage());
                        }

                    } else {
                        errorToast(SectionDetailsActivity.this,getString(R.string.somethingWentWrong));
                    }

                }

                @Override
                public void onError(Throwable t) {
                    handleApiException(SectionDetailsActivity.this,t);

                }

                @Override
                public void onComplete() {

                }
            };
            observable.subscribe(observer);
        });
    }
}