package NMob.thedrake.ui;

import NMob.thedrake.Move;

/* 
 * A context throw which a TileView communicates an action 
 * which needs to be executed. 
 */
public interface TileViewContext {
  public void tileSelected(TileView view);
  public void executeMove(Move move);
}
