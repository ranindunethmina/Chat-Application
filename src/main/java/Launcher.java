import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
        stage.setTitle("Live Chat");
        stage.getIcons().add(new javafx.scene.image.Image("/images/icon/whatsapp_logo.png"));
        stage.centerOnScreen();
        stage.show();
    }
}
