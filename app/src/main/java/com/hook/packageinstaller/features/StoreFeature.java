package com.hook.packageinstaller.features;

import android.app.Activity;
import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;

import de.robv.android.xposed.XC_MethodHook;

public final class StoreFeature implements Feature {
    private static final String TAG = "PI-Cleaner/Store";

    @Override
    public String id() {
        return "store_clean";
    }

    @Override
    public void install(HookContext context) {
        String pia = context.classes().packageInstallerActivity;
        boolean hooked = SafeXposed.hookMethod(context.classLoader, pia, "onResume",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        PromoUiFeature.hide((Activity) param.thisObject);
                    }
                });

        Class<?> resultClass = SafeXposed.findClass(context.classLoader,
                context.classes().installationControlResult);
        if (resultClass != null && context.classes().piaSetViewMethod != null) {
            SafeXposed.hookMethod(context.classLoader, pia,
                    context.classes().piaSetViewMethod, resultClass, boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            PromoUiFeature.hide((Activity) param.thisObject);
                        }
                    });
        }

        if (!hooked) {
            Log.w(TAG, "Store UI hook unavailable");
        }
    }
}
