package br.usp.iterador.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents all model data for the main program.
 *
 * @author Guilherme Silveira
 */
public class Application {

	private Information info = new Information();

	private ImageInfo image = new ImageInfo();

	private Color backgroundColor = Color.BLACK;

	private boolean clearBeforeDrawing = true;

	private Codes codes = new Codes();

	private GridOptions grid = new GridOptions();

	private List<Intermediate> intermediateExpressions = new ArrayList<Intermediate>();

	/** Parameters */
	private List<Parameter> parameters = new ArrayList<Parameter>();

	/** Points */
	private int trashPoints = 100, iteratedPoints = 2000;

	/** basic iterations */
	private List<Variable> variables = new ArrayList<Variable>();

	/** Xscale */
	private Scale xScale = new Scale("x1", -1.5, 1.5);

	/** Yscale */
	private Scale yScale = new Scale("x2", -1.5, 1.5);

    private String extraBody = "";

    /**
	 * Iteration power
	 */
	private int iterationPower = 1;

	public Application() {
		variables.add(new Variable(0.4, "x1"));
		variables.add(new Variable(0.4, "x2"));
	}

	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	public Codes getCodes() {
		return this.codes;
	}

	/**
	 * @return Returns the number of variables.
	 */
	public int getDimension() {
		return this.variables.size();
	}

	/**
	 * @return
	 */
	public GridOptions getGrid() {
		return this.grid;
	}

	public List<Intermediate> getIntermediateExpressions() {
		return intermediateExpressions;
	}

	/**
	 * @return Returns the iteratedPoints.
	 */
	public int getIteratedPoints() {
		return iteratedPoints;
	}

	/**
	 * Returns all parameters
	 *
	 * @return all parameters
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	public int getTrashPoints() {
		return trashPoints;
	}

	public List<String> getAvailableVariables() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("iteration");
		for (int i = 1; i <= getDimension(); i++) {
			list.add("x" + i);
		}
		for (Intermediate intermediario : this.intermediateExpressions) {
			list.add(intermediario.getName());
		}
		for (Parameter p : this.parameters) {
			list.add(p.getName());
		}
		return list;
	}

	public Scale getXScale() {
		return xScale;
	}

	public Scale getYScale() {
		return yScale;
	}

	public boolean isClearBeforeDrawing() {
		return clearBeforeDrawing;
	}

	public void remove(Intermediate obj) {
		this.intermediateExpressions.remove(obj);
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setClearBeforeDrawing(boolean clearBeforeDrawing) {
		this.clearBeforeDrawing = clearBeforeDrawing;
	}

	public void setIteratedPoints(int iteratedPoints) {
		this.iteratedPoints = iteratedPoints;
	}

	public void setTrashPoints(int trashPoints) {
		this.trashPoints = trashPoints;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + super.toString() + ",dim:" + getDimension() + " " + "["
				+ xScale + " " + yScale + "]" + "]";
	}

	/**
	 * @param codes
	 *            The codes to set.
	 */
	public void setCodes(Codes codes) {
		this.codes = codes;
	}

	public void setGrid(GridOptions grid) {
		this.grid = grid;
	}

	/**
	 * @param intermediarios
	 *            The intermediarios to set.
	 */
	public void setIntermediateExpressions(
			ArrayList<Intermediate> intermediarios) {
		this.intermediateExpressions = intermediarios;
	}

	/**
	 * @param parameters
	 *            The parameters to set.
	 */
	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void setXScale(Scale scale) {
		this.xScale = scale;
	}

	/**
	 * @param scale
	 *            The yScale to set.
	 */
	public void setYScale(Scale scale) {
		this.yScale = scale;
	}

	/**
	 * @return Returns the info.
	 */
	public Information getInfo() {
		return info;
	}

	public ImageInfo getImage() {
		return image;
	}

	public int getIterationPower() {
		return iterationPower;
	}

	public void setIterationPower(int iterationPower) {
		this.iterationPower = iterationPower;
	}

	public void setIteration(int i, double initialValue, String code) {
		Variable iteration = variables.get(i);
		iteration.setInitialValue(initialValue);
		iteration.setCode(code);
	}

	public List<Variable> getVariables() {
		return variables;
	}

}