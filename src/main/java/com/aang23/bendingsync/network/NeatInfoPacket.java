package com.aang23.bendingsync.network;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class NeatInfoPacket implements IMessage {

    private int entityId;
    private int length;
    private String prefix;

    public NeatInfoPacket() {
    }

    public NeatInfoPacket(int entityId, String prefix) {
        this.entityId = entityId;
        this.length = prefix.length();
        this.prefix = prefix;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        length = buf.readInt();
        prefix = buf.readCharSequence(length, Charsets.UTF_8).toString();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(length);
        buf.writeCharSequence(prefix, Charsets.UTF_8);
    }

    public String getPrefix() {
        return prefix;
    }

    public int getEntityId() {
        return entityId;
    }

    public static class Handler implements IMessageHandler<NeatInfoPacket, IMessage> {

        public Handler() {
        }

        @Override
        public IMessage onMessage(NeatInfoPacket message, MessageContext ctx) {
            return null;
        }
    }
}