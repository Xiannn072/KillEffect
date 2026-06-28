package io.github.gbui.bloodkilleffect.config;

import io.github.gbui.bloodkilleffect.BloodKillEffectMod;
import io.github.gbui.bloodkilleffect.EffectRegistry;
import io.github.gbui.bloodkilleffect.KillEffect;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraftforge.common.config.Configuration;

/**
 * Singleton configuration manager. Centralizes all config reads/writes
 * so that {@link BloodKillEffectMod} stays thin.
 */
public class ConfigManager {
    private static ConfigManager instance;

    private Configuration config;

    // Cached values (mirrors fields on BloodKillEffectMod for backward compat)
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
                config.getString("tier", CATEGORY_PERFORMANCE, "Balanced",
                    "Performance tier",
                    new String[]{"Potato", "Balanced", "High Quality", "Ultra"}));

        // Validate selected effect against registry and tier
        if (!randomMode) {
            KillEffect effect = EffectRegistry.get(selectedEffect);
            if (effect == null || !effect.isAllowedInTier(performanceTier)) {
                BloodKillEffectMod.logger.warn("Config: effect '{}' not available, switching to random mode", selectedEffect);
                randomMode = true;
                syncToMod();
                save();
                return;
            }
        }

        syncToMod();

        if (config.hasChanged()) {
            config.save();
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────

    public void save() {
        if (config == null) return;
        syncFromMod();
        if (config.hasChanged()) {
            config.save();
        }
    }

    // ── Individual setters (update mod + config + save) ───────────────────

    public void setEnabled(boolean val) {
        this.enabled = val;
        BloodKillEffectMod.enabled = val;
        config.get(Configuration.CATEGORY_GENERAL, "enabled", true).set(val);
        save();
    }

    public void setPlayersOnly(boolean val) {
        this.playersOnly = val;
        BloodKillEffectMod.playersOnly = val;
        config.get(Configuration.CATEGORY_GENERAL, "playersOnly", true).set(val);
        save();
    }

    public void setKilledByPlayerOnly(boolean val) {
        this.killedByPlayerOnly = val;
        BloodKillEffectMod.killedByPlayerOnly = val;
        config.get(Configuration.CATEGORY_GENERAL, "killedByPlayerOnly", false).set(val);
        save();
    }

    public void setPvpOnly(boolean val) {
        this.pvpOnly = val;
        BloodKillEffectMod.pvpOnly = val;
        config.get(Configuration.CATEGORY_GENERAL, "pvpOnly", false).set(val);
        save();
    }

    public void setRandomMode(boolean val) {
        this.randomMode = val;
        BloodKillEffectMod.randomMode = val;
        config.get(CATEGORY_EFFECTS, "randomMode", true).set(val);
        save();
    }

    public void setSelectedEffect(String name) {
        this.selectedEffect = name;
        BloodKillEffectMod.selectedEffect = name;
        config.get(CATEGORY_EFFECTS, "selectedEffect", "Blood").set(name);
        save();
    }

    public void setPerformanceTier(PerformanceTier tier) {
        this.performanceTier = tier;
        BloodKillEffectMod.performanceTier = tier;
        config.get(CATEGORY_PERFORMANCE, "tier", "Balanced").set(tier.name());
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

    // ── Internal helpers ──────────────────────────────────────────────────

    /** Push cached values into BloodKillEffectMod static fields. */
    private void syncToMod() {
        BloodKillEffectMod.enabled          = enabled;
        BloodKillEffectMod.playersOnly      = playersOnly;
        BloodKillEffectMod.killedByPlayerOnly = killedByPlayerOnly;
        BloodKillEffectMod.pvpOnly          = pvpOnly;
        BloodKillEffectMod.randomMode       = randomMode;
        BloodKillEffectMod.selectedEffect   = selectedEffect;
        BloodKillEffectMod.performanceTier  = performanceTier;
    }

    /** Read current values from BloodKillEffectMod static fields back into cache. */
    private void syncFromMod() {
        enabled          = BloodKillEffectMod.enabled;
        playersOnly      = BloodKillEffectMod.playersOnly;
        killedByPlayerOnly = BloodKillEffectMod.killedByPlayerOnly;
        pvpOnly          = BloodKillEffectMod.pvpOnly;
        randomMode       = BloodKillEffectMod.randomMode;
        selectedEffect   = BloodKillEffectMod.selectedEffect;
        performanceTier  = BloodKillEffectMod.performanceTier;
    }

    // ── Category constants ────────────────────────────────────────────────
    private static final String CATEGORY_EFFECTS     = "Effects";
    private static final String CATEGORY_PERFORMANCE = "Performance";
}
