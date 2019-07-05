package NMob.thedrake;

import java.io.PrintWriter;
import java.text.MessageFormat;

public class GameState implements JSONSerializable {

    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    @Override
    public void toJSON(PrintWriter writer) {

        writer.print("{\"result\":\"" + this.result + "\",\"board\":{\"dimension\":" + this.board.dimension() + "," +
                "\"tiles\":[" + this.getJSONTiles() + "]},\"blueArmy\":{");
        this.blueArmy.toJSON(writer);
        writer.print("},\"orangeArmy\":{");
        this.orangeArmy.toJSON(writer);
        writer.print("}}");
    }

    private String getJSONTiles() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.board.dimension(); i++) {
            for (int j = 0; j < this.board.dimension(); j++) {
                BoardTile boardTile = this.board.at(new BoardPos(this.board.dimension(),j,i));
                if(boardTile.equals(BoardTile.EMPTY)){
                    sb.append("\"empty\"");
                }else if(boardTile.equals(BoardTile.MOUNTAIN)){
                    sb.append("\"mountain\"");
                }
                if (i != j || j != this.board.dimension() - 1) {
                    sb.append(",");
                }

            }
        }
        return sb.toString();
    }

    public String tiles() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.board.dimension(); i++) {
            for (int j = 0; j < this.board.dimension(); j++) {
                sb.append(this.board.at(new BoardPos(this.board.dimension(), i, j)));
            }
        }
        return sb.toString();
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn); // returns blueArmy or orangeArmy depends on who is playing
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE) {
            return orangeArmy;
        }

        return blueArmy;
    }

    public Tile tileAt(BoardPos pos) {
        if (this.blueArmy.boardTroops().at(pos).isPresent()) return this.blueArmy.boardTroops().at(pos).get();
        if (this.orangeArmy.boardTroops().at(pos).isPresent()) return this.orangeArmy.boardTroops().at(pos).get();
        return this.board.at(pos);
    }

    private boolean canStepFrom(TilePos origin) {
        return result == GameResult.IN_PLAY
                && !blueArmy.boardTroops().isPlacingGuards()
                && blueArmy.boardTroops().isLeaderPlaced()
                && !orangeArmy.boardTroops().isPlacingGuards()
                && orangeArmy.boardTroops().isLeaderPlaced()
                && armyOnTurn().boardTroops().at(origin).isPresent();
    }

    private boolean canStepTo(TilePos target) {
        return result == GameResult.IN_PLAY &&
                target != TilePos.OFF_BOARD &&
                !blueArmy.boardTroops().at(target).isPresent() &&
                !orangeArmy.boardTroops().at(target).isPresent() &&
                board.at(new BoardPos(board.dimension(), target.i(), target.j())).canStepOn();
    }

    private boolean canCaptureOn(TilePos target) {
        return result == GameResult.IN_PLAY &&
                target != TilePos.OFF_BOARD &&
                armyNotOnTurn().boardTroops().at(target).isPresent();
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) { // TODO: replace code with written methods
        if (this.result != GameResult.IN_PLAY ||
                target == TilePos.OFF_BOARD ||
                this.armyOnTurn().stack().isEmpty() ||
                !canStepTo(target)) {
            return false;
        }
        if (!armyOnTurn().boardTroops().isLeaderPlaced()) {
            if (armyOnTurn() == this.blueArmy) {
                return target.j() == 0;
            } else {
                return target.j() == this.board.dimension() - 1;
            }
        }
        else if (armyOnTurn().boardTroops().isPlacingGuards()) {
            return armyOnTurn().boardTroops().leaderPosition().isNextTo(target);
        }
        return true;
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target)) {
                newResult = GameResult.VICTORY;
            }

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target)) {
                newResult = GameResult.VICTORY;
            }

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }



    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }
}
