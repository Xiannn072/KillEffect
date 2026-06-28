package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BloodEffect implements KillEffect {
    @Override
    public String getName() { return "Blood"; }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = (int) (8 * particleScale);
        BlockPos pos = new BlockPos(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        for (int i = 0; i < count; i++) {
            world.playAuxSFX(2001, pos, 152);
        }
    }

    @Override
    public boolean isAllowedInTier(PerformanceTier tier) {
        return true;
    }
}
