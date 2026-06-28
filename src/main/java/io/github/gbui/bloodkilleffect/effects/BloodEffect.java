package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class BloodEffect extends BaseKillEffect {
    public BloodEffect() {
        super("Blood", PerformanceTier.POTATO);
    }

    @Override
    public void playEffect(World world, double x, double y, double z, float particleScale) {
        int count = scaledCount(8, particleScale);
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            // Randomize each BlockPos to avoid sound/particle spam at the same location
            BlockPos pos = new BlockPos(
                x + (rand.nextDouble() - 0.5),
                y + (rand.nextDouble() - 0.5),
                z + (rand.nextDouble() - 0.5));
            world.playAuxSFX(2001, pos, 152);
        }
    }
}
