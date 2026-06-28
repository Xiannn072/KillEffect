package io.github.gbui.bloodkilleffect;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface KillEffect {
    String getName();
    void playEffect(World world, Entity entity, float particleScale);
    boolean isAllowedInTier(PerformanceTier tier);
}
