import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


public class Directions {

	/*
	 * A NU SE CONFUNDA CU ENUMERAREA DIRECTION!
	 * Clasa pentru scrierea metodelor statice
	 */
	
	private static int INF = Integer.MAX_VALUE;
	
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
	
	/**
	 * 
	 * @param b -- board-ul
	 * @param play_as -- jucatorul pentru care se face BFS
	 * 
	 * @return -- distantele de la pozitia curenta a jucatorului
	 * la fiecare din toate celelalte puncte de pe board 
	 * (INF pentru punctele la care nu poate ajunge)
	 * 
	 * Nu-s foarte mandru pentru modul cum arata metoda asta, dar face ce trebuie.
	 * 
	 */
	public static int[][] bfs (Board b, PLAYER play_as) {
		
		int[][] distance = new int[b.lines][b.cols];
		Color[][] nodeColor = new Color[b.lines][b.cols];
		// Cool Color class in Java is cool. :)

		int currentPosL = -1, currentPosC = -1; // pozitia curenta a PLAYER-ului
		Pair<Integer, Integer> pos = new Pair<Integer, Integer>();

		if (play_as == PLAYER.G) {
			pos = b.getCurrentPositionForG();
		}
		else {
			pos = b.getCurrentPositionForR();
		}
		currentPosL = pos.getFirst();
		currentPosC = pos.getSecond();

		for (int i = 0; i < b.lines; i++) {
			for (int j = 0; j < b.cols; j++) {
				// Initializare distanta si culoare pentru fiecare nod
				distance[i][j] = INF;
				nodeColor[i][j] = Color.WHITE;
			}
		}
		
		distance[currentPosL][currentPosC] = 0; // distanta pana la sursa
		nodeColor[currentPosL][currentPosC] = Color.BLACK; // marcat
		
		LinkedList<Pair<Integer, Integer>> queue = new LinkedList<Pair<Integer,Integer>>();
		// folosim LinkedList pentru ca toate Queue-urile din Java au O(N) complexitate pentru size()
		queue.add(new Pair<Integer, Integer>(currentPosL, currentPosC));
		
		while (!queue.isEmpty()) {
			
			Pair<Integer, Integer> u = queue.removeFirst();
			int ui = -1, uj = -1; // coordonatele punctului u
			ui = u.getFirst();
			uj = u.getSecond();
			
			ArrayList<Pair<Integer, Integer>> neighbours = new ArrayList<Pair<Integer,Integer>>();
			// vecinii nodului u
			
			if (b.board[ui + 1][uj] == MARK.SPACE) {
				neighbours.add(new Pair<Integer, Integer>(ui + 1, uj));
			}
			if (b.board[ui - 1][uj] == MARK.SPACE) {
				neighbours.add(new Pair<Integer, Integer>(ui - 1, uj));
			}
			if (b.board[ui][uj + 1] == MARK.SPACE) {
				neighbours.add(new Pair<Integer, Integer>(ui, uj + 1));
			}
			if (b.board[ui][uj - 1] == MARK.SPACE) {
				neighbours.add(new Pair<Integer, Integer>(ui, uj - 1));
			}
			
			for (Pair<Integer, Integer> v : neighbours) {
				
				int vi = -1, vj = -1; // coordonatele lui v
				vi = v.getFirst();
				vj = v.getSecond();
				
				if (nodeColor[vi][vj] == Color.WHITE) {
					
					distance[vi][vj] = distance[ui][uj] + 1;
					nodeColor[vi][vj] = Color.BLACK;
					queue.add(new Pair<Integer, Integer>(vi, vj));

				}
				
			}
			
			nodeColor[ui][uj] = Color.BLACK;

		}
		
		return distance;

	}

	
	public static void main (String[] args) {
		
		/**
		 * 
		 * Testare BFS
		 */
		
		Board b = new Board(15, 15);
		Scanner in = new Scanner(System.in);
		
		in.useDelimiter("\n");
		String[] map = new String[15];
		for (int i = 0; i < 15; i++) {
			map[i] = in.next();
		}
		
		b.updateMap(map, 15);
		b.setCurrentPositionForR(7, 1);

		int[][] dist = new int[15][15];
		
		dist = bfs(b, PLAYER.R);
		
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(dist[i][j] + " ");
			}
			System.out.println();
		}
		
		/**
		 * Se pare ca merge. :)
		 */
		
	}
	
	
}
