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
 * Loads a file.
 * 
 * @author Guilherme Silveira
 */
public class Load implements PulgaAction {

	private static final Logger logger = Logger.getLogger(Load.class);

	private Serializer serializer;

	public Load(Serializer serializer) {
		super();
		this.serializer = serializer;
	}

	public void execute(SimpleIteratorFrame frame, Pulga pulga) {
		Pref prefs = new Pref();
		String filename = prefs.getIo().get(Keys.lastSavedFile, ".");
		File file = new FileDialog().open(filename, frame);
		if (file != null) {
			logger.info("loading file " + file.getAbsolutePath());
			// WindowManager.closeWindows();
			try {
				Pulga newPulga = serializer.load(file);
				prefs.getIo().put(Keys.lastSavedFile, file.getAbsolutePath());
				newPulga.setCurrentFile(file);
			} catch (PulgaIOException e) {
				logger.error("Unable to load file", e);
				JOptionPane.showMessageDialog(frame, "Unable to load file");
			}
		}
	}

}
