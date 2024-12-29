import com.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        new ReservationController(primaryStage).showMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
