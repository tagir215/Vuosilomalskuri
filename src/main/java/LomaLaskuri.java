import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author OMISTAJA
 *
 */
public class LomaLaskuri implements Serializable{
	
	int dayCount;
	int dayCountAndNonRegisteredDays;
	int finalVacationDays;
	int compensationDays;
	int fullMonths;
	int full14RuleMonths;
	int full35RuleMonths;
	BigDecimal regularWorkDaysPerWeek;
	BigDecimal vacationDays14DayRule;
	BigDecimal vacationDays35HourRule;
	BigDecimal workHoursAllYear;
	BigDecimal wageAllYear;
	BigDecimal comissionAllYear;
	BigDecimal overtimeAllYear;
	BigDecimal currentWage;
	BigDecimal vacationPay;
	String year;
	LomanMääräytymisEhdot terms;
	Day[] days;
	TyösuhdeTiedot työsuhdeTiedot;
	HashMap<String,List<Day>>months;
	HashMap<String,Integer>monthsOffDayCount;
	LomaPalkkaLaskelma laskelma;
	
	LomaLaskuri(){
		
	}
	
	/**
	 * @param t
	 * @param td
	 */
	LomaLaskuri(LomanMääräytymisEhdot t, TyösuhdeTiedot td, String y) {
		terms = t;
		days = td.days;
		työsuhdeTiedot = td;
		year = y;
		laskelma = new LomaPalkkaLaskelma();
		vacationDays14DayRule  = BigDecimal.valueOf(0);
		vacationDays35HourRule = BigDecimal.valueOf(0);
		workHoursAllYear = BigDecimal.valueOf(0);
		wageAllYear = BigDecimal.valueOf(0);
		comissionAllYear = BigDecimal.valueOf(0);
		overtimeAllYear = BigDecimal.valueOf(0);
		currentWage = BigDecimal.valueOf(0);
		vacationPay = BigDecimal.valueOf(0);
	}
	
	
	
	/**
	 * @return
	 */
	LomaPalkkaLaskelma getLomapalkkaLaskelma() {
		
		divideMonths();
		countVacationDays();
		setCountingRule();
		setFinalVacationDays();
		setCalculationRule();
		countVacationPay();
		return laskelma;
	}
	
	
	
	
	
