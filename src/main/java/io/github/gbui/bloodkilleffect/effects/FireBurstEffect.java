package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class FireBurstEffect extends BaseKillEffect {
    public FireBurstEffect() {
        super("Fire", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, double x, double y, double z, float particleScale) {
        int count = scaledCount(20, particleScale);
        double baseY = y + 0.5;
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(0.5);
            double dy = randomOffset(0.5);
            double dz = randomOffset(0.5);
            // Use random offsets as velocity for a natural burst spread
            world.spawnParticle(EnumParticleTypes.FLAME,
                x + dx, baseY + dy, z + dz,
                dx * 0.2, dy * 0.2 + 0.05, dz * 0.2);
        }
    }
}
