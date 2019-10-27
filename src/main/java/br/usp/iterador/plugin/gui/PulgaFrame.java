package br.usp.iterador.plugin.gui;

import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.JFrame;

import br.usp.iterador.i18n.Messages;
import br.usp.iterador.prefs.WindowPreferences;

/**
 * Pulga frame with i18n and close operation.
 * 
 * @author Guilherme Silveira
 */
public class PulgaFrame extends JFrame {

	private static final long serialVersionUID = 8270794029115823125L;

	private final String key;

	private final WindowManager windows;

	protected PulgaFrame(WindowManager windows, String key, String title) {
		super(Messages.getString(title));
		this.windows = windows;
		this.key = key;
	}

	protected PulgaFrame(WindowManager windows, String key, String title,
			Container pane, int width, int height) throws HeadlessException {
		this(windows, key, title);
		setSize(width, height);
		WindowPreferences preferences = new WindowPreferences(this, key);
		preferences.loadConfig();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(pane);
		setVisible(true);
	}

	@Override
	public void dispose() {
		WindowPreferences preferences = new WindowPreferences(this, key);
		preferences.saveConfig();
		super.dispose();
		windows.refreshWindows();
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			windows.showingFrame(key, this);
		}
	}

	public void rebuild(Container container) {
		setContentPane(container);
		validate();
	}

}
