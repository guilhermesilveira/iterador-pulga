package br.usp.iterador.gui.util;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

import br.usp.iterador.i18n.Messages;

/**
 * Closes a window.
 * 
 * @author Guilherme Silveira
 */
public class CloseButton extends JButton {

	private static final long serialVersionUID = 3257567321554826801L;

	public CloseButton() {
		super(Messages.getString("CloseButton.caption"));
		this.setMnemonic(KeyEvent.VK_C);
		this.setToolTipText(Messages.getString("CloseButton.tooltip"));
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Container obj = CloseButton.this;
				while (!(obj instanceof Window)) {
					obj = obj.getParent();
				}
				Window frame = (Window) obj;
				frame.dispose();
			}
		});
	}

}
