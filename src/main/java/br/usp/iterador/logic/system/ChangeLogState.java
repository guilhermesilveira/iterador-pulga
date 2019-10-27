package br.usp.iterador.logic.system;

import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.Log4JConfig;

public class ChangeLogState implements PulgaAction {

    private final Log4JConfig config;

    public ChangeLogState(Log4JConfig config) {
        this.config = config;
    }

    public void execute() {
        config.changeState();
    }

}
