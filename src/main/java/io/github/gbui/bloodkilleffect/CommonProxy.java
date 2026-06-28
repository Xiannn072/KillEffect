package io.github.gbui.bloodkilleffect;

import io.github.gbui.bloodkilleffect.network.KillEffectMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Server-side proxy. Does NOT reference any client-only classes.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        // Nothing server-side needed for this mod
    }

    public void init(FMLInitializationEvent event) {
        // Register the server-side LivingDeathEvent listener for network packet dispatch
        MinecraftForge.EVENT_BUS.register(new io.github.gbui.bloodkilleffect.event.KillEffectServerHandler());
    }

    /**
     * Server-side no-op. The client override opens the actual GUI.
     */
    public void openConfigGui() { }

    /**
     * Server-side no-op for kill-effect packets. The client override schedules
     * effect playback on the main thread.
     */
    public void handleKillEffectPacket(KillEffectMessage msg) { }
}
