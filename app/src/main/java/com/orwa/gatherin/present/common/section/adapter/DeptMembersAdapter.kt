///*
// * Copyright (C) 2018 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.orwa.gatherin.present.common.section.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.orwa.gatherin.R
//import com.orwa.gatherin.databinding.ItemNotificationBinding
//import com.orwa.gatherin.databinding.RvItemSectMembersBinding
//import com.orwa.gatherin.model.group.GroupMembersResItem
//import com.orwa.gatherin.model.notify.Notification
//import com.orwa.gatherin.utils.*
//
///**
// * Adapter for the list of repositories.
// */
//class DeptMembersAdapter(private val callback: (item: GroupMembersResItem) -> Unit) :
//    PagingDataAdapter<GroupMembersResItem, DeptMembersAdapter.MyViewHolder>(REPO_COMPARATOR) {
//
//
//    private lateinit var binding: RvItemSectMembersBinding
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        binding =
//            RvItemSectMembersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding.root)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val repoItem = getItem(position)
//        if (repoItem != null) {
//            holder.bind(repoItem)
//        }
//    }
//
//    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//        val user = Pref.getUserInfo(view.context)
//
//        init {
//            binding.ivDelete.setOnClickListener {
//                getItem(bindingAdapterPosition)?.let { it1 -> callback.invoke(it1) }
//            }
//        }
//
//        fun bind(item: GroupMembersResItem) {
//            Util.loadImageWithPlaceholder(itemView.context, binding.ivProfileImage, item.picture)
//            binding.tvUserName.setText(item.fullName)
//            user?.let {
//                if (it.id == item.id) {
//                    //Current user is the master of this department.
//                    binding.ivDelete.hide()
//                } else {
//                    binding.ivDelete.show()
//                }
//            }
//        }
//
//    }
//
////    fun removeItem(itemId: Int?){
////        var i =-1
////        values.forEachIndexed { index, groupMembersResItem ->
////            if(itemId==groupMembersResItem.id){
////                i=index
////            }
////        }
////        if(i!=-1){
////            values.removeAt(i)
////            notifyItemRemoved(i)
////        }
////
////    }
//
//    companion object {
//        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<GroupMembersResItem>() {
//            override fun areItemsTheSame(
//                oldItem: GroupMembersResItem,
//                newItem: GroupMembersResItem
//            ): Boolean =
//                oldItem.id == newItem.id
//
//            override fun areContentsTheSame(
//                oldItem: GroupMembersResItem,
//                newItem: GroupMembersResItem
//            ): Boolean =
//                oldItem == newItem
//        }
//    }
//}
