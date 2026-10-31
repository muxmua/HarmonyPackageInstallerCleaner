package com.hook.packageinstaller;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.hook.packageinstaller.config.ConfigRepository;
import com.hook.packageinstaller.config.FeatureConfig;
import com.hook.packageinstaller.core.HookContext;
import com.hook.packageinstaller.core.InstallerVersion;
import com.hook.packageinstaller.core.ProfileResolver;
import com.hook.packageinstaller.core.RestartScopeReceiver;
import com.hook.packageinstaller.core.UnsupportedVersionNotice;
import com.hook.packageinstaller.core.VersionDetector;
import com.hook.packageinstaller.features.FeatureRegistry;
import com.hook.packageinstaller.profiles.GenericProfile;
import com.hook.packageinstaller.profiles.InstallerProfile;

import java.util.concurrent.atomic.AtomicBoolean;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public final class HookBootstrap {
    private static final String TAG = "PI-Cleaner";
    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    private HookBootstrap() {
    }

    public static void start(Application application, LoadPackageParam lpparam) {
        if (!STARTED.compareAndSet(false, true)) {
            return;
        }
        try {
            InstallerVersion version = VersionDetector.detect(application);
            FeatureConfig config = ConfigRepository.load(application);
            InstallerProfile profile = ProfileResolver.resolve(version);

            HookContext context = new HookContext(application, lpparam.classLoader,
                    version, config, profile);

            String device = Build.MANUFACTURER + " " + Build.MODEL
                    + " (" + Build.DEVICE + ")";
            String line = "PackageInstaller cleaner started: installer="
                    + version.displayName()
                    + ", device=" + device
                    + ", os=" + version.osRelease + " (API " + version.sdkInt + ")"
                    + ", profile=" + profile.id() + "/" + profile.label()
                    + ", auth=" + config.authBypass
                    + ", risk=" + config.riskBypass
                    + ", riskUi=" + config.riskUiClean;
            Log.i(TAG, line);
            XposedBridge.log(TAG + ": " + line);

            if (profile instanceof GenericProfile) {
                UnsupportedVersionNotice.show(application, version);
            }
            FeatureRegistry.install(context);
            RestartScopeReceiver.register(application);
        } catch (Throwable t) {
            Log.e(TAG, "Bootstrap failed", t);
            XposedBridge.log(t);
        }
    }
}
