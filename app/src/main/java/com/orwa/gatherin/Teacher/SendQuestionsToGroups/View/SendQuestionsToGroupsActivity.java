package com.orwa.gatherin.Teacher.SendQuestionsToGroups.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GroupsModel.GroupsModel;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Adapter.GroupsInQuestionsAdapter;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ActivitySendQuestionsToGroupsBinding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class SendQuestionsToGroupsActivity extends ParentClass {
    ActivitySendQuestionsToGroupsBinding binding;
    GroupsInQuestionsAdapter groupsInQuestionsAdapter;
    public static String type = "essay";
    // mcq
    public static int numberOfMcq = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendQuestionsToGroupsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getStudentsDepartments();
        initEventDriven();
    }

    private void initEventDriven() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
            Bungee.split(SendQuestionsToGroupsActivity.this);
        });

        binding.radioEssayQuestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "essay";
                    binding.radioMCQuestion.setChecked(false);
                }
            }
        });
        binding.radioMCQuestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "mcq";
                    binding.radioEssayQuestion.setChecked(false);
                }
            }
        });
        binding.etAddChoice.setOnClickListener(v -> {
            if (numberOfMcq == 2) {
                binding.etThirdChoice.setVisibility(View.VISIBLE);
                numberOfMcq = 3;
            } else if (numberOfMcq == 3) {
                binding.etFourthChoice.setVisibility(View.VISIBLE);
                numberOfMcq = 4;
            }
        });


    }

    private void getStudentsDepartments() {
        showFlipDialog();
        groupsInQuestionsAdapter = new GroupsInQuestionsAdapter(SendQuestionsToGroupsActivity.this,
                binding.tvSend, sharedPrefManager,
                binding.etEssay, binding.etMCQ, binding.etFirstChoice,
                binding.etSecondChoice, binding.etThirdChoice,
                binding.etFourthChoice);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SendQuestionsToGroupsActivity.this, RecyclerView.VERTICAL, false);
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
                        errorToast(SendQuestionsToGroupsActivity.this, homeModel.getMessage());
                    }

                } else {
                    errorToast(SendQuestionsToGroupsActivity.this, getString(R.string.somethingWentWrong));
                }

            }

            @Override
            public void onError(Throwable t) {
                dismissFlipDialog();
                handleApiException(SendQuestionsToGroupsActivity.this, t);
            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
        binding.rvGroupsNames.setAdapter(groupsInQuestionsAdapter);
    }
}