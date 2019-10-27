package br.usp.iterador.plugin.export;

import java.io.File;
import java.io.Serializable;

/**
 * Export image data
 * 
 * @author Guilherme Silveira
 * @version $Revision$
 */
public class ExportImageData implements Serializable {

	private static final long serialVersionUID = 10000L;

	private File filename = new File(new File(""), "export.png");

	/**
	 * @return Returns the filename.
	 */
	public File getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            The filename to set.
	 */
	public void setFilename(File filename) {
		this.filename = filename;
	}

}
