import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        System.out.println("Before runtime\n======================");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("======================\nAfter runtime");
    }

    @Override
    public void start(Stage window) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/deadwood_setup.fxml"));
        Scene gameSetup = new Scene(root, 600, 400);

        window.setTitle("Deadwood Game Setup");
        window.setResizable(false);
        window.initStyle(StageStyle.DECORATED);
        window.setScene(gameSetup);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
