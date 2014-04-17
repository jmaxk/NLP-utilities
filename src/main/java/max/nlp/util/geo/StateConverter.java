package max.nlp.util.geo;

import java.util.HashMap;
import java.util.Map;

public class StateConverter {
		private static Map<String, String> states = new HashMap<String, String>();

		static {
			states.put("Alabama", "AL");
			states.put("Alaska", "AK");
			states.put("Arizona", "AZ");
			states.put("Arkansas", "AR");
			states.put("California", "CA");
			states.put("Colorado", "CO");
			states.put("Connecticut", "CT");
			states.put("Delaware", "DE");
			states.put("District Of Columbia", "DC");
			states.put("Florida", "FL");
			states.put("Georgia", "GA");
			states.put("Guam", "GU");
			states.put("Hawaii", "HI");
			states.put("Idaho", "ID");
			states.put("Illinois", "IL");
			states.put("Indiana", "IN");
			states.put("Iowa", "IA");
			states.put("Kansas", "KS");
			states.put("Kentucky", "KY");
			states.put("Louisiana", "LA");
			states.put("Maine", "ME");
			states.put("Maryland", "MD");
			states.put("Massachusetts", "MA");
			states.put("Michigan", "MI");
			states.put("Minnesota", "MN");
			states.put("Mississippi", "MS");
			states.put("Missouri", "MO");
			states.put("Montana", "MT");
			states.put("Nebraska", "NE");
			states.put("Nevada", "NV");
			states.put("New Jersey", "NJ");
			states.put("New Mexico", "NM");
			states.put("New York", "NY");
			states.put("North Carolina", "NC");
			states.put("North Dakota", "ND");
			states.put("Ohio", "OH");
			states.put("Oklahoma", "OK");
			states.put("Oregon", "OR");
			states.put("Pennsylvania", "PA");
			states.put("Rhode Island", "RI");
			states.put("South Carolina", "SC");
			states.put("South Dakota", "SD");
			states.put("Tennessee", "TN");
			states.put("Texas", "TX");
			states.put("Utah", "UT");
			states.put("Vermont", "VT");
			states.put("Virginia", "VA");
			states.put("Washington", "WA");
			states.put("West Virginia", "WV");
			states.put("Wisconsin", "WI");
			states.put("Wyoming", "WY");
		}

		public static String convertToAbr(String fullState,
				boolean caseSensitive) {
			fullState = fullState.trim();
			if (caseSensitive) {
				fullState = fullState.toLowerCase();
			}
			String abbr = states.get(fullState);
			return abbr;

		}

		public static Map<String, String> getStates() {
			return states;
		}
	}
