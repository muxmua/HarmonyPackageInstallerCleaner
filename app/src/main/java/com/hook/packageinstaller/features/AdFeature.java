package com.hook.packageinstaller.features;

import android.app.Activity;
import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;
import com.hook.packageinstaller.profiles.ProfileClasses;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public final class AdFeature implements Feature {
    private static final String TAG = "PI-Cleaner/Ad";

    @Override
    public String id() {
        return "ad_clean";
    }

    @Override
    public void install(HookContext context) {
        ProfileClasses classes = context.classes();
        if (classes.adManagerClass == null || classes.adManagerMethod == null) {
            return;
        }
        Class<?> clazz = SafeXposed.findClass(context.classLoader, classes.adManagerClass);
        if (clazz == null) {
            return;
        }
        try {
            XposedHelpers.findAndHookMethod(clazz, classes.adManagerMethod,
                    Activity.class, int.class, String.class, String.class,
                    String.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            param.setResult(false);
                            Log.i(TAG, "Blocked AGD ad presentation");
                        }
                    });
        } catch (Throwable t) {
            Log.w(TAG, "Ad hook unavailable: " + classes.adManagerClass
                    + "#" + classes.adManagerMethod, t);
        }
    }
}
