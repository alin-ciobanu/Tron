package classes;

import java.util.ArrayList;

public class GetMove {

	private static final int INF = Integer.MAX_VALUE;
	private static final int DEPTH = 10;

	/**
	 * 
	 * @param board
	 *            tabla curent
	 * @param play_as
	 *            jucatorul curent
	 * @return INF in cazul in care jucatorul curent castiga, -INF daca pierde,
	 *         altfel intoarce 1 / (distanta de la un jucator la altul). Cu alte
	 *         cuvinte, cu cat un jucator este mai aproape ce capul celuilalt, cu
	 *         atat mai bine
	 *         
	 * Trebuie jucat cu ce intoarce. In momentul de fata, daca functioneaza, va juca
	 * destul de suicidal. El trebuie sa incerce sa ajunga in fata motocicletei inamice,
	 * nu sa se izbeasca in ea.
	 * 
	 * De asemenea distanta nu e calculata bine. 
	 * Nu prea am idei acum pentru un calcul mai corect
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
					return INF / 2;
				default:
					return 1/(Pair.distanceTo(pozR, pozG));

				}
			} else {
				switch (winner) {
				case PLAYER_R:
					return -INF;

				case PLAYER_G:
					return INF;

				case DRAW:
					return INF / 2;
				default:
					return 1/(Pair.distanceTo(pozR, pozG));

				}
			}
		}

		

		return 1/(Pair.distanceTo(pozR, pozG));
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
	 *            - tabla de jox
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
			DIRECTION move, PLAYER play_as) {
		if (depth == 0 || board.getWinner() != WINNER.NOBODY) {
			return evaluate(board, play_as);
		}

		ArrayList<DIRECTION> possibleMoves = board.getPossibleMoves(play_as);
		int score;

		for (DIRECTION dir : possibleMoves) {
			score = alphaBetaMini(alpha, beta, depth - 1, board, dir, play_as);
			if (score >= beta) {
				move = dir;
				return beta;
			}

			if (score > alpha) {
				alpha = score;
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
			DIRECTION move, PLAYER play_as) {

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
		for (DIRECTION dir : possibleMoves) {
			if (play_as.equals(PLAYER.R)) {
				board.makeMove(move, PLAYER.R);
				board.makeMove(dir, PLAYER.G);
			} else {
				board.makeMove(dir, PLAYER.R);
				board.makeMove(move, PLAYER.G);
			}
			score = alphaBetaMax(alpha, beta, depth - 1, board, move, play_as);

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
		DIRECTION nextMove = DIRECTION.UP;
		
		alphaBetaMax(-INF, INF, DEPTH, board, nextMove, play_as);
		
		return nextMove;
	}
}
