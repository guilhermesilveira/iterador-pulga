package br.usp.iterador.logic.ioc;

import org.picocontainer.defaults.DefaultPicoContainer;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;

/**
 * A pico container implementation of context.
 *
 * @author Guilherme Silveira
 * @since 1.2
 */
public class PicoContext implements Context {

    private static final Logger LOG = Logger.getLogger(PicoContext.class);

    private final DefaultPicoContainer container = new DefaultPicoContainer();

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> type) {
		return (T) container.getComponentInstanceOfType(type);
	}

	public <T> void put(Class<T> type, Object obj) {
		container.registerComponentInstance(obj);
	}

	public <T> void remove(Object obj) {
		container.unregisterComponent(obj);
	}

	public <T> void put(Class<T> type) {
		container.registerComponentImplementation(type, type);
	}

    public <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<?>[] declaredConstructors = clazz
                    .getDeclaredConstructors();
            for (Constructor<?> constructor : declaredConstructors) {
                Class<?>[] param = constructor.getParameterTypes();
                Object[] paramValue = new Object[param.length];
                for (int i = 0; i < param.length; i++) {
                    paramValue[i] = get(param[i]);
                }
                constructor.setAccessible(true);
                return (T) constructor.newInstance(paramValue);
            }
            LOG.error("Unable to instantiate (no nice constructor found) "
                    + clazz.getName());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.debug("registered values are " + container.toString());
        throw new RuntimeException(
                "Unable to instantiate (no nice constructor found) ");
    }

}
