package br.usp.iterador.prefs;

import javax.swing.JFrame;

import junit.framework.TestCase;

public class WindowPreferencesTest extends TestCase {

	public class MyFrame extends JFrame {
		private static final long serialVersionUID = -5715014167378448036L;
	}

	public void testDoesntFindWidth() {
		MyFrame window = new MyFrame();
		window.setSize(101, 102);
		WindowPreferences pref = new WindowPreferences(window, "myKey");
		pref.loadConfig();
		assertEquals(101, window.getWidth());
	}

	public void testDoesntFindHeight() {
		MyFrame window = new MyFrame();
		window.setSize(101, 102);
		WindowPreferences pref = new WindowPreferences(window, "myKey");
		pref.loadConfig();
		assertEquals(102, window.getHeight());
	}

	public void testDoesntFindX() {
		MyFrame window = new MyFrame();
		window.setLocation(103, 104);
		WindowPreferences pref = new WindowPreferences(window, "myKey");
		pref.loadConfig();
		assertEquals(103, window.getX());
	}

	public void testDoesntFindY() {
		MyFrame window = new MyFrame();
		window.setLocation(103, 104);
		WindowPreferences pref = new WindowPreferences(window, "myKey");
		pref.loadConfig();
		assertEquals(104, window.getY());
	}

	public void testFindsWidth() {
		MyFrame window = new MyFrame();
		window.setSize(101, 102);
		WindowPreferences pref = new WindowPreferences(window, "myKeyFound");
		pref.saveConfig();
		window.setSize(100, 100);
		pref.loadConfig();
		assertEquals(101, window.getWidth());
	}

	public void testFindsHeight() {
		MyFrame window = new MyFrame();
		window.setSize(101, 102);
		WindowPreferences pref = new WindowPreferences(window, "myKeyFound");
		pref.saveConfig();
		window.setSize(100, 100);
		pref.loadConfig();
		assertEquals(102, window.getHeight());
	}

	public void testFindsX() {
		MyFrame window = new MyFrame();
		window.setLocation(103, 104);
		WindowPreferences pref = new WindowPreferences(window, "myKeyFound");
		pref.saveConfig();
		window.setLocation(100, 100);
		pref.loadConfig();
		assertEquals(103, window.getX());
	}

	public void testFindsY() {
		MyFrame window = new MyFrame();
		window.setLocation(103, 104);
		WindowPreferences pref = new WindowPreferences(window, "myKeyFound");
		pref.saveConfig();
		window.setLocation(100, 100);
		pref.loadConfig();
		assertEquals(104, window.getY());
	}

}
