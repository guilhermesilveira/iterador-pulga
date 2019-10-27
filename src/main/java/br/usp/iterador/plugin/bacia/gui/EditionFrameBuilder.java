package br.usp.iterador.plugin.bacia.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import br.usp.iterador.gui.util.CloseButton;
import br.usp.iterador.gui.util.DataModel;
import br.usp.iterador.gui.util.ModelField;
import br.usp.iterador.gui.util.PulgaLabel;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Basic edition frame.
 * 
 * @author Guilherme Silveira
 */
public class EditionFrameBuilder {

	private DataModel model;

	private PulgaFrame frame;

	private JPanel panel = new JPanel();

	public EditionFrameBuilder(WindowManager windows, String key, String title,
			Object target, int w, int h, int len) {
		this.model = new DataModel(target);
		this.frame = windows.showFrame(key, title, getPanel(), w, h);
		this.panel.setLayout(new GridLayout(len, 2));
	}

	private JPanel getPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.panel);
		panel.add(new CloseButton(), BorderLayout.SOUTH);
		return panel;
	}

	protected void register(String caption, ModelField field) {
		this.model.addModel(field);
		panel.add(new PulgaLabel(caption));
		panel.add(field.getComponent());
	}

	protected void init() {
		this.frame.setVisible(true);
	}
}
