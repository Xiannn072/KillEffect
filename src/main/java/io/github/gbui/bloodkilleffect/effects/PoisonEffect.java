package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class PoisonEffect extends BaseKillEffect {
    public PoisonEffect() {
        super("Poison", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, double x, double y, double z, float particleScale) {
        int count = scaledCount(18, particleScale);
        double baseY = y + 0.5;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(0.4);
            double dy = randomOffset(0.4);
            double dz = randomOffset(0.4);
            // Poison particles drift upward with green tint
            world.spawnParticle(EnumParticleTypes.SPELL_MOB,
                x + dx, baseY + dy, z + dz,
                dx * 0.1, dy * 0.1, dz * 0.1);
        }
    }
}
