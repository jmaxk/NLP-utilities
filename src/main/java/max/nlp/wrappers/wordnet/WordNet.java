package max.nlp.wrappers.wordnet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import max.nlp.wrappers.WrappingConfiguration;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

public class WordNet {

	private static HashMap<String, WordNet> wordnets = new HashMap<String, WordNet>();
	private static WrappingConfiguration config = WrappingConfiguration
			.getInstance();

	private IDictionary dict;

	public IDictionary getDict() {
		return dict;
	}

	public void setDict(IDictionary dict) {
		this.dict = dict;
	}

	public static void main(String[] args) {
		WordNet w = WordNet.getInstance();
		List<String> syns = w.getSyns("fun");
		for (String s : syns)
			System.out.println(s);
	}

	public static WordNet getInstance() {
		String defaultWordnet = config.getDefaultWordNetDir();
		if (wordnets.isEmpty()) {
			WordNet w = new WordNet(defaultWordnet);
			wordnets.put(defaultWordnet, w);
			return w;
		} else
			return wordnets.get(defaultWordnet);
	}

	public static WordNet getInstance(String wordnetDirectory) {
		if (wordnets.containsKey(wordnetDirectory)) {
			return wordnets.get(wordnetDirectory);
		} else {
			WordNet w = new WordNet(wordnetDirectory);
			wordnets.put(wordnetDirectory, w);
			return w;
		}

	}

	public List<String> getSyns(String word) {
		List<IIndexWord> allSenses = getWord(word);
		List<String> syns = new ArrayList<String>();
		for (IIndexWord i : allSenses) {
			for (IWordID wordID : i.getWordIDs()) {
				IWord w = dict.getWord(wordID);
				for (IWord syn : w.getSynset().getWords()) {
					String lemma = syn.getLemma();
					if (!lemma.equals(word))
						syns.add(lemma);
				}
			}
		}
		return syns;
	}

	private WordNet(String path) {
		try {
			URL u = new URL("file", null, path);
			dict = new Dictionary(u);
			dict.open();
			// IIndexWord idxWord = dict . getIndexWord ("dog", POS. NOUN );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IIndexWord getWord(String word, POS pos) {
		return dict.getIndexWord(word, pos);
	}

	/**
	 * Get's all possible tags for a word
	 * 
	 * @param word
	 *            a word for which you don't know the part's of speech
	 * @return a list of all possible entries for <code> word </code>
	 */
	public List<IIndexWord> getWord(String word) {
		ArrayList<IIndexWord> words = new ArrayList<IIndexWord>();

		for (POS pos : POS.values()) {
			IIndexWord taggedWord = dict.getIndexWord(word, pos);
			if (taggedWord != null)
				words.add(taggedWord);
		}
		return words;
	}

	/**
	 * Iterates through all of the words. more of a template in case you want to
	 * do something with all of them.
	 */
	public void iterateThroughWords() {
		for (POS pos : POS.values()) {
			for (Iterator<IIndexWord> i = dict.getIndexWordIterator(pos); i
					.hasNext();) {
				for (IWordID wid : i.next().getWordIDs()) {
					// do something here
					System.out.println(wid);
				}
			}
		}
	}
}
