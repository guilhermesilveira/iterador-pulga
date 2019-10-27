package br.usp.iterador.internal.logic;

import java.util.List;

import org.apache.log4j.Logger;

import br.usp.iterador.model.Intermediate;
import br.usp.iterador.model.Variable;

public class ScriptFactory {

	private static final Logger LOG = Logger.getLogger(ScriptFactory.class);

	public String getIntermediateScript(List<Intermediate> scripts) {
		String total = "";
		for (Intermediate in : scripts) {
			String temp = in.getValue().trim();
			char last = temp.charAt(temp.length() - 1);
			if (last != ';' && last != '}') {
				temp = in.getName() + " = " + in.getValue() + ";";
			}
			total += "\t\t" + temp + "\n";
		}
		LOG.debug("Intermediate expression is\n" + total);
		return total;
	}

	public String getNextRoundScript(List<Variable> variables) {
		String temp = "";
		for (int i = 0; i < variables.size(); i++) {
			temp += "\t_x[" + i + "] = " + variables.get(i).getCode() + ";\n";
		}
		LOG.debug("Next round script:\n" + temp);
		return temp;
	}

}
