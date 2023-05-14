package engine;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface CompareFunc<T> {
	public boolean oper(T current, T chosen);
}

interface Check<T> {
	public boolean oper(T obj);
}

interface ExtractValue<T, K> {
	public K oper(T obj);
}

public class ArrayUtils {
	public static <T> T choose(ArrayList<T> array, CompareFunc<T> func) {
		int chosenId = -1;
		T chosenObj = null;
		for (int i = 0; i < array.size(); i++)
			if (chosenId == -1 || func.oper(array.get(i), chosenObj)) {
				chosenObj = array.get(i);
				chosenId = i;
			}
		return chosenObj;
	}
	
	private static <T, K extends Comparable<K>> T getPeak(ArrayList<T> array, ExtractValue<T, K> extract, boolean isMin) {
		return ArrayUtils.choose(array, (T a, T b) -> {
			int w = extract.oper(a).compareTo(extract.oper(b));
			return isMin && w < 0 || !isMin && w > 0;
		});
	}
	
	public static <T, K extends Comparable<K>> T getMin(ArrayList<T> array, ExtractValue<T, K> extract) {
		return ArrayUtils.getPeak(array, extract, true);
	}
	
	public static <T, K extends Comparable<K>> T getMax(ArrayList<T> array, ExtractValue<T, K> extract) {
		return ArrayUtils.getPeak(array, extract, false);
	}
	
	public static <T, K> ArrayList<K> map(ArrayList<T> list, ExtractValue<T, K> extract) {
		return new ArrayList<K>(list.stream().map((T x) -> extract.oper(x)).toList());
	}
	
	public static <T> ArrayList<T> filter(ArrayList<T> list, Check<T> extract) {
		return new ArrayList<T>(list.stream().filter((T x) -> extract.oper(x)).toList());
	}
	
	public static <T> ArrayList<T> toArrayList(T[] array) {
		return new ArrayList<>(Arrays.asList(array));
	}
}
