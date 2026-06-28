package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Random;

public class ExplosionEffect extends BaseKillEffect {
    private static final Random RAND = new Random();

    public ExplosionEffect() {
        super("Explosion", PerformanceTier.HIGH);
    }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = scaledCount(30, particleScale);
        double baseX = entity.posX;
        double baseY = entity.posY + 0.5;
        double baseZ = entity.posZ;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(RAND, 0.6);
            double dy = randomOffset(RAND, 0.6);
            double dz = randomOffset(RAND, 0.6);
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE,
                baseX + dx, baseY + dy, baseZ + dz,
                0, 0, 0);
        }
    }
}
