package io.github.gbui.bloodkilleffect.network;

import io.github.gbui.bloodkilleffect.BloodKillEffectMod;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
     * Network handler. Routes through the proxy to avoid loading client-only classes
     * on the dedicated server (which would cause NoClassDefFoundError at class-load time).
     */
    public static class Handler implements IMessageHandler<KillEffectMessage, IMessage> {
        @Override
        public IMessage onMessage(KillEffectMessage msg, MessageContext ctx) {
            // Delegate to proxy — server-side no-op, client-side schedules on main thread
            BloodKillEffectMod.proxy.handleKillEffectPacket(msg);
            return null;
        }
    }
}
