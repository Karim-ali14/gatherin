package com.orwa.gatherin.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.orwa.gatherin.db.model.NotifyModel

@Dao
interface NotificationModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifyModel: List<NotifyModel>)

    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): PagingSource<Int, NotifyModel>

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()

}