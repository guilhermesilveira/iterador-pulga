package br.usp.iterador.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Deals with i18n messages.
 * 
 * @author Guilherme Silveira
 */
public class Messages {

	/** the bundle base */
	private static final String BUNDLE_NAME = "messages";

	/** the bundle itself */
	private static ResourceBundle resourceBundle = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	private static final Logger logger = Logger.getLogger(Messages.class);

	/**
	 * Returns a message value.
	 */
	public static String getString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			logger.error("Unable to find key " + key);
			return '!' + key + '!';
		}
	}

	/**
	 * Changes the current bundle.
	 * 
	 * @param language.
	 */
	public static void changeBundle(Locale language) {
		Messages.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME,
				language);
	}
}
