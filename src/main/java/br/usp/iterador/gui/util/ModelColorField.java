package br.usp.iterador.gui.util;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;

import br.usp.iterador.jxpath.JXPathException;
import br.usp.iterador.jxpath.JXPathResolver;

/**
 * A color field.
 * 
 * @author Guilherme Silveira
 */
public class ModelColorField extends BasicModel {

	private PulgaPanel component = new PulgaPanel(new GridLayout(1, 1));

	private GuiFactory gui = new GuiFactory();

	private Canvas canvas = new Canvas();

	private String path;

	public ModelColorField() {
		init();
	}

	public ModelColorField(String xpath) {
		init();
		this.path = xpath;
	}

	public ModelColorField(Object data, String fieldName)
			throws UpdateException {
		this(fieldName);
		updateField(data, new JXPathResolver(data));
	}

	private void init() {
		this.component.add(this.canvas);
		this.component.add(gui.getButton("...", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(null,
						"Choose Background Color", canvas.getBackground());
				if (newColor == null)
					return;
				canvas.setBackground(newColor);
				canvas.repaint();
				component.execute(e);
			}
		}), BorderLayout.EAST);

		this.component.setPreferredSize(new Dimension(100, 20));
	}

	public void updateField(Object o, JXPathResolver resolver)
			throws UpdateException {
		try {
			this.canvas.setBackground((Color) new JXPathResolver(o).resolve(
					this.path).get());
			this.canvas.repaint();
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public void updateObject(Object o, JXPathResolver resolver)
			throws UpdateException {
		try {
			new JXPathResolver(o).resolve(this.path).set(
					this.canvas.getBackground());
		} catch (JXPathException e) {
			throw new UpdateException(e);
		}
	}

	public Component getComponent() {
		return component;
	}

}
