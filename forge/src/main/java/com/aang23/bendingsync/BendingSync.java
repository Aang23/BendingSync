package com.aang23.bendingsync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import com.aang23.bendingsync.client.gui.GuiServerSwitch;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@Mod(modid = BendingSync.MODID, name = BendingSync.NAME, version = BendingSync.VERSION)
public class BendingSync {
    public static final String MODID = "bendingsync";
    public static final String NAME = "BendingSync";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.registerKeyBinding(stats);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    private KeyBinding stats = new KeyBinding("GUI test", Keyboard.KEY_U, "BendingSync");

    @SubscribeEvent
    public void onJoin(ClientTickEvent e) {
        if (stats.isKeyDown()) {

            FMLClientHandler.instance().showGuiScreen(new GuiServerSwitch());
            //Minecraft.getMinecraft().setIngameNotInFocus();
        }

    }
}
