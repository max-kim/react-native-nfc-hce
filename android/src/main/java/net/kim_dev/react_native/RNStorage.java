package net.kim_dev.react_native;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Map;
import java.util.HashMap;

public class RNStorage {

    public static final String HCE_ID = "HCE_ID";
    public static final String HCE_AID = "HCE_AID";

    private static final String DEFAULT_VALUE = "F01020304050";
    private static final Object valueLock = new Object();

    public static void setValue(Context context, String value, String key) {
        synchronized (valueLock) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            prefs.edit().putString(key, value).commit();
        }
    }

    public static String getValue(Context context, String key) {
        synchronized (valueLock) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getString(key, DEFAULT_VALUE);
        }
    }
}
