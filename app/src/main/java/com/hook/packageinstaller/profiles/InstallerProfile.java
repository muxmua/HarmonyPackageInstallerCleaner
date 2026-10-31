package com.hook.packageinstaller.profiles;

import com.hook.packageinstaller.core.InstallerVersion;

public interface InstallerProfile {
    String id();

    String label();

    boolean versionHintMatches(InstallerVersion version);

    ProfileClasses classes();
}
