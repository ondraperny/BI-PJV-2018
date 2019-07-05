package NMob.thedrake.ui;

import java.util.List;

import NMob.thedrake.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BoardView extends GridPane implements TileViewContext {
    public GameState state;
    private TileView selected;
    private ValidMoves validMoves;

    public GameState getState() {
        return state;
    }

    public BoardView(GameState state) {
        this.state = state;
        this.validMoves = new ValidMoves(state);

        this.setVgap(5);
        this.setHgap(5);
        this.setPadding(new Insets(80, 0, 0, 400));
        this.setWidth(800);
        this.setAlignment(Pos.CENTER_LEFT);

        PositionFactory pf = state.board().positionFactory();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                int i = x;
                int j = 3 - y;
                BoardPos pos = pf.pos(i, j);
                this.add(new TileView(this, state.tileAt(pos), pos), x, y);
            }
        }
    }

    /*
     * Return the TileView on a given position
     */
    public TileView tileViewAt(BoardPos pos) {
        int index = (3 - pos.j()) * 4 + pos.i();
        return (TileView) getChildren().get(index);
    }

    /*
     * Clear all moves associated with TileViews
     */
    private void clearMoves() {
        for (Node n : getChildren()) {
            TileView view = (TileView) n;
            view.clearMove();
        }
    }

    /*
     * Associate given moves with corresponding target TileViews.
     */
    private void showMoves(List<Move> moves) {
        for (Move move : moves) {
            tileViewAt(move.target()).setMove(move);
        }
    }

    /*
     * Update the state of all TileViews according to the state of the game.
     */
    private void updateTiles() {
        for (Node n : getChildren()) {
            TileView view = (TileView) n;
            view.setTile(state.tileAt(view.position()));
            view.update();
        }
    }

    @Override
    public void tileSelected(TileView view) {
        if (selected != null && selected != view) {
            selected.unselect();
        }

        selected = view;
        clearMoves();
        showMoves(validMoves.boardMoves(view.position()));
    }


    @Override
    public void executeMove(Move move) {
        if(selected!=null)
            selected.unselect();
        selected = null;
        clearMoves();


        this.state = move.execute(state);
        this.validMoves = new ValidMoves(state);

        Stack stack = (Stack) getParent().getChildrenUnmodifiable().get(1);
        if(stack!=null){
            stack.setState();
            stack.updateTiles();
        }
        updateTiles();
        checkIfEnd();
    }

    public void checkIfEnd() {
        if (state.result() == GameResult.VICTORY) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Victory");
            alert.setHeaderText(null);
            if(state.sideOnTurn().toString().equals("BLUE")) {
                alert.setContentText("ORANGE is Victor!");
            } else {
                alert.setContentText("BLUE is Victor!");
            }
            alert.showAndWait();
            Stage st = (Stage)this.getScene().getWindow();
            st.close();
        }
    }
}
