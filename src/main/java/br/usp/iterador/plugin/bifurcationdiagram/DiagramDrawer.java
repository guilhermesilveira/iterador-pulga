package br.usp.iterador.plugin.bifurcationdiagram;

import java.awt.Graphics2D;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.GUIHelper;
import br.usp.iterador.gui.ImageHandler;
import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.internal.logic.Drawer;
import br.usp.iterador.internal.logic.IterationExecuter;
import br.usp.iterador.internal.logic.ValueApplier;
import br.usp.iterador.logic.IterationListener;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.model.Scale;
import br.usp.iterador.plugin.PluginManager;

/**
 * Diagram drawer.
 * 
 * @author Guilherme Silveira
 */
public class DiagramDrawer implements PulgaAction {

	private static final Logger LOG = Logger.getLogger(DiagramDrawer.class);

	private ValueApplier applier;

	public void execute(Application app, PluginManager manager,
			SimpleIteratorFrame frame) {

		applier = new ValueApplier(app);

		Scale scale = app.getXScale();
		int width = app.getImage().getWidth();

		Graphics2D g = ImageHandler.getCurrentImage().getGraphics();
		ImageInfo info = app.getImage();
		IterationListener listener = manager.getIterationListener(app, true);
		listener.init(g, new GUIHelper(info.getWidth(), info.getHeight()));
		
		String field = app.getXScale().getField();
		
		double originalValue = applier.getValue(field);

		try {
			Drawer drawer = new Drawer(app, listener, true, width, info
					.getHeight());
			drawer.init(g);
			IterationExecuter executer = new IterationExecuter(app);
			for (int pos = 0; pos < width; pos++) {
				double parameterValue = scale.getMin()
						+ (scale.getMax() - scale.getMin())
						* ((pos + 0.0) / width);
				iterate(field, executer, parameterValue, drawer, g, info, frame);
			}
		} catch (CompileTimeException e) {
			LOG.error("Unable to compile iteration code", e);
		}
		applier.applyValue(field, originalValue);
	}

	public void iterate(String field, IterationExecuter executer, double x,
			Drawer drawer, Graphics2D graphics, ImageInfo info,
			SimpleIteratorFrame frame) {

		// applies both values
		applier.applyValue(field, x);

		try {
			drawer.paint(graphics);
			frame.getCanvas().repaint();
		} catch (Exception e) {
			LOG.error("Unable to iterate", e);
		}
	}

}
