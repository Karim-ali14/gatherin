package com.orwa.gatherin.SplashAndTypeOfLogin.SplashActivity.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.orwa.gatherin.SplashAndTypeOfLogin.TypeOfLogin.View.TypeOfLoginActivity;
import com.orwa.gatherin.Teacher.Home.View.HomeTeacherActivity;
import com.orwa.gatherin.User.HomeUser.View.HomeUserActivity;
import com.orwa.gatherin.utils.BasePresenter;
import com.orwa.gatherin.utils.SharedPrefManager;

import spencerstudios.com.bungeelib.Bungee;


public class SplashPresenter extends BasePresenter implements SplashViewPresenter {
    Context context;
    SharedPrefManager sharedPrefManager;

    public SplashPresenter(Context context,SharedPrefManager sharedPrefManager) {
        this.context = context;
        this.sharedPrefManager = sharedPrefManager;
    }

    @Override
    public void go() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPrefManager.getLoginStatus()) {
                    if (sharedPrefManager.getLoginType().equals("teacher")) {
                        startActivity(HomeTeacherActivity.class);
                    } else {
                        startActivity(HomeUserActivity.class);
                    }
                } else {
                    startActivity(TypeOfLoginActivity.class);
                }
            }

        },3000);

    }

    private void startActivity(Class c){
        Intent intent = new Intent(context,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(intent);
        Bungee.split(context);
    }

}
