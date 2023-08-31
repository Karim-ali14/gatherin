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

package com.orwa.gatherin.present.notify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.orwa.gatherin.R
import com.orwa.gatherin.databinding.ItemNotificationBinding
import com.orwa.gatherin.db.model.NotifyModel
import com.orwa.gatherin.utils.DateUtil
import com.orwa.gatherin.utils.Lang
import com.orwa.gatherin.utils.Util

/**
 * Adapter for the list of repositories.
 */
class NotifyAdapter : PagingDataAdapter<NotifyModel, NotifyAdapter.NotifyViewHolder>(REPO_COMPARATOR) {


    private lateinit var binding: ItemNotificationBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotifyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }

    inner class NotifyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: NotifyModel){
            val title:String
            val body:String
            val lang: Lang = Util.getCurrentLang(itemView.context)

            if(lang== Lang.ARABIC){
                title = item.titleAr
                body = item.bodyAr
            }else{
                title =item.titleEn
                body = item.bodyEn
            }
            binding.tvTitle.text = title
            binding.tvTitle.setText(getFormattedTitleString(title,body,itemView.context))
            binding.tvTime.setText(DateUtil.getDateToDisplayFromStringDate(item.createdAt))
        }
    }

    fun getFormattedTitleString(title:String, body:String, ctx: Context): String {
        return ctx.getString(R.string.notification_content,title, body)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<NotifyModel>() {
            override fun areItemsTheSame(oldItem: NotifyModel, newItem: NotifyModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NotifyModel, newItem: NotifyModel): Boolean =
                oldItem == newItem
        }
    }
}
