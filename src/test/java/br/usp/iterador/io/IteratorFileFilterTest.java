package br.usp.iterador.io;

import java.io.File;

import junit.framework.TestCase;

public class IteratorFileFilterTest extends TestCase {

	public void testAcceptsADirectory() {
		IteratorFileFilter filter = new IteratorFileFilter();
		File dir = File.listRoots()[0];
		assertTrue(filter.accept(dir));
	}

	public void testAcceptsCorrectExtension() {
		IteratorFileFilter filter = new IteratorFileFilter();
		File dir = new File("" + IteratorFileFilter.EXTENSION);
		assertTrue(filter.accept(dir));
	}

	public void testDoesntAcceptWrongExtension() {
		IteratorFileFilter filter = new IteratorFileFilter();
		File dir = new File("" + IteratorFileFilter.EXTENSION + "a");
		assertFalse(filter.accept(dir));
	}

}
