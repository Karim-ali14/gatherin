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

package com.orwa.gatherin.present.teacher.notify

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orwa.gatherin.R
import com.orwa.gatherin.model.notify.Notification

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class RepoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val TAG=RepoViewHolder::class.java.simpleName

    private val title: TextView = view.findViewById(R.id.tvTitle)
    private val time: TextView = view.findViewById(R.id.tvTime)

    private var repo: Notification? = null

//    init {
//        view.setOnClickListener {
//            repo?.url?.let { url ->
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                view.context.startActivity(intent)
//            }
//        }
//    }

    fun bind(repo: Notification?) {
        if (repo == null) {
//            val resources = itemView.resources
//            name.text = resources.getString(R.string.loading)
//            description.visibility = View.GONE
//            language.visibility = View.GONE
//            stars.text = resources.getString(R.string.unknown)
//            forks.text = resources.getString(R.string.unknown)
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: Notification) {
//        Log.i(TAG,"ITEM_DATA=$repo")
        this.repo = repo
        title.text = repo.titleEn
        time.text = repo.createdAt

    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notification, parent, false)
            return RepoViewHolder(view)
        }
    }
}
