package br.usp.iterador.logic;

/**
 * An action handler.
 * 
 * @since 1.4
 */
public interface ActionHandler {

    void executeAction(String cmd);

    void remove(String key);

    void put(String key, Class<? extends PulgaAction> action);
}
