package br.usp.iterador.plugin.gui;

import java.awt.Container;
import java.awt.Window;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.SimpleIteratorFrame;

/**
 * Window manager.
 *
 * @author Guilherme Silveira
 */
public class WindowManager {

    private final Map<String, PulgaFrame> windows = new HashMap<String, PulgaFrame>();

    private static final Logger LOG = Logger.getLogger(WindowManager.class);

    public WindowManager() {
        LOG.debug("Starting a new window manager");
    }

    /**
     * Shows a pulga frame.
     */
    public synchronized PulgaFrame showFrame(String key, String title,
                                             Container container, int width, int height) {
        if (!windows.containsKey(key)) {
            PulgaFrame frame = new PulgaFrame(this, key, title, container, width,
                    height);
            windows.put(key, frame);
        }
        PulgaFrame frame = windows.get(key);
        return frame;
    }

    /**
     * Shows a pulga frame.
     */
    public synchronized PulgaFrame showFrame(String key, Object factory,
                                             String factoryMethod) {
        if (!windows.containsKey(key)) {
            try {
                PulgaFrame frame = (PulgaFrame) factory.getClass().getMethod(
                        factoryMethod).invoke(factory);
                windows.put(key, frame);
            } catch (Exception e) {
                LOG.error("Unable to instantiate frame " + key, e);
            }
        }
        return windows.get(key);
    }

    public synchronized PulgaFrame showFrame(String key, String title,
                                             int width, int height) {
        return showFrame(key, title, new JPanel(), width, height);
    }

    /**
     * Refreshes all windows.
     */
    public void refreshWindows() {
        for (Iterator it = windows.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            Window w = windows.get(key);
            if (w.isShowing() && w.isDisplayable() && w.isVisible()) {
                w.repaint();
            } else {
                it.remove();
            }
        }
    }

    /**
     * Closes all windows except the main one.
     */
    public void closeWindows() {
        for (PulgaFrame w : windows.values()) {
            if (!(w instanceof SimpleIteratorFrame)) {
                w.dispose();
            }
        }
    }

    protected synchronized void showingFrame(String key, PulgaFrame frame) {
        windows.put(key, frame);
	}

}
