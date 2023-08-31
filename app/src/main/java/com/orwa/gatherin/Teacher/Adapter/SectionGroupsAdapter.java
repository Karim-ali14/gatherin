package com.orwa.gatherin.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GeneralResponse.GeneralResponse;
import com.orwa.gatherin.Models.TeacherGroupsModel.Group;
import com.orwa.gatherin.Network.AppServices;
import com.orwa.gatherin.R;
import com.orwa.gatherin.Teacher.CreateNewGroup.View.CreateNewGroupActivity;
import com.orwa.gatherin.Teacher.GroupTeacherDetails.View.GroupDetailsTeacherActivity;
import com.orwa.gatherin.utils.ParentClass;
import com.orwa.gatherin.utils.RetroWeb;
import com.orwa.gatherin.databinding.ItemHomeBinding;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import spencerstudios.com.bungeelib.Bungee;

import static com.orwa.gatherin.utils.ParentClass.sharedPrefManager;

public class SectionGroupsAdapter extends RecyclerView.Adapter<SectionGroupsAdapter.ViewHolder> {

    ArrayList<Group> list;
    Context context;
    LayoutInflater layoutInflater;
    ShimmerRecyclerView recyclerView;
    String sectionId;

    public SectionGroupsAdapter(Context context,ShimmerRecyclerView recyclerView,String sectionId) {
        this.list = new ArrayList<>();
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.recyclerView = recyclerView;
        this.sectionId = sectionId;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        return new ViewHolder(ItemHomeBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.binding.tvSection.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,GroupDetailsTeacherActivity.class);
            intent.putExtra("name",list.get(position).getName());
            intent.putExtra("id",list.get(position).getId());
            context.startActivity(intent);
        });
        holder.binding.ivDelete.setOnClickListener(v -> {
            ParentClass.showFlipDialog();
            Observable observable = RetroWeb.getClient().create(AppServices.class).deleteGroup(
                    list.get(position).getId(),
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
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemMoved(position,list.size());
                            notifyDataSetChanged();
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
        holder.binding.ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context,CreateNewGroupActivity.class);
            intent.putExtra("name",list.get(position).getName());
            intent.putExtra("type","updateGroup");
            intent.putExtra("id",list.get(position).getId());
            intent.putExtra("sectionId",sectionId);
            context.startActivity(intent);
            Bungee.split(context);
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

    public void addAll(List<Group> data) {
        list.clear();
        list.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemHomeBinding binding;

        public ViewHolder(ItemHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
