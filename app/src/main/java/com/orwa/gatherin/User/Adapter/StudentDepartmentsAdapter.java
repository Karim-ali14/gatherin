package com.orwa.gatherin.User.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.StudentsDepartmentsModel.Datum;
import com.orwa.gatherin.R;
import com.orwa.gatherin.User.SectionGroups.View.SectionGroupsUserActivity;
import com.orwa.gatherin.databinding.ItemHomeUserBinding;

import java.util.ArrayList;
import java.util.List;

public class StudentDepartmentsAdapter extends RecyclerView.Adapter<StudentDepartmentsAdapter.ViewHolder> {
    ArrayList<Datum> list;
    Context context;
    LayoutInflater layoutInflater;


    public StudentDepartmentsAdapter(Context context) {
        this.list = new ArrayList<>();
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        return new StudentDepartmentsAdapter.ViewHolder(ItemHomeUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.binding.tvSection.setText(list.get(position).getName());
        holder.binding.tvGroup.setText("( " + context.getString(R.string.chefName) + " " + list.get(position).getTeacher_name() + " )");
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,SectionGroupsUserActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return list.size();

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
        ItemHomeUserBinding binding;

        public ViewHolder(ItemHomeUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
