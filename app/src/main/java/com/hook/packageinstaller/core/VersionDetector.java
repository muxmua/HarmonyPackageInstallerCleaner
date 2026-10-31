package com.hook.packageinstaller.core;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public final class VersionDetector {
    private static final String TAG = "PI-Cleaner/Version";
    public static final String TARGET_PACKAGE = "com.android.packageinstaller";

    private VersionDetector() {
    }

    public static InstallerVersion detect(Context context) {
        String versionName = null;
        long versionCode = -1L;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(TARGET_PACKAGE, 0);
            versionName = info.versionName;
            versionCode = Build.VERSION.SDK_INT >= 28
                    ? info.getLongVersionCode()
                    : info.versionCode;
        } catch (Throwable t) {
            Log.e(TAG, "Unable to read PackageInstaller version", t);
        }

        InstallerVersion version = new InstallerVersion(
                TARGET_PACKAGE,
                versionName,
                versionCode,
                Build.VERSION.SDK_INT,
                Build.VERSION.RELEASE,
                Build.DISPLAY);
        Log.i(TAG, "Detected " + version);
        return version;
    }
}
