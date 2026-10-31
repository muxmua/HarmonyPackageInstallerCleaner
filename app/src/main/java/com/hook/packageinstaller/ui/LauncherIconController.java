package com.hook.packageinstaller.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

public final class LauncherIconController {
    private static final String ALIAS_CLASS = "com.hook.packageinstaller.ui.LauncherAlias";

    private LauncherIconController() {
    }

    public static boolean isHidden(Context context) {
        int state = context.getPackageManager().getComponentEnabledSetting(
                new ComponentName(context, ALIAS_CLASS));
        return state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED;
    }

    public static void setHidden(Context context, boolean hidden) {
        int state = hidden
                ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                : PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
        try {
            context.getPackageManager().setComponentEnabledSetting(
                    new ComponentName(context, ALIAS_CLASS),
                    state,
                    PackageManager.DONT_KILL_APP);
        } catch (Throwable ignored) {
        }
    }
}
