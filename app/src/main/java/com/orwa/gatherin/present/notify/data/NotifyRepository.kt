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

package com.orwa.gatherin.present.notify.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.di.NetworkModule
import com.orwa.gatherin.db.AppDatabase
import com.orwa.gatherin.db.model.NotifyModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository class that works with local and remote data sources.
 */
@ExperimentalPagingApi
class NotifyRepository @Inject constructor(@NetworkModule.Authenticated private val service: RetrofitService, private val db: AppDatabase) {

//    /**
//     * Search repositories whose names match the query, exposed as a stream of data that will emit
//     * every time we get more data from the network.
//     */
//    fun getSearchResultStream(query: String): Flow<PagingData<Notification>> {
//        Log.d("GithubRepository", "New query: $query")
//        return Pager(
//            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false),
//            pagingSourceFactory = { NotifyPagingSource(service, query) }
//        ).flow
//    }



    fun getNotificationsFlowDb(pagingConfig: PagingConfig = PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false)): Flow<PagingData<NotifyModel>> {

        val pagingSourceFactory = { db.notifyDao().getAllNotifications() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = NotifyMediator(service, db)
        ).flow
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 10    }
}
