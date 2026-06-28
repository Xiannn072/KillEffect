package io.github.gbui.bloodkilleffect;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EffectRegistry {
    private static final Map<String, KillEffect> effects = new LinkedHashMap<>();
    private static final Random random = new Random();

    public static void register(KillEffect effect) {
        effects.put(effect.getName().toLowerCase(), effect);
    }

    public static KillEffect get(String name) {
        return effects.get(name.toLowerCase());
    }

    public static List<KillEffect> getAll() {
        return new ArrayList<>(effects.values());
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
}
