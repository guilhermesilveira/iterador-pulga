package br.usp.iterador.plugin.cor;

import java.awt.Color;

import br.usp.iterador.AbstractTestCase;

public class ColorInfoTest extends AbstractTestCase {

	public void testConvertsCorrectly() {
		ColorInfo info = new ColorInfo();
		assertEquals("red 1, green 2, blue 3", info.convertToString(new Color(1,2,3)));
	}

}
