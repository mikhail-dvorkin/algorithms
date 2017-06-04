import java.util.*;
// TODO modCount

public class SegmentsTree extends AbstractList<Integer> implements RandomAccess {
	private int n;
	private int cap; // n <= cap
	private int[] min;
	private int[] max;
	private int[] sum;
	private final static int MIN_NEUTRAL = Integer.MAX_VALUE;
	private final static int MAX_NEUTRAL = Integer.MIN_VALUE;
	private final static int SUM_NEUTRAL = 0;
	
	private static int bestCapacity(int n) {
		int c = n;
		while ((c & (c - 1)) != 0) {
			c &= c - 1;
		}
		if (n > c)
			c <<= 1;
		return c;
	}
	
	public void ensureCapacity(int minCapacity) {
		minCapacity = bestCapacity(minCapacity);
		if (minCapacity > cap) {
			int[] t = new int[2 * minCapacity];
			System.arraycopy(min, cap, t, minCapacity, n);
			min = t;
			max = min.clone();
			sum = min.clone();
			cap = minCapacity;
			adjustTree(cap, cap + n - 1);
		}
	}
	
	public SegmentsTree() {
		min = new int[0];
		max = new int[0];
		sum = new int[0];
	}

	public SegmentsTree(int n) {
		this();
        if (n < 0)
            throw new IllegalArgumentException("Illegal initial size: " + n);
        ensureCapacity(n);
        this.n = n;
	}
	
	public SegmentsTree(Collection<? extends Integer> c) {
		this();
		addAll(c);
	}
	
	public SegmentsTree(int[] a) {
		this(a, 0, a.length);
	}
	
	public SegmentsTree(int[] a, int fromIndex, int toIndex) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > a.length)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
        int m = toIndex - fromIndex;
        cap = bestCapacity(m);
        min = new int[cap];
        System.arraycopy(a, fromIndex, min, cap, m);
        max = min.clone();
        sum = min.clone();
		adjustTree(cap, cap + m - 1);
	}
	
	@Override
	public int size() {
		return n;
	}
	
	@Override
	public boolean isEmpty() {
		return n == 0;
	}
	
	@Override
	public void clear() {
		n = 0;
	}

	private void adjustTree(int left, int right) {
		left /= 2;
		right /= 2;
		while (left <= right && left > 0) {
			for (int i = left; i <= right; i++) {
				min[i] = min[2 * i];
				max[i] = max[2 * i];
				sum[i] = sum[2 * i];
				if (2 * i + 1 < cap + n) {
					min[i] = Math.min(min[i], min[2 * i + 1]);
					max[i] = Math.max(max[i], max[2 * i + 1]);
					sum[i] += sum[2 * i + 1];
				}
			}
			left /= 2;
			right /= 2;
		}
	}

	private void rangeCheck(int index) {
		if (index >= n || index < 0)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + n);
	}

    @Override
	public Integer get(int index) {
    	rangeCheck(index);
		return min[cap + index];
	}
    
	public int getInt(int index) {
    	rangeCheck(index);
		return min[cap + index];
	}
	
	public int getMin(int from, int to) {
		int left = cap + from;
		int right = cap + to - 1;
		int res = MIN_NEUTRAL;
		while (left <= right) {
			if (left % 2 == 1) {
				res = Math.min(res, min[left]);
				left++;
			}
			left /= 2;
			if (right % 2 == 0) {
				res = Math.min(res, min[right]);
				right--;
			}
			right /= 2;
		}
		return res;
	}
	
	public int getMax(int from, int to) {
		int left = cap + from;
		int right = cap + to - 1;
		int res = MAX_NEUTRAL;
		while (left <= right) {
			if (left % 2 == 1) {
				res = Math.max(res, max[left]);
				left++;
			}
			left /= 2;
			if (right % 2 == 0) {
				res = Math.max(res, max[right]);
				right--;
			}
			right /= 2;
		}
		return res;
	}
	
	public int getSum(int from, int to) {
		int left = cap + from;
		int right = cap + to - 1;
		int res = SUM_NEUTRAL;
		while (left <= right) {
			if (left % 2 == 1) {
				res += sum[left];
				left++;
			}
			left /= 2;
			if (right % 2 == 0) {
				res += sum[right];
				right--;
			}
			right /= 2;
		}
		return res;
	}
	
	public int getMin() {
		return (n > 0) ? min[1] : MIN_NEUTRAL;
	}
    
	public int getMax() {
		return (n > 0) ? max[1] : MAX_NEUTRAL;
	}
    
	public int getSum() {
		return (n > 0) ? sum[1] : SUM_NEUTRAL;
	}
    
	@Override
    public Integer set(int index, Integer element) {
    	if (element == null)
    		throw new NullPointerException("SegmentsTree cannot contain null");
    	return set(index, (int) element);
    }

    public Integer set(int index, int element) {
    	int oldValue = get(index);
    	index += cap;
    	min[index] = max[index] = sum[index] = element;
    	adjustTree(index, index);
    	return oldValue;
    }

	@Override
	public void add(int index, Integer element) {
    	if (element == null)
    		throw new NullPointerException("SegmentsTree cannot contain null");
    	add(index, (int) element);
	}
	
	public void add(int index, int element) {
    	if (index < 0 || index > n)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + n);
		ensureCapacity(n + 1);
		index += cap;
		for (int i = cap + n; i > index; i--) {
			min[i] = max[i] = sum[i] = min[i - 1];
		}
		n++;
		min[index] = max[index] = sum[index] = element;
		adjustTree(index, cap + n - 1);
	}
	
	public boolean add(int element) {
		add(n, element);
		return true;
	}
	
	@Override
	public Integer remove(int index) {
		int value = get(index);
		removeRange(index, index + 1);
		return value;
	}
	
	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index == -1)
			return false;
		removeRange(index, index + 1);
		return true;
	}
    
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > n)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
        int rem = toIndex - fromIndex;
        int tail = n - toIndex;
        int[] t = Arrays.copyOfRange(min, cap + toIndex, cap + n);
        System.arraycopy(t, 0, min, cap + fromIndex, tail);
        System.arraycopy(t, 0, max, cap + fromIndex, tail);
        System.arraycopy(t, 0, sum, cap + fromIndex, tail);
        n -= rem;
        adjustTree(cap + fromIndex, cap + n - 1);
    }
	
    @Override
	public boolean contains(Object o) {
    	return indexOf(o) >= 0;
    }
    
    public boolean contains(int v) {
    	return indexOf(v) >= 0;
    }
    
    @Override
	public int indexOf(Object o) {
		if (o == null || !(o instanceof Integer))
			return -1;
		return indexOf((int) ((Integer) o));
	}
	
	public int indexOf(int v) {
		for (int i = cap; i < cap + n; i++)
			if (min[i] == v)
				return i - cap;
		return -1;
	}
	
	@Override
	public int lastIndexOf(Object o) {
		if (o == null || !(o instanceof Integer))
			return -1;
		return lastIndexOf((int) ((Integer) o));
	}
	
	public int lastIndexOf(int v) {
		for (int i = cap + n - 1; i >= cap; i--)
			if (min[i] == v)
				return i - cap;
		return -1;
	}
	
	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		return addAll(n, c);
