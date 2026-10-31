package com.hook.packageinstaller.profiles;

import com.hook.packageinstaller.core.InstallerVersion;

public final class GenericProfile implements InstallerProfile {
    private final ProfileClasses classes = new ProfileClasses();

    @Override
    public String id() {
        return "PI_GENERIC";
    }

    @Override
    public String label() {
        return "未适配安装器版本";
    }

    @Override
    public boolean versionHintMatches(InstallerVersion version) {
        return true;
    }

    @Override
    public ProfileClasses classes() {
        return classes;
    }
}
