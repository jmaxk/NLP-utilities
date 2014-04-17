package max.nlp.wrappers.pialgin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadPhraseTable {

	public static void main(String[] args) {
		ReadPhraseTable s = new ReadPhraseTable();
		List<PialignTranslation> translations = s
				.parseTranslations("/home/max/pialign-0.2.4/out/align.1.pt");
		Map<String, PialignTranslation> english = s.indexTranslations(translations, "e");
		System.out.println(english.get("put"));
	}

	public Map<String, PialignTranslation> indexTranslations(
			List<PialignTranslation> translations, String whichLang) {
		Map<String, PialignTranslation> index = new HashMap<String, PialignTranslation>();
		boolean eLang = true;
		if (whichLang.equals("f"))
			eLang = false;
		for (PialignTranslation translation : translations) {
			String key;
			if (eLang)
				key = translation.geteWord();
			else
				key = translation.getfWord();
			index.put(key, translation);
		}
		return index;
	}

	public List<PialignTranslation> parseTranslations(String inputFile) {
		List<PialignTranslation> translations = new ArrayList<PialignTranslation>();

		try {
			BufferedReader b = new BufferedReader(new FileReader(new File(
					inputFile)));
			String line = "";
			while ((line = b.readLine()) != null) {
				String[] parts = line.split("\\|\\|\\|");
				String fWord = parts[0].trim().toLowerCase();
				String eWord = parts[1].trim().toLowerCase();

				String[] translationValues = parts[2].trim().split(" ");
				double eToFProb = Double.parseDouble(translationValues[0]);
				double fToEProb = Double.parseDouble(translationValues[1]);

				translations.add(new PialignTranslation(fWord, eWord, fToEProb,
						eToFProb));
			}
			b.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return translations;
	}
}
