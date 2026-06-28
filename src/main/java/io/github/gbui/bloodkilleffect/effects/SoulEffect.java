package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SoulEffect implements KillEffect {
    @Override
    public String getName() { return "Soul"; }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = (int) (12 * particleScale);
        world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, 
            entity.posX, entity.posY + 1, entity.posZ, 
            0, 0, 0, count);
    }

    @Override
    public boolean isAllowedInTier(PerformanceTier tier) {
        return tier != PerformanceTier.POTATO;
    }
}
