package com.orwa.gatherin.present.teacher.home.group_results

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemGroupsResultsBinding
import com.orwa.gatherin.model.group.AllGroupsResItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GroupsResultsRecyclerViewAdapter(
    private var values: List<AllGroupsResItem>, //Need update
    private val onClickListener: GroupResultClickListener?,
    private val ctx: Context,
    private val emptyVew:View
) : RecyclerView.Adapter<GroupsResultsRecyclerViewAdapter.ViewHolder>() {

    private lateinit var binding: ItemGroupsResultsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemGroupsResultsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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
        }

        fun bind(item:AllGroupsResItem){
            binding.tvGroupName.setText(item.name)
            if(!item.isAnswer){ // no answer
                binding.tvAnswersCount.setTextColor(ContextCompat.getColor(ctx,R.color.empty_answers_color))
                binding.tvAnswersCount.setText(R.string.empty_group_answers)

            }else{
                binding.tvAnswersCount.setTextColor(ContextCompat.getColor(ctx,R.color.green))
                binding.tvAnswersCount.setText(R.string.group_results_answers)
            }
        }

    }

    fun swapData(items:List<AllGroupsResItem>){
        values = items
        if(values.size==0){
            emptyVew.show()
        }else{
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }


}