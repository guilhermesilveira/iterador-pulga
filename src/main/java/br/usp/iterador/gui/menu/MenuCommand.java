package br.usp.iterador.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import br.usp.iterador.logic.Controller;

/**
 * Menu command.
 * 
 * @author Guilherme Silveira
 */
public class MenuCommand extends JMenuItem {

	private static final long serialVersionUID = -3029612681532010737L;

	public MenuCommand(String title, final String command,
			final Controller controller) {
		this(title, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeAction(command);
			}
		});
	}

	public MenuCommand(String title, ActionListener listener) {
		super(title);
		addActionListener(listener);
	}

}