	/**
	 * 
	 */
	void divideMonths() {
		months = new HashMap<String,List<Day>>();
		monthsOffDayCount = new HashMap<String,Integer>();
		currentWage = työsuhdeTiedot.getWageAtBeginning();
		String currentMonth = "";
		int offDayCount = 0;
		for(int currentDay=0; currentDay<days.length; currentDay++) {	
			String date[] = days[currentDay].PVM.split("\\D+");
			String currentDatesMonth = date[0];
		
			if(!checkIfDateWithinYear(days[currentDay]))
				continue;
			
			if(checkIfWorkEquivalentDay(days[currentDay])) {
					
				if(!currentMonth.equals(currentDatesMonth)) {
					months.put(currentDatesMonth, new ArrayList<Day>());
					offDayCount = 0;
				}
				
				currentMonth = currentDatesMonth;
				months.get(currentDatesMonth).add(days[currentDay]);
				
			}
			else {
				offDayCount++;
				monthsOffDayCount.put(currentMonth, offDayCount);
				if(työsuhdeTiedot.getWorkHoursPerWeek().size()>0) {
					BigDecimal t = työsuhdeTiedot.getWorkHoursPerWeek().get(työsuhdeTiedot.getStartDate())
							.divide(BigDecimal.valueOf(5));
					wageAllYear = wageAllYear.add(currentWage.multiply(t));
				}
			}
			addToTotalWage(days[currentDay]);
		}
		
		
	}
	
	
	/**
	 * @param day
	 * @return
	 */
	boolean checkIfDateWithinYear(Day day) {
		String date[] = day.PVM.split("\\D+");
		int month = Integer.valueOf(date[0]);
		String yearStart = String.valueOf((Integer.valueOf(year)-1));
		
		//(Vuosilomalaki § 4)
			//"lomanmääräytymisvuodella 1 päivän huhtikuuta ja 31
			//päivän maaliskuuta välistä aikaa nämä päivät mukaan luettuina"
		if(date[2].equals(yearStart) && month>3) {
			return true;
		}
		else if(date[2].equals(year) && month<=3) {
			return true;
		}
		else return false;
		
	}
	
	
	/**
	 * @param day
	 * @return
	 */
	boolean checkIfWorkEquivalentDay(Day day) {
		
		if(terms.getOffDays().get(day.TYÖPÄIVÄ)!=null) {
			return false;
		}
		
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
		if(day.TYÖPÄIVÄ!=null) {
			if(terms.getTimeAtWorkEquilentDays().get(day.TYÖPÄIVÄ)!=null) {
				int[] daysLeft = terms.getTimeAtWorkEquilentDays().get(day.TYÖPÄIVÄ);
				//...enintään kuitenkin 75 työpäivää..
				//...enintään kuitenkin 30 työpäivää kerrallaan...
				if(daysLeft[0]>0) {
					daysLeft[0]-=1;
					
					terms.getTimeAtWorkEquilentDays().put(day.TYÖPÄIVÄ, daysLeft);
				}
				else 
					return false;
			}
			else 
				return false;
			
		}
		return true;
	}
	
	
	/**
	 * 
	 */
	void setCountingRule() {
		BigDecimal workHoursPerWeek=BigDecimal.ZERO;
		regularWorkDaysPerWeek=BigDecimal.ZERO;
		
		
		if(työsuhdeTiedot.getWorkHoursPerWeek().get(työsuhdeTiedot.getStartDate())!=null) {
		 workHoursPerWeek= työsuhdeTiedot.getWorkHoursPerWeek().get(työsuhdeTiedot.getStartDate());
		 regularWorkDaysPerWeek = workHoursPerWeek.divide(BigDecimal.valueOf(8),2,RoundingMode.HALF_UP);
		
			//(Vuosilomalaki 6§)
				//Täytenä lomanmääräytymiskuukautena pidetään kalenterikuukautta, jolloin työntekijälle 
				//on kertynyt vähintään 14 työssäolopäivää tai 7 §:n 1 ja 2 momentissa tarkoitettua 
				//työssäolon veroista päivää.
			if(regularWorkDaysPerWeek.compareTo(BigDecimal.valueOf(3.3))>=0) {
				//14 päivän sääntö on ensisijainen
				terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_14.rule);
			}
			//(Vuosilomalaki 6§)
				//Jos työntekijä on sopimuksen mukaisesti työssä niin harvoina päivinä, että hänelle ei 
				//tästä syystä kerry ainoatakaan 14 työssäolopäivää sisältävää kalenterikuukautta tai vain 
				//osa kalenterikuukausista sisältää 14 työssäolopäivää, täydeksi lomanmääräytymiskuukaudeksi 
				//katsotaan sellainen kalenterikuukausi, jonka aikana työntekijälle on kertynyt vähintään 35 
				//työtuntia tai 7 §:ssä tarkoitettua työssäolon veroista tuntia.
			else if(workHoursPerWeek.divide(BigDecimal.valueOf(5))
					.multiply(BigDecimal.valueOf(22))
					.compareTo(BigDecimal.valueOf(35))>=0) {
				terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_35.rule);
			}
			//(Vuosilomalaki 6§)
				//Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa työtä tekevän 
				//työntekijän vuosilomapalkka on 9 prosenttia taikka työsuhteen jatkuttua lomakautta edeltävän 
				//lomanmääräytymisvuoden loppuun mennessä vähintään vuoden 11,5 prosenttia
			else {
				terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_MUU.rule);
			}
		}
		else
		{
			if(full14RuleMonths==12) {
				terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_14.rule);
			}
			else if(full35RuleMonths>=1) {
				terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_35.rule);
			}
			else {
				terms.setCountingRule(LomapäivienLaskentaSäännöt.SÄÄNTÖ_MUU.rule);
			}
		}
		
	}
	
	/**
	 * @param key
	 * @return
	 */
	int getWorkDaysInTheMonthCount(String key) {
		int workDaysInTheMonth = 0;
		
		if(työsuhdeTiedot.getWorkHoursPerWeek().size()==0) {
			workDaysInTheMonth = months.get(key).toArray().length;
			
		}else {
			//(Vuosilomalaki § 7)	
			//Työssäolon veroisena pidetään työstä poissaoloaikaa, jolta työnantaja on lain 
			//mukaan velvollinen maksamaan työntekijälle palkan. Työssäolon veroisena pidetään 
			//myös aikaa, jolloin työntekijä on poissa työstä sellaisen työajan tasaamiseksi 
			//annetun vapaan vuoksi, jolla hänen keskimääräinen viikkotyöaikansa tasataan
			//laissa säädettyyn enimmäismäärään.
			int offDays = 0;
			if(monthsOffDayCount.get(key)!=null)
				offDays = monthsOffDayCount.get(key);
			workDaysInTheMonth = 22-offDays;
		}
		dayCountAndNonRegisteredDays += workDaysInTheMonth;
		return workDaysInTheMonth;
	}
	
	/**
	 * @param key
	 * @return
	 */
	BigDecimal getWorkTimeInTheMonthCount(String key) {
		BigDecimal workTime = BigDecimal.ZERO;
		for(int i=0; i<months.get(key).toArray().length; i++) {
			Day d = months.get(key).get(i);
			BigDecimal time = BigDecimal.valueOf(d.TYÖAIKAA); 
			workTime=workTime.add(time);
			dayCount++;
		}
		return workTime;
	}
	
	/**
	 * @return
	 */
	void countVacationDays() {
		
		for(String key : months.keySet()) {
			
				//(Vuosilomalaki § 6)
					//"Täytenä lomanmääräytymiskuukautena pidetään kalenterikuukautta, jolloin 
					//työntekijälle on kertynyt vähintään 14 työssäolopäivää tai 7 §:n 1 ja 2 
					//momentissa tarkoitettua työssäolon veroista päivää."
				if(getWorkDaysInTheMonthCount(key)>=14) {
					if(työsuhdeTiedot.getEmployedOver1Year())
						vacationDays14DayRule = vacationDays14DayRule.add(terms.getVacationDaysPerMonthOverYear());
					else 
						vacationDays14DayRule = vacationDays14DayRule.add(terms.getVacationDaysPerMonthUnderYear());
					
					full14RuleMonths++;
				}	
			
			//(Vuosilomalaki § 6)
				//"Jos työntekijä on sopimuksen mukaisesti työssä niin harvoina päivinä, 
				//että hänelle ei tästä syystä kerry ainoatakaan 14 työssäolopäivää 
				//sisältävää kalenterikuukautta tai vain osa kalenterikuukausista 
				//sisältää 14 työssäolopäivää, täydeksi lomanmääräytymiskuukaudeksi 
				//katsotaan sellainen kalenterikuukausi, jonka aikana työntekijälle on 
				//kertynyt vähintään 35 työtuntia tai 7 §:ssä tarkoitettua työssäolon 
				//veroista tuntia."
			if(getWorkTimeInTheMonthCount(key).compareTo(BigDecimal.valueOf(35))>=0) {
				if(työsuhdeTiedot.getEmployedOver1Year())
					vacationDays35HourRule = vacationDays35HourRule.add(terms.getVacationDaysPerMonthOverYear());
				else 
					vacationDays35HourRule = vacationDays35HourRule.add(terms.getVacationDaysPerMonthUnderYear());
				
		
				full35RuleMonths++;
			}
		}
		
							
		
	}
	
	
	
	void setFinalVacationDays() {
		if(terms.getCountingRule().equals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_14.rule)) {
			//"Loman pituutta laskettaessa päivän osa pyöristetään täyteen lomapäivään."
			finalVacationDays = (int)Math.ceil(vacationDays14DayRule.doubleValue());
			fullMonths = full14RuleMonths;
		}
		if(terms.getCountingRule().equals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_35.rule)) {
			finalVacationDays = (int)Math.ceil(vacationDays35HourRule.doubleValue());
			fullMonths = full35RuleMonths;
		}
		if(työsuhdeTiedot.getWorkHoursPerWeek().size()>0)
			dayCount = dayCountAndNonRegisteredDays;
	}

	
	/**
	 * @param day
	 */
	void addToTotalWage(Day day){
		if(työsuhdeTiedot.getWages().get(day.PVM)!=null) {
			currentWage = työsuhdeTiedot.getWages().get(day.PVM);
		}	
		if(työsuhdeTiedot.getWorkHoursPerWeek().size()==0 && day.TYÖPÄIVÄ!=null && day.TYÖPÄIVÄ.equals("Arkipyhäkorvaukset")) 
			return;
		
		BigDecimal t = BigDecimal.valueOf(day.TUNNIT_YHT);
		wageAllYear = wageAllYear.add(currentWage.multiply(t));
		workHoursAllYear = workHoursAllYear.add(t);
		if(day.TYÖAIKAA>8)
			overtimeAllYear = overtimeAllYear.add(t.subtract(BigDecimal.valueOf(8)));
		if(day.PROVISIOPALKKA!=0) {
			comissionAllYear = comissionAllYear.add(BigDecimal.valueOf(day.PROVISIOPALKKA));
		}
			
	}
	
	
	
	
	
	/**
	 * @return
	 */
	void countVacationPay() {
		
		
		BigDecimal comission = BigDecimal.valueOf(0);

		if(comissionAllYear.intValue()>0 && terms.getIncludeComission()) {
			if(!terms.getCalculationRule().equals("prosenttiperusteinen")) {
			comission = comissionAllYear
					.divide(BigDecimal.valueOf(dayCount),2,RoundingMode.HALF_UP)
					.multiply(BigDecimal.valueOf(finalVacationDays));
			}
			else {
				if(työsuhdeTiedot.getEmployedOver1Year())
					comission = comissionAllYear.multiply(terms.getPrecentageOverYear());
				else
					comission = comissionAllYear.multiply(terms.getPrecentageUnderYear());
			}
			
		}
		
		//(Vuosilomalaki § 10)
			//"Työntekijällä, jonka palkka on sovittu viikolta tai sitä pidemmältä ajalta, 
			//on oikeus saada tämä palkkansa myös vuosiloman ajalta." 
		if(terms.getCalculationRule().equals(LomapalkanLaskentaSäännöt.KUUKAUSIPALKKAPERUSTEINEN.rule)) {
			BigDecimal monthlySalary = workHoursAllYear
					.divide(BigDecimal.valueOf(12),2,RoundingMode.HALF_UP)
					.multiply(työsuhdeTiedot.getWages().get(työsuhdeTiedot.getStartDate()));
			vacationPay =(monthlySalary
					.divide(terms.getConstDivider(),2,RoundingMode.HALF_UP))
					.multiply(BigDecimal.valueOf(finalVacationDays))
					.add(comission);
			laskelma.setKuukausiPalkka(currentWage); 
			laskelma.setkuukaudenTyöpäivät(terms.getConstDivider());
			laskelma.setLomapäivät(BigDecimal.valueOf(finalVacationDays));
			laskelma.setLomapalkka(vacationPay);
			laskelma.setLomajaksonTyöpäivät(BigDecimal.valueOf(dayCount));
			laskelma.setVuodenAnsioIlmanKorotusosia(wageAllYear);
			laskelma.export(terms.getCalculationRule());
		}
		
		//(Vuosilomalaki § 11)
			//"Muun kuin viikko- tai kuukausipalkalla työskentelevän sellaisen 
			//työntekijän vuosilomapalkka, joka sopimuksen mukaan työskentelee 
			//vähintään 14 päivänä kalenterikuukaudessa, lasketaan kertomalla hänen 
			//keskipäiväpalkkansa lomapäivien määrän perusteella määräytyvällä 
			//kertoimella:"
		if(terms.getCalculationRule().equals(LomapalkanLaskentaSäännöt.KESKIPÄIVÄPALKKAPERUSTEINEN.rule)) {
			BigDecimal overTime = overtimeAllYear.divide(BigDecimal.valueOf(8));
			BigDecimal averageDailyWage = wageAllYear
					.divide(BigDecimal.valueOf(dayCount),2,RoundingMode.HALF_UP);
		
		//(Vuosilomalaki § 11)
			//Jos työntekijän viikoittaisten työpäivien määrä on sopimuksen mukaan pienempi tai 
			//suurempi kuin viisi, keskipäiväpalkka kerrotaan viikoittaisten työpäivien määrällä 
			//ja jaetaan viidellä.
			if(Math.round(regularWorkDaysPerWeek.doubleValue())!=5) {
				averageDailyWage = averageDailyWage.multiply(regularWorkDaysPerWeek)
								.divide(BigDecimal.valueOf(5),2,RoundingMode.HALF_UP);
				laskelma.setVkoTyöpäivätJaettuna5(regularWorkDaysPerWeek.divide(BigDecimal.valueOf(5)));
			}
			
			vacationPay =averageDailyWage
					.multiply(terms.getVacationX().get(finalVacationDays))
					.add(comission);
			laskelma.setVuodenAnsioIlmanKorotusosia(wageAllYear);
			laskelma.setTyöpäivätJaYlityö(overTime.add(BigDecimal.valueOf(dayCount)));
			laskelma.setKeskimääräinenPäiväPalkka(averageDailyWage);
			laskelma.setLomapäivät(BigDecimal.valueOf(finalVacationDays));
			laskelma.setLomapalkka(vacationPay);
			laskelma.setLomajaksonTyöpäivät(BigDecimal.valueOf(dayCount));
			laskelma.setVuosilomalainKerroin(terms.getVacationX().get(finalVacationDays));
			laskelma.setVuodenYlityö(overtimeAllYear);
			laskelma.setVuodenProvisio(comissionAllYear);
			laskelma.export(terms.getCalculationRule());
		}
		
		//(Vuosilomalaki § 12)
			//"Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä 
			//kalenterikuukaudessa työtä tekevän työntekijän vuosilomapalkka on 9
			//prosenttia taikka työsuhteen jatkuttua lomakautta edeltävän 
			//lomanmääräytymisvuoden loppuun mennessä vähintään vuoden 11,5 prosenttia
			//lomanmääräytymisvuoden aikana työssäolon ajalta maksetusta tai 
			//maksettavaksi erääntyneestä palkasta lukuun ottamatta hätätyöstä ja 
			//lain tai sopimuksen mukaisesta ylityöstä maksettavaa korotusta."
		if(terms.getCalculationRule().equals(LomapalkanLaskentaSäännöt.PROSENTTIPERUSTEINEN.rule)) {
			BigDecimal precentage;
			if(työsuhdeTiedot.getEmployedOver1Year())
				precentage= terms.getPrecentageOverYear();
			else 
				precentage = terms.getPrecentageUnderYear();
			
			
			vacationPay = wageAllYear.multiply(precentage)
					.add(comission);
			laskelma.setVuodenAnsioIlmanKorotusosia(wageAllYear);
			laskelma.setSaamattaJäänytPalkkio(BigDecimal.ZERO);
			laskelma.setLomajaksonTyöpäivät(BigDecimal.valueOf(dayCount));
			laskelma.setKorvausprosentti(precentage);
			laskelma.setLomapäivät(BigDecimal.valueOf(finalVacationDays));
			laskelma.setLomapalkka(vacationPay);
			laskelma.setVuodenYlityö(overtimeAllYear);
			laskelma.setVuodenProvisio(comissionAllYear);
			laskelma.export(terms.getCalculationRule());
		}
		laskelma.setLomapäivienLaskentaSääntö(terms.getCountingRule());
		laskelma.setLomapalkanLaskentaSääntö(terms.getCalculationRule());
		laskelma.setKartuttavatKuukaudet(BigDecimal.valueOf(fullMonths));
		laskelma.setTyösuhdeKestänytYliVuosi(työsuhdeTiedot.getEmployedOver1Year());
		if(terms.getCountingRule().equals("prosenttiperusteinen"))
			laskelma.setLomapäivätKuukaudessa(BigDecimal.valueOf(2));
		else {
			if(työsuhdeTiedot.getEmployedOver1Year())
				laskelma.setLomapäivätKuukaudessa(terms.getVacationDaysPerMonthOverYear());
			else 
				laskelma.setLomapäivätKuukaudessa(terms.getVacationDaysPerMonthUnderYear());
		}
		
	}
	

	

	/**
	 * 
	 */
	void setCalculationRule(){
		
		
		if(!työsuhdeTiedot.getIsHourlyWage()) {
			if(terms.getCalculationRuleExceptionSalary()!=null) {
				//Työehtosopimus
				terms.setCalculationRule(terms.getCalculationRuleExceptionSalary());
			}
			else
				if(terms.getCountingRule().equals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_35.rule)) {
				//(Vuosilomalaki § 10)
					//Jos edellä 6 §:n 2 momentissa tarkoitetun työntekijän sopimuksen mukainen 
					//työaika on niin vähäinen, että tästä syystä vain osa kalenterikuukausista on
					//täysiä lomanmääräytymiskuukausia, hänen lomapalkkansa lasketaan 12 §:n mukaan.
					terms.setCalculationRule(LomapalkanLaskentaSäännöt.PROSENTTIPERUSTEINEN.rule);
			
				}
				else if(työsuhdeTiedot.getWages().size()>1 && työsuhdeTiedot.getWorkHoursPerWeek().size()>1) {
				//(Vuosilomalaki§ 10)
					//Lomapalkka lasketaan 12 §:n mukaan myös silloin, kun työntekijän työaika ja vastaavasti 
					//palkka on muuttunut lomanmääräytymisvuoden aikana.
					terms.setCalculationRule(LomapalkanLaskentaSäännöt.PROSENTTIPERUSTEINEN.rule); 
				}
			
				else
					terms.setCalculationRule(LomapalkanLaskentaSäännöt.KUUKAUSIPALKKAPERUSTEINEN.rule); 
		}
		
		
		else {
			if(terms.getCalculationRuleExceptionWage()!=null) {
				//Työehtosopimus
				terms.setCalculationRule(terms.getCalculationRuleExceptionWage());;
		}
			else {
				if(terms.getCountingRule().equals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_35.rule) || terms.getCountingRule().equals(LomapäivienLaskentaSäännöt.SÄÄNTÖ_MUU.rule))
				//(Vuosilomalaki § 12)
					//Muun kuin viikko- tai kuukausipalkalla alle 14 päivänä kalenterikuukaudessa 
					//työtä tekevän	
					terms.setCalculationRule(LomapalkanLaskentaSäännöt.PROSENTTIPERUSTEINEN.rule); 
				
				else	
				//(Vuosilomalaki § 11)
					//Muun kuin viikko- tai kuukausipalkalla työskentelevän sellaisen työntekijän vuosilomapalkka,
					//joka sopimuksen mukaan työskentelee vähintään 14 päivänä kalenterikuukaudessa,
					terms.setCalculationRule(LomapalkanLaskentaSäännöt.KESKIPÄIVÄPALKKAPERUSTEINEN.rule);
			}
		}
		
	}
	
}
