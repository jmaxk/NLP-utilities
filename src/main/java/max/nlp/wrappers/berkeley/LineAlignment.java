package max.nlp.wrappers.berkeley;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LineAlignment {

	private String[] eWords, fWords;

	public String getESentence(){
		StringBuilder sb = new StringBuilder();
		for(String word : eWords)
			sb.append(word + " ");
		return sb.toString();
	}
	
	public String getFSentence(){
		StringBuilder sb = new StringBuilder();
		for(String word : fWords)
			sb.append(word + " ");
		return sb.toString();
	}
	
	public String[] geteWords() {
		return eWords;
	}

	public void seteWords(String[] eWords) {
		this.eWords = eWords;
	}

	public String[] getfWords() {
		return fWords;
	}

	public void setfWords(String[] fWords) {
		this.fWords = fWords;
	}

	private List<WordAlignment> alignments;

	public List<WordAlignment> getAlignments() {
		return alignments;
	}

	public void setAlignments(List<WordAlignment> alignments) {
		this.alignments = alignments;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void addWordAlignment(WordAlignment w) {
		if (alignments == null)
			alignments = new ArrayList<WordAlignment>();
		alignments.add(w);
	}

	private int lineNumber;

	public static List<Integer> computeAlignments(boolean e, LineAlignment l,
			List<Integer> indeces) {
		List<Integer> alignments = new ArrayList<Integer>();
		for (WordAlignment w : l.getAlignments()) {
			for (Integer alignmentIndex : indeces) {
				int index;
				if (e)
					index = w.geteIndex();
				else
					index = w.getfIndex();
				if (index == alignmentIndex) {
					if (e)
						alignments.add(w.getfIndex());
					else
						alignments.add(w.geteIndex());
				}
			}
		}
		return alignments;
	}

	public static List<String> alignmentsAsWords(boolean e, LineAlignment l,
			List<Integer> alignments) {
		List<Integer> alignmentIndeces = computeAlignments(e, l, alignments);
		Collections.sort(alignmentIndeces);
		List<String> words = new ArrayList<String>();
		for (Integer alignment : alignmentIndeces) {
			if (e)
				words.add(l.findWordAtIndex(alignment, false));
			else
				words.add(l.findWordAtIndex(alignment, true));

		}
		return words;
	}

	public String findWordAtIndex(Integer desiredIndex, boolean e) {
		for (WordAlignment w : alignments) {
			int index;
			if (e)
				index = w.geteIndex();
			else
				index = w.getfIndex();
			if (index == desiredIndex) {
				if (e)
					return w.geteWord();
				else
					return w.getfWord();
			}
		}
		return "";
	}

	public static List<Integer> indecesOfPhrase(LineAlignment l, String phrase) {
		List<String> words = new ArrayList<String>();
		for (String word : phrase.split(" "))
			words.add(word);
		return indecesOfPhrase(l, words);
	}

	public static List<Integer> indecesOfPhrase(LineAlignment l,
			List<String> phrase) {
		String initialWord = phrase.get(0);
		int indexInPhrase = 0;
		int stoppingIndex = phrase.size();
		List<Integer> indeces = new ArrayList<Integer>();
		boolean potentialMatch = false;
		for (int i = 0; i < l.geteWords().length; i++) {
			String word =  l.geteWords()[i];
//			System.out.println(word);

			if (indexInPhrase == stoppingIndex) {
				return indeces;
			}
			if (!potentialMatch) {
				if (initialWord.equals(word)) {
					indeces.add(i);
					indexInPhrase++;
					potentialMatch = true;
//					System.out.println("start " + i);
				}
			} else {
				if (word.equals(phrase.get(indexInPhrase))) {
					indexInPhrase++;
					indeces.add(i);
//					System.out.println("next " + i);
				} else {
//					System.out.println("false " + i);
					indexInPhrase = 0;
					indeces = new ArrayList<Integer>();
					potentialMatch = false;
				}
			}

		}
		if (indexInPhrase == stoppingIndex) {
//			System.out.println("break" + indexInPhrase);
			return indeces;
		} else
			return new ArrayList<Integer>();

	}
}
