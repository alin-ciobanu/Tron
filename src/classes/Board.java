package classes;

public class Board{
	
	MARK board[][];
	PLAYER play_as;
	
	DIRECTION myDirection; // directia pe care o are botul
	// e nevoie de ea pentru a genera mutarile posibile dintr-un punct
	DIRECTION opponentDirection; // directia oponentului
	
	/*
	 * pe aici probabil mai trebuie puse cateva campuri cu lastDirection, oppLastDirection
	 * sau ceva de genul
	 */

	Pair<Integer, Integer> myCurrentPosition;
	Pair<Integer, Integer> opponentPosition;

	/**
	 * 
	 * @param lines - numarul de linii al matricei
	 * @param cols - numarul de coloane
	 */
	public Board (int lines, int cols) {
		
		board = new MARK[lines][cols];
		myCurrentPosition = new Pair<Integer, Integer>(-1, -1);
		opponentPosition = new Pair<Integer, Integer>(-1, -1);

	}
	
	/**
	 * 
	 * @param b - un board
	 * copiaza Board-ul b
	 */
	public Board (Board b) {
		// TODO
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
					board[i][j] = MARK.OBSTACLE;
				}
				
				if (map[i][j] == 'r') {
					if (play_as == PLAYER.PLAYER_R) {
						board[i][j] = MARK.ME;
					}
					else {
						board[i][j] = MARK.OPPONENT;
					}
				}
				
				if (map[i][j] == 'g') {
					if (play_as == PLAYER.PLAYER_G) {
						board[i][j] = MARK.ME;
					}
					else {
						board[i][j] = MARK.OPPONENT;
					}
				}
				
			}
		}
		
	}
	
	/**
	 * 
	 * @param ch - caracter (va fi 'g' sau 'r')
	 * -- schimba PLAYER-ul bot-ului nostru
	 */
	public void updatePlayer (char ch) {
		
		if (ch == 'r') {
			play_as = PLAYER.PLAYER_R;
		}
		else if (ch == 'g') {
			play_as = PLAYER.PLAYER_G;
		}
		else {
			System.out.println("Error. Player cannot be " + ch + ".\n");
		}
		
	}
	
	/**
	 * 
	 * @param line - linia
	 * @param col - coloana
	 */
	public void setMyCurrentPosition (int line, int col) {
		
		myCurrentPosition.setBoth(line, col);
		
	}
	
	/**
	 * 
	 * @param line - linia
	 * @param col - coloana
	 */
	public void setOpponentPosition (int line, int col) {
		
		opponentPosition.setBoth(line, col);
		
	}
	
	/**
	 * 
	 * @param myDir - directia in care trebuie sa mutam
	 * @param oppDir - directia in care muta adversarul
	 * @return - pereche de MARK-uri care retin 
	 *           ce element a fost pe pozitia [i][j] in matricea board
	 */
	public Pair<MARK, MARK> makeMove (DIRECTION myDir, DIRECTION oppDir) {

		/*
		 * Mai e de lucrat
		 * TODO Trebuie gasit un mod care sa tina minte care a fost directia inainte ca aceasta sa fie schimbata
		 */
		
		/*
		 * Pe prima pozitie din Pair este MARK-ul inlocuit de MARK.ME
		 * Pe a doua pozitie este MARK-ul inlocuit de MARK.OPPONENT
		 * 
		 * E nevoie sa tinem minte ce MARK-uri au fost inainte pentru a putea
		 * implementa metoda undoMove
		 * 
		 */
		Pair<MARK, MARK> overwrittenMarks = new Pair<MARK, MARK> ();

		int i = myCurrentPosition.getFirst();
		int j = myCurrentPosition.getSecond();

		myDirection = myDir;
		opponentDirection = oppDir;

		switch (myDir) {
		case UP:
			myCurrentPosition.setBoth(i - 1, j);
			break;
		case DOWN:
			myCurrentPosition.setBoth(i + 1, j);
			break;
		case RIGHT:
			myCurrentPosition.setBoth(i, j + 1);
			break;
		case LEFT:
			myCurrentPosition.setBoth(i, j - 1);
			break;
		default:
			System.out.println("Cum ai reusit?\n");
			break;
		}
		
		overwrittenMarks.setFirst(board[i][j]);
		// se salveaza mark-ul pozitiei curente (cea imediat dupa miscare)

		board[i][j] = MARK.ME; // marcare pozitie noua
		
		i = opponentPosition.getFirst();
		j = opponentPosition.getSecond();
		
		switch (oppDir) {
		case UP:
			opponentPosition.setBoth(i - 1, j);
			break;
		case DOWN:
			opponentPosition.setBoth(i + 1, j);
			break;
		case RIGHT:
			opponentPosition.setBoth(i, j + 1);
			break;
		case LEFT:
			opponentPosition.setBoth(i, j - 1);
			break;
		default:
			System.out.println("Cum ai reusit?\n");
			break;
		}
		
		overwrittenMarks.setSecond(board[i][j]);
		// se salveaza mark-ul pozitiei curente (cea imediat dupa miscare) a adversarului

		board[i][j] = MARK.OPPONENT; // marcare miscarea oponentului
		
		return overwrittenMarks;
		
	}
	
	public void undoMove (DIRECTION myLastMove, MARK myLastMark, DIRECTION oppLastMove, MARK oppLastMark) {

		// TODO Implement method here
		
		int i = myCurrentPosition.getFirst();
		int j = myCurrentPosition.getSecond();
		
		board[i][j] = myLastMark; // resetare MARK
		
		switch (myLastMove) {
		case UP:
			myCurrentPosition.setBoth(i + 1, j);
			break;
		case DOWN:
			myCurrentPosition.setBoth(i - 1, j);
			break;
		case RIGHT:
			myCurrentPosition.setBoth(i, j - 1);
			break;
		case LEFT:
			myCurrentPosition.setBoth(i, j + 1);
			break;
		default:
			break;
		}
		
		// To be continued.
		
	}
	
	public static void main(String[] args) {
		
	}

}
