package br.usp.iterador;

import java.util.HashSet;

/**
 * Command line info extractor.
 * 
 * @author Guilherme Silveira
 * @since 1.3
 */
public class CommandLine {

	private final boolean debugOn;

	public CommandLine(String[] args) {
		HashSet<String> hs = new HashSet<String>();
		for (String s : args) {
			hs.add(s);
		}
		this.debugOn = hs.contains("-d");
	}

	public boolean isDebugOn() {
		return debugOn;
	}

}
