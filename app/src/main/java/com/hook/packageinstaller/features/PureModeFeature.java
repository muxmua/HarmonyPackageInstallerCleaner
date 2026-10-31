package com.hook.packageinstaller.features;

import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;
import com.hook.packageinstaller.profiles.ProfileClasses;

import de.robv.android.xposed.XC_MethodHook;

public final class PureModeFeature implements Feature {
    private static final String TAG = "PI-Cleaner/PureMode";

    @Override
    public String id() {
        return "pure_mode_bypass";
    }

    @Override
    public void install(HookContext context) {
        ProfileClasses classes = context.classes();
        if (classes.pureClass == null || classes.pureStatusMethod == null) {
            return;
        }
        boolean hooked = SafeXposed.hookMethod(context.classLoader,
                classes.pureClass, classes.pureStatusMethod, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        param.setResult(-1);
                    }
                });
        if (!hooked) {
            Log.w(TAG, "Pure-mode status not hooked: "
                    + classes.pureClass + "#" + classes.pureStatusMethod);
        }
    }
}
