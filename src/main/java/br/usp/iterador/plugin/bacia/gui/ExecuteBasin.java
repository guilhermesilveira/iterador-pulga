package br.usp.iterador.plugin.bacia.gui;

import java.awt.Graphics2D;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.ImageHandler;
import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.bacia.Basin;
import br.usp.iterador.plugin.bacia.BasinController;
import br.usp.iterador.plugin.bacia.BasinTask;
import br.usp.iterador.plugin.gui.WindowManager;
import br.usp.iterador.task.LongTaskManager;

public class ExecuteBasin implements PulgaAction {

	private static final Logger logger = Logger.getLogger(ExecuteBasin.class);

	public void execute(Application app, Basin basin,
			BasinController controller, WindowManager windows,
			SimpleIteratorFrame frame) {
		try {
			Graphics2D graphics = ImageHandler.getCurrentImage().getGraphics();
			BasinTask task = new BasinTask(basin, app, graphics, controller);
			LongTaskManager manager = new LongTaskManager(windows, task, frame);
			manager.start();
		} catch (CompileTimeException e1) {
			logger.error("Compilation problem ", e1);
		}
	}

}
