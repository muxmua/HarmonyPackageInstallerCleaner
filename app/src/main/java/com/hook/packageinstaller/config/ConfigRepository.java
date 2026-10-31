package com.hook.packageinstaller.config;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.XSharedPreferences;

public final class ConfigRepository {
    private static final String TAG = "PI-Cleaner/Config";

    private ConfigRepository() {
    }

    public static FeatureConfig load(Context context) {
        FeatureConfig config = new FeatureConfig();

        try {
            Bundle bundle = context.getContentResolver().call(
                    Uri.parse("content://" + ConfigKeys.AUTHORITY),
                    ConfigKeys.METHOD_GET,
                    null,
                    null);
            if (bundle != null && bundle.containsKey(ConfigKeys.AD_CLEAN)) {
                config.apply(bundle);
                Log.i(TAG, "Loaded config from ContentProvider");
                return config;
            }
        } catch (Throwable t) {
            Log.w(TAG, "ContentProvider config unavailable: " + t);
        }

        try {
            XSharedPreferences preferences = new XSharedPreferences(
                    ConfigKeys.MODULE_PACKAGE, ConfigKeys.PREFS);
            preferences.reload();
            if (preferences.contains(ConfigKeys.AD_CLEAN)) {
                config.apply(preferences);
                Log.i(TAG, "Loaded config from XSharedPreferences");
                return config;
            }
        } catch (Throwable t) {
            Log.w(TAG, "XSharedPreferences config unavailable: " + t);
        }

        Log.i(TAG, "Using default config (all features enabled)");
        return config;
    }
}
