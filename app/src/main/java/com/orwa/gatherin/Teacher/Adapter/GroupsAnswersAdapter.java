package com.orwa.gatherin.Teacher.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orwa.gatherin.Models.GroupAnswersModel.Datum;
import com.orwa.gatherin.R;
import com.orwa.gatherin.databinding.ItemGroupsResultsBinding;

import java.util.ArrayList;
import java.util.List;


public class GroupsAnswersAdapter extends RecyclerView.Adapter<GroupsAnswersAdapter.ViewHolder> {

    ArrayList<Datum> list;
    Context context;
    LayoutInflater layoutInflater;
    String sectionId;

    public GroupsAnswersAdapter(Context context) {
        this.list = new ArrayList<>();
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sectionId = sectionId;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupsAnswersAdapter.ViewHolder(ItemGroupsResultsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.tvGroupName.setText(list.get(position).getName());
        if (list.get(position).getAnswers()==0){
            holder.binding.tvAnswersCount.setText(context.getString(R.string.noAnswer));
            holder.binding.tvAnswersCount.setTextColor(Color.parseColor("#F70008"));
        }else {
            holder.binding.tvAnswersCount.setText(context.getString(R.string.thereIsAnAnswer));
            holder.binding.tvAnswersCount.setTextColor(Color.parseColor("#51b704"));
        }
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
        ItemGroupsResultsBinding binding;

        public ViewHolder(ItemGroupsResultsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
