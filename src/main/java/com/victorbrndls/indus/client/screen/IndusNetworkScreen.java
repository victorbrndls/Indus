package com.victorbrndls.indus.client.screen;

import com.victorbrndls.indus.network.RequestNetworkSampleMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public class IndusNetworkScreen extends Screen {

    private static final int SAMPLE_INTERVAL_TICKS = 20;
    private static final int MAX_SAMPLES = 40;

    private final long networkId;
    private int tickCounter = 20; // so it requests a sample immediately on open

    private Mode mode = Mode.ENERGY;

    private final HistoryGraph energyGraph = new HistoryGraph("Energy", 0xFF00FF00, MAX_SAMPLES);
    private final HistoryGraph capacityGraph = new HistoryGraph("Capacity", 0xFFFFAA00, MAX_SAMPLES);
    private final HistoryGraph maintenanceGraph = new HistoryGraph("Maintenance", 0xFF00AAFF, MAX_SAMPLES);

    public IndusNetworkScreen(long networkId) {
        super(Component.literal("Network " + networkId));
        this.networkId = networkId;
    }

    public void addSample(int energy, int capacity, int maintenance1) {
        energyGraph.addSample(energy);
        capacityGraph.addSample(capacity);
        maintenanceGraph.addSample(maintenance1);
    }

    @Override
    protected void init() {
        super.init();

        var energyBtn = Button.builder(Component.literal("Energy"), (b) -> mode = Mode.ENERGY)
                .bounds(80, 10, 50, 20)
                .build();
        var maintenanceBtn = Button.builder(Component.literal("Maintenance"), (b) -> mode = Mode.MAINTENANCE)
                .bounds(135, 10, 70, 20)
                .build();

        addRenderableWidget(energyBtn);
        addRenderableWidget(maintenanceBtn);
    }

    @Override
    public void tick() {
        super.tick();

        tickCounter++;
        if (tickCounter >= SAMPLE_INTERVAL_TICKS) {
            tickCounter = 0;
            PacketDistributor.sendToServer(new RequestNetworkSampleMessage(networkId));
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
        int graphY = 35;
        int graphW = this.width - 20;
        int graphH = this.height - 45;

        renderGraph(gfx, graphX, graphY, graphW, graphH);

        gfx.drawString(this.font, this.title, 10, 18, 0xFFFFFFFF);

        int labelY = 18;
        int baseX = graphW - 150;

        switch (mode) {
            case ENERGY -> {
                energyGraph.renderLabel(gfx, this.font, baseX, labelY);
                capacityGraph.renderLabel(gfx, this.font, baseX + 80, labelY);
            }
            case MAINTENANCE -> {
                maintenanceGraph.renderLabel(gfx, this.font, baseX + 30, labelY);
            }
        }
    }

    private void renderGraph(GuiGraphics gfx, int x, int y, int w, int h) {
        if (energyGraph.size() < 2 && capacityGraph.size() < 2 && maintenanceGraph.size() < 2) {
            return;
        }

        int maxValue = 0;

        switch (mode) {
            case ENERGY -> {
                maxValue = Math.max(energyGraph.getMaxValue(), capacityGraph.getMaxValue());
            }
            case MAINTENANCE -> {
                maxValue = maintenanceGraph.getMaxValue();
            }
        }
        if (maxValue <= 0) return;

        maxValue += 5;

        int labelWidth = 40;
        int graphX = x + labelWidth;
        int graphY = y;
        int graphW = w - labelWidth;
        int graphH = h;

        int graphInnerW = graphW - 2;
        int graphInnerH = graphH - 2;
        int xBase = graphX + 1;
        int yBase = graphY + 1;

        gfx.fill(graphX, graphY, graphX + graphW, graphY + graphH, 0xAA000000);
        gfx.renderOutline(graphX, graphY, graphW, graphH, 0xFFFFFFFF);

        int tickCount = 4;
        for (int i = 0; i < tickCount; i++) {
            float t = i / (float) (tickCount - 1);
            int yTick = yBase + (int) (t * (graphInnerH - 1));

            int value = Math.round(maxValue * (1.0f - t));
            String text = Integer.toString(value);

            gfx.fill(graphX - 3, yTick, graphX, yTick + 1, 0xFFFFFFFF);
            gfx.drawString(this.font, text, x + 2, yTick - 4, 0xFFFFFFFF, false);
        }

        switch (mode) {
            case ENERGY -> {
                energyGraph.renderLine(gfx, xBase, yBase, graphInnerW, graphInnerH, maxValue);
                capacityGraph.renderLine(gfx, xBase, yBase, graphInnerW, graphInnerH, maxValue);
            }
            case MAINTENANCE -> {
                maintenanceGraph.renderLine(gfx, xBase, yBase, graphInnerW, graphInnerH, maxValue);
            }
        }
    }

    enum Mode {
        ENERGY,
        MAINTENANCE
    }

}
