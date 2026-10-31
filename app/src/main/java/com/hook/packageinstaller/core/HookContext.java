package com.hook.packageinstaller.core;

import android.app.Application;
import android.util.Log;

import com.hook.packageinstaller.config.FeatureConfig;
import com.hook.packageinstaller.profiles.InstallerProfile;
import com.hook.packageinstaller.profiles.ProfileClasses;

import de.robv.android.xposed.XC_MethodHook;

public final class HookContext {
    private static final String TAG = "PI-Cleaner";

    public final Application application;
    public final ClassLoader classLoader;
    public final InstallerVersion version;
    public final FeatureConfig config;
    public final InstallerProfile profile;

    public HookContext(Application application, ClassLoader classLoader,
                       InstallerVersion version, FeatureConfig config,
                       InstallerProfile profile) {
        this.application = application;
        this.classLoader = classLoader;
        this.version = version;
        this.config = config;
        this.profile = profile;
    }

    public ProfileClasses classes() {
        return profile.classes();
    }

    public boolean classExists(String className) {
        return SafeXposed.classExists(classLoader, className);
    }

    public boolean hook(String className, String methodName, Object... parameterTypesAndCallback) {
        return SafeXposed.hookMethod(classLoader, className, methodName,
                parameterTypesAndCallback);
    }

    public boolean hookAll(String className, String methodName, XC_MethodHook callback) {
        return SafeXposed.hookAllMethods(classLoader, className, methodName, callback);
    }

    public Object clean(Object result) {
        return ResultSanitizer.clean(this, result);
    }

    public void log(String message) {
        Log.i(TAG, message);
    }

    public void warn(String message, Throwable throwable) {
        Log.w(TAG, message, throwable);
    }
}
