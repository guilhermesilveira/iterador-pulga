package br.usp.iterador.tool;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import br.usp.iterador.gui.util.ScaleHelper;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Tool which shows the point coordinate when clicked.
 * 
 * @author Guilherme Silveira
 */
public class ShowPointTool implements Tool {

	private Application app;

	public ShowPointTool(Application app) {
		this.app = app;
	}

	public String getName() {
		return "show_point";
	}

	public String getTooltip() {
		return "show_point_tooltip";
	}

	public void activate(Controller controller) {
	}

	public void deactivate() {
	}

	public void onClick(int pointX, int pointY) {

		ImageInfo info = app.getImage();
		int width = info.getWidth();
		int height = info.getHeight();

		ScaleHelper scaleHelper = new ScaleHelper(app.getXScale(), app
				.getYScale());
		double p[] = scaleHelper.fromViewToWorld(pointX, width, pointY, height);

		JOptionPane.showMessageDialog(null, "You have chosen " + p[0] + ", "
				+ p[1]);

	}

	/**
	 * Does nothing
	 * 
	 * @see br.usp.iterador.plugin.tool.Tool#mouseMoved(int, int)
	 */
	public void mouseMoved(int x, int y) {
	}

	/**
	 * Returns its icon
	 * 
	 * @see br.usp.iterador.plugin.tool.Tool#getIcon()
	 */
	public Icon getIcon() {
		return new ImageIcon(ShowPointTool.class.getResource("point.gif"));
	}

}
