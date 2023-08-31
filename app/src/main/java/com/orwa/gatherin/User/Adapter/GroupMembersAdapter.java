package com.orwa.gatherin.User.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GroupMembersModel.Student;
import com.orwa.gatherin.databinding.ItemMemberBinding;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.ViewHolder> {

    ArrayList<Student> list;
    Context context;
    LayoutInflater layoutInflater;
    ShimmerRecyclerView recyclerView;


    public GroupMembersAdapter(Context context,ShimmerRecyclerView recyclerView) {
        this.list = new ArrayList<>();
        this.context = context;
        this.recyclerView = recyclerView;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        return new ViewHolder(ItemMemberBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.binding.tvUserName.setText(list.get(position).getUsername());

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
        ItemMemberBinding binding;

        public ViewHolder(ItemMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
