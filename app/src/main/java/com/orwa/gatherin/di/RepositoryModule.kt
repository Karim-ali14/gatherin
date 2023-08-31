package com.orwa.gatherin.di

import androidx.paging.ExperimentalPagingApi
import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.present.notify.data.NotifyRepository
import com.orwa.gatherin.db.AppDatabase
import com.orwa.gatherin.present.common.section.member.SectMemberRep
import com.orwa.gatherin.rep.AuthRep
import com.orwa.gatherin.rep.HomeRep
import com.orwa.gatherin.rep.NotifyRep
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @ViewModelScoped
    @Provides
    fun provideRepository(
        @NetworkModule.NonAuthenticated
        retrofitService: RetrofitService
    ): AuthRep {
        return AuthRep(retrofitService)
    }


    @ViewModelScoped
    @Provides
    fun provideRepository2(
        @NetworkModule.Authenticated retrofitService: RetrofitService
    ): HomeRep {
        return HomeRep(retrofitService)
    }


    @ViewModelScoped
    @Provides
    fun provideRepository3(
        @NetworkModule.Authenticated retrofitService: RetrofitService
    ): NotifyRep {
        return NotifyRep(retrofitService)
    }

    @ExperimentalPagingApi
    @ViewModelScoped
    @Provides
    fun provideRepository4(
        @NetworkModule.Authenticated retrofitService: RetrofitService,
        db: AppDatabase
    ): NotifyRepository {
        return NotifyRepository(retrofitService,db)
    }

    @ExperimentalPagingApi
    @ViewModelScoped
    @Provides
    fun provideRepository5(
        @NetworkModule.Authenticated retrofitService: RetrofitService,
        db: AppDatabase
    ): SectMemberRep {
        return SectMemberRep(retrofitService,db)
    }


//    @ViewModelScoped
//    @Provides
//    fun provideRepository5(
//        @NetworkModule.Authenticated retrofitService: RetrofitService
//    ): DeptMembersRepository {
//        return DeptMembersRepository(retrofitService)
//    }


//    @Singleton
//    @Provides
//    fun provideRepository4(
//        @NetworkModule.AuthenticatedForFirebase retrofitService: RetrofitService
//    ): FCMRep {
//        return FCMRep(retrofitService)
//    }
}