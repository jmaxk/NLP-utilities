package max.nlp.wrappers.pialgin;

public class PialignTranslation {

	private String fWord;
	private String eWord;
	private double fToE, eToF;
	
	@Override
	public String toString() {
		return "PialignTranslation [fWord=" + fWord + ", eWord=" + eWord
				+ ", fToE=" + fToE + ", eToF=" + eToF + "]";
	}

	public String getfWord() {
		return fWord;
	}

	public void setfWord(String fWord) {
		this.fWord = fWord;
	}

	public String geteWord() {
		return eWord;
	}

	public void seteWord(String eWord) {
		this.eWord = eWord;
	}

	public double getfToE() {
		return fToE;
	}

	public void setfToE(double fToE) {
		this.fToE = fToE;
	}

	public double geteToF() {
		return eToF;
	}

	public void seteToF(double eToF) {
		this.eToF = eToF;
	}

	public PialignTranslation(String fWord,String eWord,double fToE, double eToF) {

		this.fWord = fWord;
		this.eWord = eWord;
		this.fToE = fToE;
		this.eToF = eToF;
	}


}
