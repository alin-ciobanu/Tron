package classes;

public class Directions {

	/*
	 * A NU SE CONFUNDA CU ENUMERAREA DIRECTION!
	 * 
	 * Contine o metoda care intoarce directia opusa. Utila in anume situatii.
	 */
	
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
