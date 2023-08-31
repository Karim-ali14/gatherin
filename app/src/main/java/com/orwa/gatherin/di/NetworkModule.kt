package com.orwa.gatherin.di


import android.content.Context
import com.orwa.gatherin.api.RetrofitService
import com.orwa.gatherin.utils.Constants
import com.orwa.gatherin.utils.Pref
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    const val TAG="NETWORK_"

    private var retrofit: RetrofitService? = null

    private var authenticated = false


    @ViewModelScoped
    @Provides
    @NonAuthenticated
    fun provideRetrofitService(): RetrofitService {
//        Log.i(TAG,"AUTHENTICATED=FALSE")
        //If no previous initialization of retrofit or we need to change the status from authenticate to non-authenticated
        if(retrofit ==null || authenticated){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
//            httpClient.addInterceptor(interceptor)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
            retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_LINK).client(httpClient.build())
                .addCallAdapterFactory(
                    CoroutinesResponseCallAdapterFactory()
                ).build().create(RetrofitService::class.java)

            authenticated =false

        }
        return retrofit!!

    }


    @ViewModelScoped
    @Provides
    @Authenticated
    fun provideRetrofitServiceAuthenticated(@ApplicationContext ctx: Context): RetrofitService {
//        Log.i(TAG,"AUTHENTICATED=TRUE")
//        val user = Pref.getUserInfo(ctx)

//        val token = Pref.getInstance(ctx).getString(Constants.USER_TOKEN_KEY,"")
//        Log.i(TAG,"token=$token")
        val token2 = Pref.getUserInfo(ctx)?.token
//        Log.i(TAG,"token=$token2")



        //If no previous initialization of retrofit or we need to change the status from non-authenticate to authenticated
        if (retrofit == null || !authenticated) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
//            httpClient.addInterceptor(interceptor)
            httpClient.readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
            httpClient.addInterceptor { chain ->
                //last_user = user
                val original = chain.request()

                val request: Request = original.newBuilder()
                    .header("Accept", "application/ld+json")
                    .header("Authorization", "Bearer $token2")
                    .method(original.method, original.body)
                    .build()
//                }

//                Log.i(TAG,"request=${request.headers}")
                chain.proceed(request)
            }
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_LINK)
                .addCallAdapterFactory( CoroutinesResponseCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build().create(RetrofitService::class.java)
            authenticated = true
        }
        return retrofit!!
    }

//    @Singleton
//    @Provides
//    @AuthenticatedForFirebase
//    fun provideRetrofitServiceAuthenticatedForFirebase(): RetrofitService {
//        Log.i(TAG,"AUTHENTICATED=FALSE")
//        //If no previous initialization of retrofit or we need to change the status from authenticate to non-authenticated
//        if(retrofit ==null || authenticated){
//            val interceptor = HttpLoggingInterceptor()
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//            val httpClient = OkHttpClient.Builder()
//            httpClient.addInterceptor(interceptor)
//            httpClient.readTimeout(1, TimeUnit.MINUTES)
//                .connectTimeout(1, TimeUnit.MINUTES)
//            retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(Constants.BASE_LINK).client(httpClient.build())
//                .addCallAdapterFactory(
//                    CoroutinesResponseCallAdapterFactory()
//                ).build().create(RetrofitService::class.java)
//
//            authenticated =false
//
//        }
//        return retrofit!!
//    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class Authenticated

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NonAuthenticated


//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class AuthenticatedForFirebase

    /**
     * Used when the user is logged out
     */
    fun removeAuth(){
        retrofit = null
        authenticated =false
    }
}