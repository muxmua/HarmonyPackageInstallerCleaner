package com.hook.packageinstaller.features;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;

import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.SafeXposed;

import de.robv.android.xposed.XC_MethodHook;

public final class RiskUiCleanFeature implements Feature {
    private static final String TAG = "PI-Cleaner/RiskUi";

    private static final String[] RISK_VIEWS = new String[]{
            "risk_check_layout",
            "risk_layout",
            "risk_img",
            "risk_tip_title",
            "risk_desc_tv",
            "risk_desc_content",
            "pure_introduction_tv",
            "go_setting_layout",
            "setNetWork",
            "network_arrow"
    };

    private static final String[] RISK_UI_METHODS = new String[]{"f", "e", "c"};

    @Override
    public String id() {
        return "risk_ui_clean";
    }

    @Override
    public void install(HookContext context) {
        String pia = context.classes().packageInstallerActivity;

        SafeXposed.hookMethod(context.classLoader, pia, "onResume",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        hideLater((Activity) param.thisObject);
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
                            hideLater((Activity) param.thisObject);
                        }
                    });
        }

        if (resultClass != null) {
            for (String method : RISK_UI_METHODS) {
                SafeXposed.hookMethod(context.classLoader, pia, method,
                        resultClass, boolean.class, new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                param.setResult(null);
                            }
                        });
            }
        }
    }

    private static void hideLater(final Activity activity) {
        if (activity == null) {
            return;
        }
        hide(activity);

        activity.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                hide(activity);
            }
        });
    }

    public static void hide(Activity activity) {
        if (activity == null) {
            return;
        }
        for (String name : RISK_VIEWS) {
            try {
                int id = activity.getResources().getIdentifier(
                        name, "id", activity.getPackageName());
                if (id == 0) {
                    continue;
                }
                View view = activity.findViewById(id);
                if (view == null) {
                    continue;
                }
                view.setVisibility(View.GONE);
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageDrawable(null);
                }
            } catch (Throwable t) {
                Log.w(TAG, "Unable to hide risk view: " + name, t);
            }
        }
    }
}
