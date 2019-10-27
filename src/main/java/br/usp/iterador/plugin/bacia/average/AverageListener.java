package br.usp.iterador.plugin.bacia.average;

import java.util.HashMap;

import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.Basin;

/**
 * @author Guilherme Silveira
 */
public interface AverageListener {

	public void updateAverages(Basin b);

	public void clear();

	public double[] getTotal();

	public void initData();

	public void pinta(double[] x, HashMap<String, Integer> valores,
			Application dados);

}
