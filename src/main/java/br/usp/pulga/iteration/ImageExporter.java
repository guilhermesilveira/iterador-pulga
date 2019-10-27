package br.usp.pulga.iteration;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;

public class ImageExporter {

    private final static Logger LOG = Logger.getLogger(ImageExporter.class);

    private final File file;

    private BufferedImage exportImage;

    public ImageExporter(File file) {
        this.file = file;
    }

    public PGraphics getGraphics(int w, int h) {
        this.exportImage = new BufferedImage(w, h, ColorSpace.TYPE_RGB);
        return new PGraphics(exportImage.createGraphics(), w, h);
    }

    public void save() {
        try {
            String extension = "png";
            ImageWriter writer = (ImageWriter) ImageIO
                    .getImageWritersByFormatName(extension).next();
            LOG.info("exporting image to file: " + file.getAbsolutePath());
            ImageOutputStream ios = ImageIO.createImageOutputStream(file);
            writer.setOutput(ios);
            writer.write(exportImage);
            ios.close();
        } catch (Exception e) {
            LOG.error("error exporting image", e);
        }
    }

}
