import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class TestTyösuhdeTiedot {

	@Test
	void testImport() {
		LomanMääräytymisEhdot terms = new LomanMääräytymisEhdot();
		TyösuhdeTiedot tiedot = new TyösuhdeTiedot(terms,"4.1.2009",true,"data.ser");
		assertEquals(315,tiedot.days.length);
		assertEquals("05/04/2009",tiedot.days[100].PVM);
	}

}
