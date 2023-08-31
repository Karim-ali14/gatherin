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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.RvItemSectMembersBinding
import com.orwa.gatherin.db.model.SectMemberModel
import com.orwa.gatherin.utils.*

/**
 * Adapter for the list of repositories.
 */
class SectMembersAdapter(private val callback: (item: SectMemberModel) -> Unit) :
    PagingDataAdapter<SectMemberModel, SectMembersAdapter.MemberViewHolder>(REPO_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_sect_members,parent,false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }

    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val ivProfile = view.findViewById<ImageView>(R.id.ivProfileImage)
        private val tvUserName = view.findViewById<TextView>(R.id.tvUserName)
        private val ivMedal = view.findViewById<ImageView>(R.id.ivMedal)
        private val btnDelete = view.findViewById<ImageButton>(R.id.ivDelete)

        val user = Pref.getUserInfo(itemView.context)


        init {
            btnDelete.setOnClickListener {

                Util.showOptionMsgDialog(
                    itemView.context,
                    R.string.delete_section_member_confirm_dialog_msg
                ) {
                    getItem(
                        bindingAdapterPosition
                    )?.let { it1 -> callback.invoke(it1) }
                }

            }
        }

        fun bind(item: SectMemberModel) {


            Util.loadImageWithPlaceholder(itemView.context, ivProfile, item.img)
            tvUserName.setText(item.name)
            user?.let {
                if (it.id == item.id) {
                    //Current user is the master of this department.
                    ivMedal.setImageResource(R.drawable.ic_medal_golden)
                    ivMedal.show()
                    btnDelete.hide()
                } else {
                    btnDelete.show()
                    //check if the current user is previously joined to any group
                    if(item.isJoin){
                        ivMedal.setImageResource(R.drawable.ic_group)
                        ivMedal.show()
                    }else{
                        ivMedal.hide()
                    }
                }
            }

        }
    }

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
