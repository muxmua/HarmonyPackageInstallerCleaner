package com.hook.packageinstaller.core;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import de.robv.android.xposed.XposedBridge;

public final class UnsupportedVersionNotice {
    private static final String TAG = "PI-Cleaner/Unsupported";

    private UnsupportedVersionNotice() {
    }

    public static void show(Application application, InstallerVersion version) {
        String message = "当前应用安装器 " + version.displayName() + " 未适配";
        String detail = message + "，device=" + Build.MANUFACTURER + " " + Build.MODEL;
        Log.w(TAG, detail);
        XposedBridge.log(TAG + ": " + detail);

        try {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    Toast.makeText(application.getApplicationContext(), message,
                            Toast.LENGTH_LONG).show();
                } catch (Throwable t) {
                    Log.w(TAG, "Unable to show unsupported-version toast", t);
                }
            }, 900L);
        } catch (Throwable t) {
            Log.w(TAG, "Unable to schedule unsupported-version toast", t);
        }
    }
}
