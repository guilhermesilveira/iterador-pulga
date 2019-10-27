package br.usp.iterador.gui.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import br.usp.iterador.jxpath.JXPathResolver;

/**
 * Data model for gui.
 * 
 * @author Guilherme Silveira
 */
public class DataModel {

	private static final Logger logger = Logger.getLogger(DataModel.class);

	private final List<ModelField> fields = new ArrayList<ModelField>();

	private final List<ActionListener> listeners = new ArrayList<ActionListener>();

	private Object obj;

	private JXPathResolver resolver;

	private final Set<ModelField> updatingField = new HashSet<ModelField>();

	/**
	 * Creates the data model.
	 */
	public DataModel(Object obj) {
		this.obj = obj;
		this.resolver = new JXPathResolver(obj);
	}

	public DataModel(Object obj, ModelField... fields) {
		this.obj = obj;
		this.resolver = new JXPathResolver(obj);
		addModel(fields);
	}

	private void add(ModelField m) {
		Component component = m.getComponent();
		if (component instanceof JTextField) {
			((JTextField) component).addKeyListener(getKeyListener(m));
		} else if (component instanceof JTextArea) {
			((JTextArea) component).addKeyListener(getKeyListener(m));
		} else if (component instanceof JComboBox) {
			((JComboBox) component).addItemListener(getItemListener(m));
		} else if (component instanceof PulgaPanel) {
			((PulgaPanel) component).addActionListener(getActionListener(m));
		} else if (component instanceof JCheckBox) {
			((JCheckBox) component).addActionListener(getActionListener(m));
		} else if (component instanceof JSlider) {
			((JSlider) component).addChangeListener(getChangeListener(m));
		} else {
			throw new RuntimeException("ugh here... "
					+ component.getClass().getName());
		}
		loadValue(m);
		this.fields.add(m);
	}

	private ItemListener getItemListener(final ModelField m) {
		return new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				applyData(e, m);
			}
		};
	}

	private ActionListener getActionListener(final ModelField m) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyData(e, m);
			}
		};
	}

	private ChangeListener getChangeListener(final ModelField m) {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				//if (!((JSlider) e.getSource()).getValueIsAdjusting()) {
					applyData(e, m);
			//	}
			}
		};
	}

	private KeyListener getKeyListener(final ModelField m) {
		return new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
					logger.debug("Key event on " + obj);
					applyData(e, m);
				}
			}
		};
	}

	private void loadValue(ModelField m) {
		try {
			updatingField.add(m);
			m.updateField(obj, resolver);
		} catch (UpdateException e1) {
			logger.error("Unable to load value", e1);
		} finally {
			updatingField.remove(m);
		}
	}

	public void reloadAllValues(Object exceptComponent) {
		for (ModelField field : fields) {
			if (!field.getComponent().equals(exceptComponent)) {
				loadValue(field);
			}
		}
	}

	/**
	 * Adds a custom listener
	 * 
	 * @param listener
	 *            listener
	 */
	public void addActionListener(ActionListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Adds some models
	 * 
	 * @param fields
	 *            models
	 */
	public void addModels(ModelField[] fields) {
		for (ModelField m : fields) {
			add(m);
		}
	}

	/**
	 * Adds some models
	 * 
	 * @param fields
	 *            models
	 */
	public void addModel(ModelField... fields) {
		for (ModelField m : fields) {
			add(m);
		}
	}

	/**
	 * Applies the data
	 * 
	 */
	private void applyData(EventObject e, ModelField m) {
		// if updating this field, do not ask it to update the object
		if (updatingField.contains(m)) {
			return;
		}
		logger.debug("Applying its entire data set.");
		try {
			m.updateObject(obj, resolver);
		} catch (UpdateException e1) {
			logger.error("Unable to update value", e1);
		}
		ActionEvent event = new ActionEvent(e.getSource(), 0, "");
		for (ActionListener l : listeners) {
			l.actionPerformed(event);
		}
	}

}
