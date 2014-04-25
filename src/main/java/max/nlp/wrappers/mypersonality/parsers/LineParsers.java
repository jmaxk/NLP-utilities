package max.nlp.wrappers.mypersonality.parsers;

import java.util.function.Function;

import max.nlp.util.geo.StateConverter;
import max.nlp.wrappers.mypersonality.objects.Location;
import max.nlp.wrappers.mypersonality.objects.Status;
import max.nlp.wrappers.mypersonality.objects.User;

public class LineParsers {

	/**
	 *  * Converts a line from statuses.csv to a Status object. 
	 */
	public static Function<String, Status> lineToStatus = (line) -> {
		String[] sects = line.split(",");
		String userId = sects[0].replaceAll("\"", "");
		String date = sects[1];
		String content = sects[2];
		Status status = new Status(userId, content, date);
		return status;
	};

	
	/**
	 * Converts a line from addresses.csv to a User object. Tries to extract the hometown and current locaiton
	 */
	public static Function<String, User> lineToUser = (line) -> {
		String[] p = line.split(",");
		String id = p[0].replaceAll("\"", "");
		User u = new User(id);
		//
		String hometownId = p[4].replaceAll("\"", "");

		String hometown_state = p[12].replaceAll("\"", "");
		String abbr_hometown_state = StateConverter.convertToAbr(
				hometown_state, false);
		String hometown_city = p[11].replaceAll("\"", "");
		if (!p[5].equals("\"\"")) {
			Double hometown_lat = Double.parseDouble(p[5].replaceAll("\"", ""));
			Double hometown_lon = Double.parseDouble(p[6].replaceAll("\"", ""));
			Location hometown = new Location(hometownId, hometown_lat,
					hometown_lon, hometown_city, abbr_hometown_state);
			u.setHometown(hometown);
		}

		String currentLocId = p[1].replaceAll("\"", "");
		Location current_location;

		String current_state = p[12].replaceAll("\"", "");
		String current_abbr_state = StateConverter.convertToAbr(current_state,
				false);
		String current_city = p[11].replaceAll("\"", "");
		if (!p[2].equals("\"\"")) {
			Double current_lat = Double.parseDouble(p[2].replaceAll("\"", ""));
			Double curernt_lon = Double.parseDouble(p[3].replaceAll("\"", ""));
			current_location = new Location(currentLocId, current_lat,
					curernt_lon, current_city, current_abbr_state);
			u.setCurrentLoc(current_location);
			// }
		}
		//
		return u;
	};
	

	/**
	 * Maps a line from address.csv to a Locatoin object
	 */
	public static Function<String, Location> lineToAddress = (line) -> {
		String[] section = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
		String id = section[0];
		String location = section[1];
		String[] subLocations = location.split(",");
		if (subLocations.length == 2) {
			String city = subLocations[0].trim().replaceAll("\"", "");
			String state = subLocations[1].trim().replaceAll("\"", "");
			if (!section[2].equals("NULL") && !section[3].equals("NULL")) {
				Double latitude = Double.parseDouble(section[2]);
				Double longitude = Double.parseDouble(section[3]);
				state = (StateConverter.convertToAbr(state, false));
				Location loc = new Location(id, latitude, longitude, city,
						state);
				return loc;
			}
		}
		return null;
	};

}
