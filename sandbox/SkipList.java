package sandbox;
import java.util.*;

public class SkipList<T extends Comparable<T>> {
	static class Item<T> {
		Item<T>[] links;
		T value;

		@SuppressWarnings("unchecked")
		public Item(T value, int height) {
			this.links = new Item[height];
			this.value = value;
		}
	}
	
	Item<T> head = new Item<T>(null, 32);
	Random r = new Random(566);
	
	private Item<T>[] profile(T element) {
		@SuppressWarnings("unchecked")
		Item<T>[] result = new Item[head.links.length];
		Item<T> current = head;
		for (int i = result.length - 1; i >= 0; i--) {
			while (current.links[i] != null && current.links[i].value.compareTo(element) < 0) {
				current = current.links[i];
			}
			result[i] = current;
		}
		return result;
	}
	
	public boolean contains(T element) {
		Item<T>[] profile = profile(element);
		return profile[0].links[0] != null && profile[0].links[0].value.compareTo(element) == 0;
	}
	
	public void add(T element) {
		int height = 1 + Integer.numberOfTrailingZeros(r.nextInt());
		Item<T> addedItem = new Item<T>(element, height);
		Item<T>[] profile = profile(element);
		for (int i = addedItem.links.length - 1; i >= 0; i--) {
			addedItem.links[i] = profile[i].links[i];
			profile[i].links[i] = addedItem;
		}
	}
	
	public void remove(T element) {
		Item<T>[] profile = profile(element);
		for (int i = profile[0].links[0].links.length - 1; i >= 0; i--) {
			profile[i].links[i] = profile[i].links[i].links[i];
		}
	}
}
