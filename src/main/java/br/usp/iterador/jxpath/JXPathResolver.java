package br.usp.iterador.jxpath;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class JXPathResolver {

	private static final Logger logger = Logger.getLogger(JXPathResolver.class);

	private Object obj;

	private Map<String, RealField> cache = new HashMap<String, RealField>();

	public JXPathResolver(Object obj) {
		logger.debug("JXPath on " + obj);
		this.obj = obj;
	}

	/**
	 * Finds a field
	 */
	public RealField resolve(String path) throws JXPathException {
		logger.debug("Resolving " + path);
		path = path.trim();
		if (!cache.containsKey(path)) {
			String val[] = path.split("\\.");
			Object obj = this.obj;
			for (int i = 0; i < val.length - 1; i++) {
				obj = penetrate(obj, val[i]);
			}
			cacheIn(path, obj, val[val.length - 1]);
			logger.debug("Caching " + path);
		}
		return cache.get(path);
	}

	private Object penetrate(Object obj, String val) throws JXPathException {
		try {
			String methodName = "get" + transform(val);
			logger.debug("Calling " + methodName + " on " + obj);
			Method method = obj.getClass().getMethod(methodName);
			return method.invoke(obj);
		} catch (Exception e) {
			throw new JXPathException("Exception going into " + val, e);
		}
	}

	private String transform(String val) {
		return Character.toUpperCase(val.charAt(0)) + val.substring(1);
	}

	private RealField cacheIn(String path, Object obj, String fieldName)
			throws JXPathException {
		RealField field;
		try {
			field = new RealField(obj, obj.getClass().getDeclaredField(
					fieldName));
		} catch (SecurityException e) {
			throw new JXPathException("Security exception", e);
		} catch (NoSuchFieldException e) {
			throw new JXPathException("No such field exception", e);
		}
		cache.put(path, field);
		return field;
	}

	public Object getDataObject() {
		return this.obj;
	}

}
