import java.io.Serializable;

/**
 * @author OMISTAJA
 *
 */
public class Day implements Serializable {
	//@SerializedName("Pvm")
	String PVM;
	//@SerializedName("Työpäivä?")
	String TYÖPÄIVÄ;
	//@SerializedName("Tehtäväryhmä")
	String TEHTÄVÄRYHMÄ;
	//@SerializedName("Tehtyjen töiden kuvaus ja kulut")
	String TEHTYJEN_TÖIDEN_KUVAUS_JA_KULUT;
	//@SerializedName("Tunnit yht.")
	double TUNNIT_YHT;
	//@SerializedName("Työaikaa")
	double TYÖAIKAA;
	//@SerializedName("Norm. tunnit (ei ylitöitä, ei lisiä)")
	double NORM_TUNNIT;
	//@SerializedName("Provisiopalkka")
	double PROVISIOPALKKA;
	//@SerializedName("Column9")
	String PALKKA;
	
}
