package br.usp.iterador.gui.util;

import javax.swing.JFrame;

import junit.framework.TestCase;

public class CloseButtonTest extends TestCase {

	class MyFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		boolean closed = false;

		@Override
		public void dispose() {
			super.dispose();
			closed = true;
		}
	}

	public void testClosesContainer() {
		MyFrame f = new MyFrame();
		CloseButton b = new CloseButton();
		f.add(b);
		b.doClick();
		assertTrue(f.closed);
	}

}
