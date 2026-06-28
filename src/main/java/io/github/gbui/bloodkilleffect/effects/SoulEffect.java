package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class SoulEffect extends BaseKillEffect {
    public SoulEffect() {
        super("Soul", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, double x, double y, double z, float particleScale) {
        int count = scaledCount(12, particleScale);
        double baseY = y + 1;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(0.4);
            double dy = randomOffset(0.4);
            double dz = randomOffset(0.4);
            // Soul particles drift upward and swirl
            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
                x + dx, baseY + dy, z + dz,
                dx * 0.15, dy * 0.15 + 0.05, dz * 0.15);
        }
    }
}
