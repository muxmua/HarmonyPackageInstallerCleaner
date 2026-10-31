package com.hook.packageinstaller.features;

import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;
import com.hook.packageinstaller.profiles.ProfileClasses;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public final class RiskBypassFeature implements Feature {
    private static final String TAG = "PI-Cleaner/Risk";

    @Override
    public String id() {
        return "risk_bypass";
    }

    @Override
    public void install(HookContext context) {
        ProfileClasses classes = context.classes();
        hookStaging(context, classes);
        hookRiskJobs(context);
        hookConfirmation(context, classes);
        hookSetView(context, classes);
    }

    private static void hookRiskJobs(HookContext context) {
        String[] jobs = new String[]{"bq", "bk", "bn"};
        for (String job : jobs) {
            if (!SafeXposed.classExists(context.classLoader, job)) {
                continue;
            }
            SafeXposed.hookMethod(context.classLoader, job, "d", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    param.setResult(null);
                    Log.i(TAG, "Risk job skipped: " + job + "#d");
                }
            });
        }
    }

    private static void hookSetView(HookContext context, ProfileClasses classes) {
        if (classes.piaSetViewMethod == null) {
            return;
        }
        Class<?> resultClass = SafeXposed.findClass(context.classLoader,
                classes.installationControlResult);
        if (resultClass == null) {
            return;
        }
        SafeXposed.hookMethod(context.classLoader, classes.packageInstallerActivity,
                classes.piaSetViewMethod, resultClass, boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        if (param.args.length == 0 || param.args[0] == null) {
                            return;
                        }
                        Object clean = context.clean(param.args[0]);
                        if (clean != null) {
                            param.args[0] = clean;
                        }
                    }
                });
    }

    private static void hookStaging(HookContext context, ProfileClasses classes) {
        if (classes.stagingRiskClass == null
                || classes.stagingRiskMethod == null
                || classes.stagingContinueMethod == null) {
            return;
        }
        Class<?> resultClass = SafeXposed.findClass(context.classLoader,
                classes.installationControlResult);
        if (resultClass == null) {
            Log.w(TAG, "InstallationControlResult class missing");
            return;
        }
        if (!SafeXposed.methodExists(context.classLoader,
                classes.stagingRiskClass, classes.stagingRiskMethod)) {
            Log.w(TAG, "Staging risk method missing: "
                    + classes.stagingRiskClass + "#" + classes.stagingRiskMethod);
            return;
        }
        if (!SafeXposed.methodExists(context.classLoader,
                classes.stagingRiskClass, classes.stagingContinueMethod, resultClass)) {
            Log.w(TAG, "Staging continue method missing: "
                    + classes.stagingRiskClass + "#" + classes.stagingContinueMethod);
            return;
        }

        SafeXposed.hookMethod(context.classLoader, classes.stagingRiskClass,
                classes.stagingRiskMethod, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        Object self = param.thisObject;
                        Object clean = context.clean(null);
                        if (clean == null) {
                            return;
                        }
                        XposedHelpers.callMethod(self, classes.stagingContinueMethod, clean);
                        param.setResult(null);
                        Log.i(TAG, "Staging risk skipped, continued with clean result");
                    }
                });
    }

    private static void hookConfirmation(HookContext context, ProfileClasses classes) {
        if (classes.piaRiskMethod == null || classes.piaSetViewMethod == null) {
            return;
        }
        Class<?> resultClass = SafeXposed.findClass(context.classLoader,
                classes.installationControlResult);
        if (resultClass == null) {
            return;
        }
        if (!SafeXposed.methodExists(context.classLoader,
                classes.packageInstallerActivity, classes.piaSetViewMethod,
                resultClass, boolean.class)) {
            Log.w(TAG, "PIA setView method missing: "
                    + classes.piaSetViewMethod + "(InstallationControlResult, boolean)");
            return;
        }

        XC_MethodHook callback = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                boolean retry = false;
                if (param.args.length > 0 && param.args[0] instanceof Boolean) {
                    retry = (Boolean) param.args[0];
                }
                Object clean = context.clean(null);
                if (clean == null) {
                    return;
                }
                XposedHelpers.callMethod(param.thisObject, classes.piaSetViewMethod,
                        clean, retry);
                param.setResult(null);
                Log.i(TAG, "Confirmation risk skipped, applied clean result");
            }
        };

        if (classes.piaRiskHasBooleanArg) {
            SafeXposed.hookMethod(context.classLoader, classes.packageInstallerActivity,
                    classes.piaRiskMethod, boolean.class, callback);
        } else {
            SafeXposed.hookMethod(context.classLoader, classes.packageInstallerActivity,
                    classes.piaRiskMethod, callback);
        }
    }
}
