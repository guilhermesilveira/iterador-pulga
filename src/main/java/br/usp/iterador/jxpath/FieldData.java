package br.usp.iterador.jxpath;

import java.lang.annotation.Annotation;

public interface FieldData {

	<T extends Annotation> T getAnnotation(Class<T> annotationClass);

	boolean isAnnotationPresent(Class<? extends Annotation> annotationClass);

	String getName();

	Class<?> getType();

}
