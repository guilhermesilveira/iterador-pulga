package br.usp.iterador.logic;

import junit.framework.TestCase;

public class ControllerTest extends TestCase {

	public static class OtherType {

	}

	public static class ThisType {

		public ThisType(OtherType type) {

		}

	}

	public void testIsUnableToInstantiate() {
		Controller controller = new DefaultController();
		try {
			controller.newInstance(ThisType.class);
			fail();
		} catch (RuntimeException e) {
			// ok
		}
	}

	public void testIsAbleToInstantiateWithInjection() {
		Controller controller = new DefaultController();
		controller.registerDI(new OtherType());
		controller.newInstance(ThisType.class);
	}

}
