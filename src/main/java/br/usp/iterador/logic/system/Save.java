package br.usp.iterador.logic.system;

import java.io.File;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import br.usp.iterador.Pulga;
import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.io.PulgaIOException;
import br.usp.iterador.io.Serializer;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.prefs.Keys;
import br.usp.iterador.prefs.Pref;

/**
 * Saves a file.
 * 
 * @author Guilherme Silveira
 */
public class Save implements PulgaAction {

	private static final Logger logger = Logger.getLogger(Save.class);

	public void execute(Pulga pulga, SimpleIteratorFrame frame,
			Controller controller, Serializer serializer) {
		String arq = pulga.getCurrentFile();
		if (arq == null
				|| (new File(arq).exists() && new File(arq).isDirectory())) {
			logger.info("Unable to save: using SAVE-AS");
			controller.executeAction("save_as");
			return;
		}
		Pref prefs = new Pref();
		try {
			serializer.save(new File(arq));
		} catch (PulgaIOException e) {
			logger.error("Unable to save file", e);
			JOptionPane.showMessageDialog(frame, "Unable to save file");
		}
		pulga.setCurrentFile(new File(arq));
		prefs.getIo().put(Keys.lastSavedFile, arq);
	}

}
