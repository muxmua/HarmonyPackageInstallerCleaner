package com.hook.packageinstaller.profiles;

public final class ProfileClasses {
    public String packageInstallerActivity = "com.android.packageinstaller.PackageInstallerActivity";
    public String installationControlResult =
            "com.huawei.android.packageinstaller.storeapi.response.InstallationControlResult";

    public String stagingRiskClass;
    public String stagingRiskMethod;

    public String stagingContinueMethod;

    public String piaRiskMethod;
    public boolean piaRiskHasBooleanArg = true;

    public String piaSetViewMethod;

    public String authChildClass;
    public String authChildCheckMethod;

    public String authEntryClass;
    public String authEntryMethod;

    public String pureClass;
    public String pureStatusMethod;

    public String adManagerClass;
    public String adManagerMethod;

    public String storeClass;
    public String anonymousClass;
    public String unknownSourceClass;
    public String orientationClass;

    public ProfileClasses copy() {
        ProfileClasses copy = new ProfileClasses();
        copy.packageInstallerActivity = packageInstallerActivity;
        copy.installationControlResult = installationControlResult;
        copy.stagingRiskClass = stagingRiskClass;
        copy.stagingRiskMethod = stagingRiskMethod;
        copy.stagingContinueMethod = stagingContinueMethod;
        copy.piaRiskMethod = piaRiskMethod;
        copy.piaRiskHasBooleanArg = piaRiskHasBooleanArg;
        copy.piaSetViewMethod = piaSetViewMethod;
        copy.authChildClass = authChildClass;
        copy.authChildCheckMethod = authChildCheckMethod;
        copy.authEntryClass = authEntryClass;
        copy.authEntryMethod = authEntryMethod;
        copy.pureClass = pureClass;
        copy.pureStatusMethod = pureStatusMethod;
        copy.adManagerClass = adManagerClass;
        copy.adManagerMethod = adManagerMethod;
        copy.storeClass = storeClass;
        copy.anonymousClass = anonymousClass;
        copy.unknownSourceClass = unknownSourceClass;
        copy.orientationClass = orientationClass;
        return copy;
    }
}
