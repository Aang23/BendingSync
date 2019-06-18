package com.aang23.bendingsync.client.gui;

import java.io.IOException;

import com.aang23.bendingsync.BendingSync;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GuiServerSwitch extends GuiScreen {
    public static final ResourceLocation BACKGROUND = new ResourceLocation(BendingSync.MODID, "textures/gui/img.png");

    public GuiServerSwitch() {
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
     
    }

    @Override
    public void initGui() {
        lobby.setWidth(100);
        survival.setWidth(100);
        games.setWidth(100);
    }

    GuiButton lobby = new GuiButton(0, 10, 10, "Lobby");
    GuiButton survival = new GuiButton(0, 10, 60, "Survival");
    GuiButton games = new GuiButton(0, 10, 110, "Games");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        //mc.getTextureManager().bindTexture(BACKGROUND);
        //drawTexturedModalRect(0, 0, 0, 0, 1920, 1080);
        //drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, 1920, 1080);

        mc.fontRenderer.drawString(TextFormatting.BOLD + "Lobby menu", width/2 - 50, 2, 4210752);

        lobby.drawButton(mc, mouseX, mouseY, partialTicks);
        survival.drawButton(mc, mouseX, mouseY, partialTicks);
        games.drawButton(mc, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (lobby.isMouseOver()) {
            mc.player.closeScreen();
            mc.player.sendChatMessage("/server lobby");
            System.out.println("Lobby");
        }
        if (survival.isMouseOver()) {
            mc.player.closeScreen();
            mc.player.sendChatMessage("/server survival");
            System.out.println("Survival");
        }
        if (games.isMouseOver()) {
            mc.player.closeScreen();
            mc.player.sendChatMessage("/server games");
            System.out.println("Games");
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
