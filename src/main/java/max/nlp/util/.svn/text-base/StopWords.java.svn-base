package max.nlp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StopWords {

	private static StopWords self;

	public static StopWords getInstance() {
		if (self == null)
			self = new StopWords();
		return self;
	}

	private void readStopWords() {
		try {
			stopWords = new ArrayList<String>();
			InputStream inputStream = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("lang/eng/stopwords.txt");
			BufferedReader idiomsReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = "";
			while ((line = idiomsReader.readLine()) != null) {
				stopWords.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private StopWords() {
		if (stopWords == null)
			readStopWords();
	}

	private List<String> stopWords;

	public String removeStopWords(String unfiltered) {
		StringBuilder sb = new StringBuilder();
		String[] words = unfiltered.split(" ");
		for (String word : words) {
			if (!stopWords.contains(word.toLowerCase())) {
				sb.append(word + " ");
			}
	
		}
		return sb.toString().trim();
	}
	
	public static void main(String[] args) {
		StopWords s = StopWords.getInstance();
		String newstr = s.removeStopWords("get off of the very tall building");
		System.out.println(newstr);
	}

}
