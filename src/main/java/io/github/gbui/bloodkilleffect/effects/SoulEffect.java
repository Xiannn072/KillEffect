package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Random;

public class SoulEffect extends BaseKillEffect {
    private static final Random RAND = new Random();

    public SoulEffect() {
        super("Soul", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = scaledCount(12, particleScale);
        double baseX = entity.posX;
        double baseY = entity.posY + 1;
        double baseZ = entity.posZ;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(RAND, 0.4);
            double dy = randomOffset(RAND, 0.4);
            double dz = randomOffset(RAND, 0.4);
            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE,
                baseX + dx, baseY + dy, baseZ + dz,
                0, 0.05, 0);
        }
    }
}
