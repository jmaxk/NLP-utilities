package max.nlp.wrappers.uwn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import max.nlp.wrappers.WrappingConfiguration;

import org.lexvo.uwn.Entity;
import org.lexvo.uwn.Statement;
import org.lexvo.uwn.UWN;

public class UWNWrapper {

	private static UWNWrapper self;
	private UWN uwn;

	public UWN getUwn() {
		return uwn;
	}

	public void setUwn(UWN uwn) {
		this.uwn = uwn;
	}

	private WrappingConfiguration config = WrappingConfiguration.getInstance();

	public static UWNWrapper getInstance() {
		if (self == null)
			self = new UWNWrapper();
		return self;
	}

	private UWNWrapper() {
		try {
			uwn = new UWN(new File(config.getUWNDir()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Statement> getWord(String word, String language) {
		List<Statement> matchingwords = new ArrayList<Statement>();
		try {
			Iterator<Statement> it = uwn.getMeanings(Entity.createTerm(word,
					language));
			while (it.hasNext()) {
				Statement meaningStatement = it.next();
				Entity meaning = meaningStatement.getObject();

				Iterator<Statement> r = uwn.get(meaning);
				while (r.hasNext()) {
					matchingwords.add(r.next());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matchingwords;
	}

	public List<Statement> getMeaningInLanguage(String word, String language,
			String desiredLanguage) {
		List<Statement> matchingwords = new ArrayList<Statement>();
		try {
			Iterator<Statement> it = uwn.getMeanings(Entity.createTerm(word,
					language));
			while (it.hasNext()) {
				Statement meaningStatement = it.next();
				Entity meaning = meaningStatement.getObject();

				Iterator<Statement> r = uwn.get(meaning);
				while (r.hasNext()) {
					Statement matchingWord = r.next();
					if (matchingWord.getObject().toString()
							.contains(desiredLanguage))
						matchingwords.add(matchingWord);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matchingwords;
	}

	public List<String> getMeaningInLanguageAsStrings(String word,
			String language, String desiredLanguage) {
		List<String> matchingwords = new ArrayList<String>();
		try {
			Iterator<Statement> it = uwn.getMeanings(Entity.createTerm(word,
					language));
			while (it.hasNext()) {
				Statement meaningStatement = it.next();
				Entity meaning = meaningStatement.getObject();

				Iterator<Statement> r = uwn.get(meaning);
				while (r.hasNext()) {
					Statement matchingWord = r.next();
					if (matchingWord.getObject().toString()
							.contains(desiredLanguage))
						matchingwords.add(matchingWord.getObject().toString()
								.split("/")[2]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matchingwords;
	}

	public static void main(String[] args) {
		UWNWrapper test = UWNWrapper.getInstance();
		// List<Statement> matches = test.getMeaningInLanguage("hate", "eng",
		// "spa");
		// for(Statement e : matches){
		// System.out.println(e);
		// }
		List<String> matches = test.getMeaningInLanguageAsStrings("cake",
				"eng", "spa");
		for (String s : matches)
			System.out.println(s);
	}

}
