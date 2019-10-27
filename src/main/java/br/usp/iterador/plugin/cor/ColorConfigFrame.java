package br.usp.iterador.plugin.cor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.usp.iterador.gui.SimplePanel;
import br.usp.iterador.gui.util.CloseButton;
import br.usp.iterador.gui.util.ExecuteButton;
import br.usp.iterador.gui.util.GenericListModel;
import br.usp.iterador.gui.util.IntField;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Color configuration.
 *
 * @author Guilherme Silveira
 */
public class ColorConfigFrame {

	private ColorData data;

	private GenericListModel model;

	private JList colorsList;

	public void execute(ColorData data, WindowManager windows) {
		this.data = data;
		Container container = new JPanel();
		container.setLayout(new BorderLayout());
		container.add(getMiddle(), BorderLayout.CENTER);
		container.add(getButtons(), BorderLayout.SOUTH);
		PulgaFrame frame = windows.showFrame("colorplugin.title",
				"colorplugin.title", container, 400, 400);
	}

	private JPanel getMiddle() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new SimplePanel("Change color after how many iterations? ",
				BorderLayout.WEST, new IntField(this.data.getNIterations(),
						this.data, "NIterations")), BorderLayout.NORTH);
		this.model = new GenericListModel<Color>(this.data.getColors(), new ColorInfo());
		this.colorsList = new JList(model);
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(colorsList);
		panel.add(new SimplePanel("Color list", BorderLayout.NORTH, pane),
				BorderLayout.CENTER);
		return panel;
	}

	private JPanel getButtons() {
		JPanel panel = new JPanel(new GridLayout(1, 4));
		panel.add(new ExecuteButton("add", this, "add"));
		panel.add(new ExecuteButton("edit", this, "edit"));
		panel.add(new ExecuteButton("remove", this, "remove"));
		panel.add(new CloseButton());
		return panel;
	}

	public void add() {
		Color newColor = JColorChooser.showDialog(null,
				"Choose Background Color", Color.WHITE);
		if (newColor == null)
			return;
		data.addColor(newColor);
		model.updateData();
	}

	public void edit() {
		int sel = ColorConfigFrame.this.colorsList.getSelectedIndex();
		Color newColor = JColorChooser.showDialog(null,
				"Choose Background Color", ColorConfigFrame.this.data
						.getColors().get(sel));
		if (newColor == null)
			return;
		data.getColors().add(sel, newColor);
		data.removeColorAt(sel + 1);
	}

	public void remove() {
		data.removeColorAt(ColorConfigFrame.this.colorsList.getSelectedIndex());
		model.updateData();
	}

}
