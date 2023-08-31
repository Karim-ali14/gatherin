package com.orwa.gatherin.present.student.group

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.orwa.gatherin.databinding.ItemStudentGroupResultsParentBinding
import com.orwa.gatherin.model.group_results.DepartmentResultsResItem

import com.orwa.gatherin.present.student.home.placeholder.PlaceholderContent.PlaceholderItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * Parent adapter
 */
class StudentGroupsResultsRecyclerViewAdapter(
    private var values: List<DepartmentResultsResItem>, //Need update
    private val ctx: Context,
    private val emptyVew: View
) : RecyclerView.Adapter<StudentGroupsResultsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemStudentGroupResultsParentBinding.inflate(
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

    inner class ViewHolder(binding: ItemStudentGroupResultsParentBinding) : RecyclerView.ViewHolder(binding.root) {
        val groupName: TextView = binding.tvGroupName
        val totalMarks : TextView = binding.tvTotalMarks
        val rv:RecyclerView = binding.rvChild
        val childLayoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)


        fun bind(item:DepartmentResultsResItem){
            groupName.text = item.name
            totalMarks.text = item.totalMarks.toString()

//            Log.i(TAG,"CHILD_DATA=${item.values}")
            rv.apply {
                layoutManager = childLayoutManager
                adapter = StudentFeaturesResultsRecyclerViewAdapter(item.features,ctx)
//                setRecycledViewPool(viewPool)
            }
        }

    }

    fun swapData(items:List<DepartmentResultsResItem>){
        values = items
        if(values.size==0){
            emptyVew.show()
        }else{
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

}