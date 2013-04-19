
enum DIRECTION {
	UP,
	DOWN,
	RIGHT,
	LEFT,
	INVALID
};

// folosit pentru marcarea hartii
enum MARK {
	SPACE,
	OBSTACLE,
	PLAYER_G,
	PLAYER_R
};

enum PLAYER {
	R,
	G
};

enum WINNER {
	PLAYER_G,
	PLAYER_R,
	NOBODY,
	DRAW
};