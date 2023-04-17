package ru.nexsqaud.clickthrough;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@Mod(ClickThrough.MODID)
public class ClickThrough {
    public static final String MODID = "clickthrough";
    public static final Logger logger = LogManager.getLogger(MODID);

    public static boolean isDyeOnSign = false;

    public ClickThrough() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
    }
}