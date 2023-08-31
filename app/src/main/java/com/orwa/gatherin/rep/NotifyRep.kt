package com.orwa.gatherin.rep

import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.di.NetworkModule


import com.skydoves.sandwich.ApiResponse
import okhttp3.ResponseBody
import javax.inject.Inject

class NotifyRep @Inject constructor(
    @NetworkModule.Authenticated private val service: RetrofitService
) {

    suspend fun changeNotificationsState():ApiResponse<ResponseBody>{
        return service.changeNotificationsState()
    }

//    /**
//     * Search repositories whose names match the query, exposed as a stream of data that will emit
//     * every time we get more data from the network.
//     */
//    fun getSearchResultStream(): Flow<PagingData<Notification>> {
////        Log.d("GithubRepository", "New query: $query")
//        return Pager(
//            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
//            pagingSourceFactory = { NotificationPagingSource(service) }
//        ).flow
//    }
//
//    companion object {
//        const val NETWORK_PAGE_SIZE = 10
//    }

}