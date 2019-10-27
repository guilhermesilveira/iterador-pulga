package br.usp.iterador.plugin.cor;

import java.awt.Color;

import br.usp.iterador.AbstractTestCase;

public class ColorDataTest extends AbstractTestCase {

	public void testCiclesColors() {
		ColorData data = new ColorData();
		data.removeColorAt(0);
		data.addColor(Color.RED);
		data.addColor(Color.GREEN);
		data.addColor(Color.BLUE);
		data.setNIterations(100);
		for (int j = 0; j < 3; j++) {
			Color expected = data.getColors().get(j);
			for (int i = 0; i < 99; i++) {
				int it = i + j * 100;
				assertEquals("For iteration " + it, expected, data
						.getColorFor(it));
			}
		}
	}

	public void testStaysWithTheSingleColor() {
		ColorData data = new ColorData();
		data.removeColorAt(0);
		data.addColor(Color.RED);
		data.setNIterations(100);
		for (int i = 0; i < 101; i++) {
			assertEquals(Color.RED, data.getColorFor(i));
		}
	}

}
