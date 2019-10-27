package br.usp.iterador.jxpath;

import java.lang.annotation.Annotation;

public interface Member {

	String getName();

	Class<?> getType();

	boolean isAnnotationPresent(Class<? extends Annotation> name);

	<T extends Annotation> T getAnnotation(Class<T> name);

}
