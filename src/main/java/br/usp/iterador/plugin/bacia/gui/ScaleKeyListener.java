package br.usp.iterador.plugin.bacia.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Waits for key presses and repaints
 * 
 * @author Guilherme Silveira
 */
public class ScaleKeyListener implements ActionListener {

	private ShowAverageSamples frame;

	public ScaleKeyListener(ShowAverageSamples frame) {
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		// the user hitted enter!
		frame.askForRepaint();
	}

}
