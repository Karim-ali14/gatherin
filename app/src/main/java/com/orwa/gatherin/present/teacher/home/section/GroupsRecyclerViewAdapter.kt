package com.orwa.gatherin.present.teacher.home.section

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemGroupHomeBinding
import com.orwa.gatherin.model.section.GroupListResItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GroupsRecyclerViewAdapter(
    private var values: List<GroupListResItem>, //Need update
    private val onClickListener: GroupClickListener?,
    private val ctx: Context,
    private val emptyVew:View
) : RecyclerView.Adapter<GroupsRecyclerViewAdapter.ViewHolder>() {

    private lateinit var binding: ItemGroupHomeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemGroupHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                val item = values[bindingAdapterPosition]
                onClickListener?.onClickListener(item) }

            binding.ivDelete.setOnClickListener {
                val item = values[bindingAdapterPosition]
                onClickListener?.onDeleteListener(item.id)
            }
            binding.ivEdit.setOnClickListener {
                val item = values[bindingAdapterPosition]
                onClickListener?.onUpdateClickListener(item)
            }
        }

        fun bind(item:GroupListResItem){
            binding.tvGroupName.setText(item.name)
            val groupSizeValue = ctx.getString(R.string.group_members_placeholder,item.membersCount)
            binding.tvMembersNum.setText(groupSizeValue)
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