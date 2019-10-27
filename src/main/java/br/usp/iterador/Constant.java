package br.usp.iterador;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.text.DecimalFormat;

import br.usp.iterador.util.NumberFormatter;

public class Constant {

    public static final Stroke NORMAL_STROKE = new BasicStroke(1.0f);

    public static final Stroke DASHED_STROKE = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] {
                    10.0f, 30.0f }, 0.0f);

    public final static DecimalFormat duasCasas = NumberFormatter.getDecimal(0,
            4, 0, 2, false, true);

    public final static DecimalFormat umaCasa = NumberFormatter.getDecimal(0,
            4, 0, 1, false, true);

    public final static DecimalFormat quatroCasas = NumberFormatter.getDecimal(
            0, 4, 0, 4, false, true);

    public final static DecimalFormat oitoCasas = NumberFormatter.getDecimal(
            0, 4, 0, 8, false, true);

}