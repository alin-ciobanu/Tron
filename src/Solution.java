
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Solution {

	private static final int INF = Integer.MAX_VALUE;
	private static final int DEPTH = 6;

	
	/**
	 * 
	 * @param board
	 *            tabla curenta
	 * @param play_as
	 *            jucatorul curent
	 * @return INF in cazul in care jucatorul curent castiga, -INF daca pierde,
	 *         altfel intoarce (-1) * (distanta de la un jucator la altul). Cu alte
	 *         cuvinte, cu cat un jucator este mai aproape de capul celuilalt, cu
	 *         atat mai bine pentru ca minimaxul nostru stie sa caute solutii astfel
	 *         incat sa il inchida
	 * 
	 * Trebuie jucat cu ce intoarce. In momentul de fata, daca functioneaza, va juca
	 * destul de suicidal. El trebuie sa incerce sa ajunga in fata motocicletei inamice,
	 * nu sa se izbeasca in ea. (Raspuns pentru Alin de la Alin: Nu-i chiar suicidal.)
	 * 
	 */
	private int evaluate(Board board, PLAYER play_as) {

		WINNER winner = board.getWinner();
		Pair<Integer, Integer> pozR;
		Pair<Integer, Integer> pozG;
		pozR = board.getCurrentPositionForR();
		pozG = board.getCurrentPositionForG();
		
		if (winner != WINNER.NOBODY) {
			if (play_as == PLAYER.R) {
				switch (winner) {
				case PLAYER_R:
					return INF;

				case PLAYER_G:
					return -INF;

				case DRAW:
					return -INF + 2;
				default:
					// aici nu se intra niciodata
					return 1/(Directions.distanceBetween(pozR, pozG));

				}
			} else {
				switch (winner) {
				case PLAYER_R:
					return -INF;

				case PLAYER_G:
					return INF;

				case DRAW:
					return -INF + 2;
				default:
					// aici nu se intra niciodata
					return 1/(Directions.distanceBetween(pozR, pozG));

				}
			}
		}

		return (-1) * Directions.distanceBetween(pozR, pozG);

	}

	/**
	 * 
	 * @param alpha
	 *            - variabila alpha din AlphaBeta folosita pentru a stabili
	 *            plafonul de minim
	 * @param beta
	 *            - variabila beta din AlphaBeta pentru a stabili plafonul de
	 *            maximi
	 * @param depth
	 *            - adancimea la care am intrat in arborele de AlphaBeta
	 * @param board
	 *            - tabla de joc
	 * @param move
	 *            - miscarea pe care vrem sa o facem
	 * @param play_as
	 *            - jucatorul curent
	 * @return cea mai buna miscare pentru noi
	 * 
	 *         Max in cazul de fata nu modifica tabla de joc ci doar transmite
	 *         lui Mini miscarea pe care o va face
	 */
	private int alphaBetaMax(int alpha, int beta, int depth, Board board,
			PLAYER play_as, SingleDir nextMove) {
		
		if (depth == 0 || board.getWinner() != WINNER.NOBODY) {
			return evaluate(board, play_as);
		}

		ArrayList<DIRECTION> possibleMoves = board.getPossibleMoves(play_as);

		for (DIRECTION dir : possibleMoves) {
			int score;
			
			if (play_as == PLAYER.G)
				score = alphaBetaMini(alpha, beta, depth - 1, board, dir, PLAYER.R, new SingleDir());
			else
				score = alphaBetaMini(alpha, beta, depth - 1, board, dir, PLAYER.G, new SingleDir());
			
			if (score > alpha) {
				alpha = score;
				nextMove.dist = dir;
			}

			if (score >= beta) {
				return beta;
			}

		}

		return alpha;
	}

	/**
	 * 
	 * @param alpha
	 *            - alpha din alphaBeta
	 * @param beta
	 *            - beta din alphaBeta
	 * @param depth
	 *            - adancimea la care am ajuns in MiniMax
	 * @param board
	 *            - tabla de joc curent
	 * @param move
	 *            - miscarea pe care a facut-o jucatorul Max
	 * @param play_as
	 *            - jucatorul curent
	 * @return cea mai buna miscare pentru minimizarea pierderilor jucaturlui
	 *         mini
	 */
	private int alphaBetaMini(int alpha, int beta, int depth, Board board,
			DIRECTION move, PLAYER play_as, SingleDir nextMove) {
		
		if (depth == 0) {
			return -evaluate(board, play_as);
		}

		ArrayList<DIRECTION> possibleMoves = board.getPossibleMoves(play_as);
		/**
		 * Pentru a simula jocul real cand miscarile se fac simultan, modificam
		 * tabla de joc doar in mini cu ambele miscari.
		 * 
		 * Nu sunt foarte multumit de mecanismul asta. Prea mult ifuri.
		 */
		
		for (DIRECTION dir : possibleMoves) {

			int score;
			Board b1 = new Board(board);

			if (play_as == PLAYER.R) {
				b1.makeMove(dir, PLAYER.R);
				b1.makeMove(move, PLAYER.G);
				score = alphaBetaMax(alpha, beta, depth - 1, b1, PLAYER.G, new SingleDir());
				b1.undoMove(dir, PLAYER.R);
				b1.undoMove(move, PLAYER.G);
			} else {
				b1.makeMove(move, PLAYER.R);
				b1.makeMove(dir, PLAYER.G);
				score = alphaBetaMax(alpha, beta, depth - 1, b1, PLAYER.R, new SingleDir());
				b1.undoMove(move, PLAYER.R);
				b1.undoMove(dir, PLAYER.G);
			}

			if (score < beta) {
				beta = score;
				nextMove.dist = dir;
			}
			
			if (score <= alpha) {
				return alpha;
			}
			
		}

		return beta;
	}

	/**
	 * 
	 * @param board Tabla curent
	 * @param play_as Jucatorul curent
	 * @return urmatoarea mutare
	 */
	public DIRECTION getMove(Board board, PLAYER play_as){
		
		SingleDir nextMove = new SingleDir();

		alphaBetaMax(-INF, INF, DEPTH, board, play_as, nextMove);
		
		return nextMove.dist;
	}
	
	/**
	 * 
	 * @param in - stdin de fiecare data
	 * @return Pair cu Board-ul si cu player-ul pe care il controleaza bot-ul
	 */
	public Pair<Board, PLAYER> read (Scanner in) {
		
		Pair<Board, PLAYER> p = new Pair<Board, PLAYER>();
		Board b;

		if ((in.nextLine()).charAt(0) == 'r')
			p.setSecond(PLAYER.R);
		else
			p.setSecond(PLAYER.G);
		
		int fstR, sndR, fstG, sndG;

		String line = "";
		line = in.nextLine();
		StringTokenizer st = new StringTokenizer(line, " ");

		fstR = Integer.parseInt(st.nextToken());
		sndR = Integer.parseInt(st.nextToken());

		fstG = Integer.parseInt(st.nextToken());
		sndG = Integer.parseInt(st.nextToken());

		int lines, cols;
		line = "";
		line = in.nextLine();
		st = new StringTokenizer(line, " ");
		lines = Integer.parseInt(st.nextToken());
		cols = Integer.parseInt(st.nextToken());
		
		b = new Board(lines, cols);
		b.setCurrentPositionForG(fstG, sndG);
		b.setCurrentPositionForR(fstR, sndR);

		String[] map = new String[lines];
		for (int i = 0; i < lines; i++) {
			map[i] = in.nextLine();
		}
		
		b.updateMap(map, lines);

		p.setFirst(b);

		return p;
		
	}
	
	/**
	 * 
	 * @param dir - directia
	 * Scrie la stdout directia dir.
	 */
	public void write (DIRECTION dir) {
		
		System.out.println(dir);
		
	}
	
	public static void main (String[] args) {
		
		Solution bot = new Solution();
		Scanner in = new Scanner(System.in);
		Pair<Board, PLAYER> p = bot.read(in);
		Board b = p.getFirst();
		PLAYER play_as = p.getSecond();
		DIRECTION d;
		
		while (b.getWinner() == WINNER.NOBODY) {
			
			d = bot.getMove(b, play_as);
			bot.write(d);

			p = bot.read(in);
			b = p.getFirst();
			play_as = p.getSecond();
			
		}
		
	}
	
}

class SingleDir {
	DIRECTION dist;
	
}