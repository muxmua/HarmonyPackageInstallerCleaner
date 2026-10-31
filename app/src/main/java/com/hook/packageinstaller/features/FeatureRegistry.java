package com.hook.packageinstaller.features;

import android.util.Log;

import com.hook.packageinstaller.config.FeatureConfig;
import com.hook.packageinstaller.core.HookContext;

import java.util.ArrayList;
import java.util.List;

public final class FeatureRegistry {
    private static final String TAG = "PI-Cleaner/Features";

    private FeatureRegistry() {
    }

    public static void install(HookContext context) {
        FeatureConfig config = context.config;
        List<Feature> features = new ArrayList<>();

        if (config.orientationLockFix) {
            features.add(new OrientationLockFeature());
        }
        if (config.authBypass) {
            features.add(new AuthBypassFeature());
        }
        if (config.riskBypass) {
            features.add(new RiskBypassFeature());
        }
        if (config.riskUiClean) {
            features.add(new RiskUiCleanFeature());
        }
        if (config.adClean) {
            features.add(new AdFeature());
        }
        if (config.unknownSourceBypass) {
            features.add(new UnknownSourceFeature());
        }
        if (config.pureModeBypass) {
            features.add(new PureModeFeature());
        }
        if (config.storeClean) {
            features.add(new StoreFeature());
        }
        if (config.anonymousSourceBypass) {
            features.add(new AnonymousSourceFeature());
        }
        if (config.promoUiClean) {
            features.add(new PromoUiFeature());
        }

        Log.i(TAG, "Installing " + features.size() + " features");
        for (Feature feature : features) {
            try {
                feature.install(context);
                Log.i(TAG, "Feature installed: " + feature.id());
            } catch (Throwable t) {
                Log.e(TAG, "Feature failed: " + feature.id(), t);
            }
        }
    }
}
