package com.aang23.bendingsync.network;

import com.aang23.bendingsync.BendingSync;
import com.google.common.base.Charsets;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerSwitchPacket implements IMessage {

    private int name_length;
    private String name;
    private int target_length;
    private String target;

    public ServerSwitchPacket() {
    }

    public ServerSwitchPacket(String name, String target) {
        this.name_length = name.length();
        this.name = name;
        this.target_length = target.length();
        this.target = target;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        name_length = buf.readInt();
        name = buf.readCharSequence(name_length, Charsets.UTF_8).toString();
        target_length = buf.readInt();
        target = buf.readCharSequence(target_length, Charsets.UTF_8).toString();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(name_length);
        buf.writeCharSequence(name, Charsets.UTF_8);
        buf.writeInt(target_length);
        buf.writeCharSequence(target, Charsets.UTF_8);
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }

    public static class Handler implements IMessageHandler<ServerSwitchPacket, IMessage> {

        public Handler() {
        }

        @Override
        public IMessage onMessage(ServerSwitchPacket message, MessageContext ctx) {
            if (Sponge.getServer().getOnlinePlayers().size() > 0) {
                Player player = (Player) Sponge.getServer().getOnlinePlayers().toArray()[0];
                BendingSync.PROXY_NETWORK.sendTo(player,
                        buf -> buf.writeUTF("SendToServer").writeUTF(message.getName()).writeUTF(message.getTarget()));
            }
            return null;
        }
    }
}