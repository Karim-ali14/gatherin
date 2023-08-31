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
//package com.orwa.gatherin.present.teacher.notify.data
//
//import android.util.Log
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.orwa.gatherin.api.RetrofitService
//import com.orwa.gatherin.model.notify.Notification
//import retrofit2.HttpException
//import java.io.IOException
//
//// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
//private const val GITHUB_STARTING_PAGE_INDEX = 1
//
//class NotificationPagingSource(
//    private val service: RetrofitService)
//    : PagingSource<Int, Notification>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
//        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
////        val apiQuery = query + IN_QUALIFIER
//        return try {
//            val response = service.getNotifications( position)
//            val repos = response.notifications
//            Log.i("Paging","notifications=$repos")
////            val nextKey = if (repos.isEmpty()) {
////                null
////            } else {
////                // initial load size = 3 * NETWORK_PAGE_SIZE
////                // ensure we're not requesting duplicating items, at the 2nd request
////                position + (params.loadSize / NETWORK_PAGE_SIZE)
//////                position+1
////            }
//            LoadResult.Page(
//                data = repos,
//                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
//                nextKey = response.currentPage+1
//            )
//        } catch (exception: IOException) {
//            LoadResult.Error(exception)
//        } catch (exception: HttpException) {
//            LoadResult.Error(exception)
//        }
//    }
//
//    // The refresh key is used for the initial load of the next PagingSource, after invalidation
//    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
//        // We need to get the previous key (or next key if previous is null) of the page
//        // that was closest to the most recently accessed index.
//        // Anchor position is the most recently accessed index
////        return state.anchorPosition?.let { anchorPosition ->
////            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
////                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
////        }
//        return 2
//    }
//}
