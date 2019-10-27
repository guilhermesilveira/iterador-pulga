package br.usp.iterador.plugin.connectpoints;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;

import br.usp.iterador.gui.SimplePanel;
import br.usp.iterador.gui.util.CloseButton;
import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.ModelColorField;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Join points plugin frame.
 * 
 * @author Guilherme Silveira
 */
public class ShowConnectConfig {

	private ModelColorField color = new ModelColorField("color");

	public void execute(WindowManager windows, PluginManager pluginManager) {
		Container panel = new JPanel();
		panel.add(getMiddle(), BorderLayout.CENTER);
		panel.add(new CloseButton(), BorderLayout.SOUTH);
		windows.showFrame("connect_points", "connect_points", panel, 400, 100);
		DataModel dataModel = new DataModel(pluginManager.getActivePlugin(
				ConnectPointsPlugin.class).getData());
		dataModel.addModel(this.color);
	}

	private JPanel getMiddle() {
		return new SimplePanel("color", this.color.getComponent());
	}

}
