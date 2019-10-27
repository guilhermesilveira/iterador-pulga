package br.usp.iterador.universeiterator;

public interface UniverseIteratorExecutorListener {

	public void init();

	public void doPoint(double leftX, double leftY, double rightX,
			double rightY, int ypos, int xpos, int stepY, int stepX,
			int correctX, int correctY);

	public void finish();
}
