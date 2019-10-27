package br.usp.pulga.iteration;

import java.awt.Color;

import br.usp.pulga.Iteration;

public class ClearGraphicOnStartListener implements IterationListener {

    private final PGraphics graphics;
    private final World xWorld;
    private final World yWorld;

    public ClearGraphicOnStartListener(PGraphics pgraphics, World xWorld,
            World yWorld) {
        this.graphics = pgraphics;
        this.xWorld = xWorld;
        this.yWorld = yWorld;
    }

    public void after(Iteration iteration) {
    }

    public void before(Iteration iteration) {
    }

    public void init(Iteration iteration) {
        graphics.setColor(Color.WHITE);
        graphics.fill(0, 0, (int) xWorld.max(), (int) yWorld.max());
    }

}
