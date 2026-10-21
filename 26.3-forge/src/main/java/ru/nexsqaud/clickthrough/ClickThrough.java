package ru.nexsqaud.clickthrough;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ClickThrough.MODID)
public class ClickThrough {
    public static final String MODID = "clickthrough";
    public static final Logger logger = LogManager.getLogger(MODID);

    public static boolean isDyeOnSign = false;

    public ClickThrough(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, Configuration.CLIENT_SPEC);
    }
}