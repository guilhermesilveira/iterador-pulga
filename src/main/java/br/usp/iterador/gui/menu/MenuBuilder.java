package br.usp.iterador.gui.menu;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.i18n.Messages;
import br.usp.iterador.logic.Controller;

/**
 * Capable of building menus by reading files.
 *
 * @author Guilherme Silveira
 */
public class MenuBuilder {

	private static Logger LOG = Logger.getLogger(MenuBuilder.class);

	private final Controller controller;

	public MenuBuilder(Controller controller) {
		this.controller = controller;
	}

	public List<JMenu> init(String file) {
		InputStream is = MenuBuilder.class.getResourceAsStream(file);
		List<JMenu> menu = new ArrayList<JMenu>();
		try {
			Scanner sc = new Scanner(is);
			sc.useDelimiter(Pattern.compile("\\s+"));
			JMenuBar bar = readMenu(sc);
			while (bar.getMenuCount() != 0) {
				menu.add(bar.getMenu(0));
				bar.remove(0);
			}
			is.close();
			return menu;
		} catch (IOException e) {
			LOG.error(e);
			return menu;
		}
	}

	private JMenuBar readMenu(Scanner sc) {
		JMenuBar bar = new JMenuBar();
		LinkedList<JMenu> menus = new LinkedList<JMenu>();
		while (sc.hasNext()) {
			String key = sc.next();
			if (key.equals("menu")) {
				String[] data = Messages.getString(sc.next()).split(",");
				JMenu menu = new JMenu(data[0]);
				menu.setMnemonic(getKey(data[1]));
				LOG.debug("creating menu " + menu.getText());
				bar.add(menu);
				menus.addLast(menu);
			} else if (key.equals("/menu")) {
				LOG.debug("finished menu");
				menus.removeLast();
			} else if (key.equals("divisor")) {
				LOG.debug("Reading divisor");
				menus.getLast().addSeparator();
			} else if (key.equals("menuitem")) {
				String[] data = Messages.getString(sc.next()).split(",");
				LOG.debug("Menuitem: " + Arrays.toString(data) + " with "
						+ controller);
				MenuCommand item = new MenuCommand(data[0], sc.next(),
						controller);
				if (data.length > 1 && data[1].length() == 1) {
					item.setMnemonic(getKey(data[1]));
				}
				LOG.debug("creating menuitem " + item.getText());
				menus.getLast().add(item);
			}
		}
		LOG.debug("Added " + bar.getMenuCount() + " menus");
		for (int i = 0; i < bar.getMenuCount(); i++) {
			LOG.debug(i + " Menu: " + bar.getMenu(i).getText());
		}
		return bar;
	}

	private int getKey(String name) {
		try {
			return (Integer) KeyEvent.class.getField("VK_" + name).get(null);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return 0;
	}

	public MenuConfigurator getMenu(String title) {
		return new MenuConfigurator(controller, controller
				.find(GuiFactory.class), title);
	}

}
