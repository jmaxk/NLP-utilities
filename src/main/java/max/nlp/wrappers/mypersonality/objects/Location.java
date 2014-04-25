package max.nlp.wrappers.mypersonality.objects;

import java.util.function.Predicate;

public class Location {

	private Double latitutde = null;
	private Double longitude = null;
	private String city = null;
	private String state = null;
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getLatitutde() {
		return latitutde;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public static Location fromMyPersonality(String line){
		String[] section = line.split(",");
		String location = section[1];
		String[] subLocations = location.split(",");
		if (subLocations.length == 2){
			String city = subLocations[0];
			String state = subLocations[1];
			Double latitude = Double.parseDouble(section[2]);
			Double longitude = Double.parseDouble(section[2]);
			return new Location(latitude, longitude, city,state);
		}
		else{
			return new Location();

		}
	}
	
	@Override
	public String toString() {
		return "Location [latitutde=" + latitutde + ", longitude=" + longitude
				+ ", city=" + city + ", state=" + state + "]";
	}

	public Location(){}

	public Location(Double latitutde, Double longitude, String city,
			String state) {
		this.latitutde = latitutde;
		this.longitude = longitude;
		this.city = city;
		this.state = state;
	}
	
	public Location(String id, Double latitutde, Double longitude, String city,
			String state) {
		this.id = id;
		this.latitutde = latitutde;
		this.longitude = longitude;
		this.city = city;
		this.state = state;
	}
	
	Predicate<Location> isFromAmerica = (location) -> location != null
			&& location.getState() != null;
}
