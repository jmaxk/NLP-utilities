package max.nlp.wrappers.berkeley;

import java.util.Comparator;

public class WordAlignmentComparator implements Comparator<WordAlignment>{

	public int compare(WordAlignment arg0, WordAlignment arg1) {
		return arg0.geteIndex().compareTo(arg1.geteIndex());
	}

}
