package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ExplosionEffect extends BaseKillEffect {
    public ExplosionEffect() {
        super("Explosion", PerformanceTier.HIGH);
    }

    @Override
    public void playEffect(World world, double x, double y, double z, float particleScale) {
        int count = scaledCount(30, particleScale);
        double baseY = y + 0.5;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(0.6);
            double dy = randomOffset(0.6);
            double dz = randomOffset(0.6);
            // Explosion particles expand outward in all directions
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE,
                x + dx, baseY + dy, z + dz,
                dx * 0.1, dy * 0.1, dz * 0.1);
        }
    }
}
