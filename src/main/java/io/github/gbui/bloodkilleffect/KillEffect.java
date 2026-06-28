package io.github.gbui.bloodkilleffect;

import net.minecraft.world.World;

public interface KillEffect {
    String getName();

    /**
     * Play the kill effect at the given world coordinates.
     *
     * @param world        the world
     * @param x            entity x position
     * @param y            entity y position
     * @param z            entity z position
     * @param particleScale multiplier from performance tier
     */
    void playEffect(World world, double x, double y, double z, float particleScale);

    boolean isAllowedInTier(PerformanceTier tier);
}
