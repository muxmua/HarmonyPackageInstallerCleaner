package com.hook.packageinstaller.core;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import com.hook.packageinstaller.config.ConfigKeys;

public final class RestartScopeReceiver extends BroadcastReceiver {
    private static final String TAG = "PI-Cleaner/Reload";
    private static boolean registered = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !ConfigKeys.ACTION_RELOAD_INSTALLER.equals(intent.getAction())) {
            return;
        }
        Log.i(TAG, "Reload broadcast received, terminating PackageInstaller process");
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public static void register(Application application) {
        if (registered) {
            return;
        }
        try {
            IntentFilter filter = new IntentFilter(ConfigKeys.ACTION_RELOAD_INSTALLER);
            RestartScopeReceiver receiver = new RestartScopeReceiver();
            if (Build.VERSION.SDK_INT >= 33) {
                application.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED);
            } else {
                application.registerReceiver(receiver, filter);
            }
            registered = true;
            Log.i(TAG, "Reload receiver registered");
        } catch (Throwable t) {
            Log.w(TAG, "Unable to register reload receiver", t);
        }
    }
}
