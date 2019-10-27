package br.usp.iterador.jxpath;

import junit.framework.TestCase;
import br.usp.iterador.logic.DefaultController;

public class ReflectionResolverTest extends TestCase {

	public class MemberTest {

		int field;

		public void getMethod() {

		}

	}

	public void testFindsGenericMemberMethod() throws NoSuchFieldException {
		Member genericMember = new ReflectionResolver(new DefaultController())
				.getGenericMember(MemberTest.class, "method");
		assertEquals(MethodMember.class, genericMember.getClass());
	}

	public void testFindsGenericMemberField() throws NoSuchFieldException {
		Member genericMember = new ReflectionResolver(new DefaultController())
				.getGenericMember(MemberTest.class, "field");
		assertEquals(FieldMember.class, genericMember.getClass());
	}

	public void testDoesNotFindGenericMember() {
		try {
			new ReflectionResolver(new DefaultController()).getGenericMember(
					MemberTest.class, "field");
		} catch (NoSuchFieldException e) {
			// ok
		}
	}

}
