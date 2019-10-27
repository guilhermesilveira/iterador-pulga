package br.usp.iterador.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import br.usp.iterador.Constant;
import br.usp.iterador.model.Scale;

/**
 * Simple gui helper
 * 
 * @author Guilherme Silveira
 */
public class GUIHelper {

	private int width;

	private int height;

	/**
	 * @param size
	 */
	public GUIHelper(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * @param background
	 */
	public void clear(Color color, Graphics2D g) {
		g.setColor(color);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Executa uma regra de tres e retorna um valor entre 0 e 1
	 * 
	 * @param resultado
	 * @param minimo
	 * @param maximo
	 * @return o valor da regra de tres (proporcao)
	 */
	public double regraDeTres(double resultado, double minimo, double maximo) {
		return (resultado - minimo) / (maximo - minimo);
	}

	/**
	 * Separa o graph em diversas linhas verticais
	 */
	public void linhasVerticais(Graphics2D g, int linhas, double min,
			double max, NumberFormat f) {
		double pos;
		g.setStroke(Constant.DASHED_STROKE);
		for (int i = 0; i != linhas; i++) {
			pos = ((0.0 + i) * width) / linhas;
			pos = mudaEscala(pos, 0, width, min, max);
			g.drawLine(i * width / linhas, 0, i * width / linhas, height);
			g.drawString(f.format(pos), i * width / linhas, 10);
		}
		g.setStroke(Constant.NORMAL_STROKE);
	}

	/**
	 * Muda pos da primeira escala para a segunda
	 * 
	 * @return
	 */
	public double mudaEscala(double pos, double min1, double max1, double min2,
			double max2) {
		double val = regraDeTres(pos, min1, max1);
		return calculaPosicaoNaEscala(val, min2, max2);
	}

	/**
	 * @param val
	 * @param min2
	 * @param max2
	 * @return
	 */
	private double calculaPosicaoNaEscala(double val, double min2, double max2) {
		return val * (max2 - min2) + min2;
	}

	/**
	 * @param g2
	 * @param i
	 * @param minimo
	 * @param maximo
	 * @param quatroCasas
	 */
	public void linhasHorizontais(Graphics2D g, int linhas, double min,
			double max, DecimalFormat f) {
		double pos;
		g.setStroke(Constant.DASHED_STROKE);
		for (int i = 0; i != linhas; i++) {
			pos = ((0.0 + i) * height) / linhas;
			pos = mudaEscala(pos, 0, height, min, max);
			g.drawLine(0, height - i * height / linhas, width, height - i
					* height / linhas);
			g.drawString(f.format(pos), 10, height - i * height / linhas);
		}
		g.setStroke(Constant.NORMAL_STROKE);
	}

	/**
	 * @return
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}

	public double mudaEscala(Double val, Scale scale, int min, int max) {
		return mudaEscala(val, scale.getMin(), scale.getMax(), min, max);
	}

	public double mudaEscala(double val, int min, int max, Scale scale) {
		return mudaEscala(val, min, max, scale.getMin(), scale.getMax());
	}

	public void linhasHorizontais(Graphics2D g, int i, Scale scale,
			DecimalFormat format) {
		linhasHorizontais(g, i, scale.getMin(), scale.getMax(), format);
	}

	public void linhasVerticais(Graphics2D g, int i, Scale scale,
			DecimalFormat format) {
		linhasVerticais(g, i, scale.getMin(), scale.getMax(), format);
	}
}