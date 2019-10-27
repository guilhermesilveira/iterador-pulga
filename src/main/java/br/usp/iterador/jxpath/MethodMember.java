package br.usp.iterador.jxpath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class MethodMember implements Member {

	private Method member;

	public MethodMember(Method method) {
		this.member = method;
	}

	public String getName() {
		return member.getName();
	}

	public Class<?> getType() {
		return member.getReturnType();
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> name) {
		return member.isAnnotationPresent(name);
	}

	public <T extends Annotation> T getAnnotation(Class<T> name) {
		return member.getAnnotation(name);
	}

}
