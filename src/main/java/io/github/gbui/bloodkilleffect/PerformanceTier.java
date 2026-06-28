package io.github.gbui.bloodkilleffect;

public enum PerformanceTier {
    POTATO("Potato", 0.25f),
    BALANCED("Balanced", 1.0f),
    HIGH("High Quality", 2.0f),
    ULTRA("Ultra", 4.0f);

    private final String displayName;
    private final float particleScale;

    PerformanceTier(String displayName, float particleScale) {
        this.displayName = displayName;
        this.particleScale = particleScale;
    }

    public String getDisplayName() { return displayName; }
    public float getParticleScale() { return particleScale; }

    public static PerformanceTier fromString(String name) {
        for (PerformanceTier tier : values()) {
            if (tier.name().equalsIgnoreCase(name) || 
                tier.displayName.equalsIgnoreCase(name)) {
                return tier;
            }
        }
        return BALANCED;
    }
}
