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

@SideOnly(Side.CLIENT)
public class KillEffectClientHandler {

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        ConfigManager cfg = ConfigManager.get();
        if (!cfg.isEnabled()) return;

        EntityLivingBase dead = event.entityLiving;
        if (dead == null) return;

        World world = dead.worldObj;
        if (world == null || !world.isRemote) return;

        if (cfg.isPlayersOnly() && !(dead instanceof EntityPlayer)) return;

        EntityPlayer localPlayer = Minecraft.getMinecraft().thePlayer;
        if (localPlayer == null) return;

        DamageSource source = event.source;
        EntityPlayer killer = resolveKiller(source);

        if (cfg.isPvpOnly()) {
            if (!(dead instanceof EntityPlayer)) return;
            if (dead == localPlayer) return;
            if (killer == null) return;
        }

        if (cfg.isKilledByPlayerOnly()) {
            if (killer == null || killer != localPlayer) return;
        }

        playEffectAt(world, dead.posX, dead.posY, dead.posZ);
    }

    public static void handleServerPacket(double x, double y, double z, int entityId) {
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
