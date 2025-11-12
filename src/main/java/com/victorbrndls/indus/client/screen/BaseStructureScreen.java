package com.victorbrndls.indus.client.screen;

import com.victorbrndls.indus.Indus;
import com.victorbrndls.indus.gui.BaseStructureMenu;
import com.victorbrndls.indus.mod.structure.IndusStructureState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class BaseStructureScreen extends AbstractContainerScreen<BaseStructureMenu> {

    private static final ResourceLocation BACKGROUND = Indus.rl("textures/gui/base_structure.png");

    private Button buildBtn;

    public BaseStructureScreen(BaseStructureMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageHeight = 198;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        buildBtn = Button
                .builder(
                        Component.literal("Build"),
                        (b) -> {
                            getMinecraft().gameMode.handleInventoryButtonClick(menu.containerId, BaseStructureMenu.BUTTON_BUILD);
                            removeWidget(buildBtn);
                        })
                .bounds(
                        (width - imageWidth) / 2 + imageWidth - 50 - 8,
                        (height - imageHeight) / 2 + imageHeight - 20 - 94,
                        50,
                        20
                )
                .build();

        if (menu.getStructureState() == IndusStructureState.NOT_READY) {
            addRenderableWidget(buildBtn);
        }
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

        switch (menu.getStructureState()) {
            case NOT_READY -> {
                var pose = g.pose();
                pose.pushMatrix();
                pose.translate(left + 8, top + 18);
                pose.scale(0.75f, 0.75f);
                g.drawString(font, "Put a container on top with items", 0, 0, 0xFF292929, false);
                pose.popMatrix();

                renderRequirements(g, left + 8, top + 30);
            }
            case IN_CONSTRUCTION -> {
                String text = "Construction in progress...";
                var textWidth = font.width(text);
                g.drawString(font,
                        text,
                        left + (imageWidth - textWidth) / 2,
                        top + (imageHeight - 94) / 2,
                        0xFF292929,
                        false);
            }
            case BUILT -> {
                String state = "Working...";
                var stateWidth = font.width(state);
                g.drawString(font,
                        state,
                        left + (imageWidth - stateWidth) / 2,
                        top + (imageHeight - 94) / 2,
                        0xFF292929,
                        false);

                var idText = Component.literal("ID: " + menu.entity.getNetworkId());
                var idTextWidth = font.width(idText);
                g.drawString(font,
                        idText,
                        left + (imageWidth - idTextWidth) - 6,
                        top + 6,
                        0xFF292929,
                        false);
            }
        }

        renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        buildBtn.active = menu.canBuildClient();
    }

    private void renderRequirements(GuiGraphics g, int x, int y) {
        var requirements = menu.requirements();
        var pose = g.pose();
        int row = 0;

        for (int i = 0; i < requirements.size(); i++) {
            ItemStack required = requirements.get(i);

            int iconX = x;
            int iconY = y + row * 14;

            pose.pushMatrix();
            pose.translate(iconX, iconY);

            float scale = 0.70f;
            pose.scale(scale, scale);

            g.renderItem(required, 0, 0);

            int have = menu.getHave(i);
            int need = required.getCount();

            int color = have >= need ? 0xFF008000 : 0xFFFF5555; // green if met, red otherwise
            Component line = Component.literal(have + "/" + need + " - ").append(required.getHoverName());
            g.drawString(this.font, line, 15 + 4, 4, color, false);

            pose.popMatrix();
            row++;
        }
    }

}
