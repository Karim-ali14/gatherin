package com.orwa.gatherin.present.student.home

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.SectionItemBinding
import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem

import com.orwa.gatherin.present.student.home.placeholder.PlaceholderContent.PlaceholderItem
import com.orwa.gatherin.present.teacher.home.DepartmentClickListener

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SectionItemRecyclerViewAdapter(
    private var values: List<DepartmentsListResItem>,
    private val clickListener: DepartmentClickListener,
    private val ctx: Context) : RecyclerView.Adapter<SectionItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            SectionItemBinding.inflate(
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

    inner class ViewHolder(binding: SectionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val sectionName: TextView = binding.sectionNameTv
        val sectionMaster: TextView = binding.sectionMasterName

        init {
            binding.root.setOnClickListener{
                val item = values[bindingAdapterPosition]
                clickListener.onClickListener(item)
            }
        }

        fun bind(item:DepartmentsListResItem){
            sectionName.text = item.name
            val masterName = ctx.getString(R.string.department_master_name,item.user.fullName)
            sectionMaster.text = masterName
        }

    }

    fun swapData(items:List<DepartmentsListResItem>){
        values = items
        if(values.size==0){
//            emptyVew.show()
        }else{
//            emptyVew.hide()
        }
        notifyDataSetChanged()
    }


}