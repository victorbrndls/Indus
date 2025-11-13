package com.victorbrndls.indus.client.screen;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Random;

public class EnergyNetworkScreen extends Screen {

    private static final int SAMPLE_INTERVAL_TICKS = 20;

    private final long networkId;
    private int tickCounter = 0;

    private final IntList energyHistory = new IntArrayList();
    private final IntList capacityHistory = new IntArrayList();

    public EnergyNetworkScreen(long networkId) {
        super(Component.literal("Energy Network " + networkId));
        this.networkId = networkId;

        addSample(0, 0);
        addSample(0, 0);
    }

    public void addSample(int energy, int capacity) {
        energyHistory.add(energy);
        capacityHistory.add(capacity);

        int maxSamples = 40;
        if (energyHistory.size() > maxSamples) {
            energyHistory.removeInt(0);
            capacityHistory.removeInt(0);
        }
    }

    @Override
    public void tick() {
        super.tick();

        tickCounter++;
        if (tickCounter >= SAMPLE_INTERVAL_TICKS) {
            tickCounter = 0;
//            ClientPacketDistributor.sendToServer(new RequestEnergySamplePayload(networkId));
            addSample((new Random()).nextInt(0, 20), 20);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        super.render(gfx, mouseX, mouseY, partialTick);

        int graphX = 10;
        int graphY = 30;
        int graphW = this.width - 20;
        int graphH = this.height - 40;

        renderGraph(gfx, graphX, graphY, graphW, graphH);

        gfx.drawString(this.font, this.title, 10, 18, 0xFFFFFFFF);

        gfx.drawString(this.font, "Energy", graphW - 84, 18, 0xFF00FF00);
        gfx.drawString(this.font, "Capacity", graphW - 34, 18, 0xFFFFAA00);
    }

    private void renderGraph(GuiGraphics gfx, int x, int y, int w, int h) {
        if (energyHistory.size() < 2) return;

        // Background + border
        gfx.fill(x, y, x + w, y + h, 0xAA000000);          // semi-transparent black
        gfx.submitOutline(x, y, w, h, 0xFFFFFFFF);                     // white border

        int n = energyHistory.size();

        int maxEnergy = 0;
        for (int e : energyHistory) maxEnergy = Math.max(maxEnergy, e);
        for (int c : capacityHistory) maxEnergy = Math.max(maxEnergy, c);
        if (maxEnergy <= 0) return;

        maxEnergy += 5; // padding

        int graphInnerW = w - 2; // leave 1px padding from border
        int graphInnerH = h - 2;

        int xBase = x + 1;
        int yBase = y + 1;

        int energyColor = 0xFF00FF00;    // ARGB: opaque green
        int capacityColor = 0xFFFFAA00;  // ARGB: opaque orange

        for (int i = 1; i < n; i++) {
            float t0 = (i - 1) / (float) (n - 1);
            float t1 = i / (float) (n - 1);

            int x0 = xBase + (int) (t0 * (graphInnerW - 1));
            int x1 = xBase + (int) (t1 * (graphInnerW - 1));

            int e0 = energyHistory.getInt(i - 1);
            int e1 = energyHistory.getInt(i);
            int c0 = capacityHistory.getInt(i - 1);
            int c1 = capacityHistory.getInt(i);

            int yE0 = yBase + graphInnerH - 1 - (int) ((e0 / (float) maxEnergy) * (graphInnerH - 1));
            int yE1 = yBase + graphInnerH - 1 - (int) ((e1 / (float) maxEnergy) * (graphInnerH - 1));
            int yC0 = yBase + graphInnerH - 1 - (int) ((c0 / (float) maxEnergy) * (graphInnerH - 1));
            int yC1 = yBase + graphInnerH - 1 - (int) ((c1 / (float) maxEnergy) * (graphInnerH - 1));

            drawLine(gfx, x0, yE0, x1, yE1, energyColor);
            drawLine(gfx, x0, yC0, x1, yC1, capacityColor);
        }
    }

    private static void drawLine(GuiGraphics gfx, int x0, int y0, int x1, int y1, int color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        int x = x0;
        int y = y0;

        while (true) {
            gfx.fill(x, y, x + 1, y + 1, color);

            if (x == x1 && y == y1) break;

            int e2 = err << 1;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
    }

}
