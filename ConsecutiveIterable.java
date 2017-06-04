import java.util.*;

public class ConsecutiveIterable<E> implements Iterable<E> {
	Iterator<Iterable<E>> i;
	
	public ConsecutiveIterable(Iterable<Iterable<E>> iterables) {
		i = iterables.iterator();
	}
	
	@SuppressWarnings("unchecked")
	public ConsecutiveIterable(Iterable<E>... iterables) {
		this(Arrays.asList(iterables));
	}

	@Override
	public Iterator<E> iterator() {
		return new ConsecutiveIterator<E>(i);
	}
}
