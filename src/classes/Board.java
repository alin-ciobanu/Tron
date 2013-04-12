package classes;

import java.util.ArrayList;

public class Board{

	MARK board[][];

	Pair<Integer, Integer> currentPositionG;
	Pair<Integer, Integer> currentPositionR;
	
	int lines, cols; // dimensiunea hartii

	/**
	 * 
	 * @param lines - numarul de linii al matricei
	 * @param cols - numarul de coloane
	 * 
	 * Initializeaza si aloca memorie pentru obiecte/array-uri
	 * Constructorul trebuie urmat de updateMap(), setCurrentPosition()
	 * etc
	 */
	public Board (int lines, int cols) {
		
		board = new MARK[lines][cols];
		currentPositionG = new Pair<Integer, Integer>(-1, -1);
		currentPositionR = new Pair<Integer, Integer>(-1, -1);
		
		this.lines = lines;
		this.cols = cols;

	}
	
	/**
	 * 
	 * @param b - un board
	 * copiaza Board-ul b
	 */
	public Board (Board b) {
		
		int oldFG, oldSG; // vechiul first si second pt player G
		oldFG = b.currentPositionG.getFirst();
		oldSG = b.currentPositionG.getSecond();
		currentPositionG.setBoth(oldFG, oldSG);
		
		int oldFR, oldSR; // vechiul first si second pt player R
		oldFR = b.currentPositionR.getFirst();
		oldSR = b.currentPositionR.getSecond();
		currentPositionG.setBoth(oldFR, oldSR);
		
		for (int i = 0; i < b.lines; i++) {
			for (int j = 0; j < b.lines; j++) {
				board[i][j] = b.board[i][j];
			}
		}
		
	}

