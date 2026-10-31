package com.hook.packageinstaller.config;

import android.content.SharedPreferences;
import android.os.Bundle;

public final class FeatureConfig {
    public boolean adClean = true;
    public boolean storeClean = true;
    public boolean authBypass = true;
    public boolean riskBypass = true;
    public boolean riskUiClean = true;
    public boolean pureModeBypass = true;
    public boolean unknownSourceBypass = true;
    public boolean anonymousSourceBypass = true;
    public boolean promoUiClean = true;
    public boolean orientationLockFix = true;

    public void apply(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        adClean = get(bundle, ConfigKeys.AD_CLEAN, adClean);
        storeClean = get(bundle, ConfigKeys.STORE_CLEAN, storeClean);
        authBypass = get(bundle, ConfigKeys.AUTH_BYPASS, authBypass);
        riskBypass = get(bundle, ConfigKeys.RISK_BYPASS, riskBypass);
        riskUiClean = get(bundle, ConfigKeys.RISK_UI_CLEAN, riskUiClean);
        pureModeBypass = get(bundle, ConfigKeys.PURE_MODE_BYPASS, pureModeBypass);
        unknownSourceBypass = get(bundle, ConfigKeys.UNKNOWN_SOURCE_BYPASS, unknownSourceBypass);
        anonymousSourceBypass = get(bundle, ConfigKeys.ANONYMOUS_SOURCE_BYPASS, anonymousSourceBypass);
        promoUiClean = get(bundle, ConfigKeys.PROMO_UI_CLEAN, promoUiClean);
        orientationLockFix = get(bundle, ConfigKeys.ORIENTATION_LOCK_FIX, orientationLockFix);
    }

    public void apply(SharedPreferences preferences) {
        if (preferences == null) {
            return;
        }
        adClean = preferences.getBoolean(ConfigKeys.AD_CLEAN, adClean);
        storeClean = preferences.getBoolean(ConfigKeys.STORE_CLEAN, storeClean);
        authBypass = preferences.getBoolean(ConfigKeys.AUTH_BYPASS, authBypass);
        riskBypass = preferences.getBoolean(ConfigKeys.RISK_BYPASS, riskBypass);
        riskUiClean = preferences.getBoolean(ConfigKeys.RISK_UI_CLEAN, riskUiClean);
        pureModeBypass = preferences.getBoolean(ConfigKeys.PURE_MODE_BYPASS, pureModeBypass);
        unknownSourceBypass = preferences.getBoolean(ConfigKeys.UNKNOWN_SOURCE_BYPASS, unknownSourceBypass);
        anonymousSourceBypass = preferences.getBoolean(ConfigKeys.ANONYMOUS_SOURCE_BYPASS, anonymousSourceBypass);
        promoUiClean = preferences.getBoolean(ConfigKeys.PROMO_UI_CLEAN, promoUiClean);
        orientationLockFix = preferences.getBoolean(ConfigKeys.ORIENTATION_LOCK_FIX, orientationLockFix);
    }

    private static boolean get(Bundle bundle, String key, boolean fallback) {
        if (bundle == null || !bundle.containsKey(key)) {
            return fallback;
        }
        return bundle.getBoolean(key, fallback);
    }
}
