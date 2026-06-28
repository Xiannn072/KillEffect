package io.github.gbui.bloodkilleffect;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EffectRegistry {
    private static final Map<String, KillEffect> effects = new LinkedHashMap<>();
    private static final Random random = new Random();
    private static final Logger logger = LogManager.getLogger("KillEffect|Registry");

    public static void register(KillEffect effect) {
        String key = effect.getName().toLowerCase();
        effects.put(key, effect);
        logger.info("Registered kill effect: {}", key);
    }

    public static KillEffect get(String name) {
        if (name == null || name.isEmpty()) return null;
        return effects.get(name.toLowerCase());
    }

    public static List<KillEffect> getAll() {
        return new ArrayList<>(effects.values());
    }

    /**
     * Returns all registered effect names in lowercase (useful for tab completion).
     */
    public static List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        for (KillEffect effect : effects.values()) {
            names.add(effect.getName().toLowerCase());
        }
        return names;
    }

    public static List<KillEffect> getAllowed(PerformanceTier tier) {
        List<KillEffect> allowed = new ArrayList<>();
        for (KillEffect effect : effects.values()) {
            if (effect.isAllowedInTier(tier)) {
                allowed.add(effect);
            }
        }
        return allowed;
    }

    public static KillEffect getRandomAllowed(PerformanceTier tier) {
        List<KillEffect> allowed = getAllowed(tier);
        if (allowed.isEmpty()) return null;
        return allowed.get(random.nextInt(allowed.size()));
    }

    /**
     * Returns the selected effect if valid and tier-compatible, otherwise falls back
     * to random mode. Logs a warning when falling back.
     *
     * @param selectedName the configured effect name
     * @param randomMode   whether random mode is enabled
     * @param tier         the current performance tier
     * @return a valid KillEffect, or null if none are available
     */
    public static KillEffect getSelectedOrRandom(String selectedName, boolean randomMode, PerformanceTier tier) {
        if (!randomMode) {
            KillEffect effect = get(selectedName);
            if (effect != null && effect.isAllowedInTier(tier)) {
                return effect;
            }
            logger.warn("Selected effect '{}' not available for tier '{}', falling back to random",
                selectedName, tier.getDisplayName());
        }
        return getRandomAllowed(tier);
    }
}
