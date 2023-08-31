package com.orwa.gatherin.utils;

import com.orwa.gatherin.MyApplication;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroWeb {
//    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .addInterceptor(new ChuckInterceptor(AppController.getContext()))
//            .connectTimeout(60, TimeUnit.MINUTES)
//            .readTimeout(60, TimeUnit.MINUTES)
//            .build();
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(HeaderInterceptor())
                    .addInterceptor(new ChuckInterceptor(MyApplication.getContext()))
                    .connectTimeout(60, TimeUnit.MINUTES)
                    .readTimeout(60, TimeUnit.MINUTES)
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.baseUrl2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;

    }

    private static Interceptor HeaderInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request();

                request = request.newBuilder()
                        .addHeader("language", ParentClass.getLang(MyApplication.getContext()))
                        .build();

//                Log.e("request",request+"GOOD");
                okhttp3.Response response = chain.proceed(request);
//                Log.e("response",response+"GOoD");
                return response;
            }
        };
    }
}
