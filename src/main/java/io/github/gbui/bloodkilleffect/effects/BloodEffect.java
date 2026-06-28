package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BloodEffect extends BaseKillEffect {
    public BloodEffect() {
        super("Blood", PerformanceTier.POTATO);
    }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = scaledCount(8, particleScale);
        BlockPos pos = new BlockPos(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
        for (int i = 0; i < count; i++) {
            world.playAuxSFX(2001, pos, 152);
        }
    }
}
