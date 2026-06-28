package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SmokeEffect implements KillEffect {
    @Override
    public String getName() { return "Smoke"; }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = (int) (15 * particleScale);
        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, 
            entity.posX, entity.posY + 1, entity.posZ, 
            0, 0, 0, count);
    }

    @Override
    public boolean isAllowedInTier(PerformanceTier tier) {
        return true;
    }
}
