package com.orwa.gatherin.present.common.section

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orwa.gatherin.databinding.RvItemSectMembersBinding
import com.orwa.gatherin.model.group.GroupMembersResItem
import com.orwa.gatherin.utils.Pref
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SectMembersRecyclerViewAdapter(
    private var values: ArrayList<GroupMembersResItem>,
    private val ctx: Context,
    private val emptyVew: View,
    private val callback: (item: GroupMembersResItem) -> Unit
) : RecyclerView.Adapter<SectMembersRecyclerViewAdapter.ViewHolder>() {

    private val TAG = SectMembersRecyclerViewAdapter::class.java.simpleName

    private lateinit var binding: RvItemSectMembersBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            RvItemSectMembersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val user = Pref.getUserInfo(ctx)

        init {
            binding.ivDelete.setOnClickListener {
                callback.invoke(values[bindingAdapterPosition])
            }
        }

        fun bind(item: GroupMembersResItem) {
            Util.loadImageWithPlaceholder(ctx, binding.ivProfileImage, item.picture)
            binding.tvUserName.setText(item.fullName)
            user?.let {
                if(it.id == item.id){
                    //Current user is the master of this department.
                    binding.ivDelete.hide()
                }else{
                    binding.ivDelete.show()
                }
            }
        }

    }

    fun swapData(items: ArrayList<GroupMembersResItem>) {
//        Log.i(TAG, "ITEMS=$items")
        values = items
        if (values.size == 0) {
            emptyVew.show()
        } else {
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

    fun removeItem(itemId: Int?){
        var i =-1
        values.forEachIndexed { index, groupMembersResItem ->
            if(itemId==groupMembersResItem.id){
                i=index
            }
        }
        if(i!=-1){
            values.removeAt(i)
            notifyItemRemoved(i)
        }

    }


}