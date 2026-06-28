package io.github.gbui.bloodkilleffect.config;

import io.github.gbui.bloodkilleffect.BloodKillEffectMod;
import io.github.gbui.bloodkilleffect.EffectRegistry;
import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraftforge.common.config.Configuration;

/**
 * Singleton configuration manager. THE single source of truth for all config values.
 * BloodKillEffectMod static fields are removed; all code reads via ConfigManager.get().
 */
public class ConfigManager {
    private static ConfigManager instance;

    private Configuration config;

    private boolean enabled = true;
    private boolean playersOnly = true;
    private boolean killedByPlayerOnly = false;
    private boolean pvpOnly = false;
    private boolean randomMode = true;
    private String selectedEffect = "Blood";
    private PerformanceTier performanceTier = PerformanceTier.BALANCED;

    private ConfigManager() {}

    public static ConfigManager get() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    // ── Load ──────────────────────────────────────────────────────────────

    public void load(Configuration cfg) {
        this.config = cfg;
        config.load();

        enabled          = config.getBoolean("enabled",     Configuration.CATEGORY_GENERAL, true,  "Enable kill effects");
        playersOnly      = config.getBoolean("playersOnly", Configuration.CATEGORY_GENERAL, true,  "Only show effects for player deaths");
        killedByPlayerOnly = config.getBoolean("killedByPlayerOnly", Configuration.CATEGORY_GENERAL, false, "Only show effects when the local player is the killer");
        pvpOnly          = config.getBoolean("pvpOnly",     Configuration.CATEGORY_GENERAL, false, "Only show effects for player-vs-player kills");

        randomMode       = config.getBoolean("randomMode",    CATEGORY_EFFECTS, true, "Randomly select from enabled effects");
        selectedEffect   = config.getString("selectedEffect", CATEGORY_EFFECTS, "Blood",
                "Selected effect name",
                EffectRegistry.getAll().stream().map(KillEffect::getName).toArray(String[]::new));

        performanceTier  = PerformanceTier.fromString(
                config.getString("tier", CATEGORY_PERFORMANCE, PerformanceTier.BALANCED.name(),
                    "Performance tier",
                    new String[]{PerformanceTier.POTATO.name(), PerformanceTier.BALANCED.name(),
                                 PerformanceTier.HIGH.name(), PerformanceTier.ULTRA.name()}));

        // Validate selected effect against registry and tier
        if (!randomMode) {
            KillEffect effect = EffectRegistry.get(selectedEffect);
            if (effect == null || !effect.isAllowedInTier(performanceTier)) {
                BloodKillEffectMod.logger.warn("Config: effect '{}' not available, switching to random mode", selectedEffect);
                randomMode = true;
            }
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────

    public void save() {
        if (config == null) return;
        if (config.hasChanged()) {
            config.save();
        }
    }

    // ── Individual setters (update cached value + config file + save) ─────

    public void setEnabled(boolean val) {
        this.enabled = val;
        config.get(Configuration.CATEGORY_GENERAL, "enabled", true).set(val);
        save();
    }

    public void setPlayersOnly(boolean val) {
        this.playersOnly = val;
        config.get(Configuration.CATEGORY_GENERAL, "playersOnly", true).set(val);
        save();
    }

    public void setKilledByPlayerOnly(boolean val) {
        this.killedByPlayerOnly = val;
        config.get(Configuration.CATEGORY_GENERAL, "killedByPlayerOnly", false).set(val);
        save();
    }

    public void setPvpOnly(boolean val) {
        this.pvpOnly = val;
        config.get(Configuration.CATEGORY_GENERAL, "pvpOnly", false).set(val);
        save();
    }

    public void setRandomMode(boolean val) {
        this.randomMode = val;
        config.get(CATEGORY_EFFECTS, "randomMode", true).set(val);
        save();
    }

    public void setSelectedEffect(String name) {
        this.selectedEffect = name;
        config.get(CATEGORY_EFFECTS, "selectedEffect", "Blood").set(name);
        save();
    }

    public void setPerformanceTier(PerformanceTier tier) {
        this.performanceTier = tier;
        config.get(CATEGORY_PERFORMANCE, "tier", PerformanceTier.BALANCED.name()).set(tier.name());
        save();
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public Configuration getConfig() { return config; }
    public boolean isEnabled()           { return enabled; }
    public boolean isPlayersOnly()       { return playersOnly; }
    public boolean isKilledByPlayerOnly(){ return killedByPlayerOnly; }
    public boolean isPvpOnly()           { return pvpOnly; }
    public boolean isRandomMode()        { return randomMode; }
    public String  getSelectedEffect()   { return selectedEffect; }
    public PerformanceTier getPerformanceTier() { return performanceTier; }

    // ── Category constants (public for use in BKEGuiConfig) ──────────────
    public static final String CATEGORY_EFFECTS     = "Effects";
    public static final String CATEGORY_PERFORMANCE = "Performance";
}
