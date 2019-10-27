package br.usp.iterador.plugin;

import br.usp.iterador.plugin.bacia.BasinPlugin;
import br.usp.iterador.plugin.bifurcationdiagram.DiagramPlugin;
import br.usp.iterador.plugin.connectpoints.ConnectPointsPlugin;
import br.usp.iterador.plugin.cor.BufferedColorChange;
import br.usp.iterador.plugin.cor.ColorPlugin;
import br.usp.iterador.plugin.edo.euler.EulerPlugin;
import br.usp.iterador.plugin.export.ExportImagePlugin;
import br.usp.iterador.plugin.initialcondition.InitialConditionPlugin;
import br.usp.iterador.plugin.mandelbrot.MandelbrotPlugin;
import br.usp.iterador.plugin.manifold.StableManifoldPlugin;
import br.usp.iterador.plugin.setiterator.InitialCurvePlugin;
import br.usp.iterador.plugin.zoom.ZoomPlugin;

/**
 * Plugin installer.
 * 
 * @author Guilherme Silveira
 */
public class PluginInstaller {

	public void registerAllPlugins(PluginManager pluginManager) {
		pluginManager.registerPlugin(MandelbrotPlugin.class);
		pluginManager.registerPlugin(BasinPlugin.class);
		pluginManager.registerPlugin(ColorPlugin.class);
		pluginManager.registerPlugin(ConnectPointsPlugin.class);
		pluginManager.registerPlugin(InitialConditionPlugin.class);
		pluginManager.registerPlugin(ExportImagePlugin.class);
		pluginManager.registerPlugin(BufferedColorChange.class);
		pluginManager.registerPlugin(ZoomPlugin.class);
		pluginManager.registerPlugin(DiagramPlugin.class);
		pluginManager.registerPlugin(StableManifoldPlugin.class);
		pluginManager.registerPlugin(InitialCurvePlugin.class);
		pluginManager.registerPlugin(EulerPlugin.class);
	}

}
