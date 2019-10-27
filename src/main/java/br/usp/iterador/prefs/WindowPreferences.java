package br.usp.iterador.prefs;

import javax.swing.JFrame;

public class WindowPreferences {

	private final Preferences pref;

	private final JFrame window;

	public WindowPreferences(JFrame w, String key) {
		this.window = w;
		pref = new Preferences(w.getClass().getName() + "." + key);
	}

	public void loadConfig() {
		window.setLocation(pref.getInt("x", window.getX()), pref.getInt("y",
				window.getY()));
		window.setSize(pref.getInt("width", window.getWidth()), pref.getInt(
				"height", window.getHeight()));
	}

	public void saveConfig() {
		pref.put("x", window.getX());
		pref.put("y", window.getY());
		pref.put("width", window.getWidth());
		pref.put("height", window.getHeight());
	}

}
