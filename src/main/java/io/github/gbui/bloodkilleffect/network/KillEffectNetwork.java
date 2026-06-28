package io.github.gbui.bloodkilleffect.network;

import io.github.gbui.bloodkilleffect.BloodKillEffectMod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Network channel for sending kill-effect triggers from server to clients.
 * Registered in preInit on both physical sides.
 */
public class KillEffectNetwork {
    public static final SimpleNetworkWrapper INSTANCE =
            NetworkRegistry.INSTANCE.newSimpleChannel(BloodKillEffectMod.MODID);

    private static int discriminator = 0;

    public static void register() {
        INSTANCE.registerMessage(
                KillEffectMessage.Handler.class,
                KillEffectMessage.class,
                discriminator++,
                Side.CLIENT);
        BloodKillEffectMod.logger.info("Network channel registered");
    }
}
