package com.orwa.gatherin.present.student.section

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.GroupItemBinding
import com.orwa.gatherin.model.section.GroupListResItem

import com.orwa.gatherin.present.student.home.placeholder.PlaceholderContent.PlaceholderItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GroupItemRecyclerViewAdapter(
    private var values: List<GroupListResItem>, //Need update
    private val clickListener: GroupClickListener,
    private val ctx: Context,
    private val emptyVew: View
) : RecyclerView.Adapter<GroupItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            GroupItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: GroupItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val groupName: TextView = binding.groupName
        val groupLock : TextView = binding.groupLockTv

        init {
            binding.root.setOnClickListener{
                val item = values[bindingAdapterPosition]
                if(item.join){
                    clickListener.onClickListener(item)
                }else{
                    Toast.makeText(ctx, R.string.group_click_not_member ,Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun bind(item:GroupListResItem){
            groupName.text = item.name
            if(item.join){
                groupLock.setText(R.string.my_group_label)
                groupLock.setTextColor(ContextCompat.getColor(ctx,R.color.green))
            }
        }

    }

    fun swapData(items:List<GroupListResItem>){
        values = items
        if(values.size==0){
            emptyVew.show()
        }else{
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

}