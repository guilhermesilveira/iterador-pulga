package br.usp.iterador.plugin.bacia.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JPanel;

import br.usp.iterador.gui.SimplePanel;
import br.usp.iterador.gui.util.CloseButton;
import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.ModelIntField;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.plugin.bacia.Basin;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Controls the basic info for the basin of attraction plugin.
 * 
 * @author Guilherme Silveira
 */
public class BasicConfigurationFrame implements PulgaAction {

	public void execute(WindowManager windows, Basin basin) {
		Container pane = new JPanel(new BorderLayout());
		pane.add(getMiddle(basin), BorderLayout.CENTER);
		pane.add(new CloseButton(), BorderLayout.SOUTH);
		PulgaFrame frame = windows.showFrame("basin_of_attraction",
				"basin_of_attraction", pane, 400, 140);
		frame.setVisible(true);
	}

	private JPanel getMiddle(Basin basin) {
		JPanel panel = new JPanel(new GridLayout(4, 1));
		ModelIntField quit = new ModelIntField("earlyQuit");
		ModelIntField maxIter = new ModelIntField("maxIterationsInside");
		new DataModel(basin, quit, maxIter);
		panel.add(new SimplePanel("early_quit", quit.getComponent()));
		panel
				.add(new SimplePanel("maxIterationsInside", maxIter
						.getComponent()));
		return panel;
	}

}
