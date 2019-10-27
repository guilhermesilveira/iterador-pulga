package br.usp.iterador;

import junit.framework.TestCase;

public class CommandLineTest extends TestCase {

	public void testRunsWithDebugOn() {
		assertTrue(new CommandLine(new String[] { "-d" }).isDebugOn());
	}

	public void testRunsWithDebugOff() {
		assertFalse(new CommandLine(new String[] { "-D" }).isDebugOn());
	}

}
