package com.hook.packageinstaller.profiles;

public final class Harmony42Profile extends BaseProfile {
    public Harmony42Profile() {
        super("PI_42", "HarmonyOS 4.2 安装器", "13.3.",
                1_303_000_000L, 1_303_999_999L,
                createClasses());
    }

    private static ProfileClasses createClasses() {
        ProfileClasses c = new ProfileClasses();
        c.stagingRiskClass = "aq";
        c.stagingRiskMethod = "n";
        c.stagingContinueMethod = "b";
        c.piaRiskMethod = "a";
        c.piaRiskHasBooleanArg = true;
        c.piaSetViewMethod = "b";
        c.authChildClass = "ct";
        c.authChildCheckMethod = "a";
        c.authEntryClass = "dy";
        c.authEntryMethod = "a";
        c.pureClass = "cx";
        c.pureStatusMethod = "a";
        c.adManagerClass = "at";
        c.adManagerMethod = "a";
        c.storeClass = "ck";
        c.anonymousClass = "eq";
        c.unknownSourceClass = "dz";
        c.orientationClass = "df";
        return c;
    }
}
