package NMob.thedrake.mainmenu;

import NMob.thedrake.*;
import NMob.thedrake.ui.BoardView;
import NMob.thedrake.ui.Stack;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class Controller {
    public Button btnTwoPlayers;
    public Button btnPlayerVsAI;
    public Button btnOnlineGame;
    public Button btnQuit;
    public VBox mainMenuButtons;

    @FXML
    protected void initialize() {
    }

    @FXML
    public void twoPlayersClick(ActionEvent actionEvent){
        GameState state = createSampleState();
        BoardView boardView = new BoardView(state);

        Stage stage = new Stage();
        GridPane root = new GridPane();

        Stack stack = new Stack(state, boardView);
        Scene scene = new Scene(root,1200,800);

        scene.getStylesheets().addAll(this.getClass().getResource("mainmenu.css").toExternalForm());

        root.addRow(0,boardView);
        root.addRow(1,stack);
        stage.setScene(scene);
        final Node source = (Node) actionEvent.getSource();

        stage.showAndWait();
    }

    private GameState createSampleState() {
        Board board = new Board(4);
        PositionFactory pf = board.positionFactory();
        board = board.withTiles(
                new Board.TileAt(pf.pos(1, 1), BoardTile.MOUNTAIN));
        GameState state = new StandardDrakeSetup().startState(board);

//        /** Just for testing purposes */
//        state = state.placeFromStack(pf.pos(2, 0));
//        state = state.placeFromStack(pf.pos(2, 3));
//        state = state.placeFromStack(pf.pos(1, 0));
//        state = state.placeFromStack(pf.pos(1, 3));
//        state = state.placeFromStack(pf.pos(3, 0));
//        state = state.placeFromStack(pf.pos(3, 3));
//        state = state.placeFromStack(pf.pos(2, 1));
//        state = state.placeFromStack(pf.pos(0, 3));

        return state;
    }

    public void quitClick() {
        Platform.exit();
    }


    public void twoPlayersEnter() {
        btnTwoPlayers.setStyle("-fx-text-fill: black; -fx-font-family: BlackChancery; -fx-font-size: 34.0; -fx-pref-width: 325; -fx-underline: true;");
    }

    public void twoPlayersLeave() {
        btnTwoPlayers.setStyle("-fx-text-fill: #4d3c37; -fx-font-family: BlackChancery; -fx-font-size: 32.0; -fx-pref-width: 285; -fx-underline: false;");
    }

    public void AIEnter() {
        btnPlayerVsAI.setStyle("-fx-text-fill: black; -fx-font-family: BlackChancery; -fx-font-size: 34.0; -fx-pref-width: 325; -fx-underline: true;");
    }

    public void AILeave() {
        btnPlayerVsAI.setStyle("-fx-text-fill: #4d3c37; -fx-font-family: BlackChancery; -fx-font-size: 32.0; -fx-pref-width: 285; -fx-underline: false;");
    }

    public void onlineEnter() {
        btnOnlineGame.setStyle("-fx-text-fill: black; -fx-font-family: BlackChancery; -fx-font-size: 34.0; -fx-pref-width: 325; -fx-underline: true;");
    }

    public void onlineLeave() {
        btnOnlineGame.setStyle("-fx-text-fill: #4d3c37; -fx-font-family: BlackChancery; -fx-font-size: 32.0; -fx-pref-width: 285; -fx-underline: false;");
    }

    public void quitEnter() {
        btnQuit.setStyle("-fx-text-fill: black; -fx-font-family: BlackChancery; -fx-font-size: 34.0; -fx-pref-width: 325; -fx-underline: true;");
    }

    public void quitLeave() {
        btnQuit.setStyle("-fx-text-fill: #4d3c37; -fx-font-family: BlackChancery; -fx-font-size: 32.0; -fx-pref-width: 285; -fx-underline: false;");
    }
}
