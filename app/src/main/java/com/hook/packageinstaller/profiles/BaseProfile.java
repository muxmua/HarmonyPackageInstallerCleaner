package com.hook.packageinstaller.profiles;

import com.hook.packageinstaller.core.InstallerVersion;

public abstract class BaseProfile implements InstallerProfile {
    private final String id;
    private final String label;
    private final String versionPrefix;
    private final long minVersionCode;
    private final long maxVersionCode;
    private final ProfileClasses classes;

    protected BaseProfile(String id, String label, String versionPrefix,
                          long minVersionCode, long maxVersionCode,
                          ProfileClasses classes) {
        this.id = id;
        this.label = label;
        this.versionPrefix = versionPrefix;
        this.minVersionCode = minVersionCode;
        this.maxVersionCode = maxVersionCode;
        this.classes = classes;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public boolean versionHintMatches(InstallerVersion version) {
        if (version == null) {
            return false;
        }
        if (version.versionCode >= minVersionCode && version.versionCode <= maxVersionCode) {
            return true;
        }
        return version.versionName != null && version.versionName.startsWith(versionPrefix);
    }

    @Override
    public ProfileClasses classes() {
        return classes;
    }
}
