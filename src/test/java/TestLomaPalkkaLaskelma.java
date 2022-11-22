import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestLomaPalkkaLaskelma {

	LomanMääräytymisEhdot terms;
	Vuosilomalaki vuosilomalaki;
	TyösuhdeTiedot työsuhdeTiedot;
	Työehtosopimus työehtoSopimus;
	LomaLaskuri laskuri;
	List<Day>days;
	
	void testsThings() {
		terms = new LomanMääräytymisEhdot();
		vuosilomalaki = new Vuosilomalaki(terms);
		työsuhdeTiedot = new TyösuhdeTiedot(terms,"4.1.2009",true,"data.ser");
		työehtoSopimus = new Työehtosopimus(terms);
		laskuri = new LomaLaskuri(terms,työsuhdeTiedot,"2010");
	}
	
	
	@Test
	void testA() {
		testsThings();
		LomaPalkkaLaskelma laskelma = laskuri.getLomapalkkaLaskelma();
		
		
		/*Vuosilomalaki
		 * vaikuttaa kertyneisiin lomapäiviin ja proenttiperusteisen laskusäännön prosenttiin
		 */
		assertEquals(true,laskelma.getTyösuhdeKestänytYliVuosi());
		
		/*Vuosilomalaki
		 * vuosilomalaissa ja kaupan tessissä molemmissa kerrottu 2.5 yli vuodelta
		 */
		assertEquals(2.5,laskelma.getLomapäivätKuukaudessa().doubleValue());
		
		/*Kaupan tes
		 * tessin mukaan prosenttiperusteisessä käytetään 12.5% ylivuotisilla ja 10% alle vuotisilla
		 */
		assertEquals(0.125,laskelma.getKorvausprosentti().doubleValue());
		
		/*vuosilomalaki
		 * //	"2) sairauden tai tapaturman vuoksi, enintään kuitenkin 75 työpäivää..
			//	3) lääkärin määräämän, ammattitaudin tai tapaturman johdosta 		
			//	4) sairauden leviämisen estämiseksi annetun viranomaisen määräyksen vuoksi;
			//	5) opintovapaalaissa (273/1979) tarkoitetun opintovapaan vuoksi, enintään kuitenkin 30 työpäivää..
			//	6) työnantajan suostumuksella työn edellyttämään koulutukseen osallistumisen vuoksi.. 
			//	7) lomauttamisen takia, enintään kuitenkin 30 työpäivää kerrallaan...
			//	8) lomauttamista vastaavan työviikkojen lyhentämisen tai muun siihen verrattavan työaikajärj...
			//	9) reservin harjoituksen tai ylimääräisen palveluksen taikka siviilipalvelusl...
			//	10) sellaisen julkisen luottamustehtävän hoitamisen tai todistajana kuulemisen vu..."
		 * 
		 * karsi pois hoitovapaat ja arkipyhät, mutta ei esim. vanhempainvapaa (156 päivän ajalta)
		 */
		assertEquals(221,laskelma.getLomajaksonTyöpäivät().doubleValue());
		
		/*
		 * tuntipalkka kerrottu vuoden työtunneilla
		 */
		assertEquals(13405,laskelma.getVuodenAnsioIlmanKorotusosia().intValue());
		
		/*
		 * 0 koska aineistossa ei ole mainittu saamattomia palkkioita?
		 */
		assertEquals(0,laskelma.getSaamattaJäänytPalkkio().intValue());
		
		/*Vuosilomalaki 
		 * 	
		 * jos tuntipalkka ja 35 tunnin sääntö tai muu   lasketaan prosenttipalkalla 
		 */
		assertEquals(LomapalkanLaskentaSäännöt.PROSENTTIPERUSTEINEN.rule,laskelma.getLomapalkanLaskentaSääntö());
		
		/* Vuosilomalaki
		 * koska 14 päivää ei täyty kaikilta kk
		 * ja 35 tuntia täyttyy väh. yhdeltä kk.
		 * käytetään 35 tunnin sääntöä
		 */
		assertEquals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_35.rule,laskelma.getLomapäivienLaskentaSääntö());

		/*vuosilomalaki
		 * vanhempainvapaa kk ja hoitovapaa heinäkuu kk jäi pois
		 */
		assertEquals(10,laskelma.getKartuttavatKuukaudet().intValue());
		
		/*vuosilomalaki
		 * 35 tunnin säännöllä tuli 10 kk * 2.5 = 25
		 */
		assertEquals(25,laskelma.getLomapäivät().intValue());
		
		assertEquals(0,laskelma.getVuosilomalainKerroin().doubleValue());
		
		/*kaupan tes
		 * //8. Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa saavalla 
				//jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
				//• 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
				//• 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) 
				//mennessä vähintään vuoden
		 * tessin mukaan lomapalkka on yli vuotisella 12.5% vuoden tuloista
		 * +provisio erikseen samalla laskusäännöllä
		 */
		assertEquals(1802,laskelma.getLomapalkka().intValue());
	}
	
	@Test
	void testB() {
		testsThings();
		työsuhdeTiedot.addWorkHoursPerWeek("4.1.2009", BigDecimal.valueOf(37.5));
		LomaPalkkaLaskelma laskelma = laskuri.getLomapalkkaLaskelma();
		
		
		
		/*Vuosilomalaki
		 * vaikuttaa kertyneisiin lomapäiviin ja proenttiperusteisen laskusäännön prosenttiin
		 */
		assertEquals(true,laskelma.getTyösuhdeKestänytYliVuosi());
		
		/*Vuosilomalaki
		 * vuosilomalaissa ja kaupan tessissä molemmissa kerrottu 2.5 yli vuodelta
		 */
		assertEquals(2.5,laskelma.getLomapäivätKuukaudessa().doubleValue());
		
		/*Kaupan tes
		 * tessin mukaan prosenttiperusteisessä käytetään 12.5% ylivuotisilla ja 10% alle vuotisilla
		 */
		assertEquals(0.0,laskelma.getKorvausprosentti().doubleValue());
		
		/*
		 * 0 koska aineistossa ei ole mainittu saamattomia palkkioita?
		 */
		assertEquals(0,laskelma.getSaamattaJäänytPalkkio().intValue());
		
		/*
		 * lisää ylityötunni jaettuna 8 vuoden työpäiviin
		 */
		assertEquals(244.21875,laskelma.getTyöpäivätJaYlityö().doubleValue());
		
		/*
		 * työtunnit kertaa palkka, mutta lisättynä vielä arkipyhäkorvakset
		 */
		assertEquals(15460,laskelma.getVuodenAnsioIlmanKorotusosia().intValue());
		
		/*Vuosilomalaki
		 * ohjelma arvioi pelkän aineistossa olevan viikkotyöajan perusteella
		 * viikot kuukaudessa = 4.34
		 * vähimmäismäärä työpäivä viikossa jotta 14 pävää täyttyy = 14/4.34 = 3.3
		 * minimi määrä työpäiviä viikossa jotta sopimuksen 37.5 tuntia täyttyy = 37.5/8 = 4.7
		 * koska 4.7 > 3.3    14p  säännön täytyy täyttyä
		 */
		assertEquals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_14.rule,laskelma.getLomapäivienLaskentaSääntö());
		
		/*Vuosilomalaki 
		 * jos kk palkka ja 14 päivän sääntö   lasketaan kk palkalla, mutta jos työaika ja palkka muuttuvat prosenttipalkalla
		 * jos kk palkka ja 35 tunnin sääntö 	lasketaan prosenttipalkalla
		 * jos tuntipalkka ja 14 päivän sääntö 		lasketaan keskipäiväpalkalla    <-
		 * jos tuntipalkka ja 35 tunnin sääntö tai muu   lasketaan prosenttipalkalla
		 */
		assertEquals(LomapalkanLaskentaSäännöt.KESKIPÄIVÄPALKKAPERUSTEINEN.rule,laskelma.getLomapalkanLaskentaSääntö());
		
		/*
		 * laskee nyt nimelliset poissaolot ja vähentää ne työpäivien määrästä 22
		 */
		assertEquals(11,laskelma.getKartuttavatKuukaudet().intValue());
		
		/*vuosilomalaki
		 * 2.5 * kartuttavat kk
		 */
		assertEquals(28,laskelma.getLomapäivät().intValue());
		
		/*Vuosilomalaki
		 * //"Muun kuin viikko- tai kuukausipalkalla työskentelevän sellaisen 
			//työntekijän vuosilomapalkka, joka sopimuksen mukaan työskentelee 
			//vähintään 14 päivänä kalenterikuukaudessa, lasketaan kertomalla hänen 
			//keskipäiväpalkkansa lomapäivien määrän perusteella määräytyvällä 
			//kertoimella:"
		 */
		assertEquals(25.9,laskelma.getVuosilomalainKerroin().doubleValue());
		
		/*vuosilomalaki
		 *koko vuoden tulot jaetaan   työpäivillä + ylityötunneilla
		 *koska työpäivien määrä viikossa on 4.64  n.5   keskipäiväpalkkaa ei tarvitse kertoilla ja jakaa viidellä (countVacationPay())
		 *keskipäiväpalkka kerrotaan vuosilomalain mukaisella kertoimella
		 */
		assertEquals(1757,laskelma.getLomapalkka().intValue());
	}

}
