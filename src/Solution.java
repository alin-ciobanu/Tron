
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Solution {

	private static final int INF = Integer.MAX_VALUE;
	private static final int DEPTH = 20; // a se folosi doar DEPTH par

	
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
	 * @param play_as
	 *            - jucatorul curent
	 * @param nextMove
	 * 				- urmatoarea mutare pe care o va face botul
	 * 				- un fel de parametru de iesire
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
	 * 				- miscarea facuta de jucatorul Max
	 * @param play_as
	 *            - jucatorul curent
	 * @param nextMove
	 *            - miscarea pe care o salvam la iesire
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
	 * @return Pair cu Board-ul si cu player-ul pe care il controleaza bot-ul
	 */
	public Pair<Board, PLAYER> read (Scanner in) {
		
		Pair<Board, PLAYER> p = new Pair<Board, PLAYER>();
		Board b;

		in.useDelimiter("\r\n");
        String player = in.next();
        
        if (player.charAt(0) == 'r')
        	p.setSecond(PLAYER.R);
        else
        	p.setSecond(PLAYER.G);

        String pos = in.next();
        String[] str_pos = pos.split(" ");
        int[] position = new int[4];
        int[] sizes = new int[2];

        for(int i = 0; i < 4; i++) {
            position[i] = Integer.parseInt(str_pos[i]);
        }
        pos = in.next();
        str_pos = pos.split(" ");
        for(int i = 0; i < 2; i++) {
            sizes[i] = Integer.parseInt(str_pos[i]);
        }

        String board[] = new String[sizes[ 0 ]];

        for(int i = 0; i < sizes[0]; i++) {
            board[i] = in.next();
        }

		b = new Board(sizes[0], sizes[1]);
		b.setCurrentPositionForG(position[2], position[3]);
		b.setCurrentPositionForR(position[0], position[1]);
		b.updateMap(board, sizes[0]);

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
		
		Scanner in = new Scanner(System.in);
		Solution bot = new Solution();
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
