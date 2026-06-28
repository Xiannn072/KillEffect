package io.github.gbui.bloodkilleffect.event;

import io.github.gbui.bloodkilleffect.BloodKillEffectMod;
import io.github.gbui.bloodkilleffect.EffectRegistry;
import io.github.gbui.bloodkilleffect.KillEffect;
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
 * Event-driven kill effect handler.
 * Replaces the old per-tick entity scanning with O(1) event dispatch.
 */
public class KillEffectEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLivingDeath(LivingDeathEvent event) {
        if (!BloodKillEffectMod.enabled) return;

        EntityLivingBase dead = event.getEntityLiving();
        if (dead == null) return;

        World world = dead.worldObj;
        if (world == null || !world.isRemote) return;

        // playersOnly: only show effects when a player dies
        if (BloodKillEffectMod.playersOnly && !(dead instanceof EntityPlayer)) return;

        EntityPlayer localPlayer = Minecraft.getMinecraft().thePlayer;
        if (localPlayer == null) return;

        // pvpOnly: both victim and killer must be players, exclude self-death
        if (BloodKillEffectMod.pvpOnly) {
            if (!(dead instanceof EntityPlayer)) return;
            if (dead == localPlayer) return;
        }

        // Determine the killer from the damage source
        DamageSource source = event.getSource();
        EntityPlayer killer = resolveKiller(source);

        // killedByPlayerOnly: the local player must be the killer
        if (BloodKillEffectMod.killedByPlayerOnly) {
            if (killer == null || killer != localPlayer) return;
        }

        // Select and play the effect
        KillEffect effect = getEffectToUse();
        if (effect != null) {
            try {
                effect.playEffect(world, dead, BloodKillEffectMod.performanceTier.getParticleScale());
            } catch (Exception e) {
                BloodKillEffectMod.logger.error("Failed to play kill effect '{}': {}",
                    effect.getName(), e.getMessage());
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

    private KillEffect getEffectToUse() {
        return EffectRegistry.getSelectedOrRandom(
            BloodKillEffectMod.selectedEffect,
            BloodKillEffectMod.randomMode,
            BloodKillEffectMod.performanceTier
        );
    }
}
