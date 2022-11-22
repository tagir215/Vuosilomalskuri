import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author OMISTAJA
 *
 */
public class LomaPalkkaLaskelma implements Serializable{
	private StringBuilder stringBuilder;
	private BigDecimal kuukausiPalkka, kuukaudenTyöpäivät,lomapäivät,lomapalkka,vuodenAnsioIlmanKorotusosia,
	lomajaksonTyöpäivät,työpäivätJaYlityö,keskimääräinenPäiväPalkka,vuosilomalainKerroin,
	saamattaJäänytPalkkio,korvausprosentti,vuodenYlityö,vuodenProvisio,lomapäivätKuukaudessa,
	vkoTyöpäivätJaettuna5,kartuttavatKuukaudet;
	private String lomapalkanLaskentasääntö,lomapäivienLaskentasääntö;
	private boolean työsuhdeKestänytYliVuosi;
	
	LomaPalkkaLaskelma(){
		kuukausiPalkka = BigDecimal.valueOf(0);
		kuukaudenTyöpäivät = BigDecimal.valueOf(0);
		lomapäivät = BigDecimal.valueOf(0);
		lomapalkka = BigDecimal.valueOf(0);
		vuodenAnsioIlmanKorotusosia = BigDecimal.valueOf(0);
		lomajaksonTyöpäivät = BigDecimal.valueOf(0);
		työpäivätJaYlityö = BigDecimal.valueOf(0);
		keskimääräinenPäiväPalkka = BigDecimal.valueOf(0);
		vuosilomalainKerroin = BigDecimal.valueOf(0);
		saamattaJäänytPalkkio= BigDecimal.valueOf(0);
		korvausprosentti = BigDecimal.valueOf(0);
		vuodenYlityö = BigDecimal.valueOf(0);
		vuodenProvisio = BigDecimal.valueOf(0);
		kartuttavatKuukaudet = BigDecimal.valueOf(0);
		lomapäivätKuukaudessa = BigDecimal.valueOf(0);
		lomapalkanLaskentasääntö = "-";
		lomapäivienLaskentasääntö = "-";
		
		
		
	}
	
	
	void setKuukausiPalkka(BigDecimal b) {kuukausiPalkka=b;}
	void setkuukaudenTyöpäivät(BigDecimal b) {kuukaudenTyöpäivät=b;}
	void setLomapäivät(BigDecimal b) {lomapäivät=b;}
	void setLomapalkka(BigDecimal b) {lomapalkka=b;}
	void setVuodenAnsioIlmanKorotusosia(BigDecimal b) {vuodenAnsioIlmanKorotusosia=b;}
	void setLomajaksonTyöpäivät(BigDecimal b) {lomajaksonTyöpäivät=b;}
	void setTyöpäivätJaYlityö(BigDecimal b) {työpäivätJaYlityö=b;}
	void setKeskimääräinenPäiväPalkka(BigDecimal b) {keskimääräinenPäiväPalkka=b;}
	void setVuosilomalainKerroin(BigDecimal b) {vuosilomalainKerroin=b;}
	void setSaamattaJäänytPalkkio(BigDecimal b) {saamattaJäänytPalkkio=b;}
	void setKorvausprosentti(BigDecimal b) {korvausprosentti=b;}
	void setVuodenYlityö(BigDecimal b) {vuodenYlityö=b;}
	void setVuodenProvisio(BigDecimal b) {vuodenProvisio=b;}
	void setLomapäivätKuukaudessa(BigDecimal b) {lomapäivätKuukaudessa=b;}
	void setVkoTyöpäivätJaettuna5(BigDecimal b) {vkoTyöpäivätJaettuna5=b;}
	void setKartuttavatKuukaudet(BigDecimal b) {kartuttavatKuukaudet=b;};
	void setLomapalkanLaskentaSääntö(String s) {lomapalkanLaskentasääntö=s;}
	void setLomapäivienLaskentaSääntö(String s) {lomapäivienLaskentasääntö=s;}
	void setTyösuhdeKestänytYliVuosi(boolean b) {työsuhdeKestänytYliVuosi=b;}
	
