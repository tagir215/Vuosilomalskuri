import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
/**
 * @author OMISTAJA
 *
 */
public class TyösuhdeTiedot implements Serializable{
	Day[] days;
	
	
	private boolean isHourlyWage;
	private boolean employedOver1Year;
	private String startDate;
	private BigDecimal wageAtBeginning;
	private HashMap<String,BigDecimal>wages;
	private HashMap<String,BigDecimal>workHoursPerWeek;
	
	TyösuhdeTiedot(){
		
	}
	
	TyösuhdeTiedot(LomanMääräytymisEhdot terms, String startDate,boolean isHourlyWage, String path){
		this.startDate = startDate;
		this.isHourlyWage = isHourlyWage;
		
		File file = null;
		URL url = this.getClass().getResource(path);
		try {
			file = Paths.get(url.toURI()).toFile();
		} catch (URISyntaxException e1) {
			throw new RuntimeException(e1);
		}
		
		try {
			FileInputStream ims = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(ims);
			days = (Day[]) ois.readObject();
			ois.close();
			ims.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException ei) {
			throw new RuntimeException(ei);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		wages = new HashMap<String,BigDecimal>();
		workHoursPerWeek = new HashMap<String,BigDecimal>();
		wageAtBeginning = BigDecimal.ZERO;
		addWageChangeDates();
		setIsEmplyedOver1Year();
	}
	
	/**
	 * 
	 */
	void addWageChangeDates(){
		for(int i=0; i<days.length; i++) {
			
			if(days[i].PALKKA!=null) {
				BigDecimal wage = BigDecimal.valueOf(Double.parseDouble(days[i].PALKKA.replaceAll("[\\D]", "")));
				wages.put(days[i].PVM, wage);
				if(wageAtBeginning.equals(BigDecimal.ZERO))
					wageAtBeginning = wage;
			}
			
		}
	}
	
	/**
	 * 
	 */
	void setIsEmplyedOver1Year() {
		String[] dateStart = days[0].PVM.split("\\D+");
		String[] dateEnd = days[days.length-1].PVM.split("\\D+");
		
		int startMonth = Integer.parseInt(dateStart[0].replaceAll("\\D", ""));
		int startDay = Integer.parseInt(dateStart[1].replaceAll("\\D", ""));
		int startYear = Integer.parseInt(dateStart[2].replaceAll("\\D", ""));
		int endMonth = Integer.parseInt(dateEnd[0].replaceAll("\\D", ""));
		int endDay = Integer.parseInt(dateEnd[1].replaceAll("\\D", ""));
		int endYear = Integer.parseInt(dateEnd[2].replaceAll("\\D", ""));
		
		if(endYear-startYear>1) {
			employedOver1Year=true;
			return;
		}
		else if(endYear-startYear==1) {
			
			if(endMonth-startMonth>1) {
				employedOver1Year=true;
				return;
			}
			else if(endMonth-startMonth==1) {
				
				if(endDay-startDay>=0) {
					employedOver1Year=true;
					return;
				}
			}
		}
		
		employedOver1Year=false;
	}
	
	
	public String getStartDate() {return startDate;}
	public boolean getIsHourlyWage() {return isHourlyWage;}
	public boolean getEmployedOver1Year() {return employedOver1Year;}
	public BigDecimal getWageAtBeginning() {return wageAtBeginning;}
	public HashMap<String,BigDecimal> getWages() {return wages;}
	public HashMap<String,BigDecimal> getWorkHoursPerWeek() {return workHoursPerWeek;}
	
	public void setEmployedOver1Year(boolean b) {employedOver1Year = b;}
	public void setIsHourlyWage(boolean b) {isHourlyWage = b;}
	public void setStartDate(String d) {startDate = d;}
	public void addWageChangeDates(String s, BigDecimal d) {wages.put(s, d);}
	public void addWorkHoursPerWeek(String s,BigDecimal d) {workHoursPerWeek.put(s, d);}
	
	
	
	
	
}
