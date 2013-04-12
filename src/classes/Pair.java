package classes;


public class Pair<T, U> {

	private T first;
	private U second;

	public Pair(T first, U second) {
		this.first = first;
		this.second = second;
	}

	public Pair () {
		// Nothing here. We just need a constructor with no params.
	}

	public boolean equals (Pair<T, U> anotherPair) {
		
		if (this.first.equals(anotherPair.first) && this.second.equals(anotherPair.second)) {
			return true;
		}
		
		return false;
		
	}
	
	T getFirst () {
		return first;
	}
	
	U getSecond () {
		return second;
	}

	void setFirst (T first) {
		this.first = first;
	}

	void setSecond (U second) {
		this.second = second;
	}

	void setBoth (T first, U second) {
		this.first = first;
		this.second = second;
	}

	public String toString () {
		return "[" + first + ", " + second + "]";
	}

	public static void main(String[] args) {
		
		Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(5, 6);
		Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(5, 70);
		
		System.out.println(p1.equals(p2));
		
	}
	
}

