package br.usp.iterador.model;

import junit.framework.TestCase;

public class ScaleTest extends TestCase{
    
    public void testChangesMinimumValueToAnotherScale() {
        Scale from = new Scale("",0,1000);
        Scale to = new Scale("",100,200);
        assertEquals(100.0, from.changeValueTo(0,to));
    }

    public void testChangesMaximumValueToAnotherScale() {
        Scale from = new Scale("",0,1000);
        Scale to = new Scale("",100,200);
        assertEquals(200.0, from.changeValueTo(1000,to));
    }

    public void testMediumMaximumValueToAnotherScale() {
        Scale from = new Scale("",0,1000);
        Scale to = new Scale("",100,200);
        assertEquals(150.0, from.changeValueTo(500,to));
    }

}
