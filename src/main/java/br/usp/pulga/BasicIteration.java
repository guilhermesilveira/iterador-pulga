package br.usp.pulga;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.usp.pulga.iteration.IterationListener;

public abstract class BasicIteration implements Iteration {

	private final Map<String, Field> fields = new HashMap<String, Field>();

	private final List<IterationListener> listeners = new ArrayList<IterationListener>();

	private final Map<String, Variable> variables = new HashMap<String, Variable>();

	protected double _x[];

	private double iteration;

	public BasicIteration() {
		Class type = this.getClass();
		while (type != Object.class) {
			Field[] fields = type.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				this.fields.put(f.getName(), f);
			}
			type = type.getSuperclass();
		}
	}

	public Variable findVariable(String key) {
		if (variables.containsKey(key)) {
			return variables.get(key);
		}
		throw new RuntimeException("Cannot find variable " + key);
	}

	public double getVariable(String key) {
		if (variables.containsKey(key)) {
			return _x[variables.get(key).getId()];
		}
		if (!fields.containsKey(key)) {
			throw new RuntimeException("Cannot find field " + key);
		}
		try {
			Field field = fields.get(key);
			return (Double) field.get(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public double norm(double v1, double v2) {
		return v1 * v1 + v2 * v2;
	}

	public void addListener(IterationListener listener) {
		this.listeners.add(listener);
	}

	public void iterate() {
		this.iteration++;
		for (IterationListener listener : listeners) {
			listener.before(this);
		}
		internalIterate();
		for (IterationListener listener : listeners) {
			listener.after(this);
		}
	}

	protected abstract void internalIterate();

	public void useVariables(List<Variable> variables) {
		for (Variable variable : variables) {
			this.variables.put(variable.getName(), variable);
		}
	}

	public void init() {
		this.iteration = 0;
		this._x = new double[this.variables.size()];
		for(Variable v : this.variables.values()) {
			this._x[v.getId()] = v.getDefaultValue();
		}
		for (IterationListener listener : listeners) {
			listener.init(this);
		}
	}

}
