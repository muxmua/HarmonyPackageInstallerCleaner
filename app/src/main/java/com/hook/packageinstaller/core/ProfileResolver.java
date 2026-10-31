package com.hook.packageinstaller.core;

import android.util.Log;

import com.hook.packageinstaller.profiles.GenericProfile;
import com.hook.packageinstaller.profiles.Harmony20Profile;
import com.hook.packageinstaller.profiles.Harmony30Profile;
import com.hook.packageinstaller.profiles.Harmony40Profile;
import com.hook.packageinstaller.profiles.Harmony42Profile;
import com.hook.packageinstaller.profiles.Harmony43Profile;
import com.hook.packageinstaller.profiles.InstallerProfile;

import java.util.ArrayList;
import java.util.List;

public final class ProfileResolver {
    private static final String TAG = "PI-Cleaner/Profile";

    private static final String[][] ADAPTED_VERSIONS = new String[][]{
            {"11.5.1.302", "1105001302"},
            {"12.7.1.300", "1207001300"},
            {"13.2.1.302", "1302001302"},
            {"13.3.1.302", "1303001302"},
            {"15.2.1.300", "1502001300"}
    };

    private ProfileResolver() {
    }

    public static InstallerProfile resolve(InstallerVersion version) {
        if (version == null || !isAdaptedVersion(version)) {
            Log.w(TAG, "Installer version is not adapted: "
                    + (version == null ? "unknown" : version.displayName()));
            return new GenericProfile();
        }

        for (InstallerProfile profile : profiles()) {
            if (profile.versionHintMatches(version)) {
                Log.i(TAG, "Matched by installer version: "
                        + profile.id() + " / " + profile.label());
                return profile;
            }
        }

        Log.w(TAG, "No profile found for adapted version: " + version.displayName());
        return new GenericProfile();
    }

    public static boolean isAdaptedVersion(InstallerVersion version) {
        if (version == null) {
            return false;
        }
        for (String[] adapted : ADAPTED_VERSIONS) {
            boolean nameMatches = version.versionName != null
                    && adapted[0].equals(version.versionName);
            boolean codeMatches = version.versionCode > 0
                    && adapted[1].equals(String.valueOf(version.versionCode));
            if (nameMatches && codeMatches) {
                return true;
            }
        }
        return false;
    }

    private static List<InstallerProfile> profiles() {
        List<InstallerProfile> profiles = new ArrayList<>();
        profiles.add(new Harmony20Profile());
        profiles.add(new Harmony30Profile());
        profiles.add(new Harmony40Profile());
        profiles.add(new Harmony42Profile());
        profiles.add(new Harmony43Profile());
        return profiles;
    }
}
