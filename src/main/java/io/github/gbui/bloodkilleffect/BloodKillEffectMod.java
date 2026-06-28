package io.github.gbui.bloodkilleffect;

import io.github.gbui.bloodkilleffect.config.ConfigManager;
import io.github.gbui.bloodkilleffect.effects.*;
import io.github.gbui.bloodkilleffect.event.KillEffectEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.client.ClientCommandHandler;

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

    public static Configuration config;

    // ── Shared config state (kept as static fields for backward compat) ───
    public static boolean enabled = true;
    public static boolean playersOnly = true;
    public static boolean killedByPlayerOnly = false;
    public static boolean pvpOnly = false;
    public static boolean randomMode = true;
    public static String selectedEffect = "Blood";
    public static PerformanceTier performanceTier = PerformanceTier.BALANCED;

    // ── Lifecycle ─────────────────────────────────────────────────────────

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("KillEffect pre-init starting");

        config = new Configuration(event.getSuggestedConfigurationFile());
        registerEffects();
        ConfigManager.get().load(config);

        logger.info("KillEffect config loaded: enabled={}, tier={}, randomMode={}",
            enabled, performanceTier.getDisplayName(), randomMode);
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
        MinecraftForge.EVENT_BUS.register(new KillEffectEventHandler());
        ClientCommandHandler.instance.registerCommand(new KillEffectCommand());
        logger.info("KillEffect initialized — event handler registered");
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new KillEffectCommand());
    }

    // ── Convenience setters (delegate to ConfigManager) ───────────────────

    public void setEnabled(boolean val)            { ConfigManager.get().setEnabled(val); }
    public void setPlayersOnly(boolean val)        { ConfigManager.get().setPlayersOnly(val); }
    public void setKilledByPlayerOnly(boolean val) { ConfigManager.get().setKilledByPlayerOnly(val); }
    public void setPvpOnly(boolean val)            { ConfigManager.get().setPvpOnly(val); }
    public void setRandomMode(boolean val)         { ConfigManager.get().setRandomMode(val); }
    public void setSelectedEffect(String name)     { ConfigManager.get().setSelectedEffect(name); }
    public void setPerformanceTier(PerformanceTier t) { ConfigManager.get().setPerformanceTier(t); }
}
