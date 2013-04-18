
public class Directions {

	/*
	 * A NU SE CONFUNDA CU ENUMERAREA DIRECTION!
	 * Clasa pentru 
	 */
	
	/**
	 * Functie pentru calculul distantei dintre doua puncte
	 * @param p1 - Prima pereche de pozitii
	 * @param p2 - A doua pereche de pozitii
	 * @return - Distanta
	 */
	public static int distanceBetween(Pair<Integer,Integer> p1, Pair<Integer,Integer> p2){
		/**
		 * Variabila folosita pentru a nu avea un carnat de expresii
		 */

		return Math.abs ((p1.getFirst() - p2.getFirst()) + (p1.getSecond() - p2.getSecond()));

		}
	
	/**
	 * 
	 * @param dir - o directie (UP, DOWN, LEFT, RIGHT)
	 * @return - directia opusa
	 */
	public static DIRECTION complementDirection (DIRECTION dir) {

		switch (dir) {

		case UP:
			return DIRECTION.DOWN;
		case DOWN:
			return DIRECTION.UP;
		case RIGHT:
			return DIRECTION.LEFT;
		case LEFT:
			return DIRECTION.RIGHT;
		default:
			System.out.println("Directie gresita!\n");
			// nu prea ai cum sa ajungi pe ramura asta
			return null;
		
		}
		
	}

}
