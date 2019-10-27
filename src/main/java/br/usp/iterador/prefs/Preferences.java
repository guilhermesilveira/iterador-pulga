package br.usp.iterador.prefs;

import java.util.prefs.BackingStoreException;

import org.apache.log4j.Logger;

/**
 * Responsible for dealing with preferences.
 * 
 * @author Guilherme Silveira
 */
public class Preferences {

	private static Logger LOG = Logger.getLogger(Preferences.class);

	/**
	 * The user root node.
	 */
	private static java.util.prefs.Preferences userRoot = java.util.prefs.Preferences
			.userRoot();

	/**
	 * The specific node.
	 */
	private java.util.prefs.Preferences node;

	public Preferences(String nodeName) {
		this.node = userRoot.node("/" + Preferences.class.getName() + "/"
				+ nodeName.replace('.', '/'));
	}

	/**
	 * Returns some value
	 * 
	 * @param clazz
	 *            the class itself
	 * @param key
	 *            the key
	 * @param defaultValue
	 *            the default value
	 * @return the value
	 */
	public String get(String key, String defaultValue) {
		return node.get(key, defaultValue);
	}

	public int getInt(String key, int defaultValue) {
		return Integer.parseInt(get(key, "" + defaultValue));
	}

	/**
	 * Sets a preference property
	 * 
	 * @param clazz
	 * @param key
	 * @param value
	 */
	public void put(String key, String value) {
		node.put(key, value);
		LOG.debug("adding preferences " + key + " with value " + value);
		try {
			node.flush();
		} catch (BackingStoreException e) {
			LOG.error("Unable to store preferences", e);
		}
	}

	public void put(String key, int value) {
		put(key, "" + value);
	}

}
