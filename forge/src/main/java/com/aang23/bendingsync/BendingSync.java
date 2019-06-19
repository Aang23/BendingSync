package com.aang23.bendingsync;

import com.aang23.bendingsync.client.gui.GuiServerSwitch;
import com.aang23.bendingsync.network.ServerSwitchPacket;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BendingSync.MODID, name = BendingSync.NAME, version = BendingSync.VERSION)
public class BendingSync {
    public static final String MODID = "bendingsync";
    public static final String NAME = "BendingSync";
    public static final String VERSION = "1.0";

    public static Logger logger;
    public static SimpleNetworkWrapper NETWORK;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.registerKeyBinding(stats);
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("bendingsync");
        NETWORK.registerMessage(ServerSwitchPacket.Handler.class, ServerSwitchPacket.class, 2, Side.SERVER);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    private KeyBinding stats = new KeyBinding("GUI test", Keyboard.KEY_U, "BendingSync");

    @SubscribeEvent
    public void onJoin(ClientTickEvent e) {
        if (stats.isKeyDown()) {

            FMLClientHandler.instance().showGuiScreen(new GuiServerSwitch());
            // Minecraft.getMinecraft().setIngameNotInFocus();
        }

    }

    public static void sendSwitchPacket(String server) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        player.world.sendPacketToServer(NETWORK.getPacketFrom(new ServerSwitchPacket(player.getName(), server)));
    }
}
