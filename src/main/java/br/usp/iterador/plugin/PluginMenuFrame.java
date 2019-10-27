package br.usp.iterador.plugin;

import java.awt.event.MouseListener;

import br.usp.iterador.gui.PulgaCanvas;


/**
 * 
 * @author Guilherme Silveira
 */
public interface PluginMenuFrame {

    public abstract void updateMenu();

	public abstract void addMouseListener(MouseListener mouseListener);

	public abstract void removeMouseListener(MouseListener mouseListener);

	public abstract void repaint();

	public PulgaCanvas getCanvas();

}
