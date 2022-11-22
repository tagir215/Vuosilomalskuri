import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author OMISTAJA
 *
 */
public class LomanMääräytymisEhdot implements Serializable {
	
	private String calculationRule;
	private String calcultionRuleExceptionWage;
	private String calculationRuleExceptionSalary;
	private HashMap<String,Integer> offDays;
	private HashMap<String,int[]>timeAtWorkEquivalentDays;
	private HashMap<Integer,BigDecimal>vacationX;
	private BigDecimal vacationDaysPerMonthOverYear;
	private BigDecimal vacationDaysPerMonthUnderYear;
	private BigDecimal precentageOverYear;
	private BigDecimal precentageUnderYear;
	private BigDecimal constDivider;
	private String countingRule;
	private boolean IncludeComission;
	
	
	
	LomanMääräytymisEhdot(){
		timeAtWorkEquivalentDays = new HashMap<String,int[]>();
		vacationX = new HashMap<Integer,BigDecimal>();
		offDays= new HashMap<String,Integer>();
	}
	public HashMap<Integer,BigDecimal> getVacationX() {return vacationX;}
	public HashMap<String,Integer> getOffDays() {return offDays;}
	public HashMap<String,int[]> getTimeAtWorkEquilentDays() {return timeAtWorkEquivalentDays;}
	public BigDecimal getVacationDaysPerMonthUnderYear() {return vacationDaysPerMonthUnderYear;}
	public BigDecimal getVacationDaysPerMonthOverYear() {return vacationDaysPerMonthOverYear;}
	public BigDecimal getConstDivider() {return constDivider;}
	public BigDecimal getPrecentageUnderYear() {return precentageUnderYear;}
	public BigDecimal getPrecentageOverYear() {return precentageOverYear;}
	public String getCalculationRule() {return calculationRule;}
	public String getCalculationRuleExceptionSalary() {return calculationRuleExceptionSalary;}
	public String getCalculationRuleExceptionWage() {return calcultionRuleExceptionWage;}
	public String getCountingRule() {return countingRule;}
	public boolean getIncludeComission() {return IncludeComission;}
	
	public void addTimeAtWorkEquilentDays(String s, int[] i) {timeAtWorkEquivalentDays.put(s, i);}
	public void addVacationX(Integer i, BigDecimal b) {vacationX.put(i, b);}
	public void setVacationDaysPerMonthUnderYear(BigDecimal d) {vacationDaysPerMonthUnderYear = d;}
	public void setVacationDaysPerMonthOverYear(BigDecimal d) {vacationDaysPerMonthOverYear = d;}
	public void setConstDivider(BigDecimal i) {constDivider = i;}
	public void setPrecentageUnderYear(BigDecimal p) {precentageUnderYear = p;}
	public void setPrecentageOverYear(BigDecimal p) {precentageOverYear = p;}
	public void addOffDays(String k, Integer o) {offDays.put(k,o);}
	public void setCalculationRule(String s) {calculationRule = s;}
	public void setCalculationRuleExceptionSalary(String s) {calculationRuleExceptionSalary = s;}
	public void setCalculationRuleExceptionWage(String s) {calcultionRuleExceptionWage = s;}
	public void setCountingRule(String s) {countingRule = s;}
	public void setIncludeComission(boolean b) {IncludeComission = b;}
	
	
}
