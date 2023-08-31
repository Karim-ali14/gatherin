package com.orwa.gatherin.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.DepartmentMembersModel.Datum;
import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.utils.SharedPrefManager;
import com.orwa.gatherin.databinding.ItemGroupMembersBinding;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class DepartmentMembersAdapter extends RecyclerView.Adapter<DepartmentMembersAdapter.ViewHolder> {

    ArrayList<Datum> list;
    Context context;
    LayoutInflater layoutInflater;
    ShimmerRecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;
    String groupId;
    String sectionId;
    EditText etName;
    String categoriesId = "0";
    ArrayList<String> items = new ArrayList();
    ArrayList<String> chosenServices = new ArrayList<>();
    TextView tvConfirm;
    String type;


    public DepartmentMembersAdapter(Context context,ShimmerRecyclerView recyclerView,SharedPrefManager sharedPrefManager,String groupId,
                                    String sectionId,String type,TextView tvConfirm,EditText etName) {
        this.list = new ArrayList<>();
        this.context = context;
        this.recyclerView = recyclerView;
        this.sharedPrefManager = sharedPrefManager;
        this.groupId = groupId;
        this.sectionId = sectionId;
        this.type = type;
        this.tvConfirm = tvConfirm;
        this.etName = etName;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        return new ViewHolder(ItemGroupMembersBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.binding.tvUserName.setText(list.get(position).getUsername());
        holder.binding.checkbox.setOnCheckedChangeListener((buttonView,isChecked) -> {
            if (isChecked) {
                chosenServices.add(String.valueOf(list.get(position).getId()));
            } else {
                chosenServices.remove(String.valueOf(list.get(position).getId()));

            }
            Log.e("chosenList",chosenServices.toString() + "good");
        });

        tvConfirm.setOnClickListener(v -> {
            items.clear();
            for (int i = 0; i < chosenServices.size(); i++) {
                if (!(chosenServices.get(i).equals(""))) {
                    items.add(String.valueOf(chosenServices.get(i)));
                }
            }
            categoriesId = android.text.TextUtils.join(",",items);


            boolean cancel = false;
            View focusView = null;


            if (TextUtils.isEmpty(etName.getText().toString())) {
                etName.setError(context.getString(R.string.groupName));
                focusView = etName;
                cancel = true;
            }

            if (cancel) {

            } else {
                ParentClass.showFlipDialog();
                Observable observable;
                Observer<GeneralResponse> observer;
                if (type.equals("updateGroup")) {
                    observable = RetroWeb.getClient().create(AppServices.class).updateGroup(
                            groupId,etName.getText().toString(),
                            sharedPrefManager.getAccessToken(),categoriesId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    observer = new Observer<GeneralResponse>() {
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
                } else {
                    observable = RetroWeb.getClient().create(AppServices.class).addGroup(
                            sectionId,etName.getText().toString(),
                            sharedPrefManager.getAccessToken(),categoriesId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                    observer = new Observer<GeneralResponse>() {
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
                }
                observable.subscribe(observer);
            }
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

    public void addAll(List<Datum> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemGroupMembersBinding binding;

        public ViewHolder(ItemGroupMembersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
