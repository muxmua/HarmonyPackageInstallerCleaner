package com.hook.packageinstaller.features;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.util.Log;

import com.hook.packageinstaller.core.HookContext;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public final class OrientationLockFeature implements Feature {
    private static final String TAG = "PI-Cleaner/Orientation";

    @Override
    public String id() {
        return "orientation_lock_fix";
    }

    @Override
    public void install(HookContext context) {
        try {
            XposedHelpers.findAndHookMethod(Activity.class, "setRequestedOrientation",
                    int.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            if (!context.config.orientationLockFix) {
                                return;
                            }
                            int requested = (int) param.args[0];
                            if (requested != ActivityInfo.SCREEN_ORIENTATION_SENSOR
                                    && requested != ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR) {
                                return;
                            }
                            Activity activity = (Activity) param.thisObject;
                            if (!isRotationLocked(activity)) {
                                return;
                            }
                            if (requested == ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR) {
                                param.args[0] = ActivityInfo.SCREEN_ORIENTATION_FULL_USER;
                            } else {
                                param.args[0] = ActivityInfo.SCREEN_ORIENTATION_USER;
                            }
                            Log.i(TAG, "Rotation locked: requested=" + requested
                                    + " -> " + param.args[0]);
                        }
                    });
        } catch (Throwable t) {
            Log.w(TAG, "Unable to hook Activity.setRequestedOrientation", t);
        }
    }

    private static boolean isRotationLocked(Context context) {
        try {
            ContentResolver resolver = context.getContentResolver();
            return Settings.System.getInt(resolver,
                    Settings.System.ACCELEROMETER_ROTATION, 1) == 0;
        } catch (Throwable t) {
            return false;
        }
    }
}
