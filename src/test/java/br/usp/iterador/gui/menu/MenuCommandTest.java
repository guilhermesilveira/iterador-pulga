package br.usp.iterador.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.usp.iterador.AbstractTestCase;

public class MenuCommandTest extends AbstractTestCase {

	public void testIsConstructedWithActionListener() {
		ActionListener myListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		};
		MenuCommand cmd = new MenuCommand("title", myListener);
		assertTrue(isContained(cmd.getActionListeners(), myListener));
	}

	private boolean isContained(ActionListener[] actionListeners,
			ActionListener my) {
		for (ActionListener listener : actionListeners) {
			if (listener.equals(my)) {
				return true;
			}
		}
		return false;
	}

}
