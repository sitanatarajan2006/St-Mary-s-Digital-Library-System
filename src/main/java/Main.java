import data.DbConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.GUI;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        DbConnection.createTables();

        GUI gui = new GUI();

        Scene scene = new Scene(gui.getMainView(), 1200, 750);

        stage.setTitle("St Mary's Digital Library System");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}