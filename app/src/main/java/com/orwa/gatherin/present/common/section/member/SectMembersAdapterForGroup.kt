/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.orwa.gatherin.present.common.section.member

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemGroupMembersBinding
import com.orwa.gatherin.databinding.RvItemSectMembersBinding
import com.orwa.gatherin.db.model.SectMemberModel
import com.orwa.gatherin.model.group.GroupMembersResItem
import com.orwa.gatherin.model.group.Member
import com.orwa.gatherin.present.teacher.group.MemberChooseListener
import com.orwa.gatherin.present.teacher.group.SectMembersRecyclerViewAdapter
import com.orwa.gatherin.utils.*

/**
 * Adapter for the list of repositories.
 */
class SectMembersAdapterForGroup(
    private val memberOnClickListener: MemberChooseListener,
    ctx: Context
) :
    PagingDataAdapter<SectMemberModel, SectMembersAdapterForGroup.ViewHolder>(REPO_COMPARATOR) {

    val user = Pref.getUserInfo(ctx)


    private lateinit var registeredMembers: List<Member>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group_members, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val ivProfile = view.findViewById<ImageView>(R.id.ivProfileImage)
        private val tvUserName = view.findViewById<TextView>(R.id.tvUserName)

        //        private val ivMedal = view.findViewById<ImageView>(R.id.ivMedal)
        private val cb = view.findViewById<CheckBox>(R.id.checkbox)
        private val ivGroup = view.findViewById<ImageView>(R.id.ivGroup)

        init {

            cb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                val itemId = getItem(bindingAdapterPosition)?.id
                Log.i(TAG, "CHECKED_USER_ID=$itemId")
                itemId?.let {
                    if (isChecked) {
                        memberOnClickListener.onMemberChosen(itemId)
                    } else {
                        memberOnClickListener.onMemberRemoved(itemId)
                    }
                }

            })
        }

        fun bind(item: SectMemberModel) {
            Util.loadImageWithPlaceholder(
                itemView.context,
                ivProfile,
                item.img,
                errorImg = R.drawable.avatar_1_raster
            )
//            Log.i(TAG, "image=${item.picture}")
            tvUserName.setText(item.name)
            if (item.isJoin) {
                ivGroup.show()
            } else {
                ivGroup.hide()
            }
//            binding.checkbox.isChecked = item.isRegistered
            user?.let {
                if (item.id == it.id) {
                    cb.isChecked = true
                    cb.isEnabled = false
                    return
                }
            }
            cb.isChecked = item.isRegistered
        }

    }

    //    fun reflectCheckBokWithRegisteredUsers(users: List<Member>) {
//        for (item in values) {
//            if (inList(item, users)) {
//                item.isRegistered = true
//            }
//        }
//        notifyDataSetChanged()
//    }

    /**
     * Check if the current user is registered
     */
    fun inList(user: GroupMembersResItem): Boolean {
        for (m in registeredMembers) {
            if (user.id == m.id) {
                return true
            }
        }
        return false
    }


//    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//        val user = Pref.getUserInfo(itemView.context)
//
//
//        init {
//            binding.ivDelete.setOnClickListener {
//
//                Util.showOptionMsgDialog(
//                    itemView.context,
//                    R.string.delete_section_member_confirm_dialog_msg
//                ) {
//                    getItem(
//                        bindingAdapterPosition
//                    )?.let { it1 -> callback.invoke(it1) }
//                }
//
//            }
//        }
//
//        fun bind(item: SectMemberModel) {
//
//
//            Util.loadImageWithPlaceholder(itemView.context, binding.ivProfileImage, item.img)
//            binding.tvUserName.setText(item.name)
//            user?.let {
//                if (it.id == item.id) {
//                    //Current user is the master of this department.
//                    binding.ivMedal.show()
//                    binding.ivDelete.hide()
//                } else {
//                    binding.ivDelete.show()
//                    binding.ivMedal.hide()
//                }
//            }
//
//        }
//    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<SectMemberModel>() {
            override fun areItemsTheSame(
                oldItem: SectMemberModel,
                newItem: SectMemberModel
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SectMemberModel,
                newItem: SectMemberModel
            ): Boolean =
                oldItem == newItem
        }
    }
}
