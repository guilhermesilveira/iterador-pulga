package br.usp.iterador.logic.system;

import org.apache.log4j.Logger;

import br.usp.iterador.Pulga;
import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.DefaultController;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;

/**
 * New system.
 * 
 * @author Guilherme Silveira
 */
public class New implements PulgaAction {

	private static final Logger logger = Logger.getLogger(New.class);

	public void execute(Controller oldController, Pulga oldPulga) {
		logger.debug("Creating new pulga");
		Pulga pulga = new Pulga(new DefaultController(), new Application());
		pulga.show();
	}

}
