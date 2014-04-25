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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import max.nlp.wrappers.WrappingConfiguration;
import max.nlp.wrappers.mypersonality.objects.Location;
import max.nlp.wrappers.mypersonality.objects.Status;
import max.nlp.wrappers.mypersonality.objects.User;
import static max.nlp.wrappers.mypersonality.parsers.LineParsers.*;
public class Reader {

	// Location of the files
	private static String root = WrappingConfiguration.getInstance()
			.getMypersonalityDir();
	private static String addresses = root + "address.csv";
	private static String statuses = root + "user_status.csv";
	private static String locDict = root + "location_dict.csv";

	// The data structures that are avaialbe for use
	
	//Users indexed by their id. To initialize this, call loadUsersAndIndex()
	private Map<String, User> usersIndexedByID = new HashMap<String, User>(
			1000000);
	
	//Users indexed by their state. To initialize this, call loadUsersAndIndex(). Note that the state is hardcoded 
	// to be their hometown location, not their current location
	private Map<String, List<User>> usersIndexedByState = new HashMap<String, List<User>>(
			1000000);
	
	//A way to access statuses. The key is a user id, and the value is all of their statuses. To initliaze call
	// loadUsers(), then indexStatusesByUsers()
	private Map<String, List<Status>> statusIndexedByUser = new HashMap<String, List<Status>>(
			1000000);
	public Map<String, List<Status>> getStatusIndexedByUser() {
		return statusIndexedByUser;
	}

	private List<Location> locationDictionary;

	public Map<String, User> getUsersIndexedByID() {
		return usersIndexedByID;
	}

	public Map<String, List<User>> getUsersIndexedByState() {
		return usersIndexedByState;
	}

	public Map<String, List<Status>> get() {
		return statusIndexedByUser;
	}

	public List<Location> getLocationDictionary() {
		return locationDictionary;
	}
	
	public static void main(String[] args) {
		Reader r = new Reader();
		r.loadUsersAndIndex(User.hasCurrentLoc);
		System.out.println(r.getUsersIndexedByID().size());
		System.out.println(r.getUsersIndexedByState().size());

	}

	
	/**
	 * Loads users from addresses.csv
	 * @param userFilter, a filter which can be used to filter out users that dont meet a specific criteria. 
	 * Example filters available in User.java
	 */
	public void loadUsersAndIndex(Predicate<User> userFilter) {
		try {
			InputStream is = new FileInputStream(new File(addresses));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			// load users
			List<User> persons = br.lines().skip(1).map(lineToUser).filter(userFilter)
					.collect(Collectors.toList());

			// indexes them by Id
			persons.stream().forEach(indexUsersByIdAndState);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hardcoded to only index users that have a hometown location
	 */
	public Consumer<User> indexUsersByIdAndState = (u) -> {
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



	/**
	 * Loads the location dictionary, which maps location IDs to actual lat/long coords
	 */
	public void loadLocDict() {
		try {
			InputStream is = new FileInputStream(new File(locDict));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			locationDictionary = br.lines().skip(1).map(lineToAddress)
					.collect(Collectors.toList());
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public Collection<User> getUsers() {
		return usersIndexedByID.values();
	}

	/**
	 * Assumes you have called loadUsers before running
	 * Reads the status.csv file, and initializes statusIndexedByUser, so you can easily find statuses for a user.
	 */
	public void indexStatusesByUsers() {
		try {
			InputStream is = new FileInputStream(new File(statuses));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			br.lines().skip(1).forEach((line) -> updateUserWithStatus(line));
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Finds the statuses for one user, without loading the whole status index. This is slow because it reads
	 * through the whole status file 
	 * @param userId
	 * @return
	 */
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

	public void updateUserWithStatus(String line) {
		String[] sects = line.split(",");
		String userId = sects[0].replaceAll("\"", "");
		User u = usersIndexedByID.get(userId);

		// If the user exists, we want to give it statuses
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
