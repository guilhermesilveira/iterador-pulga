package br.usp.pulga.iteration;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

public class PGraphics {

    private final int width;

    private final int height;

    private final Graphics2D graphics;

    public PGraphics(Graphics2D graphics, int width, int height) {
        this.graphics = graphics;
        this.width = width;
        this.height = height;
    }

    public void setColor(Color color) {
        graphics.setColor(color);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void line(int x1, int y1, int x2, int y2) {
        graphics.drawLine(x1, height - y1 - 1, x2, height - y2 - 1);
    }

    public void write(String string, double x, double y, Alignment... alignments) {
        int[] current = new int[] { (int) x, (int) y };
        for (Alignment alignment : alignments) {
            alignment.adapt(graphics, current, string);
        }
        graphics
                .drawString(string, (int) current[0], height - (int) current[1]);
    }

    public void rect(int x, int y, int w, int h) {
        graphics.drawRect(x, height - y, w, h);
    }

    public void fill(int x, int y, int w, int h) {
        graphics.fillRect(x, y, w, h);
    }

	public void line(double x, double y, double x2, double y2) {
		line((int) x, (int) y, (int) x2, (int) y2);
	}

}
