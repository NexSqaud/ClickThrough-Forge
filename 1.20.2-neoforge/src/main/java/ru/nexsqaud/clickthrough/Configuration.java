package ru.nexsqaud.clickthrough;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.neoforge.common.ModConfigSpec;

// TODO: GUI handler for configuration menu

public final class Configuration {
    public static boolean needsSneakToDye;
    public static boolean onlyToContainers;

    public static final ModConfigSpec CLIENT_SPEC;

    final ModConfigSpec.BooleanValue sneakToDyeSigns;
    final ModConfigSpec.BooleanValue onlyContainers;

    Configuration(final ModConfigSpec.Builder builder) {
        builder.push("general");
        sneakToDyeSigns = builder.define("sneakToDyeSigns", false);
        onlyContainers = builder.define("onlyContainers", true);
        builder.pop();
    }

    static {
        final Pair<Configuration, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Configuration::new);
        CLIENT_SPEC = pair.getRight();

        var client = pair.getLeft();
        Configuration.needsSneakToDye = client.sneakToDyeSigns.get();
        Configuration.onlyToContainers = client.onlyContainers.get();
    }

}
