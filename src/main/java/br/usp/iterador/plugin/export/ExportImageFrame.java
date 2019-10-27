package br.usp.iterador.plugin.export;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import br.usp.iterador.gui.ImageHandler;
import br.usp.iterador.gui.SimplePanel;
import br.usp.iterador.gui.util.CloseButton;
import br.usp.iterador.gui.util.ExecuteButton;
import br.usp.iterador.gui.util.FileField;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.model.ImageInfo;
import br.usp.iterador.plugin.gui.PulgaFrame;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Export image frame.
 * 
 * @author Guilherme Silveira
 */
public class ExportImageFrame implements PulgaAction {

	private static final Logger LOG = Logger.getLogger(ExportImageFrame.class);

	private ExportImageData data;

	private PulgaFrame frame;

	private Application app;

	private WindowManager windows;

	public ExportImageFrame(Application app, WindowManager windows) {
		this.app = app;
		this.windows = windows;
	}

	public void execute(ExportImageData data) {
		this.data = data;
		this.frame = windows.showFrame("exportimage.title",
				"exportimage.title", getMainPane(), 640, 140);
	}

	private JPanel getMainPane() {
		JPanel panel = new JPanel();
		panel.add(getMiddle(), BorderLayout.CENTER);
		panel.add(getButtons(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel getMiddle() {
		JPanel panel = new JPanel(new GridLayout(1, 1));
		JPanel mpanel = new SimplePanel("file", new FileField(this.data
				.getFilename(), this.data, "Filename", ".png"));
		panel.add(mpanel, BorderLayout.NORTH);
		return panel;
	}

	private JPanel getButtons() {
		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.add(new ExecuteButton("exportimage.export", this, "export"));
		panel.add(new CloseButton());
		return panel;
	}

	public void export() {
		ImageInfo info = app.getImage();
		int w = info.getWidth();
		int h = info.getHeight();
		BufferedImage exportImage = new BufferedImage(w, h, ColorSpace.TYPE_RGB);
		Graphics2D g = exportImage.createGraphics();
		g.drawRenderedImage(ImageHandler.getCurrentImage().getBuffer(), null);

		try {
			String extension = "png";
			ImageWriter writer = (ImageWriter) ImageIO
					.getImageWritersByFormatName(extension).next();
			File filename = data.getFilename();
			LOG.info("exporting image to file: " + filename.getAbsolutePath());
			ImageOutputStream ios = ImageIO.createImageOutputStream(filename);
			writer.setOutput(ios);
			writer.write(exportImage);
			ios.close();
		} catch (Exception e) {
			LOG.error("error exporting image", e);
		}
	}

}
