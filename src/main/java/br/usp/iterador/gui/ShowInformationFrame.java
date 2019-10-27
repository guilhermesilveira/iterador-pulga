package br.usp.iterador.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.gui.util.ModelField;
import br.usp.iterador.gui.util.ModelTextField;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Show basic information.
 *
 * @author Guilherme Silveira
 */
public class ShowInformationFrame implements PulgaAction {

	private ModelField[] fields = new ModelField[] {
			new ModelTextField("info.name"),
			new ModelTextField("info.authors"),
			new ModelTextField("info.description") };

	public void execute(Application app, GuiFactory gui, WindowManager windows) {
		PulgaFrame frame = windows.showFrame("information", "information",
				getJContentPane(gui), 350, 300);
		new DataModel(app, this.fields);
		frame.setVisible(true);

	}

	private JPanel getJContentPane(GuiFactory gui) {
		JPanel jContentPane = new JPanel(new BorderLayout());
		jContentPane.add(getMiddle(gui), BorderLayout.CENTER);
		jContentPane.add(getButtons(gui), BorderLayout.SOUTH);
		return jContentPane;
	}

	private JPanel getMiddle(GuiFactory gui) {
		JPanel jPanel = new JPanel(new GridLayout(10, 1));
		jPanel.add(gui.getLabel("name"));
		jPanel.add(this.fields[0].getComponent());
		jPanel.add(gui.getLabel("authors"));
		jPanel.add(this.fields[1].getComponent());
		jPanel.add(gui.getLabel("description"));
		jPanel.add(this.fields[2].getComponent());
		return jPanel;
	}

	private JPanel getButtons(GuiFactory gui) {
		return gui.getPanel(gui.createCloseButton());
	}

}
