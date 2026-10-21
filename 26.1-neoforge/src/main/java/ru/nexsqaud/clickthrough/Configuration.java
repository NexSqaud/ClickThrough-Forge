package ru.nexsqaud.clickthrough;

import net.neoforged.neoforge.common.ModConfigSpec;

// TODO: GUI handler for configuration menu

public final class Configuration {
    public static final ModConfigSpec.BooleanValue NEEDS_SNEAK_TO_DYE;
    public static final ModConfigSpec.BooleanValue ONLY_TO_CONTAINERS;

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    static {
        BUILDER.push("general");
        NEEDS_SNEAK_TO_DYE = BUILDER.define("sneakToDyeSigns", false);
        ONLY_TO_CONTAINERS = BUILDER.define("onlyContainers", true);
        BUILDER.pop();
    }
    public static final ModConfigSpec CLIENT_SPEC = BUILDER.build();

}
