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

	/**
	 * Functie pentru calculul distantei dintre doua puncte
	 * @param p1 - Prima pereche de pozitii
	 * @param p2 - A doua pereche de pozitii
	 * @return - Distanta
	 */
	public static int distanceTo(Pair<Integer,Integer> p1, Pair<Integer,Integer> p2){
		/**
		 * Variabila folosita pentru a nu avea un carnat de expresii
		 */
		double x = Math.pow((p1.first - p2.first),2) + Math.pow((p1.second - p2.second),2);
		
		return (int )Math.sqrt(x);
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

