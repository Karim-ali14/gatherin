package com.orwa.gatherin.User.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.UserGroupsModel.Datum;
import com.orwa.gatherin.User.SectionDetailsUser.View.SectionDetailsUserActivity;
import com.orwa.gatherin.databinding.ItemSectionGroupsBinding;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentGroupsAdapter extends RecyclerView.Adapter<StudentGroupsAdapter.ViewHolder> {

    ArrayList<Datum> list;
    Context context;
    LayoutInflater layoutInflater;
    ShimmerRecyclerView recyclerView;


    public StudentGroupsAdapter(Context context,ShimmerRecyclerView recyclerView) {
        this.list = new ArrayList<>();
        this.context = context;
        this.recyclerView = recyclerView;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        return new ViewHolder(ItemSectionGroupsBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.binding.tvGroupName.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,SectionDetailsUserActivity.class);
            intent.putExtra("id",list.get(position).getId());
            context.startActivity(intent);
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
        ItemSectionGroupsBinding binding;

        public ViewHolder(ItemSectionGroupsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
