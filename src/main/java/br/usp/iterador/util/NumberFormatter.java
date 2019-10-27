package br.usp.iterador.util;

import java.text.DecimalFormat;

/**
 * @author Guilherme Silveira
 */
public class NumberFormatter {

    /**
     * Retorna um formatador
     * 
     * @return the number formater
     */
    public static DecimalFormat getDecimal(int minInt, int maxInt, int minFrac,
            int maxFrac, boolean group, boolean separator) {
        DecimalFormat df = new DecimalFormat();
        df = new DecimalFormat();
        df.setGroupingUsed(group);
        df.setMaximumFractionDigits(maxFrac);
        df.setMinimumFractionDigits(minFrac);
        df.setMaximumIntegerDigits(maxInt);
        df.setMinimumIntegerDigits(minInt);
        df.setDecimalSeparatorAlwaysShown(separator);
        return df;
    }


}