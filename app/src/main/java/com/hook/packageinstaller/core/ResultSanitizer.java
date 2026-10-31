package com.hook.packageinstaller.core;

import android.util.Log;

import de.robv.android.xposed.XposedHelpers;

public final class ResultSanitizer {
    private static final String TAG = "PI-Cleaner/Result";

    private ResultSanitizer() {
    }

    public static Object clean(HookContext context, Object original) {
        if (original == null) {
            original = newResult(context);
        }
        if (original == null) {
            return null;
        }

        Object clean = newResult(context);
        if (clean == null) {
            clean = original;
        }

        copyString(original, clean, "getPkgName", "setPkgName");

        setString(clean, "setAppId", "");
        setString(clean, "setDetailId", "");

        setInt(clean, "setDisplayPolicy", 0);

        setInt(clean, "setForwardBtnType", 1);

        setString(clean, "setControlType", "0");
        setString(clean, "setControlByteCode", "0");
        setString(clean, "setTitle", "");
        setString(clean, "setTipText", "");
        setString(clean, "setVirusDetail", "");
        setInt(clean, "setVirusType", 0);
        setInt(clean, "setRiskType", 0);
        setInt(clean, "setPwdCheckType", 0);
        setInt(clean, "setIconColor", 0);
        setInt(clean, "setBootInstall", 0);
        setBoolean(clean, "setPureModeExempt", true);
        setBoolean(clean, "setAbiUnCompatibility", false);
        setObject(clean, "setPendingIntent", (Object) null);
        setObject(clean, "setWashAppInfo", (Object) null);

        setIntField(clean, "controlByteCodeNum", 0);
        if (clean == original) {
            setIntField(clean, "pwdCheckType", 0);
            setBooleanField(clean, "isPureModeExempt", true);
            setObjectField(clean, "mPendingIntent", null);
            setObjectField(clean, "mWashAppInfo", null);
        }

        Log.i(TAG, "Sanitized result: " + describe(original) + " -> " + describe(clean));
        return clean;
    }

    private static Object newResult(HookContext context) {
        try {
            Class<?> clazz = SafeXposed.findClass(context.classLoader,
                    context.classes().installationControlResult);
            if (clazz == null) {
                return null;
            }
            return XposedHelpers.newInstance(clazz);
        } catch (Throwable t) {
            Log.w(TAG, "Cannot construct InstallationControlResult", t);
            return null;
        }
    }

    private static void copyString(Object from, Object to, String getter, String setter) {
        Object value = call(from, getter);
        if (value instanceof String) {
            setString(to, setter, (String) value);
        }
    }

    private static void copyInt(Object from, Object to, String getter, String setter) {
        Object value = call(from, getter);
        if (value instanceof Number) {
            setInt(to, setter, ((Number) value).intValue());
        }
    }

    private static Object call(Object target, String method) {
        try {
            return XposedHelpers.callMethod(target, method);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void setString(Object target, String method, String value) {
        try {
            XposedHelpers.callMethod(target, method, value);
        } catch (Throwable ignored) {
        }
    }

    private static void setInt(Object target, String method, int value) {
        try {
            XposedHelpers.callMethod(target, method, value);
        } catch (Throwable ignored) {
        }
    }

    private static void setBoolean(Object target, String method, boolean value) {
        try {
            XposedHelpers.callMethod(target, method, value);
        } catch (Throwable ignored) {
        }
    }

    private static void setObject(Object target, String method, Object value) {
        try {
            XposedHelpers.callMethod(target, method, value);
        } catch (Throwable ignored) {
        }
    }

    private static void setIntField(Object target, String field, int value) {
        try {
            XposedHelpers.setIntField(target, field, value);
        } catch (Throwable ignored) {
        }
    }

    private static void setBooleanField(Object target, String field, boolean value) {
        try {
            XposedHelpers.setBooleanField(target, field, value);
        } catch (Throwable ignored) {
        }
    }

    private static void setObjectField(Object target, String field, Object value) {
        try {
            XposedHelpers.setObjectField(target, field, value);
        } catch (Throwable ignored) {
        }
    }

    private static String describe(Object result) {
        if (result == null) {
            return "null";
        }
        Object controlType = call(result, "getControlType");
        Object pkg = call(result, "getPkgName");
        Object needPwd = call(result, "isNeedPwdVerify");
        Object pure = call(result, "isPureModeExempt");
        return "{pkg=" + pkg + ", controlType=" + controlType
                + ", needPwd=" + needPwd + ", pureExempt=" + pure + "}";
    }
}
