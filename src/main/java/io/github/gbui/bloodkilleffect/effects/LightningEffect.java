package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.world.World;

public class LightningEffect extends BaseKillEffect {
    public LightningEffect() {
        super("Lightning", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, double x, double y, double z, float particleScale) {
        // Use HarmlessLightningBolt to avoid setting blocks on fire or damaging entities
        HarmlessLightningBolt bolt = new HarmlessLightningBolt(world, x, y, z);
        world.addWeatherEffect(bolt);
    }
}
