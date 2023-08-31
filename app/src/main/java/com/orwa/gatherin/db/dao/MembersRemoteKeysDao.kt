package com.orwa.gatherin.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.orwa.gatherin.db.model.MembersRemoteKeys
import com.orwa.gatherin.db.model.RemoteKeys
import com.orwa.gatherin.db.model.SectMemberModel

@Dao
interface MembersRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<MembersRemoteKeys>)

    @Query("SELECT * FROM members_remotes WHERE memberId = :id")
    suspend fun remoteKeysId(id: Int): MembersRemoteKeys?

    @Query("DELETE FROM members_remotes")
    suspend fun clearRemoteKeys()
}

