package br.usp.iterador.io;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Extended properties class.
 * 
 * @author Guilherme Silveira
 */
public class SimpleProperties {

	private static final Logger logger = Logger
			.getLogger(SimpleProperties.class);

	private Properties properties = new Properties();

	public SimpleProperties(String configFile) {
		try {
			properties.load(SimpleProperties.class
					.getResourceAsStream(configFile));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String getString(String key) {
		return properties.getProperty(key);
	}

	public String[] getStringArray(String key) {
		String value = properties.getProperty(key);
		if (value == null || value.trim().equals("")) {
			return new String[0];
		}
		return value.split(",");
	}

	public boolean getBoolean(String key, boolean b) {
		return getString(key) == null ? b : getString(key).toLowerCase()
				.equals("true");
	}

}
