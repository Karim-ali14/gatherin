package com.orwa.gatherin.present.common.group

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemMemberBinding
import com.orwa.gatherin.model.group.Member
import com.orwa.gatherin.present.teacher.group.MemberChooseListener
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GroupMembersRecyclerViewAdapter(
    private var values: List<Member>,
    private val memberOnClickListener: MemberChooseListener,
    private val ctx: Context,
    private val emptyVew: View
) : RecyclerView.Adapter<GroupMembersRecyclerViewAdapter.ViewHolder>() {

    private val TAG = GroupMembersRecyclerViewAdapter::class.java.simpleName

//    var lastLeaderPos: Int = -1

    private lateinit var binding: ItemMemberBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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

                val userId = values[bindingAdapterPosition].id

                memberOnClickListener.onMemberChosen(userId)

            }
        }

        fun bind(item: Member) {
            Util.loadImageWithPlaceholder(ctx, binding.ivProfileImage, item.picture, R.drawable.avatar_1_raster)
            binding.tvUserName.setText(item.fullName)
            val isLeader = values[bindingAdapterPosition].isLeader
            if (isLeader) {
                binding.tvLeader.show()
            }
        }
    }

    fun setLeaderUser(userId:Int){
        for(v in values){
            if(v.id==userId){
                v.isLeader=true
            }
        }
        notifyDataSetChanged()
    }

    fun swapData(items: List<Member>) {
//        Log.i(TAG, "ITEMS=$items")
        values = items
        if (values.size == 0) {
            emptyVew.show()
        } else {
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }




}