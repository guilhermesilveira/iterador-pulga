package br.usp.iterador.plugin.cor;

import java.awt.Color;

import br.usp.iterador.gui.util.ListInfo;

/**
 * A color to string translator.
 *
 * @author Guilherme Silveira
 * @since 1.2
 */
public class ColorInfo implements ListInfo<Color> {

	public String convertToString(Color obj) {
		return String.format("red %d, green %d, blue %d", obj.getRed(), obj
				.getGreen(), obj.getBlue());
	}

}
