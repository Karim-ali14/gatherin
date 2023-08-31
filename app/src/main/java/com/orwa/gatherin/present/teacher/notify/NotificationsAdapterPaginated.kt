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
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.orwa.gatherin.model.notify.Notification

/**
 * Adapter for the list of repositories.
 */
class NotificationsAdapterPaginated : PagingDataAdapter<Notification, RepoViewHolder>(REPO_COMPARATOR) {

    private val TAG = NotificationsAdapterPaginated::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
//        Log.i(TAG,"TEST_onCreateViewHolder")
        return RepoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
//        Log.i(TAG,"TEST_onBindViewHolder=")
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean =
                oldItem == newItem
        }
    }
}
