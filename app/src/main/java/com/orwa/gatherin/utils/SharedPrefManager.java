package com.orwa.gatherin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.orwa.gatherin.Models.UserModelNew.Data;


/**
 * Created by omarn on shadow3/5/2018.
 */

public class SharedPrefManager {
    final static String SHARED_Setting_Data = "setting_data";
    final static String SHARED_PREF_NAME = "masari_shared";
    final static String LOGIN_STATUS = "masari_shared_login_status";
    final static String FIRST_TIME = "masari_shared_first_time";
    final static String type = "masari_shared_type";
    final static String type2 = "masari_shared_type2";
    final static String serial = "serial";
    final static String loginType = "loginType";
    final static String settingsCached = "settingsCached";
    final static String settingsCache = "settingsCache";
    final static String numberOfChats = "masari_sharednumberOfChats";


    Context mContext;
    Bundle bundle = new Bundle();
    Bundle args = new Bundle();
    Bundle searchArgs = new Bundle();

    public SharedPrefManager(Context mContext) {
        this.mContext = mContext;
    }

    public Boolean getLoginStatus() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        Boolean value = sharedPreferences.getBoolean(LOGIN_STATUS, false);
        return value;
    }

    public void setLoginStatus(Boolean status) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATUS, status);
        editor.apply();
    }

    public String getLoginType() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                loginType, 0);
        String value = sharedPreferences.getString("loginType", "");
        return value;
    }

    public void setLoginType(String Order) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(loginType,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginType", Order);
        editor.apply();
    }

    public Boolean isFirstTime() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        Boolean value = sharedPreferences.getBoolean(FIRST_TIME, true);
        return value;
    }

    public void setFirstTime(Boolean status) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME, status);
        editor.apply();
    }


    public String getAccessToken() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                type, 0);
        String value = sharedPreferences.getString(type, "");
        return value;
    }

    public void setAccessToken(String lat) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(type,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(type, lat);
        editor.apply();
    }


    public String getPassword() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                type2, 0);
        String value = sharedPreferences.getString(type2, "");
        return value;
    }

    public void setPassword(String lng) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(type2,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(type2, lng);
        editor.apply();
    }

    public String getSerial() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                serial, 0);
        String value = sharedPreferences.getString(serial, "");
        return value;
    }

    public void setSerial(String seriall) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(serial,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(serial, seriall);
        editor.apply();
    }

    /**
     * return userModel which hold all user data
     *
     * @return user model
     */
    public Data getUserDate() {
        Data userModel = new Data();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        userModel.setId(sharedPreferences.getString("id", ""));
        userModel.setEmail(sharedPreferences.getString("email", ""));
        userModel.setUsername(sharedPreferences.getString("name", ""));
        return userModel;
    }

    //
//
    public void setUserDate(Data user) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("name", user.getUsername());
        editor.apply();
    }


    public void setSingleId(String singleId) {

        args.putString("singleId", singleId);
    }

    public Bundle getSingleId() {
        if (args.isEmpty()) {
        }
        return args;
    }


//    public void setSettingsData(com.headshot.lazerclean.Models.SettingsModel.Data settingsData) {
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_Setting_Data,0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("about",settingsData.getAbout());
//        editor.putString("policy",settingsData.getPolicy());
//        editor.putString("mobile",settingsData.getMobile());
//        editor.putString("email",settingsData.getEmail());
//        editor.putString("telegram",settingsData.getTelegram());
//        editor.putString("snapchat",settingsData.getSnapchat());
//        editor.putString("twitter",settingsData.getTwitter());
//        editor.putString("instagram",settingsData.getInstagram());
//        editor.putString("whatsapp",settingsData.getWhatsapp());
//        editor.putString("tax",settingsData.getTax());
//        editor.putString("delivery_cost",settingsData.getDeliveryCost());
//        editor.putString("receive_cost",settingsData.getReceiveCost());
//        editor.apply();
//
//    }
//
//    public com.headshot.lazerclean.Models.SettingsModel.Data getSettingsData() {
//        com.headshot.lazerclean.Models.SettingsModel.Data data = new com.headshot.lazerclean.Models.SettingsModel.Data();
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_Setting_Data,0);
//        data.setAbout(sharedPreferences.getString("about",""));
//        data.setPolicy(sharedPreferences.getString("policy",""));
//        data.setMobile(sharedPreferences.getString("mobile",""));
//        data.setEmail(sharedPreferences.getString("email",""));
//        data.setTelegram(sharedPreferences.getString("telegram",""));
//        data.setSnapchat(sharedPreferences.getString("snapchat",""));
//        data.setTwitter(sharedPreferences.getString("twitter",""));
//        data.setInstagram(sharedPreferences.getString("instagram",""));
//        data.setWhatsapp(sharedPreferences.getString("whatsapp",""));
//        data.setTax(sharedPreferences.getString("tax",""));
//        data.setDeliveryCost(sharedPreferences.getString("delivery_cost",""));
//        data.setReceiveCost(sharedPreferences.getString("receive_cost",""));
//        return data;
//
//    }

    public void setSearch(String categoryId, String cityId, String type, String price, String model, String fe2a, String fe2aId, String year
            , String kilo, String capacity, String colour, Boolean New, Boolean Used, Boolean Selling, Boolean Wanted, String previousFragment, String typeId, String plateCountry, String typeOfDelivery
            , String typeGarageId, String transportationCompanies) {
        searchArgs.putString("categoryId", categoryId);
        searchArgs.putString("cityId", cityId);
        searchArgs.putString("type", type);
        searchArgs.putString("price", price);
        searchArgs.putString("model", model);
        searchArgs.putString("fe2a", fe2a);
        searchArgs.putString("fe2aId", fe2aId);
        searchArgs.putString("year", year);
        searchArgs.putString("kilo", kilo);
        searchArgs.putString("capacity", capacity);
        searchArgs.putString("colour", colour);
        searchArgs.putBoolean("New", New);
        searchArgs.putBoolean("Used", Used);
        searchArgs.putBoolean("Selling", Selling);
        searchArgs.putBoolean("Wanted", Wanted);
        searchArgs.putString("previousFragment", previousFragment);
        searchArgs.putString("typeId", typeId);
        searchArgs.putString("plateCountry", plateCountry);
        searchArgs.putString("typeOfDelivery", typeOfDelivery);
        searchArgs.putString("typeGarageId", typeGarageId);
        searchArgs.putString("transportationCompanies", transportationCompanies);


    }

    public Bundle getSearch() {
        if (searchArgs.isEmpty()) {
        }
        return searchArgs;

    }

    public int getNumberOfChats() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                numberOfChats, 0);
        int value = sharedPreferences.getInt(numberOfChats, 0);
        return value;
    }

    public void setNumberOfChats(int status) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(numberOfChats,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(numberOfChats, status);
        editor.apply();
    }


    /**
     * this method is responsible for user logout and clearing cache
     */
    public void Logout() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        setFirstTime(false);
    }


}
