package com.orwa.gatherin.present.teacher.home.group_results

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemSelectDepartmentBinding
import com.orwa.gatherin.model.teacher_home.DepartmentsListResItem
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class DepartmentsRecyclerViewAdapter(
    private var values: List<DepartmentsListResItem>,
    private val onClickListener: DepartmentSelectListener?,
    private val ctx: Context,
    private val emptyVew:View
) : RecyclerView.Adapter<DepartmentsRecyclerViewAdapter.ViewHolder>() {

    private val TAG = DepartmentsRecyclerViewAdapter::class.java.simpleName

    private lateinit var binding: ItemSelectDepartmentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemSelectDepartmentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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
                onClickListener?.onDepartmentSelected(item,bindingAdapterPosition) }
        }

        fun bind(item:DepartmentsListResItem){
            binding.tvDeptName.text = item.name
            if(item.wasDone){
                binding.ivState.setImageResource(R.drawable.ic_check_done_filled)
                binding.vSideBanner.background = ContextCompat.getDrawable(ctx,R.drawable.cornered_green_bg)
            }else{
                binding.ivState.setImageResource(R.drawable.ic_add_department_result)
                binding.vSideBanner.background = ContextCompat.getDrawable(ctx,R.drawable.cornered_purble_bg)

            }
        }

    }

//    fun checkItemDone(pos:Int){
//        Log.i(TAG,"VALUES_SIZE=$itemCount")
//        if(pos<values.size){
//            values[pos].wasDone=true
//            notifyItemChanged(pos)
//        }
//    }

    fun swapData(items:List<DepartmentsListResItem>){
        values = items
        if(values.size==0){
            emptyVew.show()
        }else{
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }


}