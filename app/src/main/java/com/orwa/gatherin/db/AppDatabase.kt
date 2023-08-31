package com.orwa.gatherin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.orwa.gatherin.db.dao.MembersRemoteKeysDao
import com.orwa.gatherin.db.dao.NotificationModelDao
import com.orwa.gatherin.db.dao.RemoteKeysDao
import com.orwa.gatherin.db.dao.SectMemberModelDao
import com.orwa.gatherin.db.model.MembersRemoteKeys
import com.orwa.gatherin.db.model.NotifyModel
import com.orwa.gatherin.db.model.RemoteKeys
import com.orwa.gatherin.db.model.SectMemberModel


@Database(version = 2, entities = [NotifyModel::class, RemoteKeys::class, SectMemberModel::class, MembersRemoteKeys::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun remoteDao(): RemoteKeysDao
    abstract fun notifyDao(): NotificationModelDao
    abstract fun memberRemoteDao():MembersRemoteKeysDao
    abstract fun memberDao():SectMemberModelDao


    companion object {

        val NOTIFY_DB = "gather_in.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, NOTIFY_DB).fallbackToDestructiveMigration()
                .build()
    }

}