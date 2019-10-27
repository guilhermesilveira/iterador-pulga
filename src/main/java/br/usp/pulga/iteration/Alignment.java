package br.usp.pulga.iteration;

import java.awt.Graphics2D;

public enum Alignment {

    LEFT {
        public void adapt(Graphics2D graphics, int[] current, String string) {
        }
    },
    BOTTOM {
        public void adapt(Graphics2D graphics, int[] current, String string) {
        }
    },
    TOP {
        public void adapt(Graphics2D graphics, int[] current, String string) {
            current[1] -= graphics.getFontMetrics().getHeight();
        }
    },
    RIGHT {
        public void adapt(Graphics2D graphics, int[] current, String string) {
            current[0] -= graphics.getFontMetrics().stringWidth(string);
        }
    };

    public abstract void adapt(Graphics2D graphics, int[] current, String string);

}
