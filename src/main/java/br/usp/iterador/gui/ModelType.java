package br.usp.iterador.gui;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.usp.iterador.gui.util.ModelField;

@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @ interface ModelType {

	Class<? extends ModelField> component();
	
	double width() default 0.1;
	double height() default 0.1;

}
