package br.usp.iterador.logic.system;

import java.io.File;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import br.usp.iterador.Pulga;
import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.io.FileDialog;
import br.usp.iterador.io.PulgaIOException;
import br.usp.iterador.io.Serializer;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.prefs.Keys;
import br.usp.iterador.prefs.Pref;

/**
 * Saves a file as something else.
 * 
 * @author Guilherme Silveira
 */
public class SaveAs implements PulgaAction {

	private static final Logger logger = Logger.getLogger(SaveAs.class);

	private Serializer serializer;

	public SaveAs(Serializer serializer) {
		super();
		this.serializer = serializer;
	}

	public void execute(SimpleIteratorFrame frame, Pulga pulga) {

		Pref prefs = new Pref();
		String filename = prefs.getIo().get(Keys.lastSavedFile, ".");
		File file = new FileDialog().saveAs(filename, frame);
		if (file == null)
			return;
		if (file.exists()
				&& JOptionPane
						.showConfirmDialog(frame,
								"Overwrite the existing file?", "Save as",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
			return;

		try {
			serializer.save(file);
		} catch (PulgaIOException e) {
			logger.error("Unable to save file", e);
			JOptionPane.showMessageDialog(frame, "Unable to save file");
		}
		prefs.getIo().put(Keys.lastSavedFile, file.getAbsolutePath());
		pulga.setCurrentFile(file);

	}

}
