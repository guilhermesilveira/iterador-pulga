package br.usp.pulga.hiperboloide;

import java.awt.Color;
import java.io.File;

import br.usp.pulga.Iteration;
import br.usp.pulga.iteration.Axis;
import br.usp.pulga.iteration.BorderListener;
import br.usp.pulga.iteration.ClearGraphicOnStartListener;
import br.usp.pulga.iteration.ImageExporter;
import br.usp.pulga.iteration.InitialColorListener;
import br.usp.pulga.iteration.PGraphics;
import br.usp.pulga.iteration.PlotListener;
import br.usp.pulga.iteration.World;

public class HiperboloideGammaTest extends HiperboloideTest {

	public void testGamma5EndpointsExamples() {

		int size = 300;
		World world = new World(size);

		ImageExporter exporter = new ImageExporter(new File("images/gamma_5.png"));
		PGraphics pgraphics = exporter.getGraphics(size, size);

		Axis x = new Axis(0.72, 0.82, "c");
		Axis y = new Axis(-1.5, 1.5, "x1");

		{
			Hiperboloide hiper = new Hiperboloide(0, 0, 0);
			hiper.add(new ClearGraphicOnStartListener(pgraphics, world, world));
			hiper.iterate(0);
		}

		int trashPoints = 4;
		int max = size * 5;
		for (int i = 0; i < max; i++) {
			double c = i * (1 - 0.5) / max + 0.5;
			{
				Hiperboloide hiper = new Hiperboloide(0, 0, c);
				hiper.add(new BorderListener(pgraphics, world, world, x, y));
				hiper.add(new InitialColorListener(pgraphics, Color.BLACK));
				hiper.add(new PlotListener(pgraphics, world, world, x, y,
						trashPoints));
				hiper.iterate(5);
			}
			{
				Hiperboloide hiper = new Hiperboloide(0, c, c);
				hiper.add(new InitialColorListener(pgraphics, Color.BLACK));
				hiper.add(new PlotListener(pgraphics, world, world, x, y,
						trashPoints));
				hiper.iterate(5);
			}
		}

		exporter.save();
	}

	public void executeGammaSnapshot(double c) {

		int size = 300;
		World world = new World(size);

		ImageExporter exporter = new ImageExporter(new File("images/gamma_snapshot_"
				+ c + ".png"));
		PGraphics pgraphics = exporter.getGraphics(size, size);

		Axis x = new Axis(-1.5, 1.5, "x0");
		Axis y = new Axis(-1.5, 1.5, "x1");

		Hiperboloide hiper = new Hiperboloide(0, 0, c);
		hiper.add(new ClearGraphicOnStartListener(pgraphics, world, world));
		hiper.iterate(0);

		hiper = new Hiperboloide(0, 0, c);
		hiper.add(new BorderListener(pgraphics, world, world, x, y));
		hiper.add(new InitialColorListener(pgraphics, Color.BLACK));
		hiper.add(new PlotListener(pgraphics, world, world, x, y, 0));
		Iteration iteration = hiper.getIteration();
		SetIterationListener set = new SetIterationListener(200, iteration);
		set.add(new InitialRule("x1", 0, c));
		set.iterate(7);

		exporter.save();
	}

	public void testGammaSnapshots() {

		double min = 0.72;
		double max = 0.82;
		int steps = 200;
		double step = (max - min) / steps;
		while(min <= max) {
			executeGammaSnapshot(min);
			min += step;
		}

	}
}
