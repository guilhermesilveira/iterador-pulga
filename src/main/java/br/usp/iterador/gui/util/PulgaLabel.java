package br.usp.iterador.gui.util;

import javax.swing.JLabel;

import br.usp.iterador.i18n.Messages;

/**
 * Simple i18n label.
 * 
 * @author Guilherme Silveira
 * 
 */
public class PulgaLabel extends JLabel {

	/**
	 * Serial uid
	 */
	private static final long serialVersionUID = 4982839419031248781L;

	public PulgaLabel() {
	}

	public PulgaLabel(String s) {
		super.setText(Messages.getString(s));
	}

	public void setI18NText(String s) {
		super.setText(Messages.getString(s));
	}

}
