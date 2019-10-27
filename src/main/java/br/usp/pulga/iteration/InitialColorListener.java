package br.usp.pulga.iteration;

import java.awt.Color;

import br.usp.pulga.Iteration;

public class InitialColorListener implements IterationListener {

    private final Color color;

    private final PGraphics graphics;

    public InitialColorListener(PGraphics pgraphics, Color color) {
        this.graphics = pgraphics;
        this.color = color;
    }

    public void after(Iteration iteration) {
    }

    public void before(Iteration iteration) {
    }

    public void init(Iteration iteration) {
        graphics.setColor(color);
    }

}
