package com.orwa.gatherin.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.SubscriptionPackagesModel.Datum;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.utils.SharedPrefManager;
import com.orwa.gatherin.databinding.ItemSubscriptionsListBinding;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

public class SubscriptionPackagesAdapter extends RecyclerView.Adapter<SubscriptionPackagesAdapter.ViewHolder> {

    ArrayList<Datum> list;
    Context context;
    LayoutInflater layoutInflater;
    TextView tvAssure;
    ShimmerRecyclerView recyclerView;
    String planeId;
    private int selectedPosition = -1;
    SharedPrefManager sharedPrefManager;

    public SubscriptionPackagesAdapter(Context context,ShimmerRecyclerView recyclerView,TextView tvAssure) {
        this.list = new ArrayList<>();
        this.context = context;
        this.sharedPrefManager = new SharedPrefManager(context);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.recyclerView = recyclerView;
        this.tvAssure = tvAssure;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        return new ViewHolder(ItemSubscriptionsListBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.binding.tvSubscriptionName.setText(list.get(position).getName());
        holder.binding.tvCountSections.setText(context.getString(R.string.numberOfSections) + " " + list.get(position).getMaxDepartments());
        holder.binding.tvCountGroups.setText(context.getString(R.string.numberOfGroups) + " " + list.get(position).getMaxGroups());
        holder.binding.tvPrice.setText(list.get(position).getCost() + " " + list.get(position).getCurrency());
        if (selectedPosition == position) {
            holder.binding.view.setVisibility(View.VISIBLE);
        } else {
            holder.binding.view.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            planeId = list.get(holder.getAdapterPosition()).getId();
            notifyDataSetChanged();

        });

        tvAssure.setOnClickListener(v -> {
            if (planeId.equals("")) {
                ParentClass.makeErrorToast(context,context.getString(R.string.choosePlaneFirst));
            } else {

                ParentClass.showFlipDialog();
                Observable observable = RetroWeb.getClient().create(AppServices.class).updateTeacherPlan(sharedPrefManager.getUserDate().getId(),
                        planeId,sharedPrefManager.getAccessToken())
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
        ItemSubscriptionsListBinding binding;

        public ViewHolder(ItemSubscriptionsListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
