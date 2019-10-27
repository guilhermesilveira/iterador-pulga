package br.usp.pulga.hiperboloide;

import java.awt.Color;
import java.io.File;

import br.usp.pulga.iteration.Axis;
import br.usp.pulga.iteration.BorderListener;
import br.usp.pulga.iteration.ClearGraphicOnStartListener;
import br.usp.pulga.iteration.ImageExporter;
import br.usp.pulga.iteration.InitialColorListener;
import br.usp.pulga.iteration.PGraphics;
import br.usp.pulga.iteration.PlotListener;
import br.usp.pulga.iteration.World;

public class HiperboloideExamplesTest extends HiperboloideTest {

	public void testFirstChapterExamples() {
		firstChapterExample(0.7, 0.6, 0.8);
		firstChapterExample(0.7232, 0, 1.2);
		firstChapterExample(0.75, -0.2, 1.4);
		firstChapterExample(0.79, -0.9, 1.6);
		firstChapterExample(0.82, -0.9, 1.6);
	}

	public void firstChapterExample(double c, double min, double max) {
		int size = 200;
		World world = new World(size);

		ImageExporter exporter = new ImageExporter(new File("images/c_" + c + ".png"));
		PGraphics pgraphics = exporter.getGraphics(size, size);

		Axis x = new Axis(min, max, "x0");
		Axis y = new Axis(min, max, "x1");
		Hiperboloide hiper = getHiperboloide(0.74, 0.74, c, pgraphics, x, y,
				world);
		hiper.iterate(3000);

		exporter.save();
	}

	private Hiperboloide getHiperboloide(double x0, double y0, double c,
			PGraphics pgraphics, Axis x, Axis y, World world) {
		Hiperboloide hiper = new Hiperboloide(x0, y0, c);
		hiper.add(new ClearGraphicOnStartListener(pgraphics, world, world));
		hiper.add(new BorderListener(pgraphics, world, world, x, y));
		hiper.add(new InitialColorListener(pgraphics, Color.BLACK));
		hiper.add(new PlotListener(pgraphics, world, world, x, y));
		return hiper;
	}

}
