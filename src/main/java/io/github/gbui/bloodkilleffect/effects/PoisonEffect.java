package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.Random;

public class PoisonEffect extends BaseKillEffect {
    private static final Random RAND = new Random();

    public PoisonEffect() {
        super("Poison", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        int count = scaledCount(18, particleScale);
        double baseX = entity.posX;
        double baseY = entity.posY + 0.5;
        double baseZ = entity.posZ;

        for (int i = 0; i < count; i++) {
            double dx = randomOffset(RAND, 0.4);
            double dy = randomOffset(RAND, 0.4);
            double dz = randomOffset(RAND, 0.4);
            world.spawnParticle(EnumParticleTypes.SPELL_MOB,
                baseX + dx, baseY + dy, baseZ + dz,
                0.1, 0, 0.1);
        }
    }
}
