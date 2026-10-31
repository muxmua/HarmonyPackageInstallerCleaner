package com.hook.packageinstaller.profiles;

public final class Harmony20Profile extends BaseProfile {
    public Harmony20Profile() {
        super("PI_2X", "HarmonyOS 2.0 安装器", "11.5.",
                1_105_000_000L, 1_105_999_999L,
                createClasses());
    }

    private static ProfileClasses createClasses() {
        ProfileClasses c = new ProfileClasses();
        c.stagingRiskClass = null;
        c.piaRiskMethod = "o";
        c.piaRiskHasBooleanArg = false;
        c.piaSetViewMethod = "b";
        c.authChildClass = "ci";
        c.authChildCheckMethod = "a";
        c.authEntryClass = "al";
        c.authEntryMethod = "a";
        c.pureClass = "cn";
        c.pureStatusMethod = "a";
        c.adManagerClass = "am";
        c.adManagerMethod = "a";
        c.storeClass = "cb";
        c.anonymousClass = "dz";
        c.unknownSourceClass = "ed";
        c.orientationClass = "cv";
        return c;
    }
}
