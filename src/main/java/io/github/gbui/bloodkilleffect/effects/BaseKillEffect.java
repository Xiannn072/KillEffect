package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * Abstract base class for all kill effects.
 * Provides common tier-gating logic and particle-count scaling.
 */
public abstract class BaseKillEffect implements KillEffect {
    protected final String name;
    protected final int minTierOrdinal;

    /**
     * @param name    display name (case-insensitive match in registry)
     * @param minTier the lowest {@link PerformanceTier} that allows this effect
     */
    protected BaseKillEffect(String name, PerformanceTier minTier) {
        this.name = name;
        this.minTierOrdinal = minTier.ordinal();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isAllowedInTier(PerformanceTier tier) {
        return tier.ordinal() >= minTierOrdinal;
    }

    /**
     * Compute the base particle count for a given particleScale.
     * Subclasses use this instead of hard-coding particle numbers.
     *
     * @param baseCount   the particle count at scale 1.0
     * @param particleScale the multiplier from the current performance tier
     * @return scaled count (at least 1)
     */
    protected int scaledCount(int baseCount, float particleScale) {
        return Math.max(1, (int) (baseCount * particleScale));
    }

    /**
     * Utility: random offset around an entity center for more natural particle spread.
     */
    protected double randomOffset(java.util.Random rand, double spread) {
        return (rand.nextDouble() - 0.5) * spread;
    }

    @Override
    public abstract void playEffect(World world, Entity entity, float particleScale);
}
