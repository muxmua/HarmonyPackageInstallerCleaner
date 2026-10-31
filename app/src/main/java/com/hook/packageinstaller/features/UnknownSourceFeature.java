package com.hook.packageinstaller.features;

import android.os.UserHandle;
import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public final class UnknownSourceFeature implements Feature {
    private static final String TAG = "PI-Cleaner/UnknownSource";

    @Override
    public String id() {
        return "unknown_source_bypass";
    }

    @Override
    public void install(HookContext context) {
        hookUserManager(context);
        hookAppOps(context);
    }

    private static void hookUserManager(HookContext context) {
        XC_MethodHook callback = new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                String restriction = (String) param.args[0];
                if (restriction != null && restriction.contains("no_install_unknown_sources")) {
                    param.setResult(false);
                }
            }
        };

        try {
            XposedHelpers.findAndHookMethod("android.os.UserManager",
                    context.classLoader, "hasUserRestriction", String.class, callback);
        } catch (Throwable t) {
            Log.w(TAG, "UserManager.hasUserRestriction hook failed", t);
        }

        try {
            XposedHelpers.findAndHookMethod("android.os.UserManager",
                    context.classLoader, "getUserRestrictionSource",
                    String.class, UserHandle.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            String restriction = (String) param.args[0];
                            if (restriction != null
                                    && restriction.contains("no_install_unknown_sources")) {
                                param.setResult(0);
                            }
                        }
                    });
        } catch (Throwable t) {
            Log.w(TAG, "UserManager.getUserRestrictionSource hook failed", t);
        }
    }

    private static void hookAppOps(HookContext context) {
        Class<?> appOps = SafeXposed.findClass(context.classLoader, "android.app.AppOpsManager");
        if (appOps == null) {
            return;
        }
        int hooked = 0;
        for (java.lang.reflect.Method method : appOps.getDeclaredMethods()) {
            if (!"noteOpNoThrow".equals(method.getName())
                    || method.getReturnType() != int.class) {
                continue;
            }
            try {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        param.setResult(0);
                    }
                });
                hooked++;
            } catch (Throwable t) {
                Log.w(TAG, "AppOps hook failed: " + method, t);
            }
        }
        Log.i(TAG, "Hooked AppOps noteOpNoThrow methods: " + hooked);
    }
}
