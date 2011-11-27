package stegoStrategy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomUnique {
	private Random rand;
	private int range;
	private Set<Integer> alreadyGenerated;
	
	public RandomUnique(int seed, int range) {
		rand = new Random(seed);
		this.range = range;
		alreadyGenerated = new HashSet<Integer>();
	}
	
	public int nextInt() {
		int next = rand.nextInt(range);
		if(alreadyGenerated.add(next)) {
			return next;
		}
		return nextInt();
	}
}
