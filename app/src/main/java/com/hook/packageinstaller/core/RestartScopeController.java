package com.hook.packageinstaller.core;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.hook.packageinstaller.config.ConfigKeys;

public final class RestartScopeController {
    private RestartScopeController() {
    }

    public static void restart(Context context) {
        Intent intent = new Intent(ConfigKeys.ACTION_RELOAD_INSTALLER);
        intent.setPackage("com.android.packageinstaller");
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.sendBroadcast(intent);

        try {
            ActivityManager manager = (ActivityManager)
                    context.getSystemService(Context.ACTIVITY_SERVICE);
            if (manager != null) {
                manager.killBackgroundProcesses("com.android.packageinstaller");
            }
        } catch (Throwable ignored) {
        }
    }
}
