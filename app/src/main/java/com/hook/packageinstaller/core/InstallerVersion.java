package com.hook.packageinstaller.core;

public final class InstallerVersion {
    public final String packageName;
    public final String versionName;
    public final long versionCode;
    public final int sdkInt;
    public final String osRelease;
    public final String buildDisplay;

    public InstallerVersion(String packageName, String versionName, long versionCode,
                            int sdkInt, String osRelease, String buildDisplay) {
        this.packageName = packageName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.sdkInt = sdkInt;
        this.osRelease = osRelease;
        this.buildDisplay = buildDisplay;
    }

    public String displayName() {
        return (versionName == null ? "?" : versionName) + " (" + versionCode + ")";
    }

    @Override
    public String toString() {
        return "InstallerVersion{" +
                "packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", sdkInt=" + sdkInt +
                ", osRelease='" + osRelease + '\'' +
                ", buildDisplay='" + buildDisplay + '\'' +
                '}';
    }
}
