package NMob.thedrake.ui;

import NMob.thedrake.*;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class StackTile extends Pane {
    private  Stack context;

    private final ImageView moveImage;
    private final TileBackgrounds backgrounds;
    private final Border selectBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10)));

    public StackTile(Stack context, Troop troop, GameState state) {
        this.context = context;
        moveImage = null;
        backgrounds = null;
        this.setBackground(new TileBackgrounds().getTroop(troop,state.sideOnTurn(),TroopFace.AVERS));
        this.setPrefSize(100, 100);
        this.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onClicked();
            }
        });
    }

    public void select() {
        this.setBorder(selectBorder);
        context.tileSelected(this);
    }

    public void unselect() {
        this.setBorder(null);
    }

    private void onClicked() {
        select();
    }

    public void clearTile() {
        moveImage.setVisible(false);
    }


}
