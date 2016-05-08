package com.ramimartin.sample.multibluetooth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Youngdo on 2016-04-25.
 */
public class BasePreferenceManager
{
    protected static SharedPreferences preference;


    protected BasePreferenceManager(Context context)
    {
        super();
        preference = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }


    /**
     * key 수동 설정
     *
     * @param key   키 값
     * @param value 내용
     */
    protected static void put(String key, String value)
    {
        SharedPreferences p = preference;
        SharedPreferences.Editor editor = p.edit();
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * String 값 가져오기
     *
     * @param key 키 값
     * @return String (기본값 null)
     */
    protected static String get(String key)
    {
        return preference.getString(key, null);
    }


    /**
     * String 값 가져오기
     *
     * @param key 키 값
     * @return String (기본값 "")
     */
    protected static String getWithNullToBlank(String key)
    {
        return preference.getString(key, "");
    }


    /**
     * key 설정
     *
     * @param key   키 값
     * @param value 내용
     */
    protected static void put(String key, boolean value)
    {
        SharedPreferences p = preference;
        SharedPreferences.Editor editor = p.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    /**
     * Boolean 값 가져오기
     *
     * @param key          키 값
     * @param defaultValue 기본값
     * @return Boolean
     */
    protected static boolean get(String key, boolean defaultValue)
    {
        return preference.getBoolean(key, defaultValue);
    }


    /**
     * key 설정
     *
     * @param key   키 값
     * @param value 내용
     */
    protected static void put(String key, int value)
    {
        SharedPreferences p = preference;
        SharedPreferences.Editor editor = p.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    /**
     * int 값 가져오기
     *
     * @param key          키 값
     * @param defaultValue 기본값
     * @return int
     */
    protected static int get(String key, int defaultValue)
    {
        return preference.getInt(key, defaultValue);
    }

    /**
     * key 설정
     *
     * @param key   키 값
     * @param value 내용
     */
    protected static void put(String key, long value)
    {
        SharedPreferences p = preference;
        SharedPreferences.Editor editor = p.edit();
        editor.putLong(key, value);
        editor.commit();
    }


    /**
     * int 값 가져오기
     *
     * @param key          키 값
     * @param defaultValue 기본값
     * @return int
     */
    protected static long get(String key, long defaultValue)
    {
        return preference.getLong(key, defaultValue);
    }
}