package ru.nexsqaud.clickthrough;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;

// TODO: GUI handler for configuration menu

public final class Configuration {
    public static boolean needsSneakToDye;
    public static boolean onlyToContainers;

    public static final ForgeConfigSpec CLIENT_SPEC;

    final ForgeConfigSpec.BooleanValue sneakToDyeSigns;
    final ForgeConfigSpec.BooleanValue onlyContainers;

    Configuration(final ForgeConfigSpec.Builder builder) {
        builder.push("general");
        sneakToDyeSigns = builder.define("sneakToDyeSigns", false);
        onlyContainers = builder.define("onlyContainers", true);
        builder.pop();
    }

    static {
        final Pair<Configuration, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Configuration::new);
        CLIENT_SPEC = pair.getRight();

        var client = pair.getLeft();
        Configuration.needsSneakToDye = client.sneakToDyeSigns.get();
        Configuration.onlyToContainers = client.onlyContainers.get();
    }

}
