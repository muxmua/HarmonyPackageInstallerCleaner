package com.hook.packageinstaller.features;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;
import com.hook.packageinstaller.profiles.ProfileClasses;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public final class AuthBypassFeature implements Feature {
    private static final String TAG = "PI-Cleaner/Auth";

    @Override
    public String id() {
        return "auth_bypass";
    }

    @Override
    public void install(HookContext context) {
        ProfileClasses classes = context.classes();

        SafeXposed.hookMethod(context.classLoader, classes.installationControlResult,
                "isNeedPwdVerify", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        param.setResult(false);
                    }
                });

        if (classes.authChildClass != null && classes.authChildCheckMethod != null) {
            boolean hooked = SafeXposed.hookMethod(context.classLoader,
                    classes.authChildClass, classes.authChildCheckMethod,
                    Context.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            param.setResult(false);
                        }
                    });
            if (!hooked) {
                Log.w(TAG, "Child-mode check not hooked: "
                        + classes.authChildClass + "#" + classes.authChildCheckMethod);
            }
        }

        hookAuthEntry(context, classes);
    }

    private static void hookAuthEntry(HookContext context, ProfileClasses classes) {
        if (classes.authEntryClass == null || classes.authEntryMethod == null) {
            return;
        }
        Class<?> clazz = SafeXposed.findClass(context.classLoader, classes.authEntryClass);
        if (clazz == null) {
            return;
        }

        boolean hooked = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getName().equals(classes.authEntryMethod)) {
                continue;
            }
            Class<?>[] types = method.getParameterTypes();
            if (types.length < 3 || types.length > 8) {
                continue;
            }
            if (!String.class.isAssignableFrom(types[0])) {
                continue;
            }
            boolean hasActivity = false;
            boolean hasResult = false;
            for (Class<?> type : types) {
                if (Activity.class.isAssignableFrom(type)) {
                    hasActivity = true;
                }
                if (type.getName().endsWith("InstallationControlResult")) {
                    hasResult = true;
                }
            }
            if (!hasActivity || !hasResult) {
                continue;
            }

            try {
                XposedBridge.hookMethod(method, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        try {
                            Object callback = param.args[param.args.length - 1];
                            if (callback != null) {
                                XposedHelpers.callMethod(callback, "a");
                            }
                        } catch (Throwable t) {
                            Log.w(TAG, "Auth success callback failed", t);
                        }
                        param.setResult(null);
                        Log.i(TAG, "Local auth bypassed: " + method.getName());
                    }
                });
                hooked = true;
                Log.i(TAG, "Hooked auth entry: " + method);
            } catch (Throwable t) {
                Log.w(TAG, "Auth entry hook failed: " + method, t);
            }
        }

        if (!hooked) {
            Log.w(TAG, "No auth entry matched: "
                    + classes.authEntryClass + "#" + classes.authEntryMethod);
        }
    }
}
