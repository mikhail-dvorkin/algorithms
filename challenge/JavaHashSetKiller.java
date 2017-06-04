package challenge;

import java.io.*;
import java.util.*;

public class JavaHashSetKiller {
	static int n = 1 << 18; // length of table in hashset
	static int needed = 100000;
	static int min = 0;
	static int max = 1000000000;
	
	public static void main(String[] args) throws IOException {
		PrintWriter out = new PrintWriter(JavaHashSetKiller.class.getSimpleName() + ".dat");
		List<Integer> list = new ArrayList<Integer>();
		main:
		for (int h = 0;; h++) {
			for (int i = min; i <= max; i++) {
				if (indexFor(i) == h) {
					out.println(i);
					list.add(i);
					if (list.size() == needed) {
						break main;
					}
				}
			}
			System.out.println(list.size() + " elements ready");
		}
		out.close();
		System.out.println("Prepared: " + list.size() + " elements");
		{
			long t = -System.currentTimeMillis();
			Set<Integer> set = new TreeSet<Integer>(list);
			t += System.currentTimeMillis();
			System.out.println("TreeSet: " + t + "ms for " + set.size());
		}
		{
			long t = -System.currentTimeMillis();
			Set<Integer> set = new HashSet<Integer>(list);
			t += System.currentTimeMillis();
			System.out.println("HashSet: " + t + "ms for " + set.size());
		}
	}

    static int indexFor(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        h ^= (h >>> 7) ^ (h >>> 4);
        return h & (n - 1);
    }
}
