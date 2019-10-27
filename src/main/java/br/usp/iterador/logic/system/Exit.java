package br.usp.iterador.logic.system;

import javax.swing.JOptionPane;

import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.i18n.Messages;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.plugin.gui.WindowManager;

/**
 * Saves and exits the program.
 * 
 * @author Guilherme Silveira
 */
public class Exit implements PulgaAction {

	public void execute(Controller controller, SimpleIteratorFrame frame,
			WindowManager windows) {
		if (Exit.askForSaveAndShouldCancel(controller,
				"save_before_exit_question", "exit")) {
			return;
		}
		windows.closeWindows();
		frame.dispose();
	}

	static boolean askForSaveAndShouldCancel(Controller controller,
			String string, String string2) {
		int result = JOptionPane.showConfirmDialog(null, Messages
				.getString("save_before_exit_question"), Messages
				.getString("exit"), JOptionPane.YES_NO_CANCEL_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			controller.executeAction("save");
		}
		return result == JOptionPane.CANCEL_OPTION;
	}

}
