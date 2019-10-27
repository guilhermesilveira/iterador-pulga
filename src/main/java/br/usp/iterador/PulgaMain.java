package br.usp.iterador;

import br.usp.iterador.logic.Controller;
import br.usp.iterador.logic.DefaultController;
import br.usp.iterador.logic.ioc.PicoContext;
import br.usp.iterador.model.Application;

/**
 * Execution program.
 *
 * @author Guilherme Silveira
 */
public class PulgaMain {

    public static void main(String[] args) {

		CommandLine line = new CommandLine(args);
		if (line.isDebugOn()) {
            Log4JConfig log4jconfig = new Log4JConfig();
            log4jconfig.changeState();
		}
        Controller controller = new DefaultController(new PicoContext());
        Pulga pulga = new Pulga(controller, new Application());
        pulga.show();
	}

}
