package max.nlp.wrappers.mypersonality.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import max.nlp.util.basic.MapSorter;

public class CoordSearch {
	private static List<Location> dict;

	private static String root = "/home/jmaxk/resources/mypersonality/";

	private static String baldwinCities = root + "city_collapsed_all";
	static {
		loadBaldwinCities();
	}

	public static Location findClosestLocatoin(Double lat1, Double lon1) {
		Map<Location, Double> dists = new HashMap<Location, Double>();
		for (Location l : dict) {
			Double lat2 = l.getLatitutde();
			Double lon2 = l.getLongitude();
			dists.put(l, distance(lat1, lat2, lon1, lon2));
		}
		MapSorter<Location, Double> sorter = new MapSorter<Location, Double>();
		List<Entry<Location, Double>> results = sorter.sortMap(dists);
		Iterator<Entry<Location, Double>> itr = results.iterator();
		return itr.next().getKey();
	}

	private static double distance(double lat1, double lat2, double lon1,
			double lon2) {

		final int R = 6371; // Radius of the earth

		Double latDistance = deg2rad(lat2 - lat1);
		Double lonDistance = deg2rad(lon2 - lon1);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters

		distance = Math.pow(distance, 2);
		return Math.sqrt(distance);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static void loadBaldwinCities() {
		try {
			InputStream is = new FileInputStream(new File(baldwinCities));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			dict = br.lines().parallel().skip(1).map(mapToBaldwinAddress)
					.filter(isNotNull).collect(Collectors.toList());
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static Predicate<Location> isNotNull = (user) -> user != null;

	public static Function<String, Location> mapToBaldwinAddress = (line) -> {
		// austin-tx453-us america/chicago 30.26715 -97.74306 837326
		String[] section = line.split("\t");
		String[] superCity = section[0].split("-");
		if (superCity.length > 2 && superCity[2].equals("us")) {
			String city = superCity[0];
			String state = superCity[1].replaceAll("[^A-Za-z]*", "")
					.toUpperCase();
			Double latitude = Double.parseDouble(section[2]);
			Double longitude = Double.parseDouble(section[3]);
			Location loc = new Location(latitude, longitude, city, state);
			return loc;
		} else {
			return null;
		}
	};
}
