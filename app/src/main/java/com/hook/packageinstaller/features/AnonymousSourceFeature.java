package com.hook.packageinstaller.features;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;
import com.hook.packageinstaller.profiles.ProfileClasses;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public final class AnonymousSourceFeature implements Feature {
    private static final String TAG = "PI-Cleaner/Anonymous";

    @Override
    public String id() {
        return "anonymous_source_bypass";
    }

    @Override
    public void install(HookContext context) {
        ProfileClasses classes = context.classes();
        hookDialog(context, classes.anonymousClass);
        if (classes.unknownSourceClass != null
                && !classes.unknownSourceClass.equals(classes.anonymousClass)) {
            hookDialog(context, classes.unknownSourceClass);
        }
    }

    private static void hookDialog(HookContext context, String className) {
        if (className == null) {
            return;
        }
        Class<?> clazz = SafeXposed.findClass(context.classLoader, className);
        if (clazz == null) {
            return;
        }
        try {
            XposedHelpers.findAndHookMethod(clazz, "onStart", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    if (!(param.thisObject instanceof DialogFragment)) {
                        return;
                    }
                    DialogFragment fragment = (DialogFragment) param.thisObject;
                    Dialog dialog = fragment.getDialog();
                    if (dialog instanceof AlertDialog) {
                        try {
                            ((AlertDialog) dialog).getButton(
                                    AlertDialog.BUTTON_POSITIVE).performClick();
                            Log.i(TAG, "Auto-confirmed source dialog: " + className);
                        } catch (Throwable t) {
                            Log.w(TAG, "Auto-confirm failed: " + className, t);
                        }
                    }
                }
            });
        } catch (Throwable t) {
            Log.w(TAG, "Dialog hook unavailable: " + className, t);
        }
    }
}
