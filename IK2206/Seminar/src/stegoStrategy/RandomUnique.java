package stegoStrategy;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class RandomUnique {
	private Random rand;
	private int range;
	private Set<Integer> alreadyGenerated;
	
	public RandomUnique(int seed, int range) {
		rand = new Random(seed);
		this.range = range;
		alreadyGenerated = new TreeSet<Integer>();
	}
	
	public int nextInt() {
		int next = rand.nextInt(range);
		if(alreadyGenerated.add(next)) {
			return next;
		}
		return nextInt();
	}
	
	public Set<Integer> nextInt(int size) {
		while(alreadyGenerated.size() < size) {
			alreadyGenerated.add(rand.nextInt(range));
		}
		return alreadyGenerated;
	}
}
