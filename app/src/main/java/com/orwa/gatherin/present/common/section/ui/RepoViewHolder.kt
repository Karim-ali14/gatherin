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
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.orwa.gatherin.present.common.section.model.Repo
//import com.orwa.gatherin.R
//import com.orwa.gatherin.model.group.GroupMembersResItem
//import com.orwa.gatherin.model.section.Member
//import com.orwa.gatherin.utils.Util
//import com.orwa.gatherin.utils.hide
//import com.orwa.gatherin.utils.show
//
///**
// * View Holder for a [Repo] RecyclerView list item.
// */
//class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//    private val name: TextView = view.findViewById(R.id.repo_name)
//    private val description: TextView = view.findViewById(R.id.repo_description)
//    private val stars: TextView = view.findViewById(R.id.repo_stars)
//    private val language: TextView = view.findViewById(R.id.repo_language)
//    private val forks: TextView = view.findViewById(R.id.repo_forks)
//
//    private var repo: Repo? = null
//
//    init {
//        view.setOnClickListener {
//            repo?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
//        }
//    }
//
////    fun bind(item: Member) {
////        Util.loadImageWithPlaceholder(itemView.context, binding.ivProfileImage, item.picture)
////        binding.tvUserName.setText(item.fullName)
////        user?.let {
////            if(it.id == item.id){
////                //Current user is the master of this department.
////                binding.ivDelete.hide()
////            }else{
////                binding.ivDelete.show()
////            }
////        }
////    }
//
//}
//
////    private fun showRepoData(repo: Repo) {
////        this.repo = repo
////        name.text = repo.fullName
////
////        // if the description is missing, hide the TextView
////        var descriptionVisibility = View.GONE
////        if (repo.description != null) {
////            description.text = repo.description
////            descriptionVisibility = View.VISIBLE
////        }
////        description.visibility = descriptionVisibility
////
////        stars.text = repo.stars.toString()
////        forks.text = repo.forks.toString()
////
////        // if the language is missing, hide the label and the value
////        var languageVisibility = View.GONE
////        if (!repo.language.isNullOrEmpty()) {
////            val resources = this.itemView.context.resources
//////            language.text = resources.getString(R.string.language, repo.language)
////            languageVisibility = View.VISIBLE
////        }
////        language.visibility = languageVisibility
////    }
////
////    companion object {
////        fun create(parent: ViewGroup): RepoViewHolder {
////            val view = LayoutInflater.from(parent.context)
////                .inflate(R.layout.repo_view_item, parent, false)
////            return RepoViewHolder(view)
////        }
////    }
//
