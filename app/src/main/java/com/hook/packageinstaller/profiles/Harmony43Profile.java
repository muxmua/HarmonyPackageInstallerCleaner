package com.hook.packageinstaller.profiles;

public final class Harmony43Profile extends BaseProfile {
    public Harmony43Profile() {
        super("PI_43", "HarmonyOS 4.3 安装器", "15.2.",
                1_502_000_000L, 1_502_999_999L,
                createClasses());
    }

    private static ProfileClasses createClasses() {
        ProfileClasses c = new ProfileClasses();
        c.stagingRiskClass = "t4";
        c.stagingRiskMethod = "g1";
        c.stagingContinueMethod = "i0";
        c.piaRiskMethod = "N0";
        c.piaRiskHasBooleanArg = true;
        c.piaSetViewMethod = "B0";
        c.authChildClass = "w6";
        c.authChildCheckMethod = "d";
        c.authEntryClass = "ra";
        c.authEntryMethod = "l";
        c.pureClass = "i7";
        c.pureStatusMethod = "d";
        c.adManagerClass = "j";
        c.adManagerMethod = "j";
        c.storeClass = "z2";
        c.anonymousClass = "n";
        c.unknownSourceClass = "f2";
        c.orientationClass = "x8";
        return c;
    }
}
