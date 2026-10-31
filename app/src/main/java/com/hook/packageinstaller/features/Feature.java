package com.hook.packageinstaller.features;

import com.hook.packageinstaller.core.HookContext;

public interface Feature {
    String id();

    void install(HookContext context) throws Throwable;
}
