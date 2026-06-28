package io.github.gbui.bloodkilleffect;

import io.github.gbui.bloodkilleffect.effects.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = BloodKillEffectMod.MODID, name = BloodKillEffectMod.MODNAME, 
     version = BloodKillEffectMod.VERSION, acceptedMinecraftVersions = "[1.8.9]",
     guiFactory = "io.github.gbui.bloodkilleffect.BKEGuiFactory")
public class BloodKillEffectMod {
    public static final String MODID = "BloodKillEffect";
    public static final String MODNAME = "KillEffect";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static BloodKillEffectMod instance;

    public static Configuration config;
    public static boolean enabled = true;
    public static boolean playersOnly = true;
    public static boolean killedByPlayerOnly = false;
    public static boolean pvpOnly = false;
    public static boolean randomMode = true;
    public static String selectedEffect = "Blood";
    public static PerformanceTier performanceTier = PerformanceTier.BALANCED;

    private final Map<Integer, Integer> recentAttacks = new HashMap<>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
        registerEffects();
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
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new KillEffectCommand());
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new KillEffectCommand());
    }

    private void syncConfig() {
        config.load();
        enabled = config.getBoolean("enabled", Configuration.CATEGORY_GENERAL, 
            true, "Enable kill effects");
        playersOnly = config.getBoolean("playersOnly", Configuration.CATEGORY_GENERAL, 
            true, "Only show effects for player deaths");
        killedByPlayerOnly = config.getBoolean("killedByPlayerOnly", Configuration.CATEGORY_GENERAL, 
            false, "Only show effects when the local player is the killer");
        pvpOnly = config.getBoolean("pvpOnly", Configuration.CATEGORY_GENERAL, 
            false, "Only show effects for player-vs-player kills");
        randomMode = config.getBoolean("randomMode", "Effects", 
            true, "Randomly select from enabled effects");
        selectedEffect = config.getString("selectedEffect", "Effects", 
            "Blood", "Selected effect name", 
            EffectRegistry.getAll().stream().map(KillEffect::getName).toArray(String[]::new));
        performanceTier = PerformanceTier.fromString(
            config.getString("tier", "Performance", "Balanced", 
                "Performance tier", 
                new String[]{"Potato", "Balanced", "High Quality", "Ultra"}));
        
        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.entityPlayer == Minecraft.getMinecraft().thePlayer) {
            recentAttacks.put(event.target.getEntityId(), 20);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!enabled) return;

        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return;

        for (Map.Entry<Integer, Integer> entry : recentAttacks.entrySet()) {
            if (entry.getValue() > 0) {
                entry.setValue(entry.getValue() - 1);
            }
        }
        recentAttacks.entrySet().removeIf(e -> e.getValue() <= 0);

        EntityPlayer localPlayer = Minecraft.getMinecraft().thePlayer;

        for (Object obj : world.loadedEntityList) {
            if (!(obj instanceof EntityLivingBase)) continue;
            EntityLivingBase entity = (EntityLivingBase) obj;

            if (playersOnly && !(entity instanceof EntityPlayer)) {
                continue;
            }

            if (entity.deathTime == 1) {
                if (killedByPlayerOnly && !recentAttacks.containsKey(entity.getEntityId())) {
                    continue;
                }
                if (pvpOnly && !(entity instanceof EntityPlayer)) {
                    continue;
                }
                if (pvpOnly && localPlayer != null && entity == localPlayer) {
                    continue;
                }
                playKillEffect(world, entity);
            }
        }
    }

    private void playKillEffect(World world, Entity entity) {
        KillEffect effect;
        if (randomMode) {
            effect = EffectRegistry.getRandomAllowed(performanceTier);
        } else {
            effect = EffectRegistry.get(selectedEffect);
        }

        if (effect != null && effect.isAllowedInTier(performanceTier)) {
            effect.playEffect(world, entity, performanceTier.getParticleScale());
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        config.get(Configuration.CATEGORY_GENERAL, "enabled", true).set(enabled);
        config.save();
    }

    public void setPlayersOnly(boolean playersOnly) {
        this.playersOnly = playersOnly;
        config.get(Configuration.CATEGORY_GENERAL, "playersOnly", true).set(playersOnly);
        config.save();
    }

    public void setKilledByPlayerOnly(boolean killedByPlayerOnly) {
        this.killedByPlayerOnly = killedByPlayerOnly;
        config.get(Configuration.CATEGORY_GENERAL, "killedByPlayerOnly", false).set(killedByPlayerOnly);
        config.save();
    }

    public void setPvpOnly(boolean pvpOnly) {
        this.pvpOnly = pvpOnly;
        config.get(Configuration.CATEGORY_GENERAL, "pvpOnly", false).set(pvpOnly);
        config.save();
    }

    public void setRandomMode(boolean randomMode) {
        this.randomMode = randomMode;
        config.get("Effects", "randomMode", true).set(randomMode);
        config.save();
    }

    public void setSelectedEffect(String effectName) {
        this.selectedEffect = effectName;
        config.get("Effects", "selectedEffect", "Blood").set(effectName);
        config.save();
    }

    public void setPerformanceTier(PerformanceTier tier) {
        this.performanceTier = tier;
        config.get("Performance", "tier", "Balanced").set(tier.name());
        config.save();
    }
}