	BigDecimal getKuukausiPalkka() {return kuukausiPalkka;}
	BigDecimal getkuukaudenTyöpäivät() {return kuukaudenTyöpäivät;}
	BigDecimal getLomapäivät() {return lomapäivät;}
	BigDecimal getLomapalkka() {return lomapalkka;}
	BigDecimal getVuodenAnsioIlmanKorotusosia() {return vuodenAnsioIlmanKorotusosia;}
	BigDecimal getLomajaksonTyöpäivät() {return lomajaksonTyöpäivät;}
	BigDecimal getTyöpäivätJaYlityö() {return työpäivätJaYlityö;}
	BigDecimal getKeskimääräinenPäiväPalkka() {return keskimääräinenPäiväPalkka;}
	BigDecimal getVuosilomalainKerroin() {return vuosilomalainKerroin;}
	BigDecimal getSaamattaJäänytPalkkio() {return saamattaJäänytPalkkio;}
	BigDecimal getKorvausprosentti() {return korvausprosentti;}
	BigDecimal getVuodenYlityö() {return vuodenYlityö;}
	BigDecimal getVuodenProvisio() {return vuodenProvisio;}
	BigDecimal getLomapäivätKuukaudessa() {return lomapäivätKuukaudessa;}
	BigDecimal getVkoTyöpäivätJaettuna5() {return vkoTyöpäivätJaettuna5;}
	BigDecimal getKartuttavatKuukaudet() {return kartuttavatKuukaudet;};
	String getLomapalkanLaskentaSääntö() {return lomapalkanLaskentasääntö;}
	String getLomapäivienLaskentaSääntö() {return lomapäivienLaskentasääntö;}
	boolean getTyösuhdeKestänytYliVuosi() {return työsuhdeKestänytYliVuosi;}
	
	/**
	 * @param rule
	 */
	void export(String rule) {
		String report = "VUOSILOMAPALKKALASKELMA / VUOSILOMAKORVAUSLASKELMA";
		stringBuilder = new StringBuilder(report);
		switch(rule) {
			case "kuukausipalkkaperusteinen":
				stringBuilder.append(
				"\n_________________________________________________________________"
				+"\n1.KUUKAUSIPALKKAISET"
				+"\nKuukausipalkka	Kuukauden	Lomajakson	Lomapalkka/"
				+"\nloman alkaessa	työpäivät	työpäivät	lomakorvaus"
				+"\n"+kuukausiPalkka.intValue()+"		"+kuukaudenTyöpäivät+"		"+lomajaksonTyöpäivät+"		"+lomapalkka
				+"\n_________________________________________________________________");
				break;
			case "keskipäiväpalkkaperusteinen":
				stringBuilder.append(
				"\n_________________________________________________________________"
				+"\n2.TUNTIPALKKAISET VUOSILOMALAIN MUKAAN"
				+"\nVuoden		Tehdyts		Keskimääräinen		Vuosiloma	Lomapalkka/"
				+"\nansio		työpäivät	päiväpalkka			kerroin		lomakorvaus"
				+"\n"+vuodenAnsioIlmanKorotusosia.intValue()+"		"+lomajaksonTyöpäivät+"		"+keskimääräinenPäiväPalkka+"			"+vuosilomalainKerroin+"		"+lomapalkka
				+"\n_________________________________________________________________");
				break;
			case "prosenttiperusteinen":
				stringBuilder = new StringBuilder(report);
				stringBuilder.append(
				"\n______________________________________________s___________________"
				+"\n4.PROSENTTIPERUSTEINEN LOMAPALKKA /-KORVAUS"
				+"\nVuoden		Saamatta	Korvausprosentti	Lomapalkka/"
				+"\nansio		jäänyt palkkio	9% tai 11.5%		lomakorvaus"
				+"\n"+vuodenAnsioIlmanKorotusosia.intValue()+"		"+saamattaJäänytPalkkio+"		"+korvausprosentti+"			"+lomapalkka
				+"\n_________________________________________________________________");
				break;
		}
		report = stringBuilder.toString();
		System.out.println(report);
	
	}
}
