package br.usp.pulga;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.janino.ClassBodyEvaluator;
import net.janino.Scanner;

import org.apache.log4j.Logger;

public class DefaultIterationRule implements IterationRule {

	private static Logger LOG = Logger.getLogger(DefaultIterationRule.class);

	private final ArrayList<String> methods = new ArrayList<String>();

    private String definitions = "", iteration = "", localDefinitions = "";

    private int total = 0;

    private final List<Variable>  variables = new ArrayList<Variable>();

    public void addVariable(String name, double defaultValue, String iterationRule) {
		localDefinitions += "    double " + name + " = this._x[" + total + "] ;\n";
		iteration += "    this._x[" + total + "] = " + iterationRule + ";\n";
        variables.add(new Variable(name, total, defaultValue));
        total++;
	}

	public Iteration buildIteration() {
		try {
			String code = getCode();
			LOG.debug(code);
			Scanner sc = new Scanner(null, new StringReader(code));
			Class[] interfaces = new Class[] { Iteration.class };
			Class clazz = new ClassBodyEvaluator(sc,
					BasicIteration.class, interfaces, null).evaluate();
			BasicIteration instance = (BasicIteration) clazz.newInstance();
            instance.useVariables(variables);
            return instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getCode() {
		StringBuilder entireCode = new StringBuilder();
		entireCode.append(definitions);
		entireCode.append("\n");
		entireCode.append("  public void internalIterate() {\n");
        entireCode.append(localDefinitions);
		entireCode.append(iteration);
		entireCode.append("  }\n\n");
        for(String method : this.methods) {
            entireCode.append(method);
            entireCode.append('\n');
        }
        return entireCode.toString();
	}

    public void addMethod(String s) {
        this.methods.add(s);
    }
}
