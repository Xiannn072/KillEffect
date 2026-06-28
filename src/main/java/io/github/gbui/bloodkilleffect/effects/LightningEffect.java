package io.github.gbui.bloodkilleffect.effects;

import io.github.gbui.bloodkilleffect.PerformanceTier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;

public class LightningEffect extends BaseKillEffect {
    public LightningEffect() {
        super("Lightning", PerformanceTier.BALANCED);
    }

    @Override
    public void playEffect(World world, Entity entity, float particleScale) {
        EntityLightningBolt bolt = new EntityLightningBolt(world,
            entity.posX, entity.posY, entity.posZ);
        world.addWeatherEffect(bolt);
    }
}
