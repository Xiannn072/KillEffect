package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class FireBurstEffect implements KillEffect {
    @Override
    public String getName() { return "Fire"; }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = (int) (20 * particleScale);
        world.spawnParticle(EnumParticleTypes.FLAME, 
            entity.posX, entity.posY + 0.5, entity.posZ, 
            0, 0, 0, count);
    }

    @Override
    public boolean isAllowedInTier(PerformanceTier tier) {
        return tier != PerformanceTier.POTATO;
    }
}
