package max.nlp.wrappers.berkeley;

public class WordAlignment {

	private String eWord;
	private String fWord;
	private Integer eIndex;
	private Integer fIndex;

	public String geteWord() {
		return eWord;
	}
	public void seteWord(String eWord) {
		this.eWord = eWord;
	}
	public String getfWord() {
		return fWord;
	}
	public void setfWord(String fWord) {
		this.fWord = fWord;
	}
	public Integer geteIndex() {
		return eIndex;
	}
	public void seteIndex(Integer eIndex) {
		this.eIndex = eIndex;
	}
	public Integer getfIndex() {
		return fIndex;
	}
	public void setfIndex(Integer fIndex) {
		this.fIndex = fIndex;
	}
	@Override
	public String toString() {
		return "WordAlignment [eWord=" + eWord + ", fWord=" + fWord
				+ ", eIndex=" + eIndex + ", fIndex=" + fIndex + "]";
	}
	
}
