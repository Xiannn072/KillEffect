package io.github.gbui.bloodkilleffect;

import io.github.gbui.bloodkilleffect.event.KillEffectClientHandler;
import io.github.gbui.bloodkilleffect.gui.BKEGuiConfig;
import io.github.gbui.bloodkilleffect.network.KillEffectMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Client-side proxy. All client-only classes (Minecraft, ClientCommandHandler, etc.)
 * are referenced ONLY here, preventing NoClassDefFoundError on dedicated servers.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        // Client-only: register the client event handler (renders effects on receipt of packet)
        MinecraftForge.EVENT_BUS.register(new io.github.gbui.bloodkilleffect.event.KillEffectClientHandler());

        // Client-only: register the /killeffect command
        ClientCommandHandler.instance.registerCommand(new KillEffectCommand());

        BloodKillEffectMod.logger.info("Client proxy initialized — command and client handler registered");
    }

    /**
     * Opens the KillEffect config GUI.
     */
    public void openConfigGui() {
        Minecraft.getMinecraft().displayGuiScreen(
            new BKEGuiConfig(Minecraft.getMinecraft().currentScreen));
    }

    /**
     * Client override: schedules kill-effect playback on the main client thread.
     */
    @Override
    public void handleKillEffectPacket(KillEffectMessage msg) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            KillEffectClientHandler.handleServerPacket(
                msg.getX(), msg.getY(), msg.getZ(), msg.getEntityId());
        });
    }
}
