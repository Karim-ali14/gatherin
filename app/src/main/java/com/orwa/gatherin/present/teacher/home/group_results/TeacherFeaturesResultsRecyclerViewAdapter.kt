package com.orwa.gatherin.present.teacher.home.group_results

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.orwa.gatherin.databinding.ItemFeatureGroupResultBinding
import com.orwa.gatherin.model.group_results.Value

import com.orwa.gatherin.present.student.home.placeholder.PlaceholderContent.PlaceholderItem

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * Child adapter
 */
class TeacherFeaturesResultsRecyclerViewAdapter(
    private var values: List<Value>, //Need update
    private val ctx: Context
) : RecyclerView.Adapter<TeacherFeaturesResultsRecyclerViewAdapter.ViewHolder>() {
    private val TAG = TeacherFeaturesResultsRecyclerViewAdapter::class.java.simpleName

//    private lateinit var binding:ItemFeatureGroupResultBinding

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

    inner class ViewHolder(binding: ItemFeatureGroupResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val itemName: TextView = binding.tvItemName
        val featureMark: EditText = binding.tvMark

        init {

        }

        fun bind(item: Value) {
            itemName.text = item.group.name
            val markValue = item.mark.toString()
//            Log.i(TAG,"mark_VALUE=$markValue")
            if (markValue.isNotEmpty() && !markValue.equals("null",ignoreCase = true)) {
                featureMark.setText(item.mark.toString())
//                Log.i(TAG,"VALUE_SET")
            }

            featureMark.addTextChangedListener {
                val stringMark = it.toString()
                if (stringMark.isNotEmpty() && !stringMark.equals("null",ignoreCase = true)) {
                    values[bindingAdapterPosition].mark = Integer.parseInt(stringMark)
//                    Log.i(TAG,"change_listener_VALUE_SET")

                }
            }


        }

    }

    fun getData(): List<Value> {
        return values
    }

    fun swapData(items: List<Value>) {
        values = items
        notifyDataSetChanged()
    }

}