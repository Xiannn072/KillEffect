package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SmokeEffect extends BaseKillEffect {
    public SmokeEffect() {
        super("Smoke", PerformanceTier.POTATO);
    }

    @Override
    public void playEffect(World world, double x, double y, double z, float particleScale) {
        int count = scaledCount(15, particleScale);
        double baseY = y + 1;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(0.3);
            double dy = randomOffset(0.3);
            double dz = randomOffset(0.3);
            // Smoke drifts upward with slight random horizontal velocity
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                x + dx, baseY + dy, z + dz,
                dx * 0.1, dy * 0.1 + 0.02, dz * 0.1);
        }
    }
}
