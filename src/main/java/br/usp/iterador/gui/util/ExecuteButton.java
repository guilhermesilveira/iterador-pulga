package br.usp.iterador.gui.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.JButton;

import br.usp.iterador.i18n.Messages;

/**
 * A button with a callback to a message.
 * 
 * @author Guilherme Silveira
 */
public class ExecuteButton extends JButton {

	private static final long serialVersionUID = 1112287243853159722L;

	public ExecuteButton(String msg, final Object obj, final String methodName,
			final Object... params) {
		super(Messages.getString(msg));
		Method[] declaredMethods = obj.getClass().getDeclaredMethods();
		for (final Method met : declaredMethods) {
			if (met.getName().equals(methodName)) {
				addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						try {
							met.invoke(obj, params);
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					}

				});
				return;
			}
		}
		throw new RuntimeException("Unable to create button for method "
				+ methodName + " on " + obj.getClass().getName());
	}

}
