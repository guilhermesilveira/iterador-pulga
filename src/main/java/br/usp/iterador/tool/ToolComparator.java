package br.usp.iterador.tool;

import java.util.Comparator;

import br.usp.iterador.plugin.tool.Tool;

/**
 * A tool comparator by name.
 * 
 * @author Guilherme Silveira
 */
public class ToolComparator implements Comparator<Tool> {

	public int compare(Tool o1, Tool o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
