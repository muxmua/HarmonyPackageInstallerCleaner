package com.hook.packageinstaller.core;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public final class SafeXposed {
    private static final String TAG = "PI-Cleaner/Hook";

    private SafeXposed() {
    }

    public static boolean classExists(ClassLoader classLoader, String className) {
        if (className == null || className.isEmpty()) {
            return false;
        }
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static Class<?> findClass(ClassLoader classLoader, String className) {
        if (!classExists(classLoader, className)) {
            return null;
        }
        try {
            return XposedHelpers.findClass(className, classLoader);
        } catch (Throwable t) {
            Log.w(TAG, "findClass failed: " + className, t);
            return null;
        }
    }

    public static boolean hookMethod(ClassLoader classLoader, String className,
                                     String methodName, Object... parameterTypesAndCallback) {
        Class<?> clazz = findClass(classLoader, className);
        if (clazz == null) {
            Log.w(TAG, "Hook skipped, class missing: " + className);
            return false;
        }
        try {
            XposedHelpers.findAndHookMethod(clazz, methodName, parameterTypesAndCallback);
            Log.i(TAG, "Hooked " + className + "#" + methodName);
            return true;
        } catch (Throwable t) {
            Log.w(TAG, "Hook failed: " + className + "#" + methodName, t);
            return false;
        }
    }

    public static boolean methodExists(ClassLoader classLoader, String className,
                                       String methodName, Class<?>... parameterTypes) {
        Class<?> clazz = findClass(classLoader, className);
        if (clazz == null) {
            return false;
        }
        try {
            XposedHelpers.findMethodExact(clazz, methodName, parameterTypes);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static boolean hookAllMethods(ClassLoader classLoader, String className,
                                         String methodName, XC_MethodHook callback) {
        Class<?> clazz = findClass(classLoader, className);
        if (clazz == null) {
            return false;
        }
        boolean hooked = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            if (Modifier.isAbstract(method.getModifiers())) {
                continue;
            }
            try {
                XposedBridge.hookMethod(method, callback);
                hooked = true;
                Log.i(TAG, "Hooked " + className + "#" + methodName
                        + "(" + method.getParameterTypes().length + " args)");
            } catch (Throwable t) {
                Log.w(TAG, "hookMethod failed: " + method, t);
            }
        }
        return hooked;
    }

    public static Object getField(Object target, String fieldName) {
        try {
            return XposedHelpers.getObjectField(target, fieldName);
        } catch (Throwable t) {
            Log.w(TAG, "getField failed: " + fieldName, t);
            return null;
        }
    }

    public static void setField(Object target, String fieldName, Object value) {
        try {
            XposedHelpers.setObjectField(target, fieldName, value);
        } catch (Throwable t) {
            Log.w(TAG, "setField failed: " + fieldName, t);
        }
    }

    public static Object call(Object target, String methodName, Object... args) {
        try {
            return XposedHelpers.callMethod(target, methodName, args);
        } catch (Throwable t) {
            Log.w(TAG, "callMethod failed: " + methodName, t);
            return null;
        }
    }
}
