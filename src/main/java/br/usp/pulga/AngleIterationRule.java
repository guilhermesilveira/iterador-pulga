package br.usp.pulga;

public class AngleIterationRule implements IterationRule {

	private final IterationRule rule;

	private final double x0;

	private final double x1;

    public AngleIterationRule(IterationRule rule, double x0, double x1) {
		this.rule = rule;
		this.x0 = x0;
		this.x1 = x1;
		this.rule.addVariable("omega", 0, "angleBetween()");
        this.rule.addVariable("oldX0", x0, "x0");
        this.rule.addVariable("oldX1", x1, "x1");
        this.rule.addMethod(angleBetween(x0, x1));
    }

    private String angleBetween(double x0, double x1) {
        return "public double angleBetween() {\n" +
                "   return Math.acos(norm(" + x0 + ", " + x1 +") * norm(oldX0, oldX1) / (x0*oldX0 + x1*oldX1));\n" +
                "}";
    }

    public void addVariable(String s, double i, String s1) {
        rule.addVariable(s, i, s1);
    }

    public String getCode() {
        return rule.getCode();
    }

    public void addMethod(String method) {
        rule.addMethod(method);
    }

    public Iteration buildIteration() {
        return rule.buildIteration();
    }
}
