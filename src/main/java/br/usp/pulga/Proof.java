package br.usp.pulga;

public class Proof {

	public static void main(String[] args) {
		/*{
			Funcao f = new Quadrado();
			double a = -1, b = 1, d = 1 / 2.0, delta = 0.25;
			double m = 11.0 / 5;
			System.out.println(delta + " deveria ser menor que " + 2 * d / m);
			System.out.println(delta < 2 * d / m);
			confereHMaiorQueD(f, a, b, delta, d);
		}*/
			double m = 1500;
			double delta = 0.0000039d;
			double d = 0.003;
			double a = 0.741;
			double b = 0.835;
			Funcao f = new H();
			System.out.println(delta + " deveria ser menor que " + (2 * d / m));
			System.out.println(delta < (2 * d / m));
			System.out.println(delta * m < 2 * d);
			confereHMaiorQueD(f, a, b, delta, d);
	}

	private static void confereHMaiorQueD(Funcao f, double a, double b,
			double delta, double d) {
		int i = 0;
		double last = 0;
		double at = a;
		while(true) {
			last += delta;
			at = a + last;
			if(at>b) {
				break;
			}
			double resultado = f.at(at);
			if (resultado < d) {
				System.out.println("Oops @ " + at+ " deu " + resultado);
				System.exit(0);
			}
			i++;
			if(i%50000==0) System.out.println(at + " " + (100 * (at - a)/(b - a)) + "%" + " valia " + resultado);
		}
	}

}

class H implements Funcao {

	public double at(double c) {
		double resultado[] = new double[] { 0, c };
		for (int i = 1; i <= 5; i++) {
			double a = resultado[1];
			double b = c * (1 - resultado[0] * resultado[0] + resultado[1] * resultado[1]);
		//	System.out.println(a + " " + b);
			resultado[0] = a;
			resultado[1] = b;
		}
		return -resultado[1];
	}

}

class Quadrado implements Funcao {

	public double at(double x) {
		return x * x + 1;
	}

}

interface Funcao {

	double at(double x);

}