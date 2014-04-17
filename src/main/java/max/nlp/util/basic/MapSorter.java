package max.nlp.util.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class MapSorter<K, V extends Comparable> {

	public List<Map.Entry<K, V>> sortMap(Map<K, V> counts) {
		List<Map.Entry<K, V>> entries = new ArrayList(
				counts.entrySet());
		Collections.sort(entries, new MapComparator());
		return entries;
	}

	public List<Map.Entry<K, V>> reverseSortMap(Map<K, V> counts) {
		List<Map.Entry<K, V>> entries = new ArrayList(counts.entrySet());
		Collections.sort(entries, new MapComparator());
		return entries;
	}

	private class MapComparator implements Comparator<Map.Entry<K, V>> {

		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			if (o1.getValue() == null && o2.getValue() == null)
				return 0;
			if (o1.getValue() == null)
				return -1; // Nulls last
			return -o1.getValue().compareTo(o2.getValue());
		}

		public Comparator<Entry<K, V>> reversed() {
			// TODO Auto-generated method stub
			return null;
		}

		public Comparator<Entry<K, V>> thenComparing(
				Comparator<? super Entry<K, V>> other) {
			// TODO Auto-generated method stub
			return null;
		}

		public <U> Comparator<Entry<K, V>> thenComparing(
				Function<? super Entry<K, V>, ? extends U> keyExtractor,
				Comparator<? super U> keyComparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <U extends Comparable<? super U>> Comparator<Entry<K, V>> thenComparing(
				Function<? super Entry<K, V>, ? extends U> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public Comparator<Entry<K, V>> thenComparingInt(
				ToIntFunction<? super Entry<K, V>> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public Comparator<Entry<K, V>> thenComparingLong(
				ToLongFunction<? super Entry<K, V>> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public Comparator<Entry<K, V>> thenComparingDouble(
				ToDoubleFunction<? super Entry<K, V>> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T, U> Comparator<T> comparing(
				Function<? super T, ? extends U> keyExtractor,
				Comparator<? super U> keyComparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T, U extends Comparable<? super U>> Comparator<T> comparing(
				Function<? super T, ? extends U> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingInt(
				ToIntFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingLong(
				ToLongFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingDouble(
				ToDoubleFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	
	private class ReverseMapComparator implements Comparator<Map.Entry<K, V>> {

		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			if (o1.getValue() == null && o2.getValue() == null)
				return 0;
			if (o1.getValue() == null)
				return -1; // Nulls last
			return o1.getValue().compareTo(o2.getValue());
		}

		public Comparator<Entry<K, V>> reversed() {
			// TODO Auto-generated method stub
			return null;
		}

		public Comparator<Entry<K, V>> thenComparing(
				Comparator<? super Entry<K, V>> other) {
			// TODO Auto-generated method stub
			return null;
		}

		public <U> Comparator<Entry<K, V>> thenComparing(
				Function<? super Entry<K, V>, ? extends U> keyExtractor,
				Comparator<? super U> keyComparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <U extends Comparable<? super U>> Comparator<Entry<K, V>> thenComparing(
				Function<? super Entry<K, V>, ? extends U> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public Comparator<Entry<K, V>> thenComparingInt(
				ToIntFunction<? super Entry<K, V>> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public Comparator<Entry<K, V>> thenComparingLong(
				ToLongFunction<? super Entry<K, V>> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public Comparator<Entry<K, V>> thenComparingDouble(
				ToDoubleFunction<? super Entry<K, V>> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		public <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T, U> Comparator<T> comparing(
				Function<? super T, ? extends U> keyExtractor,
				Comparator<? super U> keyComparator) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T, U extends Comparable<? super U>> Comparator<T> comparing(
				Function<? super T, ? extends U> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingInt(
				ToIntFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingLong(
				ToLongFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Comparator<T> comparingDouble(
				ToDoubleFunction<? super T> keyExtractor) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