//		will cause problems if <b>c</b> is changed concurrently 
//		if (c.isEmpty())
//			return false;
//		if (c.contains(null))
//			throw new NullPointerException("SegmentsTree cannot contain null");
//		ensureCapacity(n + c.size());
//		int nbeg = n;
//		Iterator<? extends Integer> e = c.iterator();
//		while (e.hasNext()) {
//			min[n] = max[n] = sum[n] = e.next();
//			n++;
//		}
//		adjustTree(cap + nbeg, cap + n - 1);
//		return true;
	}
	
    /**
     * Does cause problems if <b>c</b> is changed concurrently
     */
	@Override
	public boolean addAll(int index, Collection<? extends Integer> c) {
    	if (c.isEmpty())
    		return false;
		if (c.contains(null))
			throw new NullPointerException("SegmentsTree cannot contain null");
		int cs = c.size();
		ensureCapacity(n + cs);
		int[] t = new int[cs + n - index];
		Iterator<? extends Integer> e = c.iterator();
		int m = 0;
		while (e.hasNext()) {
			t[m++] = e.next();
		}
		System.arraycopy(min, cap + index, t, cs, n - index);
		System.arraycopy(t, 0, min, cap + index, t.length);
		System.arraycopy(t, 0, max, cap + index, t.length);
		System.arraycopy(t, 0, sum, cap + index, t.length);
		n += cs;
		adjustTree(cap + index, cap + n - 1);
    	return true;
    }
	
	
	public void trimToSize() {
		// TODO
	}
	
	@Override
	protected Object clone() {
		SegmentsTree st = new SegmentsTree(this);
		return st;
	}
	
	@Override
	public Object[] toArray() {
		Integer[] a = new Integer[n];
		for (int i = cap; i < cap + n; i++)
			a[i - cap] = min[i];
		return a;
	}
	
	public int[] toIntArray() {
		return Arrays.copyOfRange(min, cap, cap + n);
	}
	
    @Override
	public <T> T[] toArray(@SuppressWarnings("unused") T[] a) {
    	// TODO indecided
    	return null;
    }
    
    public static void main(String[] args) {
    	SegmentsTree st = new SegmentsTree();
    	int[] a = st.toIntArray();
    	System.out.println(a.length);
    	for (int i = 1; i <= 1000000; i++) {
    		st.add(i);
    	}
		System.out.println(st.getMin() + " " + st.getMax() + " " + st.getSum());
	}
}
