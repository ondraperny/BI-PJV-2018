package NMob.thedrake.ui;

import NMob.thedrake.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class Stack extends GridPane {
    private GameState state;
    private StackTile selected;
    private ValidMoves validMoves;
    private BoardView boardView;

    public Stack(GameState state, BoardView boardView) {
        this.state = boardView.getState();
        this.validMoves = new ValidMoves(state);
        this.boardView = boardView;
        this.setPadding(new Insets(20, 0, 0, 400));
        this.setAlignment(Pos.CENTER_LEFT);
        add();
        text();

    }

    public void setState() {
        this.state = boardView.getState();
        this.validMoves = new ValidMoves(state);
        this.getChildren().clear();
        text();
    }

    public void add(){
        int x = 0 ,y =0;
        if(state.armyOnTurn().boardTroops().isLeaderPlaced()){
            for (Troop t:state.armyOnTurn().stack()) {
                this.add(new StackTile(this,t,state), x, y);
                x+=100;

            }
        }else {
            this.add(new StackTile(this,state.armyOnTurn().stack().get(0),state), x, y);
            x+=100;
        }
    }

    public void text(){
        StackPane stackP = new StackPane();
        Text txt = new Text();
        txt.setFill(Color.BLACK);
        txt.setFont(new Font("BlackChancery", 20));
        txt.setText(state.army(PlayingSide.BLUE).side().toString()+"\n   Captured: " + state.army(PlayingSide.BLUE).captured().size() +
        "\n" + state.army(PlayingSide.ORANGE).side().toString()+"\n   Captured: " + state.army(PlayingSide.ORANGE).captured().size());

        this.add(txt,0,200);
        this.add(stackP,600,200);
    }


    public void tileSelected(StackTile stackView) {
        setState();
        if (selected != null && selected != stackView) {
            selected.unselect();
        }

        selected = stackView;
        clearMoves();
        setState();
        showMoves(validMoves.movesFromStack());
    }

    private void showMoves(List<Move> moves) {
        setState();
        for (Move move : moves) {
            boardView.tileViewAt(move.target()).setMove(move);
        }
    }

    private void clearMoves() {
        setState();
        for (Node n : boardView.getChildren()) {
            TileView view = (TileView) n;
            view.clearMove();
        }
        this.getChildren().clear();
        text();
    }

    public void updateTiles() {
        setState();
        this.getChildren().clear();
        add();
        text();
    }

}
