package de.maxhenkel.easyvillagers.gui;

import de.maxhenkel.corelib.inventory.ScreenBase;
import de.maxhenkel.easyvillagers.Main;
import de.maxhenkel.easyvillagers.net.MessageSelectTrade;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AutoTraderScreen extends ScreenBase<AutoTraderContainer> {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/container/auto_trader.png");

    private final Inventory playerInventory;

    public AutoTraderScreen(AutoTraderContainer container, Inventory playerInventory, Component name) {
        super(BACKGROUND, container, playerInventory, name);
        this.playerInventory = playerInventory;
        imageWidth = 176;
        imageHeight = 202;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new ArrowButton(leftPos + 8, topPos + 19, true, button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(false));
        }));

        addRenderableWidget(new ArrowButton(leftPos + imageWidth - 16 - 8, topPos + 19, false, button -> {
            Main.SIMPLE_CHANNEL.sendToServer(new MessageSelectTrade(true));
        }));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        if (getMenu().isLocked()) {
            guiGraphics.blit(BACKGROUND, leftPos + 83, topPos + 19, 176, 0, 28, 21);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        super.renderLabels(guiGraphics, x, y);
        drawCenteredText(guiGraphics, title, 6, FONT_COLOR);
        drawCenteredText(guiGraphics, Component.translatable("gui.easy_villagers.input"), 45, FONT_COLOR);
        drawCenteredText(guiGraphics, Component.translatable("gui.easy_villagers.output"), 77, FONT_COLOR);
        guiGraphics.drawString(font, playerInventory.getDisplayName().getVisualOrderText(), 8F, (float) (imageHeight - 96 + 3), FONT_COLOR, false);
    }

    protected void drawCenteredText(GuiGraphics guiGraphics, Component text, int y, int color) {
        int width = font.width(text);
        guiGraphics.drawString(font, text.getVisualOrderText(), imageWidth / 2F - width / 2F, y, color, false);
    }

}