package com.orwa.gatherin.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.orwa.gatherin.db.model.NotifyModel
import com.orwa.gatherin.db.model.SectMemberModel

@Dao
interface SectMemberModelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifyModel: List<SectMemberModel>)


    @Query("SELECT * FROM members WHERE deptId=:deptIdParam AND name LIKE :query ORDER BY time ASC")
    fun getAllMembersByName(deptIdParam:Int, query:String): PagingSource<Int, SectMemberModel>

    @Query("SELECT * FROM members WHERE deptId=:deptIdParam ORDER BY time ASC")
    fun getAllMembers(deptIdParam:Int): PagingSource<Int, SectMemberModel>

    @Query("UPDATE members SET isRegistered =:isRegisteredParam WHERE id=:userId")
    fun setRegisteredMember(userId:Int, isRegisteredParam:Boolean)

    @Query("DELETE FROM members")
    suspend fun clearAllMembers()

    @Query("DELETE FROM members WHERE id=:memberId")
    suspend fun deleteMember(memberId:Int)

}