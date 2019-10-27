package br.usp.iterador.plugin.bacia.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * My polygon implementation
 * 
 * @author Guilherme Silveira
 * 
 */
public class MyPolygon  {

	private ArrayList<double[]> points = new ArrayList<double[]>();
	
	private static Logger logger = Logger.getLogger(MyPolygon.class);

	/*
	 * POINT_IN_POLY1 Determina se um ponto esta dentro de um poligono. Codigo
	 * curto. (COPIADO DE ONDE?)
	 */
	private boolean point_in_poly1(int n, double x, double y) {
		int i, j;
		boolean c = false;
		for (i = 0, j = n - 1; i < n; j = i++) {
			if ((((points.get(i)[1] <= y) && (y < points.get(j)[1])) || ((points
					.get(j)[1] <= y) && (y < points.get(i)[1])))
					&& (x < (points.get(j)[0] - points.get(j)[0])
							* (y - points.get(i)[1])
							/ (points.get(j)[1] - points.get(i)[1])
							+ points.get(i)[0]))
				c = !c;
		}
		return c;
	}

	public boolean contains(double x, double y) {
		return point_in_poly1(this.points.size(), x, y);
	}

	public void addPoint(double x, double y) {
		logger.debug("Adding point: " + x + "," + y);
		points.add(new double[] { x, y });
	}

	@Override
	public String toString() {
		String s = "Bounds: ";
		for (double[] d : this.points)
			s += "(" + d[0] + "," + d[1] + ")";
		return s;
	}

	public List<double[]> getPoints() {
		return this.points;
	}

}
