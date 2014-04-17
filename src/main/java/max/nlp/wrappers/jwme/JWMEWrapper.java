package max.nlp.wrappers.jwme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import max.nlp.wrappers.WrappingConfiguration;

import edu.mit.jmwe.data.IMWE;
import edu.mit.jmwe.data.IToken;
import edu.mit.jmwe.data.Token;
import edu.mit.jmwe.detect.CompositeDetector;
import edu.mit.jmwe.detect.Consecutive;
import edu.mit.jmwe.detect.Exhaustive;
import edu.mit.jmwe.detect.IMWEDetector;
import edu.mit.jmwe.detect.ProperNouns;
import edu.mit.jmwe.detect.StopWords;
import edu.mit.jmwe.index.IMWEIndex;
import edu.mit.jmwe.index.IndexBuilder;
import edu.mit.jmwe.index.MWEIndex;

public class JWMEWrapper {

	private static JWMEWrapper self;
	private WrappingConfiguration config = WrappingConfiguration.getInstance();
	private IMWEIndex index;

	public IMWEDetector detectorFromName(String name) {
		// switch(name){
		if (name.equals("consecutive"))
			return new Consecutive(index);
		else if (name.equals("exhaustive"))
			return new Exhaustive(index); // / StopWords
		else if (name.equals("stopWords"))
			return new StopWords(index);
		else if (name.equals("properNouns"))
			return ProperNouns.getInstance();
		else if (name.equals("all"))
			return new CompositeDetector(new Consecutive(index),
					new Exhaustive(index), new StopWords(index),
					ProperNouns.getInstance());
		else
			return null;
		// }
	}

	public static JWMEWrapper getInstance() {
		if (self == null)
			self = new JWMEWrapper();
		return self;
	}

	private JWMEWrapper() {
		try {
			File idxData = new File(config.getJWMEIndexFile());
			index = new MWEIndex(idxData);
			index.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void makeNewIndexFile(String indexName, String newIndexFile,
			List<String> forms) {
		try {
			IMWEIndex newIndex = new MWEIndex(forms);
			newIndex.open();
			List<String> headerLines = new LinkedList<String>();
			headerLines.add("Index: " + indexName);
			headerLines.add("Generated on: " + new Date());
			IndexBuilder.writeDataFile(newIndex, new FileOutputStream(new File(
					newIndexFile)), headerLines);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param tokenizedWords
	 *            , a map where each entry is <Word, POS>
	 * @return
	 */
	public List<IMWE<IToken>> simpleDetect(
			LinkedHashMap<String, String> tokenizedWords) {

		// convert the words to tokens
		List<IToken> sentence = new ArrayList<IToken>();
		for (Entry<String, String> e : tokenizedWords.entrySet()) {
			sentence.add(new Token(e.getKey(), e.getValue()));
		}
		IMWEDetector detector = new Consecutive(index);
		List<IMWE<IToken>> mwes = detector.detect(sentence);
		return mwes;
	}

	/**
	 * @param detector
	 *            . Options for detector are:
	 * 
	 * @param tokenizedWords
	 * @return
	 */
	public List<IMWE<IToken>> detect(IMWEDetector detector,
			LinkedHashMap<String, String> tokenizedWords) {

		// convert the words to tokens
		List<IToken> sentence = new ArrayList<IToken>();
		for (Entry<String, String> e : tokenizedWords.entrySet()) {
			sentence.add(new Token(e.getKey(), e.getValue()));
		}
		// IMWEDetector detector = new Consecutive(index);
		List<IMWE<IToken>> mwes = detector.detect(sentence);
		return mwes;

	}

	public static void prettyPrint(List<IMWE<IToken>> mwes) {
		for (IMWE<IToken> mwe : mwes)
			System.out.println(mwe.getForm());
	}

	public static void main(String[] args) {
		List<IToken> sentence = new ArrayList<IToken>();
		sentence.add(new Token("She", "DT"));
		sentence.add(new Token(" looked ", "VBD"));
		sentence.add(new Token("up", "RP"));
		sentence.add(new Token("the", "DT"));
		sentence.add(new Token(" world ", "NN"));
		sentence.add(new Token(" record ", "NN"));
		sentence.add(new Token(".", "."));
		IMWEDetector detector = JWMEWrapper.getInstance().detectorFromName(
				"consecutive");
		// run detector and print out results
		List<IMWE<IToken>> mwes = detector.detect(sentence);
		for (IMWE<IToken> mwe : mwes)
			System.out.println(mwe);

	}
}
