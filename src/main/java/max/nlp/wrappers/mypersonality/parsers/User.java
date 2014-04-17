package max.nlp.wrappers.mypersonality.parsers;

import java.util.function.Predicate;

public class User {
	private String id;
	private Location hometown;
	private Location currentLoc;
	public User(String id, Location hometown, Location currentLoc) {
		this.id = id;
		this.hometown = hometown;
		this.currentLoc = currentLoc;
	}
	
	public User(String id){
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Location getHomeLocation() {
		return hometown;
	}
	
	

	public void setHometown(Location hometown) {
		this.hometown = hometown;
	}

	public Location getCurrentLoc() {
		return currentLoc;
	}

	public void setCurrentLoc(Location currentLoc) {
		this.currentLoc = currentLoc;
	}
//
//
//	private List<Status> statuses = new ArrayList<Status>();
//
//	public List<Status> getStatuses() {
//		return statuses;
//	}
//
//	public void setStatuses(List<Status> statuses) {
//		this.statuses = statuses;
//	}
//	
//	public void addStatus(Status status) {
//		statuses.add(status);
//	}

	
	public static Predicate<User> hasCurrentLoc = (u) -> u.getCurrentLoc() != null;
	public static Predicate<User> hasHometownLoc = (u) -> u.getHomeLocation() != null;
	
}
