package br.usp.iterador.io;

import static br.usp.iterador.io.IteratorFileFilter.EXTENSION;
import static javax.swing.JFileChooser.CANCEL_OPTION;

import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;

/**
 * Deals with file dialogs.
 * 
 * @author Guilherme Silveira
 */
public class FileDialog {

	/**
	 * Shows the open dialog.
	 * 
	 * @return returns the selected file
	 */
	public synchronized File open(String file, Window w) {
		JFileChooser c = new JFileChooser(new File(file)); //$NON-NLS-1$
		c.setFileFilter(new IteratorFileFilter());
		int result = c.showOpenDialog(w);
		if (result == CANCEL_OPTION) {
			return null;
		}
		return c.getSelectedFile();
	}

	/**
	 * Shows the save as dialog.
	 * 
	 * @param frame
	 *            the current frame
	 * @return the new selected file
	 */
	public File saveAs(String lastFile, Window w) {
		JFileChooser c = new JFileChooser(new File(lastFile));
		c.setFileFilter(new IteratorFileFilter());
		int result = c.showSaveDialog(w);
		if (result == CANCEL_OPTION) {
			return null;
		}
		File file = c.getSelectedFile();
		if (!file.getName().endsWith(EXTENSION)) {
			file = new File(file.getParentFile(), file.getName() + EXTENSION);
		}
		return file;
	}

}
