package br.usp.iterador.plugin.cor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.gui.menu.MenuCommand;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.gui.WindowManager;
import br.usp.iterador.plugin.tool.Tool;

/**
 * A color change plugin based on the current iteration number.
 *
 * @author Guilherme Silveira
 */
public class ColorPlugin implements Plugin {

	private ColorData data = new ColorData();

	private final WindowManager windows;

	public ColorPlugin(WindowManager windows) {
		this.windows = windows;
	}

	public JMenu getMenu(MenuBuilder menuBuilder) {
		JMenu menu = new JMenu("Advanced color");
		menu.add(new MenuCommand("Config...", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ColorConfigFrame().execute(data, windows);
			}
		}));
		return menu;
	}

	public IterationListener getIterationListener() {
		return new ColorIterationListener(data);
	}

	public void activate() {
	}

	public void deactivate() {

	}

	public Serializable getSerializedData() {
		return this.data;
	}

	public void activate(Object data) {
		this.data = (ColorData) data;
	}

	public Tool[] getTools() {
		return new Tool[0];
	}

}
