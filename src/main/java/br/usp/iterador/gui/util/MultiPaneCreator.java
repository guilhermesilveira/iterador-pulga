package br.usp.iterador.gui.util;

import java.awt.Component;
import java.util.List;

import javax.swing.JScrollPane;

import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitPane;

public class MultiPaneCreator {

	private GuiFactory gui = new GuiFactory();

	public JScrollPane createPane(Component... components) {
		int n = components.length;
		int val = (int) (100.0 / n);
		int tot = 0;
		String layoutDef = "(COLUMN  ";
		for (int i = 0; i < n; i++) {
			int len = i == n - 1 ? (100 - tot) : val;
			layoutDef += " (LEAF name=p" + i + " weight=0." + len + ") ";
			tot += len;
		}
		layoutDef += ")";
		MultiSplitPane pane = new MultiSplitPane();
		pane.getMultiSplitLayout().setModel(
				MultiSplitLayout.parseModel(layoutDef));
		for (int i = 0; i < n; i++) {
			pane.add(gui.createPane(components[i]), "p" + i);
		}
		return gui.createPane(pane);
	}

	public JScrollPane createPane(List<Component> panels) {
		return createPane(panels.toArray(new Component[panels.size()]));
	}
}
