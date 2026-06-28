package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class IceEffect implements KillEffect {
    @Override
    public String getName() { return "Ice"; }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = (int) (15 * particleScale);
        world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, 
            entity.posX, entity.posY + 0.5, entity.posZ, 
            0, 0, 0, count);
    }

    @Override
    public boolean isAllowedInTier(PerformanceTier tier) {
        return tier != PerformanceTier.POTATO;
    }
}
