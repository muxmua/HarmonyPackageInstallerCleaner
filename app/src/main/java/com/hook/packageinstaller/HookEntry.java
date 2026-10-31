package com.hook.packageinstaller;

import android.app.Application;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class HookEntry implements IXposedHookLoadPackage {
    private static final String TAG = "PI-Cleaner/Entry";
    private static final String TARGET_PACKAGE = "com.android.packageinstaller";

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) {
        if (!TARGET_PACKAGE.equals(lpparam.packageName)) {
            return;
        }

        Log.i(TAG, "Loaded target package: " + lpparam.packageName
                + ", process=" + lpparam.processName);

        boolean hooked = false;
        try {
            Class<?> appClass = XposedHelpers.findClass(
                    "com.android.packageinstaller.PackageInstallerApplication",
                    lpparam.classLoader);
            XposedHelpers.findAndHookMethod(appClass, "onCreate", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    HookBootstrap.start((Application) param.thisObject, lpparam);
                }
            });
            hooked = true;
            Log.i(TAG, "Hooked PackageInstallerApplication.onCreate");
        } catch (Throwable t) {
            Log.w(TAG, "PackageInstallerApplication hook unavailable", t);
        }

        if (!hooked) {
            try {
                XposedHelpers.findAndHookMethod(Application.class, "onCreate",
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) {
                                Application application = (Application) param.thisObject;
                                if (application.getClass().getName()
                                        .startsWith("com.android.packageinstaller")) {
                                    HookBootstrap.start(application, lpparam);
                                }
                            }
                        });
                Log.i(TAG, "Using Application.onCreate fallback");
            } catch (Throwable t) {
                Log.e(TAG, "Unable to install bootstrap hook", t);
                XposedBridge.log(t);
            }
        }
    }
}
