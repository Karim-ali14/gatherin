package com.orwa.gatherin.present.student.group

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.orwa.gatherin.databinding.ItemFeatureGroupResultBinding
import com.orwa.gatherin.model.group_results.FeatureX

import com.orwa.gatherin.present.student.home.placeholder.PlaceholderContent.PlaceholderItem

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * Child adapter
 */
class StudentFeaturesResultsRecyclerViewAdapter(
    private var values: List<FeatureX>, //Need update
    private val ctx: Context) : RecyclerView.Adapter<StudentFeaturesResultsRecyclerViewAdapter.ViewHolder>() {

//    private lateinit var binding:ItemFeatureGroupResultBinding


//    private val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemFeatureGroupResultBinding.inflate(
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

    inner class ViewHolder(binding: ItemFeatureGroupResultBinding) : RecyclerView.ViewHolder(binding.root) {
        val featureName: TextView = binding.tvItemName
        val featureMark : EditText = binding.tvMark

        fun bind(item:FeatureX){
            featureName.text = item.featureName
            featureMark.setText(item.featureMark)
            featureMark.isEnabled = false
        }
    }

    fun swapData(items:List<FeatureX>){
        values = items
        notifyDataSetChanged()
    }

}