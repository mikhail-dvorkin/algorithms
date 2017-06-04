import java.io.*;
import java.util.*;

public class Huffman implements Runnable {
	int[] dicFreq = {90398, 20520, 46843, 37206, 124572, 13177, 28636, 25511, 107357, 1652, 7911, 64057, 32782, 77470, 75120, 32737, 1886, 80339, 82886, 77017, 36878, 11368, 7306, 3239, 21064, 14587};
	
	class Letter implements Comparable<Letter> {
		Letter left, right;
		int c;
		int freq;
		
		public Letter(int c, int freq) {
			this.c = c;
			this.freq = freq;
		}
		
		public Letter(Letter l, Letter r, int id) {
			left = l;
			right = r;
			c = id;
			freq = left.freq + right.freq;
		}

		@Override
		public int compareTo(Letter o) {
			if (freq == o.freq)
				return c - o.c;
			return freq - o.freq;
		}

		public void assign(String[] code, String s) {
			if (left == null) {
				code[c] = s;
			} else {
				left.assign(code, s + "0");
				right.assign(code, s + "1");
			}
		}
	}
	
	private Scanner in;
	private PrintWriter out;

	@Override
	public void run() {
		try {
			solve();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public String[] huffman(int[] freq) {
		int n = freq.length;
		int nn = n;
		TreeSet<Letter> alphabet = new TreeSet<Letter>();
		for (int i = 0; i < n; i++) {
			alphabet.add(new Letter(i, freq[i]));
		}
		while (alphabet.size() > 1) {
			Letter a = alphabet.first();
			alphabet.remove(a);
			Letter b = alphabet.first();
			alphabet.remove(b);
			alphabet.add(new Letter(a, b, nn++));
		}
		String[] code = new String[n];
		alphabet.first().assign(code, "");
		return code;
	}
	
	private void solve() throws IOException {
		Locale.setDefault(Locale.US);
		in = new Scanner(new File("text.txt"));
		out = new PrintWriter("huffman.out");
		String[] c = huffman(dicFreq);
		for (int i = 0; i < dicFreq.length; i++)
			System.out.println((char) ('a' + i) + " " + c[i]);
		in.close();
		out.close();
	}
	
	public static String applyHuffman(String s, String[] code) {
		StringBuilder bin = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			bin.append(code[s.charAt(i) - 'a']);
		}
		return bin.toString();
	}
	
	public static String decodeHuffman(String s, String[] code) {
		Map<String, Character> tm = new TreeMap<String, Character>();
		for (int i = 0; i < code.length; i++)
			tm.put(code[i], (char) ('a' + i));
		StringBuilder sb = new StringBuilder();
		iloop:
		for (int i = 0; i < s.length();) {
			int j = i;
			while (!tm.containsKey(s.substring(i, j))) {
				j++;
				if (j > s.length())
					break iloop;
			}
			sb.append(tm.get(s.substring(i, j)));
			i = j;
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		new Thread(new Huffman()).start();
	}
}
