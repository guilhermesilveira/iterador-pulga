package br.usp.iterador.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;

/**
 * Some reflection util methods.
 * 
 * @author Guilherme Silveira
 * 
 */
public class ReflectionUtil {

	public static void set(Object data, String field, Class name, Object value) {
		try {
			data.getClass().getMethod("set" + field, new Class[] { name })
					.invoke(data, new Object[] { value });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ActionListener getCallback(final Object obj,
			final String method) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					obj.getClass().getMethod(method).invoke(obj);
				} catch (Exception ex) {
					logger.error(ex);
				}
			}
		};
	}

	private static final Logger logger = Logger.getLogger(ReflectionUtil.class);

	public static Object newInstance(String name) throws ReflectionException {
		try {
			return Class.forName(name).newInstance();
		} catch (InstantiationException e) {
			throw new ReflectionException(
					"Unable to instantiate class " + name, e.getCause());
		} catch (IllegalAccessException e) {
			throw new ReflectionException(
					"Unable to instantiate class " + name, e);
		} catch (ClassNotFoundException e) {
			throw new ReflectionException(
					"Unable to instantiate class " + name, e);
		}
	}

}
