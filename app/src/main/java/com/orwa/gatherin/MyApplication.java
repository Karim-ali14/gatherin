package com.orwa.gatherin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.preference.PreferenceManager;

import com.orwa.gatherin.utils.Constants;
import com.orwa.gatherin.utils.LocaleHelper;

import java.util.Locale;

import dagger.hilt.android.HiltAndroidApp;
import in.myinnos.customfontlibrary.TypefaceUtil;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


/**
 * Created by cz on 19/03/2018.
 */
@HiltAndroidApp
public class MyApplication extends MultiDexApplication {

    private final String TAG = MyApplication.class.getSimpleName();

    public static String SHARE_LINK=null;

    SharedPreferences sharedPreferences_language;
    private static MyApplication mContext;

    public String DEVICE_LANGUAGE_DEFAULT = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        sharedPreferences_language = getSharedPreferences("language", MODE_PRIVATE);

        setCustomFont();
//        initLanguage();
//        if (getLang(getApplicationContext()).equals("ar")) {
//            ParentClass.setDefaultLang("ar", getApplicationContext());
//
//        } else {
//            ParentClass.setDefaultLang("en", getApplicationContext());
//        }
//        Stetho.initializeWithDefaults(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
        MultiDex.install(this);

        DEVICE_LANGUAGE_DEFAULT = Locale.getDefault().getLanguage();
//        Log.i(TAG,"CURRENT_DEFAULT_LANG="+DEVICE_LANGUAGE_DEFAULT);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(base);
        String language =
                sp.getString(Constants.CURRENT_SET_LANGUAGE_KEY, DEVICE_LANGUAGE_DEFAULT);
//        Log.i(TAG,"CURRENT_SAVED_LANG="+language);

        super.attachBaseContext(LocaleHelper.onAttach(base, language));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        setLocale();
    }

//    private void setLocale() {
//        Locale locale = new Locale(getLang(this));
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());
//    }

    public static MyApplication getContext() {
        return mContext;
    } // fun of getContext

//    public static void initLanguage() {
//        ViewPump.init(ViewPump.builder()
//                .addInterceptor(new CalligraphyInterceptor(
//                        new CalligraphyConfig.Builder()
//                                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
//                                .setFontAttrId(R.attr.fontPath)
//                                .build()))
//                .build());
//    } // fun of initLanguage

    private void setCustomFont(){

                ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Cairo-SemiBold.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


    }
}
