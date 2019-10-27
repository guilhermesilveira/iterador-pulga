package br.usp.iterador.logic.image;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.IteratorFrame;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;

/**
 * Updates the buffer size.
 *
 * @author Guilherme Silveira
 */
public class UpdateSize implements PulgaAction {

	private static final Logger LOG = Logger.getLogger(UpdateSize.class);

	public void execute(IteratorFrame window, Application app) {
		ImageInfo info = app.getImage();
		LOG.debug("New buffer size is " + info.getWidth() + "x"
				+ info.getHeight());
		window.getCanvas().changeSize(info.getWidth(), info.getHeight());
		window.reorganize();
		window.repaint();
	}

}
