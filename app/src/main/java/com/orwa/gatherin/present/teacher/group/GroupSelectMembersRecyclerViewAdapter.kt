package com.orwa.gatherin.present.teacher.group

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemmMembersToChooseCaptainBinding
import com.orwa.gatherin.model.group.Member
import com.orwa.gatherin.utils.Pref
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class GroupSelectMembersRecyclerViewAdapter(
    private var values: List<Member>,
    private val memberOnClickListener: MemberChooseListener,
    private val ctx: Context,
    private val emptyVew: View
) : RecyclerView.Adapter<GroupSelectMembersRecyclerViewAdapter.ViewHolder>() {

    private val TAG = GroupSelectMembersRecyclerViewAdapter::class.java.simpleName

    var lastLeaderPos: Int = -1

    private lateinit var binding: ItemmMembersToChooseCaptainBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemmMembersToChooseCaptainBinding.inflate(
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
        val profileImg = view.findViewById<ImageView>(R.id.ivProfileImage)
        val name = view.findViewById<TextView>(R.id.tvUserName)


        val selectTV = view.findViewById<TextView>(R.id.tvSelectLeader)

        init {

            selectTV.setOnClickListener {
//                Log.i(TAG, "last_pos=$lastLeaderPos, current_pos=$bindingAdapterPosition")
//                val isLeader = values[bindingAdapterPosition].isLeader
                if (lastLeaderPos != -1) { //If previous leader exists
                    if (lastLeaderPos != bindingAdapterPosition) { // previous leader is different from current clicked user
                        //This mean that another leader was selected in this group
                        if (values[lastLeaderPos].isLeader) { //if other activate item is a leader remove
                            changeItemState(lastLeaderPos)
                        }
                        changeItemState(bindingAdapterPosition)
                        lastLeaderPos = bindingAdapterPosition

                    } else { // current user is a leader -> remove it
                        changeItemState(bindingAdapterPosition)
                        lastLeaderPos = -1
                        //The same leader was selected. remove it
                    }
                } else { //No previous leader
                    changeItemState(bindingAdapterPosition)
                    lastLeaderPos = bindingAdapterPosition
                }

                if(lastLeaderPos!=-1){
                    memberOnClickListener.onMemberChosen(values[lastLeaderPos].id)
                }else{
                    memberOnClickListener.onMemberChosen(-1)

                }


            }
        }

        fun bind(item: Member) {
            Util.loadImageWithPlaceholder(ctx, profileImg, item.picture,R.drawable.avatar_1_raster)
            name.setText(item.fullName)
            val isLeader = values[bindingAdapterPosition].isLeader
            setSelectButtonState(isLeader, selectTV)
            val user = Pref.getUserInfo(ctx)
            user?.let {
                if(it.id ==item.id){ //current user is the owner of this group, disable selecting it as a member
//                    selectTV.setText(R.string.group_owner_label)
//                    selectTV.setOnClickListener(null)
                    selectTV.hide()
                }
            }
//            if (isLeader) {
//                lastLeaderPos = bindingAdapterPosition
//            }

        }

    }

    fun setSelectButtonState(isLeader: Boolean, tv: TextView) {
        var right = 0
        var color = R.color.green
        if (isLeader) {
            color = R.color.grey
            right = R.drawable.ic_check_done_white
        }

        tv.setBackgroundColor(ContextCompat.getColor(ctx, color))
        tv.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            right,
            0
        )

    }

    fun changeItemState(pos: Int) {
        val isLeader = values[pos].isLeader
        values[pos].isLeader = !isLeader

        notifyItemChanged(pos)
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

//    fun setLeader(leaderId: Int) {
//        for (v in values) {
//            if (v.id == leaderId) {
//                v.isLeader = true
//            }
//        }
//        notifyDataSetChanged()
//    }

//    /**
//     * Check if the current user is registered
//     */
//    fun inList(user: GroupMembersResItem, members: List<Member>): Boolean {
//        for (m in members) {
//            if (user.id == m.id) {
//                return true
//            }
//        }
//        return false
//    }


}