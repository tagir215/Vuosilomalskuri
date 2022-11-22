import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class TestLomanMääräytymisEhdot {
	
	@Test
	void testGetters() {
		LomanMääräytymisEhdot terms = new LomanMääräytymisEhdot();
		Vuosilomalaki laki = new Vuosilomalaki(terms);
		TyösuhdeTiedot tiedot = new TyösuhdeTiedot(terms,"4.1.2009",true,"data.ser");
		Työehtosopimus sopimus = new Työehtosopimus(terms);
		assertEquals(23.2,terms.getVacationX().get(25).doubleValue());
		tiedot.addWageChangeDates("1.4.2009", BigDecimal.valueOf(10));
		tiedot.addWageChangeDates("5.10.2009", BigDecimal.valueOf(11));
		assertEquals(10,tiedot.getWages().get("1.4.2009").doubleValue());
		assertEquals(11,tiedot.getWages().get("5.10.2009").doubleValue());
	}

}
