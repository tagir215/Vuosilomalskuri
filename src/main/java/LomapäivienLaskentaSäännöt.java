
public enum LomapäivienLaskentaSäännöt {
	SÄÄNTÖ_14("14_päivän_sääntö"), 
	SÄÄNTÖ_35("35_tunnin_sääntö"), 
	SÄÄNTÖ_MUU("prosenttiperusteinen");
	String rule;
	LomapäivienLaskentaSäännöt(String s){
		rule = s;
	}
}
