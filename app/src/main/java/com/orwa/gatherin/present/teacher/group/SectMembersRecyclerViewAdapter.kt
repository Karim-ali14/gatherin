package com.orwa.gatherin.present.teacher.group

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemGroupMembersBinding
import com.orwa.gatherin.model.group.GroupMembersResItem
import com.orwa.gatherin.model.group.Member
import com.orwa.gatherin.utils.Pref
import com.orwa.gatherin.utils.Util
import com.orwa.gatherin.utils.hide
import com.orwa.gatherin.utils.show


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SectMembersRecyclerViewAdapter(
    private var values: List<GroupMembersResItem>,
    private val memberOnClickListener: MemberChooseListener,
    private val ctx: Context,
    private val emptyVew: View
) : RecyclerView.Adapter<SectMembersRecyclerViewAdapter.ViewHolder>() {

    private val TAG = SectMembersRecyclerViewAdapter::class.java.simpleName

    private lateinit var binding: ItemGroupMembersBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemGroupMembersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {

            binding.checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                val itemId = values[bindingAdapterPosition].id
                if (isChecked) {
                    memberOnClickListener.onMemberChosen(itemId)
                } else {
                    memberOnClickListener.onMemberRemoved(itemId)
                }
            })
        }

        fun bind(item: GroupMembersResItem) {
            Util.loadImageWithPlaceholder(ctx, binding.ivProfileImage, item.picture, errorImg = R.drawable.avatar_1_raster)
//            Log.i(TAG, "image=${item.picture}")
            binding.tvUserName.setText(item.fullName)
            binding.checkbox.isChecked = item.isRegistered
            val user = Pref.getUserInfo(ctx)
            user?.let {
                if (item.id == it.id) {
                    binding.checkbox.isChecked = true
                    binding.checkbox.isEnabled = false
                }
            }
        }

    }

    fun swapData(items: List<GroupMembersResItem>) {
//        Log.i(TAG, "ITEMS=$items")
        values = items
        if (values.size == 0) {
            emptyVew.show()
        } else {
            emptyVew.hide()
        }
        notifyDataSetChanged()
    }

    fun reflectCheckBokWithRegisteredUsers(users: List<Member>) {
        for (item in values) {
            if (inList(item, users)) {
                item.isRegistered = true
            }
        }
        notifyDataSetChanged()
    }

    /**
     * Check if the current user is registered
     */
    fun inList(user: GroupMembersResItem, members: List<Member>): Boolean {
        for (m in members) {
            if (user.id == m.id) {
                return true
            }
        }
        return false
    }


}