package br.usp.iterador.gui.util;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A simple button which runs the action listener when clicked.
 *
 * @author Guilherme Silveira
 */
public class ClassicButton extends JButton {

	private static final long serialVersionUID = 4122537691536634680L;

	public ClassicButton(String caption, ActionListener listener) {
		super(caption);
		this.addActionListener(listener);
	}

}
