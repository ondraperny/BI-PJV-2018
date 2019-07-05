package NMob.thedrake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu/mainmenu.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Drake");
        stage.getIcons().add(new Image("NMob/thedrake/assets/icon.png"));
        stage.show();
    }
}