	/**
	 * 
	 * @param map - harta care contine: (#, -, r, g)
	 * updateaza board-ul
	 */
	public void updateMap (char[][] map, int lines, int cols) {

		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < cols; j++) {
				
				if (map[i][j] == '#') {
					board[i][j] = MARK.OBSTACLE;
				}
				
				if (map[i][j] == '-') {
					board[i][j] = MARK.SPACE;
				}
				
				if (map[i][j] == 'r') {
					board[i][j] = MARK.PLAYER_R;
				}
				
				if (map[i][j] == 'g') {
					board[i][j] = MARK.PLAYER_G;
				}
				
			}
		}
		
	}
	
	/**
	 * 
	 * @param map -- vector de string-uri care contin harta
	 * @param lines -- numarul de linii pe care le are harta
	 */
	public void updateMap (String[] map, int lines) {
		
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < map[i].length(); j++) {
				
				if (map[i].charAt(j) == 'r') {
					board[i][j] = MARK.PLAYER_R;
				}

				if (map[i].charAt(j) == 'g') {
					board[i][j] = MARK.PLAYER_G;
				}

				if (map[i].charAt(j) == '-') {
					board[i][j] = MARK.SPACE;
				}

				if (map[i].charAt(j) == '#') {
					board[i][j] = MARK.OBSTACLE;
				}

			}
		}
		
	}
	
	/**
	 * 
	 * @param line - linia
	 * @param col - coloana
	 */
	public void setCurrentPositionForG (int line, int col) {
		
		currentPositionG.setBoth(line, col);
		
	}
	
	/**
	 * 
	 * @param line - linia
	 * @param col - coloana
	 */
	public void setCurrentPositionForR (int line, int col) {

		currentPositionR.setBoth(line, col);
		
	}
	
	/**
	 * 
	 * @param play_as - playerul pentru care vrem mutarile posibile (R sau G)
	 * @return - mutarile pe care le poate face player-ul pentru a nu pierde
	 *  -- returneaza doar spatiile
	 */
	public ArrayList<DIRECTION> getPossibleMoves (PLAYER play_as) {
		
		ArrayList<DIRECTION> freeMoves = new ArrayList<DIRECTION>();
		int i, j;

		if (play_as == PLAYER.G) {
			
			i = currentPositionG.getFirst();
			j = currentPositionG.getSecond();
			
			if (board[i - 1][j] == MARK.SPACE) {
				freeMoves.add(DIRECTION.UP);
			}
			
			if (board[i + 1][j] == MARK.SPACE) {
				freeMoves.add(DIRECTION.DOWN);
			}
			
			if (board[i][j - 1] == MARK.SPACE) {
				freeMoves.add(DIRECTION.LEFT);
			}
			
			if (board[i][j + 1] == MARK.SPACE) {
				freeMoves.add(DIRECTION.RIGHT);
			}
			
		}
		
		else if (play_as == PLAYER.R) {
			
			i = currentPositionR.getFirst();
			j = currentPositionR.getSecond();
			
			if (board[i - 1][j] == MARK.SPACE) {
				freeMoves.add(DIRECTION.UP);
			}
			
			if (board[i + 1][j] == MARK.SPACE) {
				freeMoves.add(DIRECTION.DOWN);
			}
			
			if (board[i][j - 1] == MARK.SPACE) {
				freeMoves.add(DIRECTION.LEFT);
			}
			
			if (board[i][j + 1] == MARK.SPACE) {
				freeMoves.add(DIRECTION.RIGHT);
			}
			
		}
		
		return freeMoves;
		
	}
	
	/**
	 * 
	 * @param dir -- directia in care se face miscare
	 * @param play_as -- player-ul pentru care se face miscarea
	 * 
	 * !!! Mutarile se vor face intotdeauna peste un spatiu.
	 * Adica peste unul din rezultatele date de getPossibbleMoves()
	 */
	public void makeMove (DIRECTION dir, PLAYER play_as) {

		int fstG, sndG; // vor fi indici pentru currentPositionG
		int fstR, sndR; // pt currentPositionR

		fstG = currentPositionG.getFirst();
		sndG = currentPositionG.getSecond();
		fstR = currentPositionR.getFirst();
		sndR = currentPositionR.getSecond();
		
		switch (dir) {
		
		case UP:
			if (play_as == PLAYER.G) {
				fstG = fstG - 1;
				currentPositionG.setBoth(fstG, sndG);
				board[fstG][sndG] = MARK.PLAYER_G;
			}
			else if (play_as == PLAYER.R) {
				fstR = fstR + 1;
				currentPositionR.setBoth(fstR, sndR);
				board[fstR][sndR] = MARK.PLAYER_R;
			}
			break;
		
		case DOWN:
			if (play_as == PLAYER.G) {
				fstG = fstG + 1;
				currentPositionG.setBoth(fstG, sndG);
				board[fstG][sndG] = MARK.PLAYER_G;
			}
			else if (play_as == PLAYER.R) {
				fstR = fstR + 1;
				currentPositionR.setBoth(fstR, sndR);
				board[fstR][sndR] = MARK.PLAYER_R;
			}			
			break;
		
		case LEFT:
			if (play_as == PLAYER.G) {
				sndG = sndG - 1;
				currentPositionG.setBoth(fstG, sndG);
				board[fstG][sndG] = MARK.PLAYER_G;
			}
			else if (play_as == PLAYER.R) {
				sndR = sndR - 1;
				currentPositionR.setBoth(fstR, sndR);
				board[fstR][sndR] = MARK.PLAYER_R;
			}

			break;

		case RIGHT:
			if (play_as == PLAYER.G) {
				sndG = sndG + 1;
				currentPositionG.setBoth(fstG, sndG);
				board[fstG][sndG] = MARK.PLAYER_G;
			}
			else if (play_as == PLAYER.R) {
				sndR = sndR + 1;
				currentPositionR.setBoth(fstR, sndR);
				board[fstR][sndR] = MARK.PLAYER_R;
			}
			
			break;
		
		default:
			System.out.println("Are you nuts?\n");
			break;
		}
		
	}
	
	/**
	 * 
	 * @param dir - directia pentru care se face undo
	 * @param play_as - jucatorul pentru care se face undo
	 * 
	 * !! Atentie la folosire!
	 * Parametrul dir este acelasi cu cel pentru makeMove()
	 * Daca facem makeMove(dir), pentru a sterge mutarea facuta vom apela undoMove(dir)
	 * si nu undoMove(Directions.complementDirection(dir))
	 */
	public void undoMove (DIRECTION dir, PLAYER play_as) {

		int fstG, sndG; // indici pentru pozitia curenta a lui G
		int fstR, sndR; // la fel, dar pt R
		
		fstG = currentPositionG.getFirst();
		sndG = currentPositionG.getSecond();
		fstR = currentPositionR.getFirst();
		sndR = currentPositionR.getSecond();
		
		/*
		 * Marcare cu SPACE pentru pozitia curenta
		 */
		if (play_as == PLAYER.G) {
			board[fstG][sndG] = MARK.SPACE;
		}
		else if (play_as == PLAYER.R) {
			board[fstR][sndR] = MARK.SPACE;
		}
		
		/*
		 * Mutarea pozitiei curente pentru jucator
		 */
		switch (dir) {

		case UP:
			if (play_as == PLAYER.G) {
				fstG = fstG + 1;
				currentPositionG.setBoth(fstG, sndG);
			}
			else if (play_as == PLAYER.R) {
				fstR = fstR + 1;
				currentPositionR.setBoth(fstR, sndR);
			}
			break;
			
		case DOWN:
			if (play_as == PLAYER.G) {
				fstG = fstG - 1;
				currentPositionG.setBoth(fstG, sndG);
			}
			else if (play_as == PLAYER.R) {
				fstR = fstR - 1;
				currentPositionR.setBoth(fstR, sndR);
			}
			break;
			
		case LEFT:
			if (play_as == PLAYER.G) {
				sndG = sndG + 1;
				currentPositionG.setBoth(fstG, sndG);
			}
			else if (play_as == PLAYER.R) {
				sndR = sndR + 1;
				currentPositionR.setBoth(fstR, sndR);
			}
			break;
			
		case RIGHT:
			if (play_as == PLAYER.G) {
				sndG = sndG - 1;
				currentPositionG.setBoth(fstG, sndG);
			}
			else if (play_as == PLAYER.R) {
				sndR = sndR - 1;
				currentPositionR.setBoth(fstR, sndR);
			}
			break;

		default:
			System.out.println("This must be an error. Direction doesn't exist.\n");
			break;
		}
		
	}

	/**
	 * 
	 * @return castigatorul jocului
	 */
	public WINNER getWinner () {

		int possibleMovesForG = getPossibleMoves(PLAYER.G).size();
		int possibleMovesForR = getPossibleMoves(PLAYER.R).size();
		
		if (currentPositionG.equals(currentPositionR)) {
			return WINNER.DRAW;
		}
		
		if (possibleMovesForG == possibleMovesForR && possibleMovesForG == 0) {
			return WINNER.DRAW;
		}
		
		if (possibleMovesForG == 0 && possibleMovesForR > 0) {
			return WINNER.PLAYER_R;
		}
		
		if (possibleMovesForG > 0 && possibleMovesForR == 0) {
			return WINNER.PLAYER_G;
		}
		
		if (possibleMovesForG == possibleMovesForR && possibleMovesForG > 0) {
			return WINNER.NOBODY;
		}

		System.out.println("There's something wrong with Board.getWinner() method.");
		return null; // nu se ajunge niciodata aici, dar e prost eclipse
		// ma pune sa returnez ceva neaparat

	}
	
	public static void main(String[] args) {
		// TODO Test Board class here
	}

}
