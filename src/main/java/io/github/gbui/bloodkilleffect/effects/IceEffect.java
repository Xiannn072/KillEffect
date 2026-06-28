package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class IceEffect extends BaseKillEffect {
    public IceEffect() {
        super("Ice", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, double x, double y, double z, float particleScale) {
        int count = scaledCount(15, particleScale);
        double baseY = y + 0.5;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(0.5);
            double dy = randomOffset(0.5);
            double dz = randomOffset(0.5);
            // Snow falls downward with slight random drift
            world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL,
                x + dx, baseY + dy + 1, z + dz,
                dx * 0.1, dy * 0.1 - 0.05, dz * 0.1);
        }
    }
}
