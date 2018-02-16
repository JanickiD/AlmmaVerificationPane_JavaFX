package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 

public class Main extends Application {
	 
	private static Stage primaryStage;
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void setPrimaryStage(Stage primaryStage) {
		Main.primaryStage = primaryStage;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			setPrimaryStage(primaryStage);
			Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/application/view/Login.fxml"));
			Scene scene = new Scene(parent);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Logowanie");
			primaryStage.show();
			
			 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
