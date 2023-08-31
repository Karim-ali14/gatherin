package com.orwa.gatherin.di

import android.content.Context
import com.orwa.gatherin.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
class DatabaseModule {

    @ViewModelScoped
    @Provides
    fun provideAppDatabase(
        @ApplicationContext ctx:Context
    ): AppDatabase {
        return AppDatabase.getInstance(ctx)
    }
}