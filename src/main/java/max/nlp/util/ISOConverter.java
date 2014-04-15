package max.nlp.util;

import java.util.Locale;

public class ISOConverter {

	public static String ISO2toISO3(String iso2LetterCode) {
		Locale l = new Locale(iso2LetterCode);
		System.out.println(l.getDisplayLanguage());
		return l.getISO3Language();
	}
	
	

	public static String displayToISO3(String display) {
		String[] loc = Locale.getISOLanguages();
		for (int a = 0; a < loc.length; a++) {
			Locale l = new Locale(loc[a]);
			if (l.getDisplayLanguage().equals(display)){
				return l.getISO3Language();
			}
		}
		return "";
	}
}
