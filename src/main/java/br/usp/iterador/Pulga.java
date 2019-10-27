package br.usp.iterador;

import java.io.File;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.IteratorFrame;
import br.usp.iterador.gui.PanelBuilder;
import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.gui.ToolBarBuilder;
import br.usp.iterador.gui.menu.MenuBuilder;
import br.usp.iterador.gui.menu.MenuLoader;
import br.usp.iterador.gui.util.GuiFactory;
import br.usp.iterador.i18n.Messages;
import br.usp.iterador.io.Serializer;
import br.usp.iterador.jxpath.DefaultExpressionEvaluator;
import br.usp.iterador.jxpath.ReflectionResolver;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.DefaultPluginManager;
import br.usp.iterador.plugin.MenuManager;
import br.usp.iterador.plugin.PanelManager;
import br.usp.iterador.plugin.PluginActivationException;
import br.usp.iterador.plugin.PluginInstaller;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.plugin.PluginSerializer;
import br.usp.iterador.plugin.gui.WindowManager;
import br.usp.iterador.plugin.tool.DefaultToolRepository;
import br.usp.iterador.plugin.tool.ToolManager;

public class Pulga {

	private static final Logger LOG = Logger.getLogger(Pulga.class);

	private final PluginManager pluginManager;

	private final Application application;

	private String currentFile = new File(".").getAbsolutePath();

	private final Controller controller;

	public Pulga(Controller controller, Application application) {
		this.application = application;
		this.controller = controller;
        controller.registerDI(Log4JConfig.class);
		// window manager
		controller.registerDI(WindowManager.class);
		// base
		controller.registerDI(controller);
		controller.registerDI(application);
		// gui
		controller.registerDI(GuiFactory.class);
		// reflection
		controller.registerDI(ReflectionResolver.class);
		controller.registerDI(DefaultExpressionEvaluator.class);
		// managers
		controller.registerDI(DefaultToolRepository.class);
		controller.registerDI(ToolManager.class);
		controller.registerDI(MenuBuilder.class);
		controller.registerDI(MenuManager.class);
		controller.registerDI(MenuLoader.class);
		pluginManager = controller.newInstance(DefaultPluginManager.class);
		controller.registerDI(pluginManager);
		// gui
		controller.registerDI(PanelBuilder.class);
		controller.registerDI(PanelManager.class);
		// register plugins
		new PluginInstaller().registerAllPlugins(pluginManager);
		// io
		controller.registerDI(PluginSerializer.class);
		controller.registerDI(Serializer.class);
		// itself
		controller.registerDI(this);

		controller.registerDI(ToolBarBuilder.class);

		// resets the tool
		controller.executeAction("ResetTool");
		controller.registerDI(SimpleIteratorFrame.class);

    }

	/**
	 * Displays the frame
	 */
	public void show() {
		try {
			pluginManager.reset();
			controller.find(IteratorFrame.class).reorganize();
		} catch (PluginActivationException e) {
			LOG.error("Error initializing plugins", e);
		}
	}

	public Application getApplication() {
		return application;
	}

	public Controller getController() {
		return controller;
	}

	public void setCurrentFile(File file) {
		controller.find(IteratorFrame.class).setTitle(
				Messages.getString("Iterador.title") + " - " + file.getName());
		this.currentFile = file.getAbsolutePath();
	}

	public String getCurrentFile() {
		return currentFile;
	}
}
