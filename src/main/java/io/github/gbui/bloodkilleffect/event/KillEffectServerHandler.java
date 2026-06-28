package io.github.gbui.bloodkilleffect.event;

import io.github.gbui.bloodkilleffect.config.ConfigManager;
import io.github.gbui.bloodkilleffect.network.KillEffectMessage;
import io.github.gbui.bloodkilleffect.network.KillEffectNetwork;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Server-side event handler. Listens for entity deaths, determines the killer,
 * and sends a network packet to all nearby clients to trigger the kill effect.
 *
 * This class MUST NOT reference any client-only classes (Minecraft, etc.)
 * because it is registered on both physical sides via CommonProxy.
 */
public class KillEffectServerHandler {

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        ConfigManager cfg = ConfigManager.get();
        if (!cfg.isEnabled()) return;

        EntityLivingBase dead = event.entityLiving;
        if (dead == null) return;

        // Guard: only process on the server side
        if (dead.worldObj.isRemote) return;

        // playersOnly: only show effects when a player dies
        if (cfg.isPlayersOnly() && !(dead instanceof EntityPlayer)) return;

        // Resolve the killer from the damage source
        DamageSource source = event.source;
        EntityPlayer killer = resolveKiller(source);

        // pvpOnly: killer must be a player, victim must be a player, exclude self-death
        if (cfg.isPvpOnly()) {
            if (!(dead instanceof EntityPlayer)) return;
            if (killer == dead) return;
            if (killer == null) return;
        }

        // killedByPlayerOnly: a player must be the killer
        if (cfg.isKilledByPlayerOnly()) {
            if (killer == null) return;
        }

        // Send packet to all players tracking this entity
        KillEffectMessage pkt = new KillEffectMessage(
            dead.posX, dead.posY, dead.posZ, dead.getEntityId());

        for (Object obj : dead.worldObj.playerEntities) {
            EntityPlayerMP player = (EntityPlayerMP) obj;
            // Only send to players within 128 blocks to avoid unnecessary network traffic
            double distanceSq = player.getDistanceSq(dead.posX, dead.posY, dead.posZ);
            if (distanceSq < 128 * 128) {
                KillEffectNetwork.INSTANCE.sendTo(pkt, player);
            }
        }
    }

    /**
     * Resolve the player killer from a damage source.
     * Checks both direct source and responsible entity.
     */
    private EntityPlayer resolveKiller(DamageSource source) {
        if (source == null) return null;

        // Direct source (e.g., the player's fist)
        if (source.getSourceOfDamage() instanceof EntityPlayer) {
            return (EntityPlayer) source.getSourceOfDamage();
        }
        // Responsible entity (e.g., arrow shooter)
        if (source.getEntity() instanceof EntityPlayer) {
            return (EntityPlayer) source.getEntity();
        }
        return null;
    }
}
