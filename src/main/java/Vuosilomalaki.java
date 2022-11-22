import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author OMISTAJA
 *
 */
public class Vuosilomalaki implements Serializable{
	
	
	
	LomanMääräytymisEhdot terms;
	
	Vuosilomalaki(){
		
	}
	
	Vuosilomalaki(LomanMääräytymisEhdot terms){
		this.terms = terms;
		
		
		terms.setIncludeComission(true);
		terms.setConstDivider(BigDecimal.valueOf(25));
		terms.setPrecentageOverYear(BigDecimal.valueOf(0.115));
		terms.setPrecentageUnderYear(BigDecimal.valueOf(0.090));
		terms.setVacationDaysPerMonthOverYear(BigDecimal.valueOf(2.5));
		terms.setVacationDaysPerMonthUnderYear(BigDecimal.valueOf(2));
		terms.addOffDays("Hoitopvapaa",1);
		terms.addOffDays("Arkipyhäkorvus", 1);
		
		//(Vuosilomalaki § 7)
			//	"2) sairauden tai tapaturman vuoksi, enintään kuitenkin 75 työpäivää..
			//	3) lääkärin määräämän, ammattitaudin tai tapaturman johdosta 		
			//	4) sairauden leviämisen estämiseksi annetun viranomaisen määräyksen vuoksi;
			//	5) opintovapaalaissa (273/1979) tarkoitetun opintovapaan vuoksi, enintään kuitenkin 30 työpäivää..
			//	6) työnantajan suostumuksella työn edellyttämään koulutukseen osallistumisen vuoksi.. 
			//	7) lomauttamisen takia, enintään kuitenkin 30 työpäivää kerrallaan...
			//	8) lomauttamista vastaavan työviikkojen lyhentämisen tai muun siihen verrattavan työaikajärj...
			//	9) reservin harjoituksen tai ylimääräisen palveluksen taikka siviilipalvelusl...
			//	10) sellaisen julkisen luottamustehtävän hoitamisen tai todistajana kuulemisen vu..."
		
		terms.addTimeAtWorkEquilentDays("vanhempainvapaa",new int[] {156,0} );
		terms.addTimeAtWorkEquilentDays("äitiysvapaa",new int[] {156,0} );
		terms.addTimeAtWorkEquilentDays("isyysvapaa",new int[] {156,0} );
		terms.addTimeAtWorkEquilentDays("sairasloma",new int[] {75,9} );
		terms.addTimeAtWorkEquilentDays("opintovapaa",new int[] {30,0} );
		terms.addTimeAtWorkEquilentDays("lomautus",new int[] {30,0} );
		
		//(Vuosilomalaki § 11)
			//(Vuosilomalaki § 11)
			//"Muun kuin viikko- tai kuukausipalkalla työskentelevän sellaisen 
			//työntekijän vuosilomapalkka, joka sopimuksen mukaan työskentelee 
			//vähintään 14 päivänä kalenterikuukaudessa, lasketaan kertomalla hänen 
			//keskipäiväpalkkansa lomapäivien määrän perusteella määräytyvällä 
			//kertoimella:"
		terms.addVacationX(2,BigDecimal.valueOf(1.8));
		terms.addVacationX(3, BigDecimal.valueOf(2.7));
		terms.addVacationX(4,BigDecimal.valueOf(3.6));
		terms.addVacationX(5, BigDecimal.valueOf(4.5));
		terms.addVacationX(6, BigDecimal.valueOf(5.4));
		terms.addVacationX(7, BigDecimal.valueOf(6.3));
		terms.addVacationX(8, BigDecimal.valueOf(7.2));
		terms.addVacationX(9, BigDecimal.valueOf(8.1));
		terms.addVacationX(10, BigDecimal.valueOf(9.0));
		terms.addVacationX(11, BigDecimal.valueOf(9.9));
		terms.addVacationX(12, BigDecimal.valueOf(10.8));
		terms.addVacationX(13, BigDecimal.valueOf(11.8));
		terms.addVacationX(14, BigDecimal.valueOf(12.7));
		terms.addVacationX(15, BigDecimal.valueOf(13.6));
		terms.addVacationX(16, BigDecimal.valueOf(14.5));
		terms.addVacationX(17, BigDecimal.valueOf(15.5));
		terms.addVacationX(18, BigDecimal.valueOf(16.4));
		terms.addVacationX(19, BigDecimal.valueOf(17.4));
		terms.addVacationX(20, BigDecimal.valueOf(18.3));
		terms.addVacationX(21, BigDecimal.valueOf(19.3));
		terms.addVacationX(22, BigDecimal.valueOf(20.3));
		terms.addVacationX(23, BigDecimal.valueOf(21.3));
		terms.addVacationX(24, BigDecimal.valueOf(22.2));
		terms.addVacationX(25, BigDecimal.valueOf(23.2));
		terms.addVacationX(26, BigDecimal.valueOf(24.1));
		terms.addVacationX(27, BigDecimal.valueOf(25.0));
		terms.addVacationX(28, BigDecimal.valueOf(25.9));
		terms.addVacationX(29, BigDecimal.valueOf(26.9));
		terms.addVacationX(30, BigDecimal.valueOf(27.8));
	}
		
		
		
}
	
	
	


