package br.usp.iterador.logic.system;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.IteratorFrame;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.plugin.PluginManager;

/**
 * Redraws.
 *
 * @author Guilherme Silveira
 */
public class Redraw implements PulgaAction {

	private static final Logger LOG = Logger.getLogger(Redraw.class);

	public void execute(IteratorFrame frame, PluginManager manager) {
		try {
			frame.updateDrawing(manager);
		} catch (Exception e) {
			LOG.error("Error trying to update drawing", e);
		}
	}

}
