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

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.di.NetworkModule
import com.orwa.gatherin.db.AppDatabase
import com.orwa.gatherin.db.model.NotifyModel
import com.orwa.gatherin.db.model.SectMemberModel
import com.orwa.gatherin.model.group.Member
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository class that works with local and remote data sources.
 */
@ExperimentalPagingApi
class SectMemberRep @Inject constructor(
    @NetworkModule.Authenticated private val service: RetrofitService,
    private val db: AppDatabase
) {


    fun getMembersFlowDb(
        departmentId: Int,
        query: String="",
        members: List<Member>? = null,
        pagingConfig: PagingConfig = PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    ): Flow<PagingData<SectMemberModel>> {
        val newQuery = if (query.isEmpty()) query else "%$query%"

        val pagingSourceFactory = {
            if (query.isEmpty()) db.memberDao().getAllMembers(departmentId) else
            {
                db.memberDao()
                    .getAllMembersByName(departmentId, newQuery)
            }
        }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = MembersMediator(departmentId,newQuery, members, service, db)
        ).flow
    }

    suspend fun deleteMemberFromDB(memberId: Int) {
        db.memberDao().deleteMember(memberId)
    }

    suspend fun deleteAllMemberFromDB() {
        db.memberDao().clearAllMembers()
    }

    suspend fun setRegisteredMemberIntoDB(userId: Int, isRegistered: Boolean) {
        db.memberDao().setRegisteredMember(userId, isRegistered)
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 10
    }
}
