package br.usp.iterador.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.logic.Controller;

public class MenuConfigurator {

	private static final Logger logger = Logger
			.getLogger(MenuConfigurator.class);

	private JMenu menu;

	private GuiFactory guiFactory;

	private Controller controller;

	public MenuConfigurator(Controller controller, GuiFactory guiFactory,
			String title) {
		this.menu = guiFactory.getMenu(title);
		this.guiFactory = guiFactory;
		this.controller = controller;
	}

	public MenuConfigurator add(String link, final Class<?> typeToInstantiate) {
		this.menu.add(guiFactory.getMenuItem(link, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
                    controller.newInstance(typeToInstantiate);
				} catch (Exception ex) {
					logger.error("Unable to instantiate " + typeToInstantiate.getName(), ex);
				}
			}

		}));
		return this;
	}

	public JMenu getJMenu() {
		return this.menu;
	}

	/**
	 * Adds a menu with a callback
	 */
	public MenuConfigurator add(String title, final Object obj,
			final String method) {
		JMenuItem menu = guiFactory.getMenuItem(title, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					obj.getClass().getMethod(method).invoke(obj);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				}
			}

		});
		this.menu.add(menu);
		return this;
	}

	public MenuConfigurator addExecute(String link, final String cmd) {
		this.menu.add(guiFactory.getMenuItem(link, guiFactory.getLogicListener(
				cmd, controller)));
		return this;
	}

}
