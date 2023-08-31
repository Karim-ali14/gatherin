package com.orwa.gatherin.present.teacher.home.send_quest

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemGroupNamesBinding
import com.orwa.gatherin.model.group.AllGroupsResItem

import com.orwa.gatherin.present.student.home.placeholder.PlaceholderContent.PlaceholderItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GroupItemRecyclerViewAdapter(
    private var values: List<AllGroupsResItem>, //Need update
    private val ctx: Context,
    private val emptyVew: View,
    private val showOnlyGroupName: Boolean = false,
    private val uncheckAllCallBack:()->Unit
) : RecyclerView.Adapter<GroupItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemGroupNamesBinding.inflate(
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

    inner class ViewHolder(binding: ItemGroupNamesBinding) : RecyclerView.ViewHolder(binding.root) {
        val groupName: TextView = binding.tvGroupName
        val selectGroup: CheckBox = binding.cbGroup

        init {
            selectGroup.setOnCheckedChangeListener { buttonView, isChecked ->
                values[bindingAdapterPosition].isSelected = isChecked

                if (!isChecked) {
                    uncheckAllCallBack.invoke()
                }
                //
            }
        }

        fun bind(item: AllGroupsResItem) {
            selectGroup.isChecked = item.isSelected
            if (showOnlyGroupName) {
                groupName.text = item.name
            } else {
                groupName.text = getLabelName(item.name)
            }
        }
    }

    fun swapData(items: List<AllGroupsResItem>) {
        values = items
        if (values.size == 0) {
            emptyVew.show()
        } else {
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }


    fun getLabelName(groupName: String): String {
        return ctx.getString(R.string.select_group_to_send, groupName)
    }

    fun getSelectedGroupsIds(): List<Int> {
        val l = ArrayList<Int>()
        for (item in values) {
            if (item.isSelected) {
                l.add(item.id)
            }
        }
        return l.toList()
    }

    /**
     * Set all groups to be checkec
     */
    fun checkAllGroups() {
        for (v in values) {
            v.isSelected = true
        }
        notifyDataSetChanged()
    }

}