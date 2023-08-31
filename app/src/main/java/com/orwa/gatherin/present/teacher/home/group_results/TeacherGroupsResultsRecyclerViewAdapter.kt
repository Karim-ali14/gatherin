package com.orwa.gatherin.present.teacher.home.group_results

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
//import com.orwa.gatherin.databinding.ItemAddColumnBinding
import com.orwa.gatherin.databinding.ItemGroupResultsParentBinding
import com.orwa.gatherin.model.group_results.Feature

import com.orwa.gatherin.present.student.home.placeholder.PlaceholderContent.PlaceholderItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * Parent adapter
 */
class TeacherGroupsResultsRecyclerViewAdapter(
    private var values: ArrayList<Feature>, //Need update
    private val ctx: Context,
    private val emptyVew: View
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = TeacherGroupsResultsRecyclerViewAdapter::class.java.simpleName

//    private val viewPool = RecyclerView.RecycledViewPool()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

//        if(viewType== VIEW_TYPE_FEATURE){
            return FeatureViewHolder(
                ItemGroupResultsParentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
//        }
//        else{
//            return AddColumnViewHolder(
//                ItemAddColumnBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        if(item.type==ItemType.FEATURE){
            (holder as FeatureViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val itemType = values[position].type
        if(itemType==ItemType.ADD_COLUMN){ //Last item
            return VIEW_TYPE_ADD_COLUMN
        }else{
            return VIEW_TYPE_FEATURE
        }
    }

    override fun getItemCount(): Int = values.size

    inner class FeatureViewHolder(binding: ItemGroupResultsParentBinding) : RecyclerView.ViewHolder(binding.root) {
        val featureName: TextView = binding.etFeatureName
        val rv : RecyclerView = binding.rvChild
        val childLayoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)

        fun bind(item:Feature){
            featureName.text = item.title
            featureName.isEnabled = !item.isFixed

            featureName.addTextChangedListener {
                values[bindingAdapterPosition].title = it?.trim().toString()
            }

//            Log.i(TAG,"CHILD_DATA=${item.values}")
            rv.apply {
                layoutManager = childLayoutManager
                adapter = TeacherFeaturesResultsRecyclerViewAdapter(item.values,ctx)
//                setRecycledViewPool(viewPool)
            }
        }

    }

//    inner class AddColumnViewHolder(binding: ItemAddColumnBinding) : RecyclerView.ViewHolder(binding.root) {
//        val addBtn = binding.btnAddColumn
//        init {
//            addBtn.setOnClickListener {
//                addColumnListener.onAddColumnClicked()
//            }
//        }
//    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

        if(holder is FeatureViewHolder){
            val oldFeatureName = holder.featureName.text
            val oldValues = (holder.rv.adapter as TeacherFeaturesResultsRecyclerViewAdapter).getData()
            val currentItem = values[holder.bindingAdapterPosition]// this is the old data item
            //Set new values of items to the old save values
            currentItem.title = oldFeatureName.toString()
            currentItem.values = oldValues
        }
    }

    fun swapData(items:ArrayList<Feature>){
        values = items
        if(values.size==0){
            emptyVew.show()
        }else{
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

//    fun addItem(item:Feature){
//        val previousItem = values[itemCount-2]
//        val copy = previousItem.copy()
//        copy.title=""
//        copy.type = ItemType.FEATURE
//        copy.isFixed = false
//        for(v in copy.values){
//            v.mark=null
//        }
//
//        val lastItem = values[itemCount-1]
//        lastItem.type = copy.type
//        lastItem.isFixed=copy.isFixed
//        lastItem.title=copy.title
//        lastItem.values = copy.values
//
//        values.add(item)//add (add column) item in the list
//        notifyItemRangeChanged(itemCount-1,itemCount)
//    }

    /**
     * check all required fields.
     * @return false if one or editText is empty
     */
    fun validateEntryData():Boolean{
        values.forEachIndexed { index, feature ->
            if(feature.title.isEmpty()){
//                Log.i(TAG,"EMPTY_ERROR_INDEX=$index")
                return false
            }else{
                for(featureX in feature.values){
                    if(featureX.mark==null){
//                        Log.i(TAG,"NULL_ERROR_INDEX=$index")
                        return false
                    }
                }
            }
        }
        return true
    }

    fun getData():List<Feature>{
        return values
    }

    fun addItem(item:Feature){
        values.add(item)
        notifyItemInserted(itemCount-1)
    }

    companion object{
        const val VIEW_TYPE_FEATURE=1
        const val VIEW_TYPE_ADD_COLUMN=2
    }

    enum class ItemType{
        FEATURE,ADD_COLUMN
    }

}