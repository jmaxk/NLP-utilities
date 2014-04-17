package max.nlp.wrappers.mypersonality.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import max.nlp.util.geo.StateConverter;

public class Reader {

	private static String root = "/home/jmaxk/resources/mypersonality/";
	private static String addresses = root + "address.csv";
	private static String statuses = root + "user_status.csv";

	private static String locDict = root + "location_dict.csv";

	private Map<String, User> usersIndexedByID = new HashMap<String, User>(1000000);
	private Map<String, List<User>> usersIndexedByState = new HashMap<String, List<User>>(1000000);
	private Map<String, List<Status>> statusIndexedByUser = new HashMap<String, List<Status>>(1000000);

	public Map<String, User> getUsersIndexedByID() {
		return usersIndexedByID;
	}

	public Map<String, List<User>> getUsersIndexedByState() {
		return usersIndexedByState;
	}

	public Map<String, List<Status>> getStatusIndexedByUser() {
		return statusIndexedByUser;
	}

	public List<Location> getLocationDictionary() {
		return locationDictionary;
	}

	private List<User> persons;
	private List<Location> locationDictionary;

	public void loadUsers(Predicate<User> userFilter) {
		try {
			InputStream is = new FileInputStream(new File(addresses));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			persons = br.lines().skip(1).map(lineToUser)
					.filter(userFilter).collect(Collectors.toList());
			persons.stream().forEach(index);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Consumer<User> index = (u) -> {
		usersIndexedByID.put(u.getId(), u);
		Location location = u.getHomeLocation();
		if (location != null) {
			String state = location.getState();
			List<User> usersForState = usersIndexedByState.getOrDefault(state,
					new ArrayList<User>());
			usersForState.add(u);
			usersIndexedByState.put(state, usersForState);
		}
	};

	public Map<String, User> indexbyIds(List<User> users) {
		Map<String, User> index = new HashMap<String, User>();
		for (User u : users) {
			index.put(u.getId(), u);
		}
		return index;
	}

	public Function<String, User> lineToUser = (line) -> {
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
		// else {
		// System.out.println(hometownId);
		// for(Location la: locationDictionary){
		// if(la.getId().equals(p[4].replaceAll("\"", "")){
		//
		// }
		// }
		// }

		String currentLocId = p[1].replaceAll("\"", "");
		Location current_location;
		// if (!currentLocId.isEmpty()) {
		// List<Location> currentLocs = locationDictionary
		// .stream()
		// .filter((location) -> location.getId().equals(currentLocId))
		// .collect(Collectors.toList());
		// if (currentLocs.size() == 1) {
		// current_location = currentLocs.get(0);
		// }
		// } else {
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

	public void loadLocDict() {
		try {
			InputStream is = new FileInputStream(new File(locDict));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			locationDictionary = br.lines().skip(1)
					.map(mapToAddress).collect(Collectors.toList());
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	Predicate<Location> isFromAmerica = (location) -> location != null
			&& location.getState() != null;

	public static Function<String, Location> mapToAddress = (line) -> {
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

	public Collection<User> getUsers() {
		return usersIndexedByID.values();
	}

	public void attachStatusesToUsers() {
		try {
			InputStream is = new FileInputStream(new File(statuses));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			br.lines().skip(1)
					.forEach((line) -> updateUserWithStatus(line));
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadStatuses(Consumer<String> statusProcessor) {
		try {
			InputStream is = new FileInputStream(new File(statuses));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			br.lines().skip(1).forEach(statusProcessor);
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Status> getStatusesForUser(String userId) {
		List<Status> lines = new ArrayList<Status>();
		try {
			InputStream is = new FileInputStream(new File(statuses));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] sects = line.split(",");
				String id = sects[0].replaceAll("\"", "");
				if (id.equals(userId)) {
					String date = sects[1];
					String content = sects[2];
					Status status = new Status(userId, content, date);
					lines.add(status);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}

	public static Function<String, Status> lineToStatus = (line) -> {
		String[] sects = line.split(",");
		String userId = sects[0].replaceAll("\"", "");
		String date = sects[1];
		String content = sects[2];
		Status status = new Status(userId, content, date);
		return status;
	};

	public void updateUserWithStatus(String line) {
		String[] sects = line.split(",");
		String userId = sects[0].replaceAll("\"", "");
		User u = usersIndexedByID.get(userId);
		
		//If the user exists, we want to give it statuses
		if (u != null) {
			List<Status> statusesForUser = statusIndexedByUser.getOrDefault(
					userId, new ArrayList<Status>());
			String date = sects[1];
			String content = sects[2];
			Status status = new Status(userId, content, date);
			statusesForUser.add(status);
			statusIndexedByUser.put(userId, statusesForUser);
		}
	}

}
