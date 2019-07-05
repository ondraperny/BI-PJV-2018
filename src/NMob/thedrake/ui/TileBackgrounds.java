package NMob.thedrake.ui;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import NMob.thedrake.BoardTile;
import NMob.thedrake.PlayingSide;
import NMob.thedrake.Tile;
import NMob.thedrake.Troop;
import NMob.thedrake.TroopFace;
import NMob.thedrake.TroopTile;

/**
 * Manages backgrounds for all possible Tiles
 */

public class TileBackgrounds {

	public static final Background EMPTY_BG = new Background(
			new BackgroundFill(new Color(0.9, 0.9, 0.9, 1), null, null));
	private final Background mountainBg;
	
	public TileBackgrounds() {
		 Image img = new Image(getClass().getResourceAsStream("/NMob/thedrake/assets/mountain.png"));
		 this.mountainBg = new Background(
				 new BackgroundImage(img, null, null, null, null));
	}

	/*
	 * Returns appropriate background for a given Tile
	 */
	public Background get(Tile tile) {
		if(tile.hasTroop()) {
			TroopTile armyTile = ((TroopTile)tile);
			return getTroop(armyTile.troop(), armyTile.side(), armyTile.face());
		}
		
		if(tile == BoardTile.MOUNTAIN) {
			return mountainBg;
		}
		
		return EMPTY_BG;
	}
	
	/*
	 * Returns background for a given Troop
	 */
	public Background getTroop(Troop info, PlayingSide side, TroopFace face) {
		TroopImageSet images = new TroopImageSet(info.name());
		BackgroundImage bgImage = new BackgroundImage(
				images.get(side, face), null, null, null, null);
		 
		return new Background(bgImage);
	}
}
