package com.aang23.bendingsync;

import java.util.HashMap;
import java.util.Map;

import com.aang23.bendingsync.client.gui.GuiServerSwitch;
import com.aang23.bendingsync.network.PlayerInfoPacket;
import com.aang23.bendingsync.network.ServerSwitchPacket;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * BendingSync's forge mod main class
 * 
 * @author Aang23
 */
@Mod(modid = BendingSync.MODID, name = BendingSync.NAME, version = BendingSync.VERSION)
public class BendingSync {
    public static final String MODID = "bendingsync";
    public static final String NAME = "BendingSync";
    public static final String VERSION = "1.0";

    public static Logger logger;
    public static SimpleNetworkWrapper NETWORK;

    public static Map<Integer, String> PLAYER_PREFIX_CACHE = new HashMap<Integer, String>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.registerKeyBinding(stats);
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("bendingsync");
        NETWORK.registerMessage(PlayerInfoPacket.Handler.class, PlayerInfoPacket.class, 1, Side.CLIENT);
        NETWORK.registerMessage(ServerSwitchPacket.Handler.class, ServerSwitchPacket.class, 2, Side.SERVER);
    }

    private KeyBinding stats = new KeyBinding("Server Switch GUI", Keyboard.KEY_U, "BendingSync");

    /**
     * Listen for keypresses
     * 
     * @param e
     */
    @SubscribeEvent
    public void onClientTick(ClientTickEvent e) {
        if (stats.isKeyDown()) {
            FMLClientHandler.instance().showGuiScreen(new GuiServerSwitch());
            // Minecraft.getMinecraft().setIngameNotInFocus();
        }

    }

    /**
     * Adds the prefix on the nametag
     * 
     * @param e
     */
    @SubscribeEvent
    public void onTagRender(PlayerEvent.NameFormat e) {
        String prefix = "";
        if (PLAYER_PREFIX_CACHE.containsKey(e.getEntityPlayer().getEntityId()))
            prefix = PLAYER_PREFIX_CACHE.get(e.getEntityPlayer().getEntityId());
        String name = getFormattedText(prefix) + TextFormatting.RESET + " " + e.getDisplayname();
        e.setDisplayname(name);
    }

    /**
     * Sends a server switch request to the server
     * 
     * @param server
     */
    public static void sendSwitchPacket(String server) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        player.world.sendPacketToServer(NETWORK.getPacketFrom(new ServerSwitchPacket(player.getName(), server)));
    }

    /**
     * Simple function to format a given string with &
     * 
     * @param text
     * @return
     */
    private String getFormattedText(String text) {
        String finalString = "";
        for (int i = 0; i < text.length(); i++) {
            char ch = text.toCharArray()[i];
            if (ch == '&') {
                int value = -1;
                try {
                    value = Integer.parseInt(String.valueOf(text.toCharArray()[i + 1]));
                } catch (NumberFormatException e) {
                }
                i++;
                if (value != -1)
                    finalString += TextFormatting.values()[(value)];
            } else
                finalString += ch;
        }
        return finalString;
    }
}
