package max.nlp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("rawtypes")
public class MapSorter<K,V extends Comparable>{

	@SuppressWarnings("unchecked")

	public List<Map.Entry<K, V>> sortMap(Map<K, V> counts) {
		List<Map.Entry<K, V>> entries = new ArrayList(
				counts.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			
			public int compare(Entry<K, V> o1,
					Entry<K, V> o2) {
				if (o1.getValue() == null && o2.getValue() == null)
					return 0;
				if (o1.getValue() == null)
					return -1; // Nulls last
				return -o1.getValue().compareTo(o2.getValue());
			}
		});
		return entries;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<Map.Entry<K, V>> reverseSortMap(Map<K, V> counts) {
		List<Map.Entry<K, V>> entries = new ArrayList(
				counts.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			
			public int compare(Entry<K, V> o1,
					Entry<K, V> o2) {
				if (o1.getValue() == null && o2.getValue() == null)
					return 0;
				if (o1.getValue() == null)
					return -1; // Nulls last
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		return entries;
	}
}
