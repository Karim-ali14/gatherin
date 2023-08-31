package com.orwa.gatherin.utils;

import android.content.Context;

public interface MainRequest {

    void startLoading();

    void stopLoading();



    void errorToast(Context context, String msg);

    void successToast(Context context, String msg);

    void onFailure(String message);

    void handleApiException(Context context, Throwable t);


    Context getContext();
}
