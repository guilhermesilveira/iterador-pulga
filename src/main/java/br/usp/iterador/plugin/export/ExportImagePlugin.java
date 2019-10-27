package br.usp.iterador.plugin.export;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;

import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.plugin.Plugin;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Exports an image.
 * 
 * @author Guilherme Silveira
 */
public class ExportImagePlugin implements Plugin {

	private ExportImageData data = new ExportImageData();

	public JMenu getMenu(MenuBuilder menuBuilder) {
		return null;
	}

	/**
	 * Returns its iteration listener
	 * 
	 * @see br.usp.iterador.plugin.Plugin#getIterationListener()
	 */
	public IterationListener getIterationListener() {
		return null;
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public Serializable getSerializedData() {
		return this.data;
	}

	/**
	 * Sets its internal data by reading from a serialized object
	 * 
	 * @see br.usp.iterador.plugin.Plugin#activate(java.lang.Object)
	 */
	public void activate(Object data) {
		this.data = (ExportImageData) data;
	}

	/**
	 * Returns the list of scripted objects
	 */
	public Map<String, Object> getScriptObjects() {
		return new HashMap<String, Object>();
	}

	public Map<String, Class<? extends Object>> getAliases() {
		HashMap<String, Class<? extends Object>> map = new HashMap<String, Class<? extends Object>>();
		map.put("export-image-plugin", ExportImageData.class);
		return map;
	}

	public Tool[] getTools() {
		return new Tool[] { new ExportImageTool(this.data) };
	}

}
