package com.hook.packageinstaller.profiles;

public final class Harmony30Profile extends BaseProfile {
    public Harmony30Profile() {
        super("PI_3X", "HarmonyOS 3.0 安装器", "12.7.",
                1_207_000_000L, 1_207_999_999L,
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
        c.authEntryClass = "at";
        c.authEntryMethod = "a";
        c.pureClass = "cv";
        c.pureStatusMethod = "a";
        c.adManagerClass = "au";
        c.adManagerMethod = "a";
        c.storeClass = "ck";
        c.anonymousClass = "ei";
        c.unknownSourceClass = "em";
        c.orientationClass = "dd";
        return c;
    }
}
