import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class TestLomaLaskuri {
	LomanMääräytymisEhdot terms;
	Vuosilomalaki vuosilomalaki;
	TyösuhdeTiedot työsuhdeTiedot;
	Työehtosopimus työehtoSopimus;
	LomaLaskuri laskuri;
	
	/**
	 * 
	 */
	void testThings() {
		terms = new LomanMääräytymisEhdot();
		vuosilomalaki = new Vuosilomalaki(terms);
		työsuhdeTiedot = new TyösuhdeTiedot(terms,"4.1.2009",true,"data.ser");
		työehtoSopimus = new Työehtosopimus(terms);
		laskuri = new LomaLaskuri(terms,työsuhdeTiedot,"2010");
	}
	
	//(Vuosilomalaki § 4)
	//"lomanmääräytymisvuodella 1 päivän huhtikuuta ja 31
	//päivän maaliskuuta välistä aikaa nämä päivät mukaan luettuina"
	@Test
	void testDivideMonths() {
		testThings();
		laskuri.divideMonths();
		
		/* 1 päivän huhtikuuta ja 31 päivän maaliskuuta välistä aikaa nämä päivät mukaan luettuina
		 * jos vuosi on 2009 ja kk >3    tai     jos vuosi 2010 ja kk <=3 
		 * päivät omiin listoihin kk perusteella ja listoja pitäisi olla 12
		 */ 
		assertEquals(12,laskuri.months.size());
	}
	
	
	
	//(Vuosilomalaki 6§)
	//Täytenä lomanmääräytymiskuukautena pidetään kalenterikuukautta, jolloin työntekijälle 
	//on kertynyt vähintään 14 työssäolopäivää tai 7 §:n 1 ja 2 momentissa tarkoitettua 
	//työssäolon veroista päivää.
		//(Vuosilomalaki § 5)
	//Jos työntekijä on sopimuksen mukaisesti työssä niin harvoina päivinä, että hänelle ei 
	//tästä syystä kerry ainoatakaan 14 työssäolopäivää sisältävää kalenterikuukautta tai vain 
	//osa kalenterikuukausista sisältää 14 työssäolopäivää, täydeksi lomanmääräytymiskuukaudeksi 
	//katsotaan sellainen kalenterikuukausi, jonka aikana työntekijälle on kertynyt vähintään 35 
	//työtuntia tai 7 §:ssä tarkoitettua työssäolon veroista tuntia.
		//(Vuosilomalaki 6§)
	//Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa työtä tekevän 
	//työntekijän vuosilomapalkka on 9 prosenttia taikka työsuhteen jatkuttua lomakautta edeltävän 
	//lomanmääräytymisvuoden loppuun mennessä vähintään vuoden 11,5 prosenttia
	@Test
	void testSetCountingRule() {
		testThings();
		työsuhdeTiedot.addWorkHoursPerWeek(työsuhdeTiedot.getStartDate(), BigDecimal.valueOf(35));
		laskuri.divideMonths();
		laskuri.setCountingRule();
		
		/* on kertynyt vähintään 14 työssäolopäivää, on kertynyt vähintään 35 työtuntia
		 * 
		 * ohjelma arvioi pelkän aineistossa olevan viikkotyöajan perusteella
		 * viikot kuukaudessa = 4.34
		 * vähimmäismäärä työpäivä viikossa jotta 14 pävää täyttyy = 14/4.34 = 3.3
		 * minimi määrä työpäiviä viikossa jotta sopimuksen 37.5 tuntia täyttyy = 37.5/8 = 4.7
		 * koska 4.7 > 3.3    14p  säännön täytyy täyttyä
		 * 
		 * 35 tunnin sääntöä käytetään jos työsopimuksen säännöllinen vko työaika 37.5 jaettuna 5, kertaa 
		 * kk työpäivät 22 on >= 35 tuntia
		 */
		assertEquals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_14.rule,terms.getCountingRule());
		
		/* 14päivän sääntö on ensisijainen (Vuosilomalaki 6§)
		 * 14 päivän sääntö on ensimmäisenä if lauseessa, jonkä jälkeen 35 tunninsääntö ja muut on else lauseissa
		 */
		assertNotEquals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_35.rule,terms.getCountingRule());
		
		
	}
	
	@Test
	void testGetWorkDaysInTheMonth() {
		testThings();
		laskuri.divideMonths();
		assertEquals(20,laskuri.getWorkDaysInTheMonthCount("10"));
		
		työsuhdeTiedot.addWorkHoursPerWeek("4.1.2009",BigDecimal.valueOf(37.5));
		assertEquals(22,laskuri.getWorkDaysInTheMonthCount("10"));
	}
	
	
		//(Vuosilomalaki § 6)
	//"Jos työntekijä on sopimuksen mukaisesti työssä niin harvoina päivinä, 
	//että hänelle ei tästä syystä kerry ainoatakaan 14 työssäolopäivää 
	//sisältävää kalenterikuukautta tai vain osa kalenterikuukausista 
	//sisältää 14 työssäolopäivää, täydeksi lomanmääräytymiskuukaudeksi 
	//katsotaan sellainen kalenterikuukausi, jonka aikana työntekijälle on 
	//kertynyt vähintään 35 työtuntia tai 7 §:ssä tarkoitettua työssäolon 
	//veroista tuntia."
		//(Vuosilomalaki § 6)
	//"Täytenä lomanmääräytymiskuukautena pidetään kalenterikuukautta, jolloin 
	//työntekijälle on kertynyt vähintään 14 työssäolopäivää tai 7 §:n 1 ja 2 
	//momentissa tarkoitettua työssäolon veroista päivää."
		//(Vuosilomalaki  § 8)
	//Työntekijällä, joka sopimuksen mukaan tekee kaikkina kalenterikuukausina työtä 
	//alle 14 päivää tai 35 tuntia, on työsuhteen kestäessä oikeus halutessaan saada 
	//vapaata kaksi arkipäivää kultakin kalenterikuukaudelta
	@Test
	void testCountVacationDays() {
		testThings();
		laskuri.divideMonths();
		terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_14.rule);
		laskuri.countVacationDays();
		
		/*kertynyt vähintään 14 päivää/35 työtuntia tai 7 §:ssä tarkoitettua työssäolon veroista tuntia
		 * alle 14 päivää tai 35 tuntia, on työsuhteen kestäessä oikeus halutessaan saada vapaata kaksi arkipäivää kultakin kalenterikuukaudelta
		 * 
		 * looppaa kaikki kuukausi listat ja looppaa vielä kuukausien päivät
		 * divideMonths() on jo karsinut vuoden ulkopuolelle joutuneet päivät ja työajan ulkopuolelle joutuneet päivät
		 * jos kk listan pituus on >= 14     14 päivän säänö täyttyy
		 * jos 35 tunnin sääntö   jokaisen päivän työtunnit lisätään  kk yht. tuntimäärään ja jos >= 35     35 tunnin sääntö täyttyy 
		 * jos kumpikaan ei täyty niin prosenttiperusteinen sääntö täyttyy ja joka kk vain 2 pv lomaa
		 */
		assertEquals(25.0,laskuri.vacationDays14DayRule.doubleValue());
	}	
	
	
	
	
	
	
		//(Vuosilomalaki § 10)
	//Jos edellä 6 §:n 2 momentissa tarkoitetun työntekijän sopimuksen mukainen 
	//työaika on niin vähäinen, että tästä syystä vain osa kalenterikuukausista on
	//täysiä lomanmääräytymiskuukausia, hänen lomapalkkansa lasketaan 12 §:n mukaan.
		//(Vuosilomalaki§ 10)
	//Lomapalkka lasketaan 12 §:n mukaan myös silloin, kun työntekijän työaika ja vastaavasti 
	//palkka on muuttunut lomanmääräytymisvuoden aikana.
		//(Vuosilomalaki § 12)
	//Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa 
	//työtä tekevän	
		//(Vuosilomalaki § 11)
	//Muun kuin viikko- tai kuukausipalkalla työskentelevän sellaisen työntekijän vuosilomapalkka,
	//joka sopimuksen mukaan työskentelee vähintään 14 päivänä kalenterikuukaudessa,
	@Test
	void testSetCalculationRule() {
		testThings();
		laskuri.divideMonths();
		terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_14.rule);
		laskuri.setCalculationRule();
		
		
		/*Vuosilomalaki § 10-12
		 * jos kk palkka ja 14 päivän sääntö   lasketaan kk palkalla, mutta jos työaika ja palkka muuttuvat prosenttipalkalla
		 * jos kk palkka ja 35 tunnin sääntö 	lasketaan prosenttipalkalla
		 * jos tuntipalkka ja 14 päivän sääntö 		lasketaan keskipäiväpalkalla
		 * jos tuntipalkka ja 35 tunnin sääntö tai muu   lasketaan prosenttipalkalla
		 */
		assertEquals(LomapalkanLaskentaSäännöt.KESKIPÄIVÄPALKKAPERUSTEINEN.rule,terms.getCalculationRule());
	}
	
	
	
		//(Vuosilomalaki § 10)
	//"Työntekijällä, jonka palkka on sovittu viikolta tai sitä pidemmältä ajalta, 
	//on oikeus saada tämä palkkansa myös vuosiloman ajalta." 
		//(Vuosilomalaki § 11)
	//"Muun kuin viikko- tai kuukausipalkalla työskentelevän sellaisen 
	//työntekijän vuosilomapalkka, joka sopimuksen mukaan työskentelee 
	//vähintään 14 päivänä kalenterikuukaudessa, lasketaan kertomalla hänen 
	//keskipäiväpalkkansa lomapäivien määrän perusteella määräytyvällä 
	//kertoimella:"
		//(Vuosilomalaki § 12)
	//"Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä 
	//kalenterikuukaudessa työtä tekevän työntekijän vuosilomapalkka on 9
	//prosenttia taikka työsuhteen jatkuttua lomakautta edeltävän 
	//lomanmääräytymisvuoden loppuun mennessä vähintään vuoden 11,5 prosenttia
	//lomanmääräytymisvuoden aikana työssäolon ajalta maksetusta tai 
	//maksettavaksi erääntyneestä palkasta lukuun ottamatta hätätyöstä ja 
	//lain tai sopimuksen mukaisesta ylityöstä maksettavaa korotusta."
	@Test
	void testCountVacationPay() {
		testThings();
		laskuri.divideMonths();
		laskuri.finalVacationDays = 25;
		laskuri.workHoursAllYear = BigDecimal.valueOf(20);
		laskuri.comissionAllYear = BigDecimal.valueOf(2);
		laskuri.wageAllYear = BigDecimal.valueOf(750);
		laskuri.dayCount = 20;
		terms.setCalculationRule(LomapalkanLaskentaSäännöt.PROSENTTIPERUSTEINEN.rule);
		terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_35.rule);
		laskuri.countVacationPay();
		assertEquals(94.0,laskuri.laskelma.getLomapalkka().doubleValue());
	}
	

}
