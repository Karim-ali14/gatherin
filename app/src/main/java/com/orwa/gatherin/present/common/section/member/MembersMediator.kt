package com.orwa.gatherin.present.common.section.member

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.present.notify.data.NotifyRepository.Companion.DEFAULT_PAGE_INDEX
import com.orwa.gatherin.db.AppDatabase
import com.orwa.gatherin.db.model.MembersRemoteKeys
import com.orwa.gatherin.db.model.SectMemberModel
import com.orwa.gatherin.model.group.Member
import com.orwa.gatherin.model.section.Member as SectMember

import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
/**
 * @param members used only when editing the group info in order
 * to check the registered user in the currently editing group. Its value equals null when creating new group.
 */
class MembersMediator(
    val departmentId: Int,
    val query:String,
    val members:List<Member>?,
    val apiService: RetrofitService,
    val appDatabase: AppDatabase
) :
    RemoteMediator<Int, SectMemberModel>() {

    private val TAG = MembersMediator::class.java.simpleName


    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, SectMemberModel>
    ): MediatorResult {

//        Log.i(TAG, "LOAD_TYPE=$loadType")

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
//                Log.i(TAG, "CURRENT_NEXT_KEY=$nextKey")
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

//        Log.i(TAG, "page=$page")

        try {
            val response = apiService.getDepartmentsMembers2(departmentId, page, query)
//            val isEndOfList = response.member.isEmpty()
            val isEndOfList = response.nextPage==null
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.memberRemoteDao().clearRemoteKeys()
                    appDatabase.memberDao().clearAllMembers()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.member.map { it ->
                    MembersRemoteKeys(it.id, prevKey = prevKey, nextKey = nextKey)
                }
//                Log.i(TAG, "inserted_next_key=$nextKey")
                appDatabase.memberRemoteDao().insertAll(keys)
                appDatabase.memberDao().insertAll(response.member.mapIndexed {index:Int, it1 ->

                    val isRegistered = inList(it1)
                    SectMemberModel(
                        id = it1.id,
                        deptId = departmentId,
                        name = it1.fullName,
                        img = it1.picture,
                        isJoin = it1.isJoin,
                        isRegistered = isRegistered,
                        time = System.currentTimeMillis()+index
                    )
                })

            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * Check if the current user is registered
     */
    fun inList(user: SectMember): Boolean {
        if (members != null) {
            for (m in members) {
                if (user.id == m.id) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, SectMemberModel>): MembersRemoteKeys? {

        val lastRemote = state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { item2 ->
                appDatabase.memberRemoteDao().remoteKeysId(item2.id)

            }

//        Log.i(TAG, "LAST_REMOTE=$lastRemote")

        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { item ->
                appDatabase.memberRemoteDao().remoteKeysId(item.id)

            }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, SectMemberModel>): MembersRemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { item -> appDatabase.memberRemoteDao().remoteKeysId(item.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, SectMemberModel>): MembersRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { itemId ->
                appDatabase.memberRemoteDao().remoteKeysId(itemId)
            }
        }
    }

}