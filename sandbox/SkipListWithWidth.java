package sandbox;
import java.io.*;
import java.util.*;

public class SkipListWithWidth<T extends Comparable<T>> {
	static class Item<T> {
		Item<T>[] links;
		int[] sizes;
		T value;

		@SuppressWarnings("unchecked")
		public Item(T value, int height) {
			this.links = new Item[height];
			this.sizes = new int[height];
			this.value = value;
		}
	}
	
	int maxHeight = 1;
	Item<T> head = new Item<T>(null, 50);
	Random r = new Random(566);
	
	private Item<T>[] profile(T element) {
		@SuppressWarnings("unchecked")
		Item<T>[] result = new Item[maxHeight];
		Item<T> current = head;
		for (int i = maxHeight - 1; i >= 0; i--) {
			while (current.links[i] != null && current.links[i].value.compareTo(element) < 0) {
				current = current.links[i];
			}
			result[i] = current;
		}
		return result;
	}
	
	public boolean contains(T element) {
		Item<T>[] profile = profile(element);
		return profile[0].links[0] != null && element.compareTo(profile[0].links[0].value) == 0;
	}
	
	public T prev(T element) {
		Item<T>[] profile = profile(element);
		return profile[0] == head ? null : profile[0].value;
	}
	
	public T next(T element) {
		Item<T>[] profile = profile(element);
		Item<T> next = profile[0].links[0];
		if (next != null && next.value.compareTo(element) == 0) {
			next = next.links[0];
		}
		return next == null ? null : next.value;
	}
	
	public T getKth(int k) {
		Item<T> current = head;
		for (int i = maxHeight - 1; i >= 0; i--) {
			while (current.sizes[i] <= k) {
				k -= current.sizes[i];
				current = current.links[i];
				if (current == null) {
					return null;
				}
			}
		}
		return current.value;
	}
	
	private void recalc(Item<T> item, int i) {
		if (i == 0) {
			item.sizes[i] = 1;
			return;
		}
		Item<T> current = item;
		item.sizes[i] = 0;
		while (current != item.links[i]) {
			item.sizes[i] += current.sizes[i - 1];
			current = current.links[i - 1];
		}
	}

	public void add(T element) {
		int height = 1 + Integer.numberOfTrailingZeros(r.nextInt());
		maxHeight = Math.max(maxHeight, height);
		Item<T> addedItem = new Item<T>(element, height);
		Item<T>[] profile = profile(element);
		for (int i = 0; i < height; i++) {
			addedItem.links[i] = profile[i].links[i];
			profile[i].links[i] = addedItem;
			recalc(profile[i], i);
			recalc(addedItem, i);
		}
	}
	
	public void remove(T element) {
		Item<T>[] profile = profile(element);
		int height = profile[0].links[0].links.length;
		for (int i = 0; i < height; i++) {
			profile[i].links[i] = profile[i].links[i].links[i];
			recalc(profile[i], i);
		}
	}
	
	public static void main(String[] args) throws IOException {
		SkipListWithWidth<Integer> skipList = new SkipListWithWidth<Integer>();
		BufferedReader in = new BufferedReader(new FileReader("bst1.in"));
		PrintWriter out = new PrintWriter(new File("bst1.out"));
		while (true) {
			String s = in.readLine();
			if (s == null) {
				break;
			}
			StringTokenizer st = new StringTokenizer(s);
			s = st.nextToken();
			int x = Integer.parseInt(st.nextToken());
			if (s.equals("insert") && !skipList.contains(x)) {
				skipList.add(x);
			}
			if (s.equals("delete") && skipList.contains(x)) {
				skipList.remove(x);
			}
			if (s.equals("exists")) {
				out.println(skipList.contains(x));
			}
			if (s.equals("next")) {
				Integer v = skipList.next(x);
				out.println(v == null ? "none" : v);
			}
			if (s.equals("prev")) {
				Integer v = skipList.prev(x);
				out.println(v == null ? "none" : v);
			}
			if (s.equals("kth")) {
				Integer v = skipList.getKth(x);
				out.println(v == null ? "none" : v);
			}
		}
		in.close();
		out.close();
	}
}
