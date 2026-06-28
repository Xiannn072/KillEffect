package io.github.gbui.bloodkilleffect.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Packet sent from server → client to trigger a kill effect at specific coordinates.
 * Carries only position data; the client selects the effect locally
 * (randomMode/selectedEffect are client config).
 */
public class KillEffectMessage implements IMessage {
    private double x;
    private double y;
    private double z;
    private int entityId;

    /** Required no-arg constructor for Forge deserialization. */
    public KillEffectMessage() {}

    public KillEffectMessage(double x, double y, double z, int entityId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityId = entityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(entityId);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public int getEntityId() { return entityId; }

    /**
     * Client-side handler. Schedules effect playback on the main client thread.
     */
    public static class Handler implements IMessageHandler<KillEffectMessage, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(KillEffectMessage msg, MessageContext ctx) {
            // Schedule on the client main thread to be thread-safe
            net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
                io.github.gbui.bloodkilleffect.event.KillEffectClientHandler
                    .handleServerPacket(msg.getX(), msg.getY(), msg.getZ());
            });
            return null;
        }
    }
}
