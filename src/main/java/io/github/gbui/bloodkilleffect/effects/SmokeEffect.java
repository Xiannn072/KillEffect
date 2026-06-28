package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Random;

public class SmokeEffect extends BaseKillEffect {
    private static final Random RAND = new Random();

    public SmokeEffect() {
        super("Smoke", PerformanceTier.POTATO);
    }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = scaledCount(15, particleScale);
        double baseX = entity.posX;
        double baseY = entity.posY + 1;
        double baseZ = entity.posZ;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(RAND, 0.3);
            double dy = randomOffset(RAND, 0.3);
            double dz = randomOffset(RAND, 0.3);
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
                baseX + dx, baseY + dy, baseZ + dz,
                0, 0.02, 0);
        }
    }
}
