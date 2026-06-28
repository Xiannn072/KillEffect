package io.github.gbui.bloodkilleffect.gui;

import io.github.gbui.bloodkilleffect.BloodKillEffectMod;
import io.github.gbui.bloodkilleffect.EffectRegistry;
import io.github.gbui.bloodkilleffect.PerformanceTier;
import io.github.gbui.bloodkilleffect.config.ConfigManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class BKEGuiConfig extends GuiConfig {

    public BKEGuiConfig(GuiScreen parent) {
        super(parent, buildConfigElements(), BloodKillEffectMod.MODID, false, false,
              GuiConfig.getAbridgedConfigPath(ConfigManager.get().getConfig().toString()));
    }

    private static List<IConfigElement> buildConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        Configuration cfg = ConfigManager.get().getConfig();

        list.add(new ConfigElement(cfg.get(Configuration.CATEGORY_GENERAL,
                "enabled", true,
                "Enable or disable all kill effects")));
        list.add(new ConfigElement(cfg.get(Configuration.CATEGORY_GENERAL,
                "playersOnly", true,
                "Only show effects when players die")));
        list.add(new ConfigElement(cfg.get(Configuration.CATEGORY_GENERAL,
                "killedByPlayerOnly", false,
                "Only show effects when the local player is the killer")));
        list.add(new ConfigElement(cfg.get(Configuration.CATEGORY_GENERAL,
                "pvpOnly", false,
                "Only show effects for player-vs-player kills")));

        Property selectedProp = cfg.get(ConfigManager.CATEGORY_EFFECTS, "selectedEffect", "Blood",
                "Selected effect name (used when random mode is off)");
        selectedProp.setValidValues(
                EffectRegistry.getAllNames().toArray(new String[0]));
        list.add(new ConfigElement(cfg.getCategory(ConfigManager.CATEGORY_EFFECTS)));

        Property tierProp = cfg.get(ConfigManager.CATEGORY_PERFORMANCE, "tier",
                PerformanceTier.BALANCED.name(),
                "Performance tier: POTATO, BALANCED, HIGH, ULTRA");
        tierProp.setValidValues(new String[]{
                PerformanceTier.POTATO.name(),
                PerformanceTier.BALANCED.name(),
                PerformanceTier.HIGH.name(),
                PerformanceTier.ULTRA.name()});
        list.add(new ConfigElement(cfg.getCategory(ConfigManager.CATEGORY_PERFORMANCE)));

        return list;
    }
}
