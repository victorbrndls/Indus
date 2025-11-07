package com.victorbrndls.indus.client.screen;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.blocks.structure.IndusStructure;
import com.victorbrndls.indus.blocks.structure.StructureRequirements;
import com.victorbrndls.indus.blocks.tileentity.TreeFarmBlockEntity;
import com.victorbrndls.indus.inventory.TreeFarmMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class TreeFarmScreen extends AbstractContainerScreen<TreeFarmMenu> {

    private static final ResourceLocation BACKGROUND = Indus.rl("textures/gui/tree_farm.png");

    private final TreeFarmBlockEntity entity;

    public TreeFarmScreen(TreeFarmMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageHeight = 198;
        this.inventoryLabelY = this.imageHeight - 94;

        this.entity = menu.entity;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND, i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partial) {
        renderBackground(g, mouseX, mouseY, partial);
        super.render(g, mouseX, mouseY, partial);

        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;

        var pose = g.pose();
        pose.pushMatrix();
        pose.translate(left + 8, top + 18);
        pose.scale(0.75f, 0.75f);
        g.drawString(font, "Put items in input container", 0, 0, 0xFF292929, false);
        pose.popMatrix();

        renderRequirements(g, left + 8, top + 30);

        renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        super.renderLabels(g, mouseX, mouseY);
    }

    private void renderRequirements(GuiGraphics g, int x, int y) {
        var reqs = StructureRequirements.getRequirements(IndusStructure.TREE_FARM);
        var pose = g.pose();
        int row = 0;

        for (ItemStack required : reqs) {
            int iconX = x;
            int iconY = y + row * 14;

            pose.pushMatrix();
            pose.translate(iconX, iconY);

            float scale = 0.70f;
            pose.scale(scale, scale);

            g.renderItem(required, 0, 0);

            int have = 0;
            int need = required.getCount();

            int color = have >= need ? 0xFF008000 : 0xFFFF5555; // green if met, red otherwise
            Component line = Component.literal(have + "/" + need + " - ").append(required.getHoverName());
            g.drawString(this.font, line, 15 + 4, 4, color, false);

            pose.popMatrix();
            row++;
        }
    }

}
