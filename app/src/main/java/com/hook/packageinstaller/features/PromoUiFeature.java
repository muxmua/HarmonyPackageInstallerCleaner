package com.hook.packageinstaller.features;

import android.app.Activity;
import android.view.View;
import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;

import de.robv.android.xposed.XC_MethodHook;

public final class PromoUiFeature implements Feature {
    private static final String TAG = "PI-Cleaner/Promo";

    private static final String[] PROMO_VIEWS = new String[]{
            "offical_button",
            "decide_to_continue",
            "go_setting_layout",
            "setNetWork",
            "network_arrow"
    };

    @Override
    public String id() {
        return "promo_ui_clean";
    }

    @Override
    public void install(HookContext context) {
        String pia = context.classes().packageInstallerActivity;
        SafeXposed.hookMethod(context.classLoader, pia, "onResume",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        hide((Activity) param.thisObject);
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
                            hide((Activity) param.thisObject);
                        }
                    });
        }
    }

    public static void hide(Activity activity) {
        if (activity == null) {
            return;
        }
        for (String name : PROMO_VIEWS) {
            try {
                int id = activity.getResources().getIdentifier(
                        name, "id", activity.getPackageName());
                if (id == 0) {
                    continue;
                }
                View view = activity.findViewById(id);
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            } catch (Throwable t) {
                Log.w(TAG, "Unable to hide promo view: " + name, t);
            }
        }
    }
}
