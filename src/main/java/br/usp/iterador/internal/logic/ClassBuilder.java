package br.usp.iterador.internal.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.usp.iterador.model.Application;
import br.usp.iterador.model.Codes;
import br.usp.iterador.model.Intermediate;
import br.usp.iterador.model.Parameter;
import br.usp.iterador.model.PieceOfCode;
import br.usp.iterador.model.Variable;

public class ClassBuilder {

	private String sb = "";

	private static Logger logger = Logger.getLogger(ClassBuilder.class);

	private static final StringBuffer BASIC_CODE = new StringBuffer();

	static {
		Scanner scanner = new Scanner(ClassBuilder.class
				.getResourceAsStream("static_methods.txt"));
		while (scanner.hasNextLine()) {
			BASIC_CODE.append(scanner.nextLine() + "\n");
		}
	}

	private void setX(double[] x) {
		for (int i = 0; i < x.length; i++) {
			sb += String.format(Locale.ENGLISH, "\tdouble x%d;\n", (i + 1),
					x[i]);
		}
		sb += "\n\n";
	}

	public String getCode(int dim) {

		String code = BASIC_CODE.toString();
		code += this.sb;

		code += "\tpublic void prepare() {\n";
		for (int i = 0; i < dim; i++) {
			code += "\t\tx" + (i + 1) + " = _x[" + (i) + "];\n";
		}
		code += "\t}";

		code += "\n";

		logger.debug("ClassBuilder code:\n" + code);
		return code;
	}

	private void setParameters(Collection<Parameter> parameters) {
		for (Parameter p : parameters) {
			sb += String.format(Locale.ENGLISH, "\tdouble %s = %s;\n", p
					.getName(), p.getValue());
		}
		sb += "\n\n";
	}

	private void setIntermediateScript(String intermediateScript) {
		sb += String.format(
				"\tpublic void nextIntermediateRound() {\n%s\n\t}\n\n",
				intermediateScript);
	}

	private void setNextRoundScript(String nextRoundScript) {
		sb += String.format("\tpublic void nextRound() {\n%s\n\t}\n\n",
				nextRoundScript);
	}

	private void addIntermediateValues(List<Intermediate> intermediates) {
		for (Intermediate i : intermediates) {
			sb += String.format("\t%s %s;\n", (i.getType() != null
					&& !i.getType().equals("") ? i.getType() : "double"), i
					.getName());
		}
	}

	private void addPieceOfCodes(Codes codes) {

		sb += "\tpublic void runPieceOfCode(int trash,int iteration) {\n";
		for (PieceOfCode c : codes.getCodes()) {
			sb += "// piece of code level " + c.getLevel() + "\n";
			sb += "if(((iteration+1) - trash) % " + c.getIterations()
					+ "==0) {\n";
			sb += c.getCode() + "\n";
			sb += "}\n\n";
		}
		sb += "\t}\n";

	}

	public void build(ScriptFactory factory, Application app) {
		setX(copyInitialValues(app.getVariables()));
		setParameters(app.getParameters());
		addIntermediateValues(app.getIntermediateExpressions());
		setIntermediateScript(factory.getIntermediateScript(app
				.getIntermediateExpressions()));
		setNextRoundScript(factory.getNextRoundScript(app.getVariables()));
		addPieceOfCodes(app.getCodes());
		addRealValueMethod(app.getParameters(), app.getIntermediateExpressions());
	}

	public void addRealValueMethod(List<Parameter> params,
			List<Intermediate> interms) {
		sb += "\tpublic double getRealValue(String _name) {\n";
		for (Parameter p : params) {
			sb += String.format("\t\tif(_name.equals(\"%s\")) return %s;\n", p
					.getName(), p.getName());
		}
		for (Intermediate p : interms) {
			if (p.getType() == null || p.getType().equals("")
					|| p.getType().trim().equals("double")) {
				sb += String.format(
						"\t\tif(_name.equals(\"%s\")) return %s;\n", p
								.getName(), p.getName());
			}
		}
		sb += "\t\tthrow new RuntimeException(\"Field not found!\");\n";
		sb += "\t}\n\n";
	}

	/**
	 * Copies an array
	 * 
	 * @param initial
	 *            data
	 * @return the copied data
	 */
	public static double[] copy(double[] initial) {
		double[] d = new double[initial.length];
		for (int i = 0; i < d.length; i++) {
			d[i] = initial[i];
		}
		return d;
	}

	public static double[] copyInitialValues(List<Variable> iterations) {
		double[] d = new double[iterations.size()];
		for (int i = 0; i < d.length; i++) {
			d[i] = iterations.get(i).getInitialValue();
		}
		return d;
	}

}
