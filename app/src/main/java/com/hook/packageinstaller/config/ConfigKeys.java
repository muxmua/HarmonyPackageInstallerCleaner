package com.hook.packageinstaller.config;

public final class ConfigKeys {
    public static final String MODULE_PACKAGE = "com.hook.packageinstaller";
    public static final String AUTHORITY = "com.hook.packageinstaller.config";
    public static final String METHOD_GET = "get_config";
    public static final String PREFS = "config";

    public static final String AD_CLEAN = "ad_clean";
    public static final String STORE_CLEAN = "store_clean";
    public static final String AUTH_BYPASS = "auth_bypass";
    public static final String RISK_BYPASS = "risk_bypass";
    public static final String RISK_UI_CLEAN = "risk_ui_clean";
    public static final String PURE_MODE_BYPASS = "pure_mode_bypass";
    public static final String UNKNOWN_SOURCE_BYPASS = "unknown_source_bypass";
    public static final String ANONYMOUS_SOURCE_BYPASS = "anonymous_source_bypass";
    public static final String PROMO_UI_CLEAN = "promo_ui_clean";
    public static final String ORIENTATION_LOCK_FIX = "orientation_lock_fix";
    public static final String HIDE_LAUNCHER_ICON = "hide_launcher_icon";

    public static final String ACTION_RELOAD_INSTALLER =
            "com.hook.packageinstaller.action.RELOAD_INSTALLER";

    public static final String[] ALL = new String[]{
            AD_CLEAN,
            STORE_CLEAN,
            AUTH_BYPASS,
            RISK_BYPASS,
            RISK_UI_CLEAN,
            PURE_MODE_BYPASS,
            UNKNOWN_SOURCE_BYPASS,
            ANONYMOUS_SOURCE_BYPASS,
            PROMO_UI_CLEAN,
            ORIENTATION_LOCK_FIX
    };

    private ConfigKeys() {
    }
}
