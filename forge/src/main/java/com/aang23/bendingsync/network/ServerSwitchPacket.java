package com.aang23.bendingsync.network;

import com.google.common.base.Charsets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Packet sent to the server to request a sever switch
 * 
 * @author Aang23
 */
public class ServerSwitchPacket implements IMessage {

    private int uuid_length;
    private String uuid;
    private int target_length;
    private String target;

    public ServerSwitchPacket() {
    }

    public ServerSwitchPacket(String uuid, String target) {
        this.uuid_length = uuid.length();
        this.uuid = uuid;
        this.target_length = target.length();
        this.target = target;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid_length = buf.readInt();
        uuid = buf.readCharSequence(uuid_length, Charsets.UTF_8).toString();
        target_length = buf.readInt();
        target = buf.readCharSequence(target_length, Charsets.UTF_8).toString();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(uuid_length);
        buf.writeCharSequence(uuid, Charsets.UTF_8);
        buf.writeInt(target_length);
        buf.writeCharSequence(target, Charsets.UTF_8);
    }

    public String getUuid() {
        return uuid;
    }

    public String getTarget() {
        return target;
    }

    public static class Handler implements IMessageHandler<ServerSwitchPacket, IMessage> {

        public Handler() {
        }

        @Override
        public IMessage onMessage(ServerSwitchPacket message, MessageContext ctx) {
            return null;
        }
    }
}