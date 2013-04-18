
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.text.PlainDocument;

public class Solution {

	private static final int INF = Integer.MAX_VALUE;
	private static final int DEPTH = 10;

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
	int evaluate(Board board, PLAYER play_as) {

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
			DIRECTION move, PLAYER play_as, SingleDir nextMove) {

		if (depth == 0 || board.getWinner() != WINNER.NOBODY) {
			return evaluate(board, play_as);
		}

		ArrayList<DIRECTION> possibleMoves = board.getPossibleMoves(play_as);
		int score;

		for (DIRECTION dir : possibleMoves) {
			score = alphaBetaMini(alpha, beta, depth - 1, board, dir, play_as, new SingleDir());
			if (score >= beta) {
				return beta;
			}

			if (score > alpha) {
				alpha = score;
				nextMove.dist = dir;
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
		int score;
		/**
		 * Pentru a simula jocul real cand miscarile se fac simultan, modificam
		 * tabla de joc doar in mini cu ambele miscari.
		 * 
		 * Nu sunt foarte multumit de mecanismul asta. Prea mult ifuri.
		 */
		
		/*
		 * TODO in functia asta: lucreaza pe un board copiat, nu direct pe ce primesti in param
		 */
		
		for (DIRECTION dir : possibleMoves) {
			if (play_as.equals(PLAYER.R)) {
				board.makeMove(move, PLAYER.R);
				board.makeMove(dir, PLAYER.G);
			} else {
				board.makeMove(dir, PLAYER.R);
				board.makeMove(move, PLAYER.G);
			}
			score = alphaBetaMax(alpha, beta, depth - 1, board, dir, play_as, new SingleDir());

			if (play_as.equals(PLAYER.R)) {
				board.undoMove(move, PLAYER.R);
				board.undoMove(dir, PLAYER.G);
			} else {
				board.undoMove(dir, PLAYER.R);
				board.undoMove(move, PLAYER.G);
			}

			if (score <= alpha) {
				return alpha;
			}
			if (score < beta) {
				beta = score;
				nextMove.dist = dir;
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

		alphaBetaMax(-INF, INF, DEPTH, board, DIRECTION.DOWN, play_as, nextMove );
		
		return nextMove.dist;
	}
	
	public Pair<Board, PLAYER> read (Scanner in) {
		
		Pair<Board, PLAYER> p = new Pair<Board, PLAYER>();
		Board b;

		if (in.next().charAt(0) == 'r')
			p.setSecond(PLAYER.R);
		else
			p.setSecond(PLAYER.G);
		
		int fstR, sndR, fstG, sndG;
		fstR = in.nextInt();
		sndR = in.nextInt();
		
		fstG = in.nextInt();
		sndG = in.nextInt();
		
		int lines, cols;
		lines = in.nextInt();
		cols = in.nextInt();
		
		b = new Board(lines, cols);
		b.setCurrentPositionForG(fstG, sndG);
		b.setCurrentPositionForR(fstR, sndR);
		
		String[] map = new String[lines];
		int i = 0;
		while (in.hasNextLine()) {
			map[i] = in.nextLine();
			i++;
		}
		
		b.updateMap(map, lines);
		
		p.setFirst(b);

		return p;
		
	}
	
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