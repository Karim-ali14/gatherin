package com.orwa.gatherin.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.GroupMembersModel.Student;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.utils.SharedPrefManager;
import com.orwa.gatherin.databinding.ItemmMembersToChooseCaptainBinding;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class ChooseLeaderAdapter extends RecyclerView.Adapter<ChooseLeaderAdapter.ViewHolder> {

    ArrayList<Student> list;
    Context context;
    LayoutInflater layoutInflater;
    ShimmerRecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;
    String groupId;


    public ChooseLeaderAdapter(Context context,ShimmerRecyclerView recyclerView,SharedPrefManager sharedPrefManager,String groupId) {
        this.list = new ArrayList<>();
        this.context = context;
        this.recyclerView = recyclerView;
        this.sharedPrefManager = sharedPrefManager;
        this.groupId = groupId;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        return new ViewHolder(ItemmMembersToChooseCaptainBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.binding.tvUserName.setText(list.get(position).getUsername());
        holder.binding.tvSelectLeader.setOnClickListener(v -> {
            ParentClass.showFlipDialog();
            Observable observable = RetroWeb.getClient().create(AppServices.class).setLeader(groupId,list.get(position).getId(),
                    sharedPrefManager.getAccessToken())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            Observer<GeneralResponse> observer = new Observer<GeneralResponse>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(GeneralResponse userModel) {
                    ParentClass.dismissFlipDialog();
                    if (userModel != null) {
                        if (userModel.getStatus()) {
                            Intent intent = new Intent(context,HomeTeacherActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            context.startActivity(intent);
                            Bungee.split(context);
                            ParentClass.makeSuccessToast(context,userModel.getMessage());


                        } else {
                            ParentClass.makeErrorToast(context,userModel.getMessage());
                        }
                    } else {
                        ParentClass.makeErrorToast(context,context.getString(R.string.somethingWentWrong));
                    }

                }

                @Override
                public void onError(Throwable e) {
                    ParentClass.handleException(context,e);
                    ParentClass.dismissFlipDialog();
                }

                @Override
                public void onComplete() {

                }
            };
            observable.subscribe(observer);

        });

    }

    @Override
    public int getItemCount() {

        if (list.size() == 0) {
            recyclerView.showShimmerAdapter();
            return 10;
        } else {
            return list.size();
        }

    }

    public int getItemViewType(int position) {
        return position;
    }

    public void addAll(List<Student> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemmMembersToChooseCaptainBinding binding;

        public ViewHolder(ItemmMembersToChooseCaptainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
