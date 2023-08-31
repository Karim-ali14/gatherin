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
//package com.orwa.gatherin.present.common.section.ui
//
//import android.content.Intent
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.orwa.gatherin.present.common.section.model.Repo
//import com.orwa.gatherin.R
//import com.orwa.gatherin.databinding.RvItemSectMembersBinding
//import com.orwa.gatherin.model.group.GroupMembersResItem
//import com.orwa.gatherin.model.section.Member
//import com.orwa.gatherin.utils.*
//
///**
// * View Holder for a [Repo] RecyclerView list item.
// */
//class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//
//     var user = Pref.getUserInfo(view.context)
//
//
//    private val userName: TextView = view.findViewById(R.id.tvUserName)
//    private val profileImg: ImageView = view.findViewById(R.id.ivProfileImage)
//    private val delete: ImageButton = view.findViewById(R.id.ivDelete)
//
//
//    fun bind(item: Member) {
//        Util.loadImageWithPlaceholder(itemView.context, profileImg, item.picture)
//        userName.setText(item.fullName)
//        user?.let {
//            if(it.id == item.id){
//                //Current user is the master of this department.
//                delete.hide()
//            }else{
//                delete.show()
//            }
//        }
//    }
//
//
//    companion object {
//        fun create(parent: ViewGroup): MemberViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.rv_item_sect_members, parent, false)
//            return MemberViewHolder(view)
//        }
//    }
//}
