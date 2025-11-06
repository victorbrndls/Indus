package com.victorbrndls.indus.client.screen;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.tileentity.TreeFarmBlockEntity;
import com.victorbrndls.indus.inventory.TreeFarmMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class TreeFarmScreen extends AbstractContainerScreen<TreeFarmMenu> {

    private static final ResourceLocation TEXTURE = Indus.rl("textures/gui/tree_farm.png");

    private final TreeFarmBlockEntity entity;

    public TreeFarmScreen(TreeFarmMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.entity = menu.entity;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (mouseX > leftPos + 7 && mouseX < leftPos + 29 && mouseY > topPos + 10
                && mouseY < topPos + 77) {
            var component = Component.literal("Energy");
            var clienttooltipcomponent = ClientTooltipComponent.create(component.getVisualOrderText());
            guiGraphics.renderTooltip(
                    font,
                    List.of(clienttooltipcomponent),
                    mouseX, mouseY,
                    DefaultTooltipPositioner.INSTANCE,
                    null
            );
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, "222", (imageWidth / 2 - font.width("2312") / 2) + 14, 20,
                0xFF333333, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0,
                this.imageWidth, this.imageHeight, 256, 256);

        int y = this.getEnergyScaled(60);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + 10, this.topPos + 12 + y,
                this.imageWidth, 0, 16, 60 - y, 256, 256);
    }

    private String getEnergyFormatted(int energy) {
        if (energy >= 1000000) {
            return (energy / 1000) + " kFE";
        } else {
            return energy + " FE";
        }
    }

    private int getEnergyScaled(int pixels) {
        return pixels - (pixels * getPercent() / 100);
    }

    private int getPercent() {
        return 50;
    }
}
