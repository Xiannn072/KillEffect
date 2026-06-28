package io.github.gbui.bloodkilleffect;

import io.github.gbui.bloodkilleffect.config.ConfigManager;
import io.github.gbui.bloodkilleffect.effects.*;
import io.github.gbui.bloodkilleffect.network.KillEffectNetwork;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BloodKillEffectMod.MODID, name = BloodKillEffectMod.MODNAME,
     version = BloodKillEffectMod.VERSION, acceptedMinecraftVersions = "[1.8.9]",
     guiFactory = "io.github.gbui.bloodkilleffect.BKEGuiFactory")
public class BloodKillEffectMod {
    public static final String MODID = "BloodKillEffect";
    public static final String MODNAME = "KillEffect";
    public static final String VERSION = "1.0.0";

    public static final Logger logger = LogManager.getLogger(MODNAME);

    @Mod.Instance(MODID)
    public static BloodKillEffectMod instance;

    @SidedProxy(
        clientSide = "io.github.gbui.bloodkilleffect.ClientProxy",
        serverSide = "io.github.gbui.bloodkilleffect.CommonProxy")
    public static CommonProxy proxy;

    public static Configuration config;

    // ── Lifecycle ─────────────────────────────────────────────────────────

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("KillEffect pre-init starting");

        config = new Configuration(event.getSuggestedConfigurationFile());
        registerEffects();
        EffectRegistry.buildCaches();
        ConfigManager.get().load(config);

        // Register network channel on both physical sides
        KillEffectNetwork.register();

        ConfigManager cfg = ConfigManager.get();
        logger.info("KillEffect config loaded: enabled={}, tier={}, randomMode={}",
            cfg.isEnabled(), cfg.getPerformanceTier().getDisplayName(), cfg.isRandomMode());
    }

    private void registerEffects() {
        EffectRegistry.register(new BloodEffect());
        EffectRegistry.register(new LightningEffect());
        EffectRegistry.register(new FireBurstEffect());
        EffectRegistry.register(new SmokeEffect());
        EffectRegistry.register(new SoulEffect());
        EffectRegistry.register(new ExplosionEffect());
        EffectRegistry.register(new PoisonEffect());
        EffectRegistry.register(new IceEffect());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        logger.info("KillEffect initialized");
    }
}
