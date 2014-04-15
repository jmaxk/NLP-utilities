package max.nlp.util.basic;

import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class MapUtils {

	public static void printMap(Map map) {
		Iterator iterator = map.keySet().iterator();

		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String value = map.get(key).toString();
			System.out.print("Key:[ " + key + "] Value:[" + value + "]");
		}
		System.out.println();
	}

	public static void printMap(Map map, int limit) {
		Iterator iterator = map.keySet().iterator();
		int count = 0;
		while (iterator.hasNext()) {
			if (count++ >= limit)
				break;
			String key = iterator.next().toString();
			String value = map.get(key).toString();
			System.out.print("Key:[ " + key + "] Value:[" + value + "]");
		}
		System.out.println();
	}
}
