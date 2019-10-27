package br.usp.iterador.plugin.export;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.plugin.tool.Tool;

/**
 * Export image toolbar icon.
 *
 * @author Guilherme Silveira
 */
public class ExportImageTool implements Tool {

	private final ExportImageData data;

	public ExportImageTool(ExportImageData data) {
		this.data = data;
	}

	public String getName() {
		return "export_image";
	}

	public String getTooltip() {
		return "export_image_tooltip";
	}

	public void activate(Controller controller) {
		ExportImageFrame frame = controller.newInstance(ExportImageFrame.class);
		frame.execute(this.data);
		controller.executeAction("ResetTool");
	}

	public void deactivate() {
	}

	public void onClick(int x, int y) {
	}

	public void mouseMoved(int x, int y) {
	}

	public Icon getIcon() {
		URL url = ExportImageTool.class.getResource("export.gif");
		return new ImageIcon(url);
	}

}
