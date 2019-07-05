package NMob.thedrake;

public class Board {
    private int dimension;
    private BoardTile[][] boardTiles;

    public Board(int dimension) {
        if(dimension < 0)
            throw new IllegalArgumentException("The dimension needs to be positive.");

        this.dimension = dimension;
        boardTiles = new BoardTile[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boardTiles[i][j] = BoardTile.EMPTY;
            }
        }
    }

    private Board(Board board, TileAt... ats) {
        this.dimension = board.dimension();
        boardTiles = new BoardTile[dimension][];

        for(int i = 0; i < dimension; i++) {
            boardTiles[i] = board.boardTiles[i].clone();
        }

        for(TileAt newTile : ats) {
            boardTiles[newTile.pos.i()][newTile.pos.j()] = newTile.tile;
        }
    }

    public int dimension() {
		return this.dimension;
	}

    public BoardTile at(BoardPos pos) {
        return this.boardTiles[pos.i()][pos.j()];
    }

    public Board withTiles(TileAt... ats) {
        return new Board(this, ats);
    }

    public PositionFactory positionFactory() {
        return new PositionFactory(dimension);
	}

	public static class TileAt {
		public final BoardPos pos;
		public final BoardTile tile;

		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}
}

