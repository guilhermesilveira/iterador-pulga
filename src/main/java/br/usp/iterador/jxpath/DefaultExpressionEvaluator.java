package br.usp.iterador.jxpath;

import java.util.HashMap;
import java.util.Map;

import net.janino.ScriptEvaluator;

import org.apache.log4j.Logger;

/**
 * An expression evaluator.
 *
 * @author Guilherme Silveira
 * @since 1.2
 */
public class DefaultExpressionEvaluator implements ExpressionEvaluator {

	private final Map<String, ScriptEvaluator> cache = new HashMap<String, ScriptEvaluator>();

	private static final Logger LOG = Logger
			.getLogger(DefaultExpressionEvaluator.class);

	public double evaluate(String expression) {
		try {
			if (!cache.containsKey(expression)) {
				ScriptEvaluator ee = new ScriptEvaluator(
						"import java.util.*;\nimport java.math.*;\nreturn 0.0 + ("
								+ expression + ");", double.class);
				LOG.debug("caching " + expression);
				cache.put(expression, ee);
			}
			return (Double) cache.get(expression).evaluate(new Object[] {});
		} catch (Exception e) {
			throw new RuntimeException("Unable to compile script: "
					+ expression, e);
		}
	}

}
