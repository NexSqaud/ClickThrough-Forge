package ru.nexsqaud.clickthrough;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModLoadingContext;

@Mod(ClickThrough.MODID)
public class ClickThrough {
    public static final String MODID = "clickthrough";
    public static final Logger logger = LogManager.getLogger(MODID);

    public static boolean isDyeOnSign = false;

    public ClickThrough() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
    }
}