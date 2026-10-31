package com.hook.packageinstaller.profiles;

public final class Harmony40Profile extends BaseProfile {
    public Harmony40Profile() {
        super("PI_40", "HarmonyOS 4.0 安装器", "13.2.",
                1_302_000_000L, 1_302_999_999L,
                createClasses());
    }

    private static ProfileClasses createClasses() {
        ProfileClasses c = new ProfileClasses();
        c.stagingRiskClass = "aq";
        c.stagingRiskMethod = "h";
        c.stagingContinueMethod = "a";
        c.piaRiskMethod = "a";
        c.piaRiskHasBooleanArg = true;
        c.piaSetViewMethod = "b";
        c.authChildClass = "cr";
        c.authChildCheckMethod = "a";
        c.authEntryClass = "dt";
        c.authEntryMethod = "a";
        c.pureClass = "cv";
        c.pureStatusMethod = "a";
        c.adManagerClass = "at";
        c.adManagerMethod = "a";
        c.storeClass = "ck";
        c.anonymousClass = "ek";
        c.unknownSourceClass = "eo";
        c.orientationClass = "dd";
        return c;
    }
}
