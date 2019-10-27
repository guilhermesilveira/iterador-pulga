package br.usp.iterador.logic;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.ShowCodeFrame;
import br.usp.iterador.gui.ShowFormulaFrame;
import br.usp.iterador.gui.ShowInformationFrame;
import br.usp.iterador.gui.ShowIterationsFrame;
import br.usp.iterador.logic.image.ClearWindow;
import br.usp.iterador.logic.image.ExportPoints;
import br.usp.iterador.logic.image.ProportionalPlot;
import br.usp.iterador.logic.image.UpdateSize;
import br.usp.iterador.logic.ioc.Context;
import br.usp.iterador.logic.ioc.PicoContext;
import br.usp.iterador.logic.system.*;
import br.usp.iterador.plugin.ShowPluginManager;
import br.usp.iterador.plugin.bacia.gui.BasicConfigurationFrame;
import br.usp.iterador.plugin.bacia.gui.ExecuteBasin;
import br.usp.iterador.plugin.bacia.gui.ShowAverageSamples;

/**
 * System controller.
 *
 * @author Guilherme Silveira
 */
public class DefaultController implements Controller {

	private static final Logger LOG = Logger.getLogger(DefaultController.class);

	public static final String REDRAW = "redraw";

	public static final String SHOW_FORMULA_FRAME = "showFormulaFrame";

    private final ActionHandler actions;

	private final Context context;

	public DefaultController() {
		this(new PicoContext());
	}

	public DefaultController(Context context) {
		this.context = context;
        this.actions = new DefaultActionHandler(context);

		registerAction("exit", Exit.class);
		registerAction("save", Save.class);
		registerAction("save_as", SaveAs.class);
		registerAction("load", Load.class);
		registerAction("about", About.class);
		registerAction(REDRAW, Redraw.class);
		registerAction("new", New.class);
		registerAction("clear", ClearWindow.class);
		registerAction("basic/exportPoints", ExportPoints.class);
		registerAction("basic/updateSize", UpdateSize.class);
		registerAction("image/proportionalPlot", ProportionalPlot.class);
		registerAction("changeLogState", ChangeLogState.class);
		registerAction("ResetTool", ResetTool.class);
		registerAction("UpdateMenu", UpdateMenu.class);

		registerAction("showInformation", ShowInformationFrame.class);
		registerAction("showIterations", ShowIterationsFrame.class);
		registerAction("showPieceOfCode", ShowCodeFrame.class);
		registerAction(SHOW_FORMULA_FRAME, ShowFormulaFrame.class);

		// plugin manager
		registerAction("showPluginFrame", ShowPluginManager.class);

		// boa
		registerAction("showBOAConfiguration", BasicConfigurationFrame.class);
		registerAction("boa.showAverageSamples", ShowAverageSamples.class);
		registerAction("boa.executeBasin", ExecuteBasin.class);

	}

	public void executeAction(String cmd) {
        actions.executeAction(cmd);
    }

	@SuppressWarnings("unchecked")
	public <T> T find(Class<T> type) {
		return context.get(type);
	}

	@SuppressWarnings("unchecked")
	public <T> T newInstance(Class<T> clazz) {
        return context.newInstance(clazz);
    }

	public void registerAction(String key, Class<? extends PulgaAction> action) {
		actions.put(key, action);
	}

	public void registerDI(Object value) {
		Class<? extends Object> key = value.getClass();
		LOG.debug("registering " + key);
		context.put(key, value);
	}

	public void unregisterAction(String key) {
		actions.remove(key);
	}

	public void unregisterIOC(Object o) {
		context.remove(o);
	}

	public <T> void registerDI(Class<T> type) {
		if(type.isAnnotationPresent(Component.class)) {
			Component annotation = type.getAnnotation(Component.class);
			if(annotation.autoCreate()) {
				registerDI(newInstance(type));
				return;
			}
		}
		LOG.debug("registering without instance " + type);
		context.put(type);
	}

}
