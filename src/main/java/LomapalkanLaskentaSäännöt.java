
public enum LomapalkanLaskentaSäännöt {
	
	KUUKAUSIPALKKAPERUSTEINEN("kuukausipalkkaperusteinen"), 
	KESKIPÄIVÄPALKKAPERUSTEINEN("keskipäiväpalkkaperusteinen"),
	PROSENTTIPERUSTEINEN("prosenttiperusteinen");
	String rule;
	LomapalkanLaskentaSäännöt(String s){
		rule = s;
	}
}
