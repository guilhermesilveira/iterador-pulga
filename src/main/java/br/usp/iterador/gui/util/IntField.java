package br.usp.iterador.gui.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;

import javax.swing.JTextField;

/**
 * Dynamic int field
 * 
 * @author Guilherme Silveira
 */
public class IntField extends JTextField {

	private static final long serialVersionUID = 3257567325765777717L;

	private Method method;

	private Object target;

	/**
	 * Creates an int field with default value, object and field to be set
	 * 
	 * @param i
	 *            default value
	 * @param target
	 *            target
	 * @param field
	 *            default field
	 */
	public IntField(int i, Object target, String field) {
		init(i, target, field);
	}

	private void init(int i, Object target, String field) {
		this.setText("" + i);
		this.target = target;
		try {
			this.method = target.getClass().getMethod("set" + field,
					new Class[] { int.class });
		} catch (Exception e1) {
			throw new RuntimeException("Unable to find the object set method",
					e1);
		}
		this.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				IntField.this.updateValue();
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				IntField.this.updateValue();
			}

		});
	}

	public IntField(Object target, String field) {
		try {
			init(
					Integer.parseInt(target.getClass().getMethod("get" + field,
							new Class[] {}).invoke(target, new Object[] {})
							.toString()), target, field);
		} catch (Exception e1) {
			throw new RuntimeException("Unable to find the object get method",
					e1);
		}
	}

	/**
	 * Updates the target value
	 * 
	 */
	private void updateValue() {
		try {
			int val = Integer.parseInt(this.getText());
			method.invoke(this.target, new Object[] { val });
		} catch (NumberFormatException e) {
			// swallows the number format exception: ignores it
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
