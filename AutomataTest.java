import static org.junit.Assert.*;

import org.junit.Test;

public class AutomataTest {
	@SuppressWarnings("static-method")
	@Test
	public void testREShortestIntersection() {
		Automata.alphabetString = "ab";
		String stringA = "a(ab)*a";
		String stringB = "a(a|b)*ba";
		Automata.RegualarExpression regA = Automata.RegualarExpression.stupidParse(stringA);
		Automata.RegualarExpression regB = Automata.RegualarExpression.stupidParse(stringB);
		Automata.EpsNFA autoA = regA.toEpsNFA();
		Automata.EpsNFA autoB = regB.toEpsNFA();
		Automata.EpsNFA autoAandB = autoA.multiply(autoB);
		int[] shortest = autoAandB.shortestAccepted();
		assertArrayEquals(shortest, new int[]{0, 0, 1, 0});
	}
}
