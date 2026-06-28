package io.github.gbui.bloodkilleffect.event;

import io.github.gbui.bloodkilleffect.BloodKillEffectMod;
import io.github.gbui.bloodkilleffect.EffectRegistry;
import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Client-side event handler. Provides two paths for effect playback:
 *
 * 1. <b>Direct (singleplayer/LAN host):</b> The client resolves the killer from
 *    {@link LivingDeathEvent#getSource()} and plays the effect immediately.
 *    This works because in singleplayer/LAN the server and client share the same JVM,
 *    so the DamageSource is accurate.
 *
 * 2. <b>Network (dedicated server):</b> The server sends a packet via
 *    {@link io.github.gbui.bloodkilleffect.network.KillEffectMessage};
 *    {@link io.github.gbui.bloodkilleffect.network.KillEffectMessage.Handler}
 *    calls {@link #handleServerPacket} to play the effect.
 *    The {@code playersOnly} filter is applied here; other filters (pvpOnly,
 *    killedByPlayerOnly) are applied server-side before sending the packet.
 *
 * This class IS client-only and is registered ONLY via {@link io.github.gbui.bloodkilleffect.ClientProxy}.
 */
@SideOnly(Side.CLIENT)
public class KillEffectClientHandler {

    /**
     * Direct path: client-side prediction for singleplayer / LAN host.
     * Works because in these modes the DamageSource on the client is accurate.
     */
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        ConfigManager cfg = ConfigManager.get();
        if (!cfg.isEnabled()) return;

        EntityLivingBase dead = event.entityLiving;
        if (dead == null) return;

        World world = dead.worldObj;
        if (world == null || !world.isRemote) return;

        // playersOnly: only show effects when a player dies
        if (cfg.isPlayersOnly() && !(dead instanceof EntityPlayer)) return;

        EntityPlayer localPlayer = Minecraft.getMinecraft().thePlayer;
        if (localPlayer == null) return;

        // Resolve killer locally (accurate in singleplayer/LAN host)
        DamageSource source = event.source;
        EntityPlayer killer = resolveKiller(source);

        // pvpOnly: victim and killer must be players, exclude self-death
        if (cfg.isPvpOnly()) {
            if (!(dead instanceof EntityPlayer)) return;
            if (dead == localPlayer) return;
            if (killer == null) return;
        }

        // killedByPlayerOnly: local player must be the killer
        if (cfg.isKilledByPlayerOnly()) {
            if (killer == null || killer != localPlayer) return;
        }

        // If we got here, the local client prediction says this kill qualifies
        playEffectAt(world, dead.posX, dead.posY, dead.posZ);
    }

    /**
     * Network path: called from the network handler when a packet arrives from the server.
     * The server has already applied pvpOnly / killedByPlayerOnly filters, so we
     * only need to play the effect at the given coordinates.
     */
    public static void handleServerPacket(double x, double y, double z) {
        ConfigManager cfg = ConfigManager.get();
        if (!cfg.isEnabled()) return;

        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return;

        playEffectAt(world, x, y, z);
    }

    private static void playEffectAt(World world, double x, double y, double z) {
        ConfigManager cfg = ConfigManager.get();
        KillEffect effect = EffectRegistry.getSelectedOrRandom(
            cfg.getSelectedEffect(),
            cfg.isRandomMode(),
            cfg.getPerformanceTier());

        if (effect != null) {
            try {
                effect.playEffect(world, x, y, z,
                    cfg.getPerformanceTier().getParticleScale());
            } catch (Exception e) {
                BloodKillEffectMod.logger.error("Failed to play kill effect '{}'",
                    effect.getName(), e);
            }
        }
    }

    private EntityPlayer resolveKiller(DamageSource source) {
        if (source == null) return null;
        if (source.getSourceOfDamage() instanceof EntityPlayer) {
            return (EntityPlayer) source.getSourceOfDamage();
        }
        if (source.getEntity() instanceof EntityPlayer) {
            return (EntityPlayer) source.getEntity();
        }
        return null;
    }
}
