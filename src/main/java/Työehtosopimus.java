import java.io.Serializable;
import java.math.BigDecimal;

public class Työehtosopimus implements Serializable{
	
	Työehtosopimus(){
		
	}
	
	Työehtosopimus(LomanMääräytymisEhdot terms){
		
		
				///(KAUPAN työehtosopimus 2022, 72)
				//2. Lomaa ansaitaan täydeltä lomanmääräytymiskuukaudelta työsuhteen kestettyä 
				//lomanmääräytymisvuoden (1.4.–31.3.) loppuun mennessä:
				//• alle vuoden: 2 arkipäivää
				//• vähintään vuoden: 2,5 arkipäivää
			terms.setVacationDaysPerMonthOverYear(BigDecimal.valueOf(2.5));
			terms.setVacationDaysPerMonthUnderYear(BigDecimal.valueOf(2));
		
			//(KAUPAN työehtosopimus 2022, 74)
				//6. Vuosilomapäivän palkka saadaan kuukausipalkasta jakajalla 25		
			terms.setConstDivider(BigDecimal.valueOf(25));
			
			
			//(Kaupan alan työehtosopimus 2022, 75)
				//8. Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa saavalla 
				//jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
				//• 10 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) mennessä alle vuoden
				//• 12,5 % työsuhteen kestettyä lomanmääräytymisvuoden loppuun (31.3.) 
				//mennessä vähintään vuoden
			terms.setPrecentageOverYear(BigDecimal.valueOf(0.125));
			terms.setPrecentageUnderYear(BigDecimal.valueOf(0.10));
			
			
			
			
			//(Kaupan alan työehtosopimus 2022, 75)
				//8. Lomapalkka tai -korvaus on sekä tuntipalkkaisella että suhteutettua kuukausipalkkaa saavalla 
				//jäljempänä esitetystä lomanmääräytymisvuoden ansiosta:
			//työehtoSopimus.setCalculationRuleExceptionWage("prosenttiperusteinen");
	}
	

	
	
	
		
}
