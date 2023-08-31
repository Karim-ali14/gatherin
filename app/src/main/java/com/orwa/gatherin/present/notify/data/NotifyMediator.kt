package com.orwa.gatherin.present.notify.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.present.notify.data.NotifyRepository.Companion.DEFAULT_PAGE_INDEX
import com.orwa.gatherin.db.AppDatabase
import com.orwa.gatherin.db.model.NotifyModel
import com.orwa.gatherin.db.model.RemoteKeys
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class NotifyMediator(val doggoApiService: RetrofitService, val appDatabase: AppDatabase) :
    RemoteMediator<Int, NotifyModel>() {

    private val TAG = NotifyMediator::class.java.simpleName


    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, NotifyModel>
    ): MediatorResult {

//        Log.i(TAG,"LOAD_TYPE=$loadType")

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
//                Log.i(TAG,"CURRENT_NEXT_KEY=$nextKey")
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

//        Log.i(TAG,"page=$page")



        try {
            val response = doggoApiService.getPaginatedNotifications(page)
            val isEndOfList = response.notifications.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteDao().clearRemoteKeys()
                    appDatabase.notifyDao().clearAllNotifications()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.notifications.map { it->
                    RemoteKeys(it.id , prevKey = prevKey, nextKey = nextKey)
                }
//                Log.i(TAG,"inserted_next_key=$nextKey")
                appDatabase.remoteDao().insertAll(keys)
                appDatabase.notifyDao().insertAll(response.notifications.map { it1-> NotifyModel(it1.id,it1.titleEn,it1.titleAr, it1.bodyEn, it1.bodyAr, it1.createdAt) })
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, NotifyModel>): RemoteKeys? {


        val lastRemote = state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { item2 -> appDatabase.remoteDao().remoteKeysId(item2.id)

            }

//        Log.i(TAG,"LAST_REMOTE=$lastRemote")


        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { item -> appDatabase.remoteDao().remoteKeysId(item.id)

            }

    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, NotifyModel>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { item -> appDatabase.remoteDao().remoteKeysId(item.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, NotifyModel>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { itemId ->
                appDatabase.remoteDao().remoteKeysId(itemId)
            }
        }
    }

}