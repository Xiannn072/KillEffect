package io.github.gbui.bloodkilleffect.effects;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;

/**
 * Visual-only lightning bolt. Overrides onUpdate to skip block fire and entity damage.
 * Necessary because the 1.8.9 {@link EntityLightningBolt} constructor does NOT have
 * the boolean "effectOnly" parameter — that was added in 1.9+.
 */
public class HarmlessLightningBolt extends EntityLightningBolt {
    public HarmlessLightningBolt(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void onUpdate() {
        // Only play the thunder sound on the first tick, then die immediately.
        // This skips ALL of the parent's fire-setting, damage, and block-destruction logic.
        if (this.ticksExisted == 0) {
            this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ,
                "ambient.weather.thunder", 10000F, 0.8F + this.rand.nextFloat() * 0.2F);
        }

        // Update boltVertex so the vanilla renderer (RenderLightningBolt) animates
        // the lightning shape each tick instead of rendering a static line.
        this.boltVertex = this.rand.nextLong();

        if (++this.ticksExisted >= 8) {
            this.setDead();
        }
    }
}
