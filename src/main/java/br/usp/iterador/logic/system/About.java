package br.usp.iterador.logic.system;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Shows the about dialog box.
 * 
 * @author Guilherme Silveira
 */
public class About implements PulgaAction {

	private GuiFactory guiFactory;

	public About(GuiFactory guiFactory) {
		this.guiFactory = guiFactory;
	}

	public void execute(WindowManager windows) {
		PulgaFrame frame = windows.showFrame("about", "about.title",
				getJContentPane(), 360, 223);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private JPanel getJContentPane() {
		JPanel jContentPane = new JPanel(new BorderLayout());
		jContentPane.add(getJPanel(), BorderLayout.CENTER);
		return jContentPane;
	}

	private JPanel getJPanel() {
		JPanel jPanel = new JPanel(new BorderLayout());
		jPanel.add(guiFactory.getLabel("about.name", JLabel.CENTER),
				BorderLayout.NORTH);
		jPanel.add(guiFactory.getLabel("about.description"));
		jPanel.add(guiFactory.createCloseButton(), BorderLayout.SOUTH);
		return jPanel;
	}

}
