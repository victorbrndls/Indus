package com.victorbrndls.indus.client.screen;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;

public class HistoryGraph {

    private final IntList history = new IntArrayList();
    private final int color;
    private final String label;
    private final int maxSamples;

    public HistoryGraph(String label, int color, int maxSamples) {
        this.label = label;
        this.color = color;
        this.maxSamples = maxSamples;
    }

    public void addSample(int value) {
        history.add(value);
        if (history.size() == 1) {
            history.add(value);
        }
        if (history.size() > maxSamples) {
            history.removeInt(0);
        }
    }

    public int size() {
        return history.size();
    }

    public int getMaxValue() {
        int max = 0;
        for (int v : history) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    public void renderLine(GuiGraphics gfx, int xBase, int yBase, int graphInnerW, int graphInnerH, int maxValue) {
        int n = history.size();
        if (n < 2 || maxValue <= 0) {
            return;
        }

        for (int i = 1; i < n; i++) {
            float t0 = (i - 1) / (float) (n - 1);
            float t1 = i / (float) (n - 1);

            int x0 = xBase + (int) (t0 * (graphInnerW - 1));
            int x1 = xBase + (int) (t1 * (graphInnerW - 1));

            int v0 = history.getInt(i - 1);
            int v1 = history.getInt(i);

            int y0 = yBase + graphInnerH - 1 - (int) ((v0 / (float) maxValue) * (graphInnerH - 1));
            int y1 = yBase + graphInnerH - 1 - (int) ((v1 / (float) maxValue) * (graphInnerH - 1));

            drawLine(gfx, x0, y0, x1, y1, color);
        }
    }

    public void renderLabel(GuiGraphics gfx, Font font, int x, int y) {
        gfx.drawString(font, label, x, y, color);
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

            if (x == x1 && y == y1) {
                break;
            }

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
