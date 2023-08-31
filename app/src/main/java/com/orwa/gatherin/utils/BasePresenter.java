package com.orwa.gatherin.utils;

import android.content.Context;
import android.widget.ImageView;

import static com.orwa.gatherin.utils.ParentClass.LoadImageWithPicasso;
import static com.orwa.gatherin.utils.ParentClass.dismissFlipDialog;
import static com.orwa.gatherin.utils.ParentClass.handleException;
import static com.orwa.gatherin.utils.ParentClass.makeErrorToast;
import static com.orwa.gatherin.utils.ParentClass.makeSuccessToast;
import static com.orwa.gatherin.utils.ParentClass.showFlipDialog;


public class BasePresenter {


    public void startLoading() {
        showFlipDialog();
    }

    public void stopLoading() {
        dismissFlipDialog();
    }

    public void errorToast(Context context, String msg) {
        makeErrorToast(context, msg);
    }

    public void successToast(Context context, String msg) {
        makeSuccessToast(context, msg);
    }

    public void handleApiException(Context context, Throwable t) {
        handleException(context, t);
    }

    public void loadImage(String url, Context context, ImageView imageView) {
        LoadImageWithPicasso(url, context, imageView);
    }
}
