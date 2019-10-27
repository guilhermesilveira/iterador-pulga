package br.usp.iterador.jxpath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldMember implements Member {

	private final Field member;

	public FieldMember(Field field) {
		this.member = field;
	}

	public String getName() {
		return member.getName();
	}

	public Class<?> getType() {
		return member.getType();
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> name) {
		return member.isAnnotationPresent(name);
	}

	public <T extends Annotation> T getAnnotation(Class<T> name) {
		return member.getAnnotation(name);
	}

}
