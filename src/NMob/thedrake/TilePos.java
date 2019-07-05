package NMob.thedrake;

import java.util.List;

public interface TilePos {
	public int i(); /// cislo sloupce
	public int j();	/// cislo radku
	public char column(); /// pismeno sloupce
	public int row(); /// cislo radku

	public TilePos step(int columnStep, int rowStep);
	public TilePos step(Offset2D step);
	public List<? extends TilePos> neighbours();
	public boolean isNextTo(TilePos pos);		
	public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side);
	  	
	public boolean equalsTo(int i, int j);
	
	public static final TilePos OFF_BOARD = new TilePos() {

		@Override
		public int i() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public int j() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public char column() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int row() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TilePos step(int columnStep, int rowStep) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TilePos step(Offset2D step) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<TilePos> neighbours() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNextTo(TilePos pos) {
			throw new UnsupportedOperationException();
		}

		@Override
		public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equalsTo(int i, int j) {
			return false;
		}
		
		@Override
		public String toString() {
			return "off-board";
		}
	};
}
