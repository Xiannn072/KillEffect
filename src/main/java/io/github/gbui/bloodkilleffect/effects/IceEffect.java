package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Random;

public class IceEffect extends BaseKillEffect {
    private static final Random RAND = new Random();

    public IceEffect() {
        super("Ice", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = scaledCount(15, particleScale);
        double baseX = entity.posX;
        double baseY = entity.posY + 0.5;
        double baseZ = entity.posZ;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(RAND, 0.5);
            double dy = randomOffset(RAND, 0.5);
            double dz = randomOffset(RAND, 0.5);
            world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL,
                baseX + dx, baseY + dy + 1, baseZ + dz,
                0, -0.05, 0);
        }
    }
}
