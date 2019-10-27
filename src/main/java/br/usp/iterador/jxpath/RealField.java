package br.usp.iterador.jxpath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class RealField implements FieldData {

	private Object obj;

	private Field field;

	public RealField(Object obj, Field field) {
		this.obj = obj;
		this.field = field;
		this.field.setAccessible(true);
	}

	public RealField(Object obj, String field) throws SecurityException,
			NoSuchFieldException {
		this(obj, obj.getClass().getField(field));
	}

	public void set(Object value) throws JXPathException {
		try {
			field.set(this.obj, value);
		} catch (IllegalArgumentException e) {
			throw new JXPathException("Illegal argument", e);
		} catch (IllegalAccessException e) {
			throw new JXPathException("Illegal access", e);
		}
	}

	public Object get() throws JXPathException {
		try {
			return field.get(this.obj);
		} catch (IllegalArgumentException e) {
			throw new JXPathException("Illegal argument", e);
		} catch (IllegalAccessException e) {
			throw new JXPathException("Illegal access", e);
		}
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		return field.getAnnotation(annotationClass);
	}

	public boolean isAnnotationPresent(
			Class<? extends Annotation> annotationClass) {
		return field.isAnnotationPresent(annotationClass);
	}

	public String getName() {
		return field.getName();
	}

	public Class<?> getType() {
		return field.getType();
	}

}
