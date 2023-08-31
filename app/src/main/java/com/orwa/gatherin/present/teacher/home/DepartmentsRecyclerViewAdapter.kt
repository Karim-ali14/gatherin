package com.orwa.gatherin.present.teacher.home

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemDepartmentHomeBinding
import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class DepartmentsRecyclerViewAdapter(
    private var values: List<DepartmentsListResItem>,
    private val onClickListener: DepartmentClickListener?,
    private val ctx: Context,
    private val emptyVew:View,
    private var activateActions:Boolean=false
) : RecyclerView.Adapter<DepartmentsRecyclerViewAdapter.ViewHolder>() {

    private lateinit var binding: ItemDepartmentHomeBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDepartmentHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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
                onClickListener?.onUpdateClickListener(item.id,item.name,item.code)
            }
        }

        fun bind(item:DepartmentsListResItem){
            binding.tvSection.setText(item.name)
            val groupSizeValue = ctx.getString(R.string.section_group_size,item.groups.size, item.membersCounter)
            binding.tvGroupNum.setText(groupSizeValue)
            binding.tvCode.text = item.code
        }

    }

    fun swapData(items:List<DepartmentsListResItem>){
        values = items
        if(values.size==0){
            emptyVew.show()
        }else{
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

    fun swapActivateActions(newValue:Boolean){
        activateActions = newValue
    }


}